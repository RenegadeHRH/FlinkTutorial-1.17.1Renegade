package org.SCAU.DynamicCEP.complier;
import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.Parser.classParser;
import org.SCAU.DynamicCEP.Parser.conditionParser;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

import java.lang.reflect.Method;

public  class conditionComplier {
    static simpleCondition.BinaryExpression binaryExpression;
    private static Class<?> eventType = null;


    public conditionComplier(simpleCondition.BinaryExpression be, Class<?> eventType) {
        this.binaryExpression = be;
        this.eventType = eventType;
    }

    public static Method extractMethod(String s) throws NoSuchMethodException {
        //e.getSymbol()
//        System.out.println(s.split("\\.")[1]);
        String methodName=s.split("\\.")[1].replace("()","");
//        System.out.println(methodName);
//        System.out.println(eventType);
//        System.out.println(classParser.getMethod(eventType,methodName));
        return classParser.getMethod(eventType,methodName.trim());

    }

public static SimpleCondition<stockSerializable> complie() {

    String left=binaryExpression.getLeft().trim();
    String op = binaryExpression.getOp().trim();
    String right=binaryExpression.getRight().trim();

    if (left.contains("e.")&&right.contains("e.")){


        return new SimpleCondition<stockSerializable>() {

            @Override
            public boolean filter(stockSerializable event) throws Exception {

                conditionParser cp=new conditionParser();
                return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,(String)extractMethod(right).invoke(event),binaryExpression.getVariableType());
            }


        };
    }
    if(left.contains("e.")){

        return new SimpleCondition<stockSerializable>() {
            @Override
            public boolean filter(stockSerializable event) throws Exception {

                conditionParser cp=new conditionParser();
//                System.out.println(event.toString());
//                if ( cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType())){
//                    System.out.println(binaryExpression.toString());
//                    System.out.println(cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType()));
//                }
//                System.out.println(binaryExpression.toString());
//                System.out.println(cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType()));
                return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType());
            }
        };
    }
    if(right.contains("e.")){
        return new SimpleCondition<stockSerializable>() {
            @Override
            public boolean filter(stockSerializable event) throws Exception {
                conditionParser cp=new conditionParser();
                return cp.BinaryLogicOperation(left,op,(String)extractMethod(right).invoke(event), binaryExpression.getVariableType());
            }



        };
    }
    return null;
}
}
