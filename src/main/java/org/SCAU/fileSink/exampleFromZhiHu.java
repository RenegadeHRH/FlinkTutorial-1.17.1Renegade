package org.SCAU.fileSink;

import org.SCAU.SerializerDeserializer.socialStockSerializerDeserializer;
import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
/**
 * Flink Stream 流计算，将DataStream 保存至文件系统，使用FileSystem Connector
 */
public class exampleFromZhiHu {
    public static void main(String[] args) throws Exception{
// 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

// 启用Checkpoint，时间间隔为10秒
        env.enableCheckpointing(TimeUnit.SECONDS.toMillis(10));

// 2. 数据源：source
//        DataStreamSource<String> inputDataStream = env.socketTextStream("node1.itcast.cn", 9999);
        String intTopic = "test";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.199.165:5092");
        properties.setProperty("group.id", "test");
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<socialMediaStocks2>(
                intTopic, new socialStockSerializerDeserializer(), properties
        );
        consumer.setStartFromLatest();
        DataStream<socialMediaStocks2> inputDataStream = env.addSource(consumer);
// 4. 数据终端-sink
// a. 输出文件名称设置
        OutputFileConfig config = OutputFileConfig
                .builder()
                .withPartPrefix("prefix")
                .withPartSuffix(".txt")
                .build();
// b. FlinkSink对象创建
        FileSink<socialMediaStocks2> fileSink = FileSink
                .forRowFormat(
                        new Path("output/fileSinkTest"),
                        new SimpleStringEncoder<socialMediaStocks2>("UTF-8") ) //
/**
 * 设置桶分配政策
 * DateTimeBucketAssigner --默认的桶分配政策，默认基于时间的分配器，每小时产生一个桶，格式如下yyyy-MM-dd--HH
 * BasePathBucketAssigner ：将所有部分文件（part file）存储在基本路径中的分配器（单个全局桶）
 */
                .withBucketAssigner(new DateTimeBucketAssigner<>())
/**
 * 有三种滚动政策
 * CheckpointRollingPolicy
 * DefaultRollingPolicy
 * OnCheckpointRollingPolicy
 */
                .withRollingPolicy(
/**
 * 滚动策略决定了写出文件的状态变化过程
 * 1. In-progress ：当前文件正在写入中
 * 2. Pending ：当处于 In-progress 状态的文件关闭（closed）了，就变为 Pending 状态
 * 3. Finished ：在成功的 Checkpoint 后，Pending 状态将变为 Finished 状态
 * 观察到的现象
 * 1.会根据本地时间和时区，先创建桶目录
 * 2.文件名称规则：part-<subtaskIndex>-<partFileIndex>
 * 3.在macos中默认不显示隐藏文件，需要显示隐藏文件才能看到处于In-progress和Pending状态的文件，
 因为文件是按照.开头命名的
 *
 */
                        DefaultRollingPolicy
                                .builder()
                                .withRolloverInterval(TimeUnit.SECONDS.toMillis(2)) //设置滚动间隔
                                .withInactivityInterval(TimeUnit.SECONDS.toMillis(1)) //设置不活动时间间隔
                                .withMaxPartSize(1024 * 1024 * 1024) // 最大尺寸
                                .build()
                )
                .withOutputFileConfig(config)
                .build();
// c. 添加Sink
//        inputDataStream.print();
        inputDataStream.sinkTo(fileSink);
// 5. 触发执行-execute
        env.execute();
    } }