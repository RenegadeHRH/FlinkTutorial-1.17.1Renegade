package org.SCAU.checkPoint;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class checkPointListener {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 开启检查点，并设置检查点间隔
        env.enableCheckpointing(5000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        // 添加数据源和数据处理逻辑
        DataStream<String> stream = env.addSource(new MySourceFunction());

        // 数据处理逻辑
        stream.map(new MyMapFunction()).addSink(new MySinkFunction());

        env.execute("Checkpoint Listener Example");
    }

    // 数据源示例
    public static class MySourceFunction implements SourceFunction<String> {
        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            // Your source logic goes here
            // For the sake of example, emitting some data
            ctx.collect("Example Data");
        }

        @Override
        public void cancel() {

        }
    }

    // 自定义 MapFunction 示例
    public static class MyMapFunction implements MapFunction<String, String> {
        @Override
        public String map(String value) throws Exception {
            // Your mapping logic goes here
            return value.toUpperCase(); // For example, converting input to upper case
        }
    }

    // 自定义 SinkFunction 示例
    public static class MySinkFunction implements SinkFunction<String> {
        @Override
        public void invoke(String value, Context context) throws Exception {
            // Your sink logic goes here
            // For the sake of example, printing the value
            System.out.println(value);
        }
    }
}
