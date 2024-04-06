package org.SCAU.DynamicCEP;

import org.SCAU.DynamicCEP.Patterns.testPaterns;
import org.SCAU.model.stockSerializable;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.SCAU.DynamicCEP.dataSource.parseJsonFile.parseJonFile;

public class CEPDEMO2_emulation {
    public static void main(String[] args) throws IOException {

        Runtime r = Runtime.getRuntime();
        r.gc();//计算内存前先垃圾回收一次
        long stime = System.nanoTime();
        long startMem = r.totalMemory(); // 开始Memory

        List<stockSerializable> stocks=parseJonFile();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        StreamExecutionEnvironment env= StreamExecutionEnvironment.getExecutionEnvironment();
        final ExecutionConfig config = env.getConfig();
        env.enableCheckpointing(20000L, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(20000L);
        env.getCheckpointConfig().setCheckpointTimeout(120000L);
        env.setParallelism(10);

        env.disableOperatorChaining();
        DataStream<stockSerializable> stockStream = env.addSource(new SourceFunction<stockSerializable>() {
            @Override
            public void run(SourceContext<stockSerializable> ctx) throws Exception {
                for (stockSerializable stock : stocks) {
                    ctx.collect(stock); // 发送单个元素到流中
                }
                ctx.close();
            }

            @Override
            public void cancel() {
                // 可选的取消逻辑
            }
        });
//        stockStream.print();
//        DataStream<stockSerializable>
        KeyedStream<stockSerializable,String>
                keyedStream = stockStream
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.noWatermarks()
                )
                .flatMap(new FlatMapFunction<stockSerializable, stockSerializable>() {
                    @Override
                    public void flatMap(stockSerializable value, Collector<stockSerializable> out) throws Exception {
//                        System.out.println(value);
                        // 处理每个元素的逻辑
                        out.collect(value);
                    }
                })
                .keyBy(stock -> stock.getSymbol());

//                .assignTimestampsAndWatermarks(
//                        WatermarkStrategy.<stockSerializable>forBoundedOutOfOrderness(Duration.ofSeconds(3))
//                                .withTimestampAssigner(
//                                        new SerializableTimestampAssigner<stockSerializable>() {
//                                            @Override
//                                            public long extractTimestamp(stockSerializable stock, long l) {
//                                                try {
//
//                                                    return ft.parse(stock.getDate()).getTime();
//                                                } catch (ParseException e) {
//                                                    throw new RuntimeException(e);
//                                                }
//                                            }
//                                        }
//                                )
//                )


        Pattern<stockSerializable,stockSerializable> pattern = testPaterns.Pattern6();





        DataStream<String> result = CEP.pattern(keyedStream, pattern)
//                .inEventTime()
                .inProcessingTime()
                .flatSelect(
                        (p, o) -> {
                            StringBuilder builder = new StringBuilder();
//                            System.out.println(p.get("1").get(0));


                            builder.append(p.get("1").get(0))

                                    .append(",\n");
//                                    .append(p.get("second").get(0))
//                                    .append(",\n");

                            o.collect(builder.toString());
                        },
                        Types.STRING);
//        keyedStream.print();
        result.print();
        long endMem =r.freeMemory(); // 末尾Memory
        long etime = System.nanoTime();
        //输出

        try {

            env.execute("Flink Streaming Example");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("用时消耗: "+String.valueOf( etime  - stime+"纳秒"));
        System.out.println("内存消耗: "+String.valueOf((startMem- endMem)/1024)+"KB");
    }
}
