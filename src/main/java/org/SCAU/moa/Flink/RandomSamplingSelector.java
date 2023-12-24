package org.SCAU.moa.Flink;
//import java.util.Collections;
//import java.util.Random;
//
//import org.apache.commons.math3.random.MersenneTwister;
//
//import org.apache.flink.util.Collector;
//import org.apache.flink.util.OutputTag;
//import com.yahoo.labs.samoa.instances.Instance;
//import org.apache.flink.streaming.api.functions.ProcessFunction;
//import org.apache.flink.util.OutputTag;
//import moa.core.Example;
//
//
//public class RandomSamplingSelector extends ProcessFunction<Example<Instance>, Example<Instance>> {
//
//    private OutputTag<Example<Instance>> samplingTag;
//    private double split;
//    //...
//    Random rand = new Random();
//    @Override
//    public void processElement(Example<Instance> value, Context ctx, Collector<Example<Instance>> out) {
//        // 随机抽样逻辑
//        if (rand.nextDouble() < split) {
//            ctx.output(samplingTag, value);
//        } else {
//            out.collect(value);
//        }
//    }
//}

//import java.util.Collections;
//
//import org.apache.commons.math3.random.MersenneTwister;
//import org.apache.flink.streaming.api.collector.selector.OutputSelector;
//
//import com.yahoo.labs.samoa.instances.Instance;
//
//import moa.core.Example;
//
//public class RandomSamplingSelector implements OutputSelector<Example<Instance>> {
//
//    //names of the streams
//    public final static String TEST = "test";
//    public final static String TRAIN = "train";
//
//    public final static Iterable<String> TEST_LIST = Collections.singletonList(TEST);
//    public final static Iterable<String> TRAIN_LIST = Collections.singletonList(TRAIN);
//
//    private static final long serialVersionUID = 1L;
//    private double split;
//    private MersenneTwister rand = new MersenneTwister(11);
//
//    public RandomSamplingSelector(double split) {
//        this.split = split;
//    }
//
//    @Override
//    public Iterable<String> select(Example<Instance> value) {
//        //random sampling
//        if (rand.nextFloat() < split) {
//            return TEST_LIST;
//        }
//        return TRAIN_LIST;
//    }
//}