package org.SCAU.cep;

import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.TimeBehaviour;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

public class dynamicPattern {
    public static void main(String[] args) {
//        // DataStream Source
//        DataStreamSource<socialMediaStocks2> source =
//                env.fromSource(
//                        kafkaSource,
//                        WatermarkStrategy.<socialMediaStocks2>forMonotonousTimestamps()
//                                .withTimestampAssigner((event, ts) -> event.getEventTime()),
//                        "Kafka Source");
//
//        env.setParallelism(1);
//        // keyBy userId and productionId
//        // Notes, only events with the same key will be processd to see if there is a match
//        KeyedStream<socialMediaStocks2, Tuple2<Integer, Integer>> keyedStream =
//                source.keyBy(
//                        new KeySelector<socialMediaStocks2, Tuple2<Integer, Integer>>() {
//
//                            @Override
//                            public Tuple2<Integer, Integer> getKey(socialMediaStocks2 value) throws Exception {
//                                return Tuple2.of(value.getId(), value.getProductionId());
//                            }
//                        });
//
//        SingleOutputStreamOperator<String> output =
//                CEP.dynamicPatterns(
//                        keyedStream,
//                        new JDBCPeriodicPatternProcessorDiscovererFactory<>(
//                                JDBC_URL, JDBC_DRIVE, TABLE_NAME, null, JDBC_INTERVAL_MILLIS),
//                        TimeBehaviour.ProcessingTime,
//                        TypeInformation.of(new TypeHint<String>() {}));
//
//        output.print();
//        // Compile and submit the job
//        env.execute("CEPDemo");
    }
}
