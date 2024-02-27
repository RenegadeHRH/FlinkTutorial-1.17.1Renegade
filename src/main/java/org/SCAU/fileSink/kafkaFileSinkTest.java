package org.SCAU.fileSink;

import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.source.datagen.RandomGenerator;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class kafkaFileSinkTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(30000);
        env.getCheckpointConfig().setCheckpointTimeout(60000); // checkpoint超时时间
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1); // 同时只允许一个checkpoint
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000); // 两个checkpoint间最小间隔
        env.getConfig().setAutoWatermarkInterval(2000);

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.199.165:5092");
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), props);
//        final DataStream<String> source = env.addSource(
//                        new DataGeneratorSource<>(
//                                new RandomGenerator<String>() {
//                                    @Override
//                                    public String next() {
//                                        return String.join("|"
//                                                , "" + random.nextInt(0, 1)
//                                                , "" + (System.currentTimeMillis() + random.nextInt(-1000 * 60, 1000 * 60))
//                                        );
//                                    }
//                                }
//                                , 10000, 100000L)
//                )
//                .returns(Types.STRING);
        DataStream<String> stream = env.addSource(kafkaSource);

        FileSink<String> sink = FileSink
                .forRowFormat(new Path("output/kafkafile"), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofSeconds(10))
                                .withInactivityInterval(Duration.ofSeconds(10))
                                .withMaxPartSize(MemorySize.ofMebiBytes(1000))
                                .build()
//                        OnCheckpointRollingPolicy.build()
                )
                .build();

        stream.sinkTo(sink);

        env.execute();
    }
}