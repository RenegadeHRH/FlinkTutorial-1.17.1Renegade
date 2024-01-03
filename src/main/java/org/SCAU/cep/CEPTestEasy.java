package org.SCAU.cep;

import org.SCAU.model.StockEventNew;
import org.SCAU.model.socialMediaStocks;
import org.SCAU.model.socialMediaStocks2;
import org.SCAU.utils.OutputUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.RichPatternSelectFunction;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CEPTestEasy {

    //sink,将keyby结果保存为文件
    public static class PartitionSink extends RichSinkFunction<socialMediaStocks> {

        private String path=".\\output";
        private OutputStream out;

        @Override
        public void open(Configuration parameters) throws Exception {
            // 为每个子任务打开文件
            File file = new File(path + getRuntimeContext().getIndexOfThisSubtask());
            out = new FileOutputStream(file);
        }

        @Override
        public void invoke(socialMediaStocks value) throws Exception {
            // 写入对应分区文件
            out.write(value.toString().getBytes());
        }
    }
    public static void main(String[] args) throws Exception {


        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(5);
        //配置连接属性
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        properties.setProperty("auto.offset.reset", "earliest");


        //创建消费者，从kafka的testtopic获取事件
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "test", new SimpleStringSchema(), properties);
        KeyedStream<socialMediaStocks, String> stream = env
                .addSource(consumer)
                .name("KafkaSource").map(line->{
                    //将行按逗号分割
                    //数据示例：3,AAOI,20190913,11.09,11.3026,11.05,11.17,281600
                    String[] fields = line.split(",");
                    //列标头：index,Ticker,Date,Open,High,Low,Close,Volume
                    //构造Event
//                    socialMediaStocks event = new socialMediaStocks();
                    socialMediaStocks event = new socialMediaStocks(fields[0],fields[1],fields[2],fields[3],fields[4],fields[5],fields[6],fields[7]);
//                    System.out.println(event.toString());

                    //                    System.out.print("Construct Evvent\n");
                    return event;
                }).assignTimestampsAndWatermarks(WatermarkStrategy
                        .<socialMediaStocks>forBoundedOutOfOrderness(Duration.ofDays(3000))
                       .withTimestampAssigner(new SerializableTimestampAssigner<socialMediaStocks>() {
                            @Override
                            public long extractTimestamp(socialMediaStocks element, long recordTimestamp) {
//                                System.out.print("Timestamp: " + recordTimestamp+"\n");
//                                System.out.print(element.getDate().getTime()+"\n");
                                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                                return element.getDate().getTime();
                            }
                        })
                )

                .keyBy(r->r.symbol);

//        查看keyby效果
//        stream.addSink(new PartitionSink());

                SimpleCondition<socialMediaStocks> priceRise = new SimpleCondition<socialMediaStocks>() {
            @Override
            public boolean filter(socialMediaStocks event) {
                return event.getPriceChange() > 0.01;
            }
        };
//        Pattern<socialMediaStocks, socialMediaStocks> pattern1 = Pattern.<socialMediaStocks>begin("first")
//        .where(new SimpleCondition<socialMediaStocks>() {
//            @Override
//            public boolean filter(socialMediaStocks value) throws Exception {
//                return value.high>1;
//            }
//        });
//        .next("second")
//        .where(priceRise)
//        .next("third")
//        .where(priceRise);
        Pattern<socialMediaStocks,socialMediaStocks> pattern1 = Pattern.<socialMediaStocks>begin("first").where(
                new SimpleCondition<socialMediaStocks>() {
                    @Override
                    public boolean filter(socialMediaStocks value) throws Exception {

                        return value.symbol.equals("FB");
                    }
                }
        );

        //应用到流上
        PatternStream <socialMediaStocks> patternStream=CEP.pattern(stream,pattern1);
        // 4. 将匹配到的复杂事件选择出来，然后包装成字符串报警信息输出
        patternStream.select(
                new PatternSelectFunction<socialMediaStocks, String >() {
                    @Override
                    public String select(Map<String, List<socialMediaStocks>> pattern) throws Exception {
                        socialMediaStocks event1st =pattern.get("first").get(0);
//                        System.out.print("?");
                        return event1st.toString();
                    }


                }
        ).print();
        Pattern<socialMediaStocks, socialMediaStocks> pattern2 =Pattern.<socialMediaStocks>begin("start").where(new SimpleCondition<socialMediaStocks>() {
            @Override
            public boolean filter(socialMediaStocks value) throws Exception {
                System.out.print("first");
                return true;
            }
        }).next("end").where(new SimpleCondition<socialMediaStocks>() {
            @Override
            public boolean filter(socialMediaStocks end) throws Exception {
                double threshold = 1.01;
                return end.close >= threshold * end.close;
            }
        });

        PatternStream<socialMediaStocks> patternStream2 = CEP.pattern(stream, pattern2);
//        System.out.print("???");

        DataStream<String> result = patternStream2.select(new RichPatternSelectFunction<socialMediaStocks, String>() {

            @Override
            public void open(Configuration config) throws Exception {
                super.open(config);

                // 运行时上下文
                RuntimeContext context = getRuntimeContext();

                // 获取子任务索引
                int subtaskIndex = context.getIndexOfThisSubtask();

                // 打印索引
                System.out.println(subtaskIndex);
            }
            @Override
            public String select(Map<String, List<socialMediaStocks>> pattern) throws Exception {
                System.out.print("Select");
                return pattern.get("start").get(0) + " -> " + pattern.get("end").get(0);
            }
        });

        result.print();
        Pattern<socialMediaStocks,socialMediaStocks> pattern3 = Pattern.<socialMediaStocks>begin("first")     // 以第一个登录失败事件开始
                .where(new SimpleCondition<socialMediaStocks> () {

                    @Override
                    public boolean filter(socialMediaStocks event)   throws Exception {
//                        System.out.print("first\n");
//                        System.out.print(loginEvent.ipAddress);
//                        Long threadId = Thread.currentThread().getId();
//                        System.out.println(":::"+threadId);
//                        RuntimeContext ctx= getRuntimeContext();



                        return event.low>1.0;
                    }
                })
                .next("second")    // 接着是第二个登录失败事件
                .where(new SimpleCondition<socialMediaStocks>() {
                    @Override
                    public boolean filter(socialMediaStocks socialMediaStocks) throws Exception {
                        return socialMediaStocks.high>1.0;
                    }
                })
                .next("third")     // 接着是第三个登录失败事件
                .where(new SimpleCondition<socialMediaStocks>() {
                    @Override
                    public boolean filter(socialMediaStocks socialMediaStocks) throws Exception {
                        return socialMediaStocks.close>1.0;
                    }
                });

        // 3. 将Pattern应用到流上，检测匹配的复杂事件，得到一个PatternStream
        PatternStream<socialMediaStocks> patternStream3= CEP.pattern(stream, pattern3);

        // 4. 将匹配到的复杂事件选择出来，然后包装成字符串报警信息输出
        patternStream
                .select(new RichPatternSelectFunction<socialMediaStocks, String>() {
                    //                            继承RichPatternSelectFunction，输出并行的子任务序号
                    @Override
                    public void open(Configuration config) throws Exception {
                        super.open(config);

                        // 运行时上下文
                        RuntimeContext context = getRuntimeContext();

                        // 获取子任务索引
                        int subtaskIndex = context.getIndexOfThisSubtask();

                        // 打印索引
                        System.out.println(subtaskIndex);
                    }
                    @Override
                    public String select(Map<String, List<socialMediaStocks>> map) throws Exception {
                        socialMediaStocks first = map.get("first").get(0);

                        socialMediaStocks second = map.get("second").get(0);
                        socialMediaStocks third = map.get("third").get(0);
                        return first.symbol + " " + first.date + " " + first.close + " " + first.high + " " + first.low + " " + first.open + " " + first.volume + " " + first.adjClose + " " +  " " + first.volume + " " + first.volume + " " ;
                    }
                })
                .print("warning");

//        stream.print();
        env.execute();
    }

}
