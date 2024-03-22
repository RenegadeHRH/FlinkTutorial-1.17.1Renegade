package org.SCAU.DynamicCEP.POJOs;

import junit.framework.TestCase;
import org.SCAU.model.stockSerializable;

import java.lang.reflect.InvocationTargetException;

public class eventTypeTest extends TestCase {

    public void testTestToString() throws InvocationTargetException, IllegalAccessException {
        final stockSerializable stock=new stockSerializable("2018-01-01","aapl","1","2","3","0.9","1","5");
        eventType eventType = new eventType("org.SCAU.model.stockSerializable");
        System.out.println(eventType.toString());
        System.out.println(eventType.getMethod("getSymbol").invoke(stock));

    }
}