package org.SCAU.DynamicCEP.complier;
import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.Parser.conditionParser;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

import java.lang.reflect.Method;

public class conditionComplier {
    private org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression binaryExpression;
    private final Class<?> eventType;

    public conditionComplier(simpleCondition.BinaryExpression be, Class<?> eventType) {
        this.binaryExpression = be;
        this.eventType = eventType;
    }

    public Method extractMethod(String s) throws NoSuchMethodException {
        //e.getSymbol()
//        System.out.println(s);
        String methodName=s.split("\\.")[1].replace("()","");

        return eventType.getMethod(methodName);

    }
    public SimpleCondition complie() {

        String left=binaryExpression.getLeft();
        String op = binaryExpression.getOp();
        String right=binaryExpression.getRight();
        if (left.contains("e.")&&right.contains("e.")){


            return new SimpleCondition<Class<?>>() {

                @Override
                public boolean filter(Class<?> event) throws Exception {
                    conditionParser cp=new conditionParser();
                    return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,(String)extractMethod(right).invoke(event),binaryExpression.getVariableType());
                }


            };
        }
        if(left.contains("e.")){

            return new SimpleCondition<Class<?>>() {
                @Override
                public boolean filter(Class<?> event) throws Exception {
                    conditionParser cp=new conditionParser();
                    return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType());
                }



            };
        }
        if(right.contains("e.")){
            return new SimpleCondition<Class<?>>() {
                @Override
                public boolean filter(Class<?> event) throws Exception {
                    conditionParser cp=new conditionParser();
                    return cp.BinaryLogicOperation(left,op,(String)extractMethod(right).invoke(event), binaryExpression.getVariableType());
                }



            };
        }
        return null;
    }
}
