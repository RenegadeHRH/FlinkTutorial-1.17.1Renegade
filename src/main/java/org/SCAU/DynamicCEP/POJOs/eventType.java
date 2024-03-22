package org.SCAU.DynamicCEP.POJOs;

import org.SCAU.DynamicCEP.Parser.classParser;

import java.lang.reflect.Method;

public class eventType {

    private String eventTypeStr;
    private Class<?> eventTypeClass;
    public eventType(String s){
        this.eventTypeStr=s;
        classParser cp = new classParser();
        this.eventTypeClass=cp.getClass(s);
    }
    public Method getMethod(String method){
        classParser cp = new classParser();
        return cp.getMethod(this.eventTypeClass,method);
    }

    @Override
    public String toString() {
        return "eventType{" +
                "eventTypeStr='" + eventTypeStr + '\'' +
                ", eventTypeClass=" + eventTypeClass +
                '}';
    }
}
