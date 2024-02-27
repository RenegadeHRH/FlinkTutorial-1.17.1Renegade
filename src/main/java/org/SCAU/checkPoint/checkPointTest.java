package org.SCAU.checkPoint;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class checkPointTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(1000);
        // 开启 Checkpoint，每 1000毫秒进行一次 Checkpoint
        env.enableCheckpointing(1000);

        // Checkpoint 语义设置为 EXACTLY_ONCE
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        // CheckPoint 的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);

        // 同一时间，只允许 有 1 个 Checkpoint 在发生
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

        // 两次 Checkpoint 之间的最小时间间隔为 500 毫秒
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);

        // 当 Flink 任务取消时，保留外部保存的 CheckPoint 信息
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 当有较新的 Savepoint 时，作业也会从 Checkpoint 处恢复
//        env.getCheckpointConfig().setPreferCheckpointForRecovery(true);

        // 作业最多允许 Checkpoint 失败 1 次（flink 1.9 开始支持）
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(1);

        // Checkpoint 失败后，整个 Flink 任务也会失败（flink 1.9 之前）
//        env.getCheckpointConfig.setFailTasksOnCheckpointingErrors(true);
    }


}
