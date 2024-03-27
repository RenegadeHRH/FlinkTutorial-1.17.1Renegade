package org.SCAU.DynamicCEP.Parser;

import junit.framework.TestCase;
import org.SCAU.model.stockSerializable;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;

public class classParserTest extends TestCase {
    classParser cp=new classParser();
    final stockSerializable stock=new stockSerializable("2018-01-01","aapl","1","2","3","0.9","1","5");
    public void testGetMethods() {

        System.out.println(cp.getMethods("org.SCAU.model.stockSerializable"));
    }

    public void testGetDeclaredMethodsName() {
        System.out.println(cp.getDeclaredMethodsName("org.SCAU.model.stockSerializable"));
    }

    public void testGetMethod() {

        System.out.println(cp.getMethod("org.SCAU.model.stockSerializable","getSymbol"));
        Class<?> c= cp.getClass("org.SCAU.model.stockSerializable");
        System.out.println(c.getSimpleName());
        for (Method m:c.getMethods()){
            System.out.println(m.getName());
        }

    }
    public void testGetgetClassInstance(){
        Object obj = cp.getClassInstance("org.SCAU.model.stockSerializable");
        System.out.println(obj.getClass()); // 输出: class org.SCAU.model.stockSerializable
    }

    public void testTestGetMethod() {
        System.out.println(cp.getMethod("org.SCAU.model.stockSerializable","getSymbol"));
    }

    public void testTestGetClass() {
        System.out.println(cp.getClass("org.SCAU.model.stockSerializable"));
    }
    public void testExcuteMethod() {
        Method m=cp.getMethod("org.SCAU.model.stockSerializable","getSymbol");
        System.out.println(m);
        String symbol=null;
        try {
            symbol=(String) m.invoke(stock);
        }
        catch (Exception e){

        }
        Assertions.assertEquals("aapl",symbol);
    }
}