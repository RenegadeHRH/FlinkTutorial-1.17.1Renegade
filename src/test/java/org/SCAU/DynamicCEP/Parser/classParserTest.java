package org.SCAU.DynamicCEP.Parser;

import junit.framework.TestCase;
import meka.core.A;
import org.SCAU.model.stockSerializable;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.Assertions;
import weka.attributeSelection.ASSearch;

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