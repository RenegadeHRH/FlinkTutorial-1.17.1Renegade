package org.SCAU.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

public class kafkaSource {
    public static void main(String[] args) throws Exception {
//        Configuration conf = new Configuration();
//        conf.set(RestOptions.BIND_PORT,"8082");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // TODO 从Kafka读： 新Source架构
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.199.165:5092") // 指定kafka节点的地址和端口
                .setTopics("test")   // 指定消费的 Topic
                .setValueOnlyDeserializer(new SimpleStringSchema()) // 指定 反序列化器，这个是反序列化value
                .setStartingOffsets(OffsetsInitializer.latest())  // flink消费kafka的策略
                .build();


        env
//                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafkasource")
                .fromSource(kafkaSource, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)), "kafkasource")
                .print();


        env.execute();
    }
}
