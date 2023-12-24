package org.SCAU.DataStreamAPI;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FirstStreamProject {
    //todo:
    //1.获取执行环境execution environment
    //2.获取数据源source
    //3.基于数据的转换操作transformations
    //4.定义计算结果的输出位置(sink)
    //5.触发程序执行
    //info:
    //切换批处理模式:flink run -Dexecution.runtime-mode=BATCH
    public static void main(String[] args) throws Exception {
//        Configuration conf = new Configuration();
//        conf.set(RestOptions.BIND_PORT,"8082");
        //1.获取执行环境execution environment
//        方法一 自动判断当前环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment
//                .getExecutionEnvironment(conf)
                .getExecutionEnvironment();
//        方法二 创建本地环境

//        LocalStreamEnvironment lenv = StreamExecutionEnvironment.createLocalEnvironment();

//        方法三 创建远程环境
//        StreamExecutionEnvironment remoteEnvironment = StreamExecutionEnvironment.createRemoteEnvironment(
//          "host",//jobmanager主机名
//          1234,//jobmanager进程端口号 6123
//          "path/to/jarFile.jar"//提交给jobmanager的JAR包
//        );
//        设置BATCH模式
//        env.setRuntimeMode(RuntimeExecutionMode.BATCH)
//        设置并行度
//        env.setParallelism(1);
//        DataStreamSource<Object> DS = env.addSource();
        env.socketTextStream("master",7777)
                .flatMap(
                (String value, Collector<Tuple2<String, Integer>> out) -> {
                    String[] words = value.split(" ");
                    for (String word : words) {
                        out.collect(Tuple2.of(word, 1));
                    }
                }
        );

//启动执行
        env.executeAsync("Flink Stream test");//监听数据，等待执行上面的业务代码
        //...其它代码
//        env.executeAsync();

    }
}
