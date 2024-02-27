package org.SCAU.fileSink;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.file.sink.compactor.DecoderBasedReader;
import org.apache.flink.connector.file.sink.compactor.FileCompactStrategy;
import org.apache.flink.connector.file.sink.compactor.RecordWiseFileCompactor;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.source.datagen.DataGeneratorSource;
import org.apache.flink.streaming.api.functions.source.datagen.RandomGenerator;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class fileSinkTest {
    public static void main(String[] args) throws Exception {

        final String outputBasePath = "output/fileSinkTest";

        // 设置环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final ExecutionConfig config = env.getConfig();
        config.setAutoWatermarkInterval(1000L);
        config.enableObjectReuse();

        // check point
        env.enableCheckpointing(20000L, CheckpointingMode.AT_LEAST_ONCE);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(20000L);
        env.getCheckpointConfig().setCheckpointTimeout(120000L);

        env.setParallelism(1);
        env.disableOperatorChaining();

        // source
        final SingleOutputStreamOperator<String> source = env.addSource(
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
                                , 10000, 1000000L)
                )
                .returns(Types.STRING)
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofMinutes(1))
                                .withTimestampAssigner((SerializableTimestampAssigner<String>) (message, l) -> Long.parseLong(message.split("\\|", -1)[1]))
                                .withIdleness(Duration.ofMinutes(1))
                )
                .returns(Types.STRING);

        final FileSink<String> fileSink = FileSink
                .forRowFormat(new Path(outputBasePath), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofMinutes(1))
                                .withInactivityInterval(Duration.ofMinutes(1))
                                .withMaxPartSize(MemorySize.ofMebiBytes(1))
                                .build()
                )
                .withBucketAssigner(new BucketAssigner<String, String>() {
                    @Override
                    public String getBucketId(String element, Context context) {
                        final long l = Long.parseLong(element.split("\\|", -1)[1]);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'p_date='yyyy-MM-dd/'p_hour='HH");
                        final String format = simpleDateFormat.format(new Date(l));
                        System.out.println("format = " + format);
                        return format;
                    }

                    @Override
                    public SimpleVersionedSerializer<String> getSerializer() {
                        return SimpleVersionedStringSerializer.INSTANCE;
                    }
                })
                .withOutputFileConfig(new OutputFileConfig("ourPrefix", "ourSuffix"))
//                .enableCompact(
//                        FileCompactStrategy.Builder.newBuilder()
//                                .setNumCompactThreads(1)
//                                .setSizeThreshold(MemorySize.ofMebiBytes(5).getBytes())
//                                .enableCompactionOnCheckpoint(5)
//                                .build(),
//                        new RecordWiseFileCompactor<>(new DecoderBasedReader.Factory<>(SimpleStringDecoder::new))
//                )
                .build();

        source.sinkTo(fileSink).uid("fileSink");
        source.print();
        env.execute();
    }

}
