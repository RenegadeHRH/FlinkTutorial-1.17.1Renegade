//package org.SCAU.moa.Flink;
//
//
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.ProcessFunction;
//import org.apache.flink.util.Collector;
//import org.apache.flink.util.OutputTag;
//
//import com.yahoo.labs.samoa.instances.Instance;
//
//import moa.classifiers.Classifier;
//import moa.classifiers.trees.HoeffdingTree;
//import moa.core.Example;
//public class TrainTestRun {
//
//    public static void main(String[] args) throws Exception {
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        DataStream<Example<Instance>> rrbfSource = env.addSource(new RRBFSource());
//
//        OutputTag<Example<Instance>> testOutputTag = new OutputTag<>("test") {};
//        OutputTag<Example<Instance>> trainOutputTag = new OutputTag<>("train") {};
//
//        DataStream<Example<Instance>> splitStream = rrbfSource
//                .process(new RandomSamplingProcessFunction(0.02, testOutputTag, trainOutputTag));
//
//        DataStream<Example<Instance>> testStream = splitStream.getSideOutput(testOutputTag);
//        DataStream<Example<Instance>> trainStream = splitStream.getSideOutput(trainOutputTag);
//
//        SingleOutputStreamOperator<Classifier> classifier = trainStream
//                .process(new moa.flink.traintest.LearningProcessFunction(HoeffdingTree.class, 1000));
//
//        DataStream<Tuple2<Example<Instance>, Classifier>> classifiedAndUpdatedStream = testStream
//                .connect(classifier)
//                .process(new ClassifyAndUpdateClassifierFunction());
//
//        DataStream<PerformanceMetrics> performanceStream = classifiedAndUpdatedStream
//                .countWindowAll(10_000)
//                .aggregate(new PerformanceAggregator());
//
//        performanceStream.print();
//
//        env.execute("MOA Flink - MeetUp Krakow - January 16th 2018");
//    }
//}