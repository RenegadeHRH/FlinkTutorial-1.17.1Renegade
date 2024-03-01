package org.SCAU.reflection;
import org.SCAU.model.stockSerializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class reflectionTest1 {
    private static final Logger LOG = LoggerFactory.getLogger(reflectionTest1.class);
    public static List getMethods(String className) {
        try {
            Class<?> c = Class.forName(className);
            Method [] methods = c.getMethods();
            for (Method m : methods) {
                System.out.println(m.getName());
            }
            return Arrays.asList(methods);
        } catch (ClassNotFoundException e) {
            LOG.warn("can not find class: {}", className);
            return null;
        }
    }
    public static List<String> getDeclaredMethodsName(String className) {
        try {
            List<String> methodNames = new ArrayList<>();
            Class<?> c = Class.forName(className);
            Method [] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                methodNames.add(m.getName());
            }
            return methodNames;
        } catch (ClassNotFoundException e) {
            LOG.warn("can not find class: {}", className);
            return null;
        }
    }
    /**
     * 根据类名和方法名获取对应的方法
     *
     * @param name 类名
     * @param method 方法名
     * @return a Method, null 找不到
     */
    public static Method getMethod(String name, String method) {
        try {
            Class<?> c = Class.forName(name);
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(method)) {
                    return m;
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            LOG.warn("can not find class: {}", name);
            return null;
        }
    }

    /**
     * 根据类名和方法名获取对应的方法
     *
     * @param cls 类
     * @param method 方法名
     * @return a Method, null 找不到
     */
    public static Method getMethod(Class<?> cls, String method) {
        for (Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    public static Class<?> getClass(String name) {
        try {
            Class<?> c = Class.forName(name);
            return c;
        } catch (ClassNotFoundException e) {
            LOG.warn("can not find class: {}", name);
            return null;
        }
    }
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        stockSerializable stock=new stockSerializable("2018-01-01","aapl","1","2","3","0.9","1","5");
//        getMethods(stock.getClass().getName());
//        System.out.println(stock.getClass().getName());
//        getMethods("org.SCAU.model.stockSerializable");
//        List<String> declaredMethodsName = getDeclaredMethodsName("org.SCAU.model.stockSerializable");
//        System.out.println(declaredMethodsName);
        Method func=null;

        func=getMethod("org.SCAU.model.stockSerializable", "getSymbol");
        System.out.println(func);
        System.out.println(getClass("org.SCAU.model.stockSerializable"));
//        System.out.println(stockSerializable.class);
        String symbol=(String)func.invoke(stock);
        System.out.println(symbol);
    }
}
