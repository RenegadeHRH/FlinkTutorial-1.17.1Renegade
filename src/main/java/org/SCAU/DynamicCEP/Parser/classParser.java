package org.SCAU.DynamicCEP.Parser;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class classParser {
    private static final PrintStream out=System.out;

    public classParser() {
    }

    public static List getMethods(String className) {
//        获取类的全部方法
        try {
            Class<?> c = Class.forName(className);
            Method[] methods = c.getMethods();
            for (Method m : methods) {
                out.println(m.getName());

            }
            return Arrays.asList(methods);
        } catch (ClassNotFoundException e) {
            out.println("Class not found"+className);
            return null;
        }
    }
    public static List<String> getDeclaredMethodsName(String className) {
//        获取用户定义的类方法
        try {
            List<String> methodNames = new ArrayList<>();
            Class<?> c = Class.forName(className);
            Method [] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                methodNames.add(m.getName());
            }
            return methodNames;
        } catch (ClassNotFoundException e) {
            out.println("can not find class:"+className);
            return null;
        }
    }
    /**
     * 根据类名和方法名获取对应的方法
     *
     * @param classname 类名
     * @param method 方法名
     * @return a Method, null 找不到
     */
    public static Method getMethod(String classname, String method) {
        try {
            Class<?> c = Class.forName(classname);
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(method)) {
                    return m;
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            out.println("can not find class:"+classname);
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
//            System.out.println(m.getName());
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
    /**
     * 根据字符串类名获取类class
     *
     * @param name sting,类名字
     * @return a class, null 找不到
     */
    public static Class<?> getClass(String name) {
        try {
            Class<?> c = Class.forName(name);
            return c;
        } catch (ClassNotFoundException e) {
            out.println("can not find class: "+name);
            return null;
        }
    }
    public static Object getClassInstance(String name) {
        try {
            Class<?> c = Class.forName(name);
            return c.newInstance();
        } catch (ClassNotFoundException e) {
            out.println("can not find class: " + name);
            return null;
        } catch (InstantiationException e) {
            out.println("Cannot instantiate class: " + name);
            return null;
        } catch (IllegalAccessException e) {
            out.println("Cannot access class: " + name);
            return null;
        }
    }

}
