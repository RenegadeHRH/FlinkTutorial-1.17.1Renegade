package org.SCAU.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.alibaba.fastjson.JSON.toJSONString;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
public class testSystemUsage {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ShowMemInfo {
    }
    public class ShowMemInfoDecorator implements InvocationHandler {
        private final Object target;

        public ShowMemInfoDecorator(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.isAnnotationPresent(ShowMemInfo.class)) {

                long startTime = System.currentTimeMillis();
                Object result = method.invoke(target, args);
                long endTime = System.currentTimeMillis();
                System.out.println("Method " + method.getName() + " took " + (endTime - startTime) + " ms");
                return result;
            } else {
                return method.invoke(target, args);
            }
        }
    }


    public static void main(String[] args) {





        Runtime r = Runtime.getRuntime();
        r.gc();//计算内存前先垃圾回收一次
        long start = System.currentTimeMillis();//开始Time
        long startMem = r.totalMemory(); // 开始Memory
        new String("1");//！！！！！被测的程序！！！！！
        long endMem =r.freeMemory(); // 末尾Memory
        long end = System.currentTimeMillis();//末尾Time
        //输出
        System.out.println("用时消耗: "+String.valueOf(end - start)+"ms");
        System.out.println("内存消耗: "+String.valueOf((startMem- endMem)/1024)+"KB");
    }

//    @Test
//    public void testSystemUsage() {
//        final long GB = 1024 * 1024 * 1024;
//        while (true) {
//            OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
//            String osJson = toJSONString(operatingSystemMXBean);
////            System.out.println("osJson is " + osJson);
//            JSONObject jsonObject = JSON.parseObject(osJson);
//            double processCpuLoad = jsonObject.getDouble("processCpuLoad") * 100;
//            double systemCpuLoad = jsonObject.getDouble("systemCpuLoad") * 100;
//            Long totalPhysicalMemorySize = jsonObject.getLong("totalPhysicalMemorySize");
//            Long freePhysicalMemorySize = jsonObject.getLong("freePhysicalMemorySize");
//            double totalMemory = 1.0 * totalPhysicalMemorySize / GB;
//            double freeMemory = 1.0 * freePhysicalMemorySize / GB;
//            double memoryUseRatio = 1.0 * (totalPhysicalMemorySize - freePhysicalMemorySize) / totalPhysicalMemorySize * 100;
//
//            StringBuilder result = new StringBuilder();
//            result.append("系统CPU占用率: ")
//                    .append(twoDecimal(systemCpuLoad))
//                    .append("%，内存占用率：")
//                    .append(twoDecimal(memoryUseRatio))
//                    .append("%，系统总内存：")
//                    .append(twoDecimal(totalMemory))
//                    .append("GB，系统剩余内存：")
//                    .append(twoDecimal(freeMemory))
//                    .append("GB，该进程占用CPU：")
//                    .append(twoDecimal(processCpuLoad))
//                    .append("%");
//            System.out.println(result.toString());
//
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public double twoDecimal(double doubleValue) {
//        BigDecimal bigDecimal = new BigDecimal(doubleValue).setScale(2, RoundingMode.HALF_UP);
//        return bigDecimal.doubleValue();
//    }


}



