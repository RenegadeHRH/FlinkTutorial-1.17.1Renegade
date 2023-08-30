package org.SCAU.Stock;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;

import java.text.DecimalFormat;
import java.util.Properties;

public class StockEvent_EventTimeWatermark {


    public static void main(String[] args) throws Exception {
        // 设置Flink执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 开启检查点以实现容错
        env.enableCheckpointing(5000).setParallelism(5);

        // Kafka配置
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "master:9092");
        properties.setProperty("group.id", "hrh");

        // 创建一个Kafka消费者
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                "test15",
                new SimpleStringSchema(),
                properties
        );

        // 从Kafka读取数据
        DataStream<String> kafkaStream = env.addSource(kafkaConsumer);

        // 将字符串数据映射为StockEvent对象，并配置事件时间水位线
        DataStream<StockEvent> stockEventStream = kafkaStream
                .map(line -> {
                    String[] parts = line.split(" ");
                    return new StockEvent(parts[0], parts[1], parts[2]);
                })
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<StockEvent>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<StockEvent>() {
                                    @Override
                                    public long extractTimestamp(StockEvent event, long recordTimestamp) {
                                        return event.timeStamp;
                                    }
                                })
                );

        // 根据名称进行分组，然后对价格字段进行累加
        stockEventStream
//                .keyBy(StockEvent::getName)
//                .sum("price")
//                .map(event -> event.toString() + " Event Time: " + new DecimalFormat("0").format(event.timeStamp))
                .print();


        // 执行任务
        env.execute("Stock Event Processing");
    }
}
