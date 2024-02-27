package org.SCAU.fileSink;
//没有水位线不影响文件正常命名
import org.SCAU.SerializerDeserializer.socialStockSerializerDeserializer;
import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.state.CheckpointListener;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.streaming.api.functions.source.datagen.DataGeneratorSource;
import org.apache.flink.streaming.api.functions.source.datagen.RandomGenerator;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class fileSinkNoWaterMark {
    static public class CheckpointListenerExample implements CheckpointListener {
        @Override
        public void notifyCheckpointComplete(long checkpointId) throws Exception {
            System.out.println("Checkpoint completed, id: " + checkpointId);
        }
    }

    public static void main(String[] args) throws Exception {

        final String outputBasePath = "output/fileSinkTest";

        // 设置环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setAutoWatermarkInterval(5*1000);
        final ExecutionConfig config = env.getConfig();
        config.enableObjectReuse();

        // check point
        env.enableCheckpointing(2000L, CheckpointingMode.AT_LEAST_ONCE);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(2000L);
        env.getCheckpointConfig().setCheckpointTimeout(1200L);
        env.setParallelism(1);
        env.disableOperatorChaining();
        //kafka消费者参数
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        String intTopic = "test";
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<socialMediaStocks2>(
                intTopic, new socialStockSerializerDeserializer(), properties
        );
        consumer.setStartFromLatest();
        // source
        final DataStream<String> source = env.addSource(
                        new DataGeneratorSource<>(
                                new RandomGenerator<String>() {
                                    @Override
                                    public String next() {
                                        return String.join("|"
                                                , "" + random.nextInt(0, 1)
                                                , "" + (System.currentTimeMillis() + random.nextInt(-1000 * 60, 1000 * 60))
                                        );
                                    }
                                }
                                , 10000, 100000L)
                )
                .returns(Types.STRING);
//        DataStream<socialMediaStocks2> source = env.addSource(consumer);
        final FileSink<String> fileSink = FileSink
                .forRowFormat(new Path(outputBasePath), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofSeconds(10))
                                .withInactivityInterval(Duration.ofSeconds(10))
                                .withMaxPartSize(MemorySize.ofMebiBytes(10))
                                .build()
//                        OnCheckpointRollingPolicy.build()
                )

                .withOutputFileConfig(new OutputFileConfig("ourPrefix", "ourSuffix"))

                .build();

        source.sinkTo(fileSink).uid("fileSink");
//        source.print();
        env.execute();
    }
}
