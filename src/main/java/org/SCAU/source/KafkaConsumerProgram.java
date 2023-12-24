package org.SCAU.source;


import org.SCAU.cep.LoginEvent;
import org.SCAU.model.StockEventNew;
import org.SCAU.utils.OutputUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.operators.Output;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumerProgram {

    private static class StockPatternProcessFunction extends PatternProcessFunction<StockEventNew, OutputUtils.OutputRecord> {

        @Override
        public void processMatch(Map<String, List<StockEventNew>> pattern,
                                 Context ctx,
                                 Collector<OutputUtils.OutputRecord> out) throws Exception {

            StockEventNew event = pattern.get("first").get(0);

            OutputUtils.OutputRecord output = new OutputUtils.OutputRecord();
            output.setEvent(event);

            out.collect(output);
        }

    }
    public static void main(String[] args) throws Exception {

        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(5);
        //配置连接属性
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        properties.setProperty("auto.offset.reset", "earliest");
        List<Pattern<StockEventNew, StockEventNew>> patterns = new ArrayList<>();
        //创建消费者，从kafka的testtopic获取事件
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "test", new SimpleStringSchema(), properties);
        DataStream<StockEventNew> stream = env
                .addSource(consumer)
                .name("KafkaSource").map(line->{
                    //将行按逗号分割
                    //数据示例：3,AAOI,20190913,11.09,11.3026,11.05,11.17,281600
                    String[] fields = line.split(",");
                    //列标头：index,Ticker,Date,Open,High,Low,Close,Volume
                    //构造Event
                    StockEventNew event = new StockEventNew(fields[0],fields[1],fields[2],fields[3],fields[4],fields[5],fields[6],fields[7]);
                    return event;
                }).assignTimestampsAndWatermarks(
                        //分配时间戳和水印，来源于数据的字段
                        WatermarkStrategy
                                //forBoundedOutOfOrderness(有界乱序数据)：
                                // 主要针对乱序流，由于乱序流中需要等待迟到数据到齐，所以必须设置一个固定量的延迟时间。这时生成水位线的时间戳，
                                // 就是当前数据流中最大的时间戳减去延迟的结果，相当于把表调慢，当前时钟会滞后于数据的最大时间戳。
                                // 这个方法需要传入一个 maxOutOfOrderness 参数，表示“ 最大乱序程度 ” ，它表示数据流中乱序数据时间戳的最大差值。

                                .<StockEventNew>forBoundedOutOfOrderness(Duration.ofDays(30))

                                .withTimestampAssigner(
                                        //分配时间戳
                                        new SerializableTimestampAssigner<StockEventNew>() {
                                            //
                                            @Override
                                            public long extractTimestamp(StockEventNew element, long recordTimestamp) {
                                                //原数据是以日为单位
                                                return element.date.getTime();
                                                //单机运行，暂时用系统时间表示
//                                                return  System.currentTimeMillis();
                                            }

                                        }

                        )
                )
                .keyBy(r->r.ticker);

        //定义复杂事件规则
        //规则1：连续3天股票上涨超过5%
        //定义简单条件
        SimpleCondition<StockEventNew> priceRise = new SimpleCondition<StockEventNew>() {
            @Override
            public boolean filter(StockEventNew event) {
                return event.getPriceChange() > 0.05;
            }
        };
        //定义规则
        Pattern<StockEventNew, StockEventNew> pattern1 = Pattern.<StockEventNew>begin("first")
                .where(priceRise)
                .next("second")
                .where(priceRise)
                .next("third")
                .where(priceRise);
        patterns.add(pattern1);
        //规则2：股票上涨超过3%
        SimpleCondition<StockEventNew> stockRise = new SimpleCondition<StockEventNew>() {
            @Override
            public boolean filter(StockEventNew event) {
                return event.getPriceChange() > 0.03;
            }
        };
        Pattern<StockEventNew, StockEventNew> pattern2 = Pattern.<StockEventNew>begin("first")
                .where(stockRise)
                .followedBy("second")
                .where(stockRise)
                .followedBy("third")
                .where(stockRise);
        patterns.add(pattern2);
        //规则3：单只股票波动超过20%
        SimpleCondition<StockEventNew> priceFluctuate = new SimpleCondition<StockEventNew>() {
            @Override
            public boolean filter(StockEventNew event) {
                return Math.abs(event.getPriceChange()) > 0.02;
            }
        };

        Pattern<StockEventNew, StockEventNew> pattern3 = Pattern.<StockEventNew>begin("first")
                .next("second")
                .where(priceFluctuate);
        patterns.add(pattern3);
        //应用规则到流上
        PatternStream<StockEventNew> PatternStream1 = CEP.pattern(stream,pattern1);
//        PatternStream<StockEventNew> patternStream = CEP.pattern(stream, patterns);
//        List<PatternStream<StockEventNew>> patternStreamList = new ArrayList<>();
//        for(Pattern<StockEventNew, StockEventNew> pattern : patterns){
//            PatternStream<StockEventNew> patternStream = CEP.pattern(stream, pattern);
//            patternStreamList.add(patternStream);
//        }
        //处理匹配事件

//        List<SingleOutputStreamOperator<OutputUtils.OutputRecord>> outputStreamOperatorList= new ArrayList<>();

//        for(PatternStream<StockEventNew> PS:patternStreamList) {
//            SingleOutputStreamOperator<OutputUtils.OutputRecord> output = PS
//                    .process(
//
//                    )
//        }
        try {


        SingleOutputStreamOperator<String> warningStream1 = PatternStream1
                .process(new PatternProcessFunction<StockEventNew, String>() {
                    @Override
                    public void processMatch(Map<String, List<StockEventNew>> match, Context ctx, Collector<String> out) throws Exception {
                        // 提取三次登录失败事件
//                        pattern1.getName();
                        System.out.print('1');
                        StockEventNew firstEvent = match.get("first").get(0);
                        StockEventNew secondEvent = match.get("second").get(0);
                        StockEventNew thirdEvent = match.get("third").get(0);
                        System.out.print(firstEvent.toString());
                        System.out.print(secondEvent.toString());
                        out.collect(firstEvent.ticker + " 连续三天上涨5%：起始日期" +
                                firstEvent.date.toString() + ", 事件index" +
                                firstEvent.index+","+secondEvent.index+","+thirdEvent.index);

                    }
                });
            warningStream1.print();
        }
        catch (Exception e){
            System.out.print(e.toString());
        }
//
//        SingleOutputStreamOperator<String> warningStream2 = patternStreamList.get(1)
//                .process(new PatternProcessFunction<StockEventNew, String>() {
//                    @Override
//                    public void processMatch(Map<String, List<StockEventNew>> match, Context ctx, Collector<String> out) throws Exception {
//                        // 提取三次登录失败事件
////                        pattern1.getName();
//                        StockEventNew firstEvent = match.get("first").get(0);
//                        StockEventNew secondEvent = match.get("second").get(0);
//                        StockEventNew thirdEvent = match.get("third").get(0);
//
//                        out.collect(firstEvent.ticker + " 连续三天上涨5%：起始日期" +
//                                firstEvent.date.toString() + ", 事件index" +
//                                firstEvent.index+","+secondEvent.index+","+thirdEvent.index);
//
//                    }
//                });
//        warningStream.addSink()
        // 打印输出
        //输出流中事件

//        stream.print();
//        for (SingleOutputStreamOperator<OutputUtils.OutputRecord> out :outputStreamOperatorList){
//            out.print();
//        }
        //输出匹配结果
//        output.print();
        //执行
        env.execute();
    }
}