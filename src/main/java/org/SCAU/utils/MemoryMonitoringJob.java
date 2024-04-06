//package org.SCAU.utils;
//
//import org.apache.flink.api.common.functions.RichMapFunction;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.metrics.Counter;
//import org.apache.flink.metrics.Gauge;
//import org.apache.flink.runtime.taskexecutor.TaskExecutorGateway;
//import org.apache.flink.runtime.taskmanager.TaskManagerLocation;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//
//public class MemoryMonitoringJob extends RichMapFunction<String, String> {
//    private transient Counter heapMemoryCounter;
//    private transient Gauge<Double> offHeapMemoryGauge;
//
//    @Override
//    public void open(Configuration parameters) {
//        heapMemoryCounter = getRuntimeContext().getMetricGroup().counter("heap-memory");
//        offHeapMemoryGauge = getRuntimeContext().getMetricGroup().gauge("off-heap-memory", this::getOffHeapMemory);
//    }
//
//    @Override
//    public String map(String input) throws Exception {
//        heapMemoryCounter.inc(10);
//        return input;
//    }
//
//    private double getOffHeapMemory() {
//        // 获取当前任务的 TaskManagerLocation
//        TaskManagerLocation taskManagerLocation = getRuntimeContext().getTaskManagerLocation();
//        ResourceID resourceId = taskManagerLocation.getResourceID();
//
//        // 获取 TaskExecutorGateway
//        TaskExecutorGateway taskExecutorGateway = getRuntimeContext().getTaskExecutorGateway();
//
//        // 通过 TaskExecutorGateway 获取离堆内存使用情况
//        return taskExecutorGateway.requestTaskManagerMetrics(resourceId).get().getOffHeapMemoryUsage();
//    }
//}