package org.SCAU.cep;

import org.SCAU.DynamicCEP.Patterns.patternsForLoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.RichPatternSelectFunction;
import org.apache.flink.cep.pattern.GroupPattern;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class LoginFailDetectExample {
    /**
     * 登录失败检测示例
     */
    public static void main(String[] args) throws Exception {
        // 创建流执行环境并设置并行度为1
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        // 1. 获取登录事件流，并提取时间戳、生成水位线
        KeyedStream<LoginEvent, String> stream = env
                .fromElements(
                        new LoginEvent("user_1", "192.168.0.1", "fail", 2000L),
                        new LoginEvent("user_1", "192.168.0.2", "fail", 3000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 4000L),
                        new LoginEvent("user_1", "171.56.23.10", "success", 4500L),
                        new LoginEvent("user_1", "171.56.23.10", "fail", 5000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 7000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 8000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 8500L),
                        new LoginEvent("user_2", "192.168.1.29", "success", 6000L),
                        new LoginEvent("user_2", "192.168.1.29", "success", 9000L)

                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<LoginEvent>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<LoginEvent>() {
                                            @Override
                                            public long extractTimestamp(LoginEvent loginEvent, long l) {
                                                return loginEvent.timestamp;
                                            }
                                        }
                                )
                )
                .keyBy(r -> r.userId);

        // 2. 定义Pattern，连续的三个登录失败事件
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("first")     // 以第一个登录失败事件开始
                .where(new SimpleCondition<LoginEvent> () {

                    @Override
                    public boolean filter(LoginEvent loginEvent)   throws Exception {
//                        System.out.print("first\n");
//                        System.out.print(loginEvent.ipAddress);
//                        Long threadId = Thread.currentThread().getId();
//                        System.out.println(":::"+threadId);
//                        RuntimeContext ctx= getRuntimeContext();



                        return loginEvent.eventType.equals("fail");
                    }
                })
                .next("second")    // 接着是第二个登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                })
                .next("third")     // 接着是第三个登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                });
        Pattern<LoginEvent ,?> cp = new patternsForLoginEvent().getCompoundPattern();
        // 3. 将Pattern应用到流上，检测匹配的复杂事件，得到一个PatternStream
        PatternStream<LoginEvent> patternStream = CEP.pattern(stream, cp);

        // 4. 将匹配到的复杂事件选择出来，然后包装成字符串报警信息输出
        patternStream
//                .select(new PatternSelectFunction<LoginEvent, String>() {
//                    @Override
//                    public String select(Map<String, List<LoginEvent>> map) throws Exception {
//                        LoginEvent first = map.get("first").get(0);
//                        LoginEvent second = map.get("second").get(0);
//                        LoginEvent third = map.get("third").get(0);
//                        return first.userId + " 连续三次登录失败！登录时间：" + first.timestamp + ", " + second.timestamp + ", " + third.timestamp;
//                    }
//                })
//                .print("warning");
                        .select(new RichPatternSelectFunction<LoginEvent, String>() {
//                            继承RichPatternSelectFunction，输出并行的子任务序号
                            @Override
                            public void open(Configuration config) throws Exception {
                                super.open(config);

                                // 运行时上下文
                                RuntimeContext context = getRuntimeContext();

                                // 获取子任务索引
                                int subtaskIndex = context.getIndexOfThisSubtask();

                                // 打印索引
                                System.out.println(subtaskIndex);
                            }
                        @Override
                        public String select(Map<String, List<LoginEvent>> map) throws Exception {
                            LoginEvent first = map.get("first").get(0);

                            LoginEvent second = map.get("second").get(0);
                            LoginEvent third = map.get("third").get(0);
                            return first.userId + " 连续三次登录失败！登录时间：" + first.timestamp + ", " + second.timestamp + ", " + third.timestamp;
                        }
                })
                .print("warning");
        env.execute();
    }
}
