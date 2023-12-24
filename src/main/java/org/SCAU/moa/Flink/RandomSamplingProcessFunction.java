package org.SCAU.moa.Flink;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import com.yahoo.labs.samoa.instances.Instance;

import moa.core.Example;

class RandomSamplingProcessFunction extends ProcessFunction<Example<Instance>, Example<Instance>> {

    private final double split;
    private final OutputTag<Example<Instance>> testOutputTag;
    private final OutputTag<Example<Instance>> trainOutputTag;

    public RandomSamplingProcessFunction(double split, OutputTag<Example<Instance>> testOutputTag, OutputTag<Example<Instance>> trainOutputTag) {
        this.split = split;
        this.testOutputTag = testOutputTag;
        this.trainOutputTag = trainOutputTag;
    }

    @Override
    public void processElement(Example<Instance> value, Context ctx, Collector<Example<Instance>> out) throws Exception {
        // Random sampling
        if (Math.random() < split) {
            ctx.output(testOutputTag, value); // Send to test output
        } else {
            ctx.output(trainOutputTag, value); // Send to train output
        }
    }
}