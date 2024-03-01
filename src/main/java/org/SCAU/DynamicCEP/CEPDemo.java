package org.SCAU.DynamicCEP;

import org.SCAU.DynamicCEP.POJOs.PatternPOJO;
import org.SCAU.DynamicCEP.Patterns.Singles;
import org.SCAU.SerializerDeserializer.socialStockSerializerDeserializer;
import org.SCAU.model.socialMediaStocks2;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.BasePathBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.InstantiationUtil;

import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

//---测试数据---
//数据源：kafka
//输入：socialMediaStocks2
//-------------
//
public class CEPDemo {



    public static void main(String[] args) {
//流数据环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        final ExecutionConfig config = streamEnv.getConfig();
        config.enableObjectReuse();
//        设置CheckPoint，让文件sink完整输出
        streamEnv.enableCheckpointing(20000L, CheckpointingMode.EXACTLY_ONCE);
        streamEnv.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        streamEnv.getCheckpointConfig().setMinPauseBetweenCheckpoints(20000L);
        streamEnv.getCheckpointConfig().setCheckpointTimeout(120000L);
        streamEnv.setParallelism(1);
        streamEnv.disableOperatorChaining();
        //kafka消费者参数
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        String intTopic = "test";
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<socialMediaStocks2>(
                intTopic, new socialStockSerializerDeserializer(), properties
        );
        consumer.setStartFromLatest();
        DataStream<socialMediaStocks2> input = streamEnv.addSource(consumer);
//        使用组合pattern
        Pattern<socialMediaStocks2,?> pattern = new Singles().getcompoundPattern();
        DataStream<String> result = CEP.pattern(input, pattern)
                .inProcessingTime()
                .flatSelect(
                        (p, o) -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append("\n");
                            builder.append(p.get("start").get(0))
                                    .append(",\n")
                                    .append(p.get("second").get(0))
                                    .append(",\n");

                            o.collect(builder.toString());
                        },
                        Types.STRING);

//        StreamingFileSink<String> sink = StreamingFileSink
//                .forRowFormat(new Path("output/"), new SimpleStringEncoder<String>("UTF-8"))
//                .withRollingPolicy(DefaultRollingPolicy.builder()
//                        .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
//                        .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
//                        .withMaxPartSize(1024 * 1024 * 1024)
//                        .build())
//                .build();

        FileSink<String> sink2=FileSink
                .forRowFormat(new Path("output/"), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(DefaultRollingPolicy.builder()
                        .withRolloverInterval(Duration.ofMinutes(1))
                        .withInactivityInterval(Duration.ofMinutes(1))
                        .withMaxPartSize(MemorySize.ofMebiBytes(1))
                        .build())
                .withOutputFileConfig(new OutputFileConfig("stream1",""))
                .build();
        result.sinkTo(sink2).uid("CEPDeomo");
//        String streamName = "1"; // 从流中获取实际的名称
//        String newFileName = "output_" + streamName + ".txt"; // 根据需要自定义文件名格式
//        result.addSink(sink).name(newFileName);
        result.print();

        try {
            streamEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
