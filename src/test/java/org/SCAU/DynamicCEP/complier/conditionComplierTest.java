package org.SCAU.DynamicCEP.complier;

import junit.framework.TestCase;
import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.Parser.classParser;
import org.SCAU.DynamicCEP.Parser.conditionParser;
import org.SCAU.DynamicCEP.Patterns.singles2;
import org.SCAU.model.stockSerializable;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.RichPatternSelectFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.SCAU.DynamicCEP.dataSource.parseJsonFile.parseJonFile;

public class conditionComplierTest extends TestCase {

    public void testExtractMethod() throws Exception {

        stockSerializable event = new stockSerializable("2022-04-04", "PINS", "7.399999618530273","27.399999618530273", "27.950000762939453", "25.5", "25.5", "16858000.0");
        conditionComplier c = new conditionComplier(new simpleCondition.BinaryExpression("f:e.getHigh() < 27.950000762939453"), stockSerializable.class);
        String left=c.binaryExpression.getLeft();
        String op = c.binaryExpression.getOp();
        String right=c.binaryExpression.getRight();
        SimpleCondition<?> condition = c.complie();
        conditionParser cp=new conditionParser();
//        System.out.println(stockSerializable.class);
        Class<?> eventType;
//        classParser.getMethods("org.SCAU.model.stockSerializable");
        eventType= classParser.getClass("org.SCAU.model.stockSerializable");

//        System.out.println(eventType);
//        System.out.println(classParser.getMethod("org.SCAU.model.stockSerializable","getSymbol").invoke(event));
//        System.out.println(c.extractMethod(left).toString());
//        System.out.println(cp.BinaryLogicOperation(
//                (String)classParser.getMethod("org.SCAU.model.stockSerializable","getSymbol").invoke(event),
//                op,
//                "PINS"
//                , c.binaryExpression.getVariableType()
//        ));
        System.out.println((String)c.extractMethod(left).invoke(event));
        System.out.println(op);
        System.out.println(right);
        System.out.println(cp.BinaryLogicOperation(
                (String)c.extractMethod(left).invoke(event),
                op,
                "106.7699966430664",
                c.binaryExpression.getVariableType()
        ));

//        System.out.println(c.extractMethod("e.getSymbol()"));
    }

    public void testComplie() {
        stockSerializable event = new stockSerializable("2022-04-04", "PINS", "7.399999618530273","27.399999618530273", "27.950000762939453", "25.5", "25.5", "16858000.0");
        conditionComplier c = new conditionComplier(new simpleCondition.BinaryExpression("f:e.getHigh() < 27.950000762939453"), stockSerializable.class);
        System.out.println(c.complie().getClass());



        Pattern<stockSerializable,stockSerializable> pattern1 = Pattern.<stockSerializable>begin("1").where(
        c.complie()
        );

    }
    public void testEmulation() throws IOException {
        List<stockSerializable> stocks=parseJonFile();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        StreamExecutionEnvironment env= StreamExecutionEnvironment.getExecutionEnvironment();
        final ExecutionConfig config = env.getConfig();
        env.enableCheckpointing(20000L, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(20000L);
        env.getCheckpointConfig().setCheckpointTimeout(120000L);
        env.setParallelism(10);

        env.disableOperatorChaining();
        DataStream<stockSerializable> stockStream = env.addSource(new SourceFunction<stockSerializable>() {
            @Override
            public void run(SourceContext<stockSerializable> ctx) throws Exception {
                for (stockSerializable stock : stocks) {
                    ctx.collect(stock); // 发送单个元素到流中
                }
                ctx.close();
            }

            @Override
            public void cancel() {
                // 可选的取消逻辑
            }
        });
//        stockStream.print();
//        DataStream<stockSerializable>
        KeyedStream<stockSerializable,String>
                keyedStream = stockStream
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.noWatermarks()
                )
                .flatMap(new FlatMapFunction<stockSerializable, stockSerializable>() {
                    @Override
                    public void flatMap(stockSerializable value, Collector<stockSerializable> out) throws Exception {
//                        System.out.println(value);
                        // 处理每个元素的逻辑
                        out.collect(value);
                    }
                })
                .keyBy(stock -> stock.getSymbol());

//                .assignTimestampsAndWatermarks(
//                        WatermarkStrategy.<stockSerializable>forBoundedOutOfOrderness(Duration.ofSeconds(3))
//                                .withTimestampAssigner(
//                                        new SerializableTimestampAssigner<stockSerializable>() {
//                                            @Override
//                                            public long extractTimestamp(stockSerializable stock, long l) {
//                                                try {
//
//                                                    return ft.parse(stock.getDate()).getTime();
//                                                } catch (ParseException e) {
//                                                    throw new RuntimeException(e);
//                                                }
//                                            }
//                                        }
//                                )
//                )

        Pattern<stockSerializable,?> pattern = new singles2().getcompoundPattern();
//        System.out.println('1');
        PatternStream<stockSerializable> patternStream = CEP.pattern(keyedStream, pattern);
        patternStream.select(
                new RichPatternSelectFunction<stockSerializable, stockSerializable>() {
                    @Override
                    public void open(Configuration config) throws Exception {
                        super.open(config);

                        // 运行时上下文
                        RuntimeContext context = getRuntimeContext();

                        // 获取子任务索引
                        int subtaskIndex = context.getIndexOfThisSubtask();
//                        System.out.println(context.toString());
                        // 打印索引
//                        System.out.println(subtaskIndex);
                    }
                    @Override
                    public stockSerializable select(Map<String, List<stockSerializable>> pattern) throws Exception {
                        System.out.println('?');
                        return null;
                    }
                }
        ).print("warning");
        DataStream<String> result = CEP.pattern(keyedStream, pattern)
//                .inEventTime()
                .inProcessingTime()
                .flatSelect(
                        (p, o) -> {
                            System.out.println('1');

                            StringBuilder builder = new StringBuilder();
                            builder.append("\n");
                            builder.append(p.get("start").get(0))
                                    .append(",\n")
                                    .append(p.get("second").get(0))
                                    .append(",\n");

                            o.collect(builder.toString());
                        },
                        Types.STRING);
//        keyedStream.print();
        result.print();

        try {
            env.execute("Flink Streaming Example");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}