package org.SCAU.window;

import org.SCAU.bean.WaterSensor;
import org.SCAU.functions.WaterSensorMapFunction;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class TimeWindow {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        SingleOutputStreamOperator<WaterSensor> sensorDS = env
                .socketTextStream("hadoop102", 7777)
                .map(new WaterSensorMapFunction());


        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // 1. 窗口分配器
        WindowedStream<WaterSensor, String, org.apache.flink.streaming.api.windowing.windows.TimeWindow> sensorWS = sensorKS
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10))); // 滚动窗口，窗口长度10秒
//                .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)));//滑动窗口，长度10s，步长5s
//                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(5)));//会话窗口，间隔5s
//                .window(ProcessingTimeSessionWindows.withDynamicGap(
//                        new SessionWindowTimeGapExtractor<WaterSensor>() {
//                            @Override
//                            public long extract(WaterSensor element) {
//                                // 从数据中提取ts，作为间隔,单位ms
//                                return element.getTs() * 1000L;
//                            }
//                        }
//                ));// 会话窗口，动态间隔，每条来的数据都会更新 间隔时间


        SingleOutputStreamOperator<String> process = sensorWS
                .process(
                        new ProcessWindowFunction<WaterSensor, String, String, org.apache.flink.streaming.api.windowing.windows.TimeWindow>() {
                            /**
                             * 全窗口函数计算逻辑：  窗口触发时才会调用一次，统一计算窗口的所有数据
                             * @param s   分组的key
                             * @param context  上下文
                             * @param elements 存的数据
                             * @param out      采集器
                             * @throws Exception
                             */
                            @Override
                            public void process(String s, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                                // 上下文可以拿到window对象，还有其他东西：侧输出流 等等
                                long startTs = context.window().getStart();
                                long endTs = context.window().getEnd();
                                String windowStart = DateFormatUtils.format(startTs, "yyyy-MM-dd HH:mm:ss.SSS");
                                String windowEnd = DateFormatUtils.format(endTs, "yyyy-MM-dd HH:mm:ss.SSS");

                                long count = elements.spliterator().estimateSize();

                                out.collect("key=" + s + "的窗口[" + windowStart + "," + windowEnd + ")包含" + count + "条数据===>" + elements.toString());


                            }
                        }
                );


        process.print();

        env.execute();
    }
}
