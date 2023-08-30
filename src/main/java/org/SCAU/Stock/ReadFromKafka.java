package org.SCAU.Stock;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.IOException;
import java.util.Properties;

public class ReadFromKafka {

    public static void main(String[] args) throws Exception {
        // 设置Flink执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

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
        env.setParallelism(5);

        // 将字符串数据映射为StockEvent对象
        DataStream<StockEvent> stockEventStream = kafkaStream.map(line -> {
            String[] parts = line.split(" ");
            return new StockEvent(parts[0], parts[1], parts[2]);
        });

        // 根据名称进行分组，然后进行计数
        stockEventStream
                .print();
        // 要等待执行完毕才会输出，等待时长相当长
        // 执行任务
        env.execute("Stock Event Processing");
    }

}
