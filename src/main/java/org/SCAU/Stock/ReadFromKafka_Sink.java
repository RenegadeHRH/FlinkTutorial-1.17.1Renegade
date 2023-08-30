package org.SCAU.Stock;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.IOException;
import java.util.Properties;

public class ReadFromKafka_Sink {

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

        // 将字符串数据映射为StockEvent对象
        DataStream<StockEvent> stockEventStream = kafkaStream.map(line -> {
            String[] parts = line.split(" ");
            return new StockEvent(parts[0], parts[1], parts[2]);
        });

        // 根据名称进行分组，然后进行计数
        stockEventStream

                .addSink(new PrintSinkFunction<>());

        // 执行任务
        env.execute("Stock Event Processing");
    }

}
