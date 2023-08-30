package org.SCAU.Stock;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class KafkaReaderTest {

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

        // 直接输出数据
        kafkaStream.print();

        // 执行任务
        env.execute("Kafka Reader Test");
    }
}
