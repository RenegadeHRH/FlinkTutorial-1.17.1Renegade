package org.SCAU.utils.test;

import org.SCAU.DynamicCEP.expose.singlePattern;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        Runtime r = Runtime.getRuntime();
        r.gc();//计算内存前先垃圾回收一次
        long start = System.currentTimeMillis();//开始Time
        long startMem = r.totalMemory(); // 开始Memory

        singlePattern sp = new singlePattern("\"11th\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100 | f:e.adjclose>100 | f:e.high>100 & f:e.close>0  ]·(3:)_[e.adjclose<100]~~(3:)");
        // 创建需要被装饰的对象


        long endMem =r.freeMemory(); // 末尾Memory
        long end = System.currentTimeMillis();//末尾Time
        //输出
        System.out.println("用时消耗: "+String.valueOf(end - start)+"ms");
        System.out.println("内存消耗: "+String.valueOf((startMem- endMem)/1024)+"KB");

    }

}
