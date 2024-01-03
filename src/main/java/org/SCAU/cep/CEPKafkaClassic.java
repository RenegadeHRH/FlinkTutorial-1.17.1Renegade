package org.SCAU.cep;

import org.SCAU.SerializerDeserializer.socialStockSerializerDeserializer;
import org.SCAU.model.socialMediaStocks;
import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

public class CEPKafkaClassic {

    public static void main(String[] args) {
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        String intTopic = "test";
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<socialMediaStocks2>(intTopic, new socialStockSerializerDeserializer(), properties);
        consumer.setStartFromLatest();
        DataStream<socialMediaStocks2> input = streamEnv.addSource(consumer);
        Pattern<socialMediaStocks2, ?> pattern = Pattern.<socialMediaStocks2>begin("start").where(
                new SimpleCondition<socialMediaStocks2>() {
                    @Override
                    public boolean filter(socialMediaStocks2 stock) {
//                        System.out.println("here");
                        return Float.valueOf(stock.high)>10;
                    }
                }
        ).next("bottom").where(
                new SimpleCondition<socialMediaStocks2>() {
                    @Override
                    public boolean filter(socialMediaStocks2 stock) {
                        return Float.valueOf(stock.close)<10;

                    }
                }
        ).next("up").where(
                new SimpleCondition<socialMediaStocks2>() {
                    @Override
                    public boolean filter(socialMediaStocks2 Stock) {
                        return Float.valueOf(Stock.volume)> 10;
//                        return true;
                    }
                }

        );
        DataStream<String> result = CEP.pattern(input, pattern)
                .inProcessingTime()
                .flatSelect(
                        (p, o) -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append("\n");
                            builder.append(p.get("start").get(0))
                                    .append(",\n")
                                    .append(p.get("bottom").get(0))
                                    .append(",\n")
                                    .append(p.get("up").get(0));

                            o.collect(builder.toString());
                        },
                        Types.STRING);
        result.print();
        try {
            streamEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(topicOut, new SimpleStringSchema(), properties);

}
