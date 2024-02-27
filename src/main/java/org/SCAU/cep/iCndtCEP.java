package org.SCAU.cep;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;
import java.util.Map;


public class iCndtCEP {
    public static void main(String[] args) throws Exception {
        // 获取执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();



        // 设置并行度
        env.setParallelism( 1);

        // 输入流
        DataStream<Tuple2<String, Integer>> input = env.fromElements(
                Tuple2.of("a", 1), Tuple2.of("b", 2),
                Tuple2.of("a", 2), Tuple2.of("b", 3),
                Tuple2.of("a", 3), Tuple2.of("b", 4)
        );

        // 定义迭代条件模式
        Pattern<Tuple2<String, Integer>, ?> pattern = Pattern.<Tuple2<String, Integer>>begin("start")
                .where(new IterativeCondition<Tuple2<String, Integer>>() {
                    @Override
                    public boolean filter(Tuple2<String, Integer> value, Context<Tuple2<String, Integer>> ctx) throws Exception {
                        // 每个符合条件的事件将触发迭代
                        return value.f1 % 2 == 0; // 偶数条件
                    }
                });

        // 将模式应用于输入流
        PatternStream<Tuple2<String, Integer>> patternStream = CEP.pattern(input, pattern).inProcessingTime();

        // 选择符合条件的事件
        DataStream<String> result = patternStream.select(new PatternSelectFunction<Tuple2<String, Integer>, String>() {
            @Override
            public String select(Map<String, List<Tuple2<String, Integer>>> pattern) throws Exception {

                StringBuilder builder = new StringBuilder();
                builder.append("Pattern matched: ");
                List<Tuple2<String, Integer>> start = pattern.get("start");
                for (Tuple2<String, Integer> event : start) {
                    builder.append(event).append(",");
                }
                return builder.toString();
            }
        });

        // 打印结果
        result.print();

        // 执行程序
        env.execute("Flink CEP Iterative Condition Example");

    }
}
