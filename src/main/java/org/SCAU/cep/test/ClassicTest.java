package org.SCAU.cep.test;

import org.SCAU.SerializerDeserializer.socialStockSerializerDeserializer;
import org.SCAU.model.socialMediaStocks;
import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class ClassicTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        String intTopic = "test";
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<socialMediaStocks2>(intTopic, new socialStockSerializerDeserializer(), properties);
        consumer.setStartFromLatest();
        DataStream<socialMediaStocks2> input = streamEnv.addSource(consumer);
        input.print();
        try {
            streamEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
