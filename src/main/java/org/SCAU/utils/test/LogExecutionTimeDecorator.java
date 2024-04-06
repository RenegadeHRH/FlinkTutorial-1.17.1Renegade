package org.SCAU.utils.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogExecutionTimeDecorator implements InvocationHandler {
    private Object target;

    public LogExecutionTimeDecorator(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(LogExecutionTime.class)) {
            long startTime = System.currentTimeMillis();
            Object result = method.invoke(target, args);
            long endTime = System.currentTimeMillis();
            System.out.println("Method " + method.getName() + " took " + (endTime - startTime) + " ms");
            return result;
        } else {
            return method.invoke(target, args);
        }
    }
//    public class Main {
//        public static <MyService> void main(String[] args) {
//            // 创建需要被装饰的对象
//            MyService service = new MyServiceImpl();
//
//            // 创建装饰器
//            LogExecutionTimeDecorator decorator = new LogExecutionTimeDecorator(service);
//
//            // 创建代理对象
//            MyService proxy = (MyService) Proxy.newProxyInstance(
//                    Main.class.getClassLoader(),
//                    new Class<?>[]{MyService.class},
//                    decorator);
//
//            // 调用被装饰的方法
//            proxy.doSomething();
//        }
//    }

}
