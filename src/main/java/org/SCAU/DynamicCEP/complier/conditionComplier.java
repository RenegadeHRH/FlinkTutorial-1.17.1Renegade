package org.SCAU.DynamicCEP.complier;
import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.Parser.classParser;
import org.SCAU.DynamicCEP.Parser.conditionParser;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

import java.lang.reflect.Method;

public  class conditionComplier {
    public   simpleCondition.BinaryExpression binaryExpression;
    private  Class<?> eventType = null;
    static class conditonKeeper{
        public static simpleCondition.BinaryExpression binaryExpression;
        private static Class<?> eventType = null;

        public conditonKeeper(simpleCondition.BinaryExpression be,Class<?> eventType){
//            System.out.println(be.toString());
            this.binaryExpression = be;
            this.eventType = eventType;
        }
        public static Method extractMethod(String s) throws NoSuchMethodException {
            String methodName=s.split("\\.")[1].replace("()","");
            return classParser.getMethod(eventType,methodName.trim());
    }
        public static  SimpleCondition<stockSerializable> complie() {


            String left=binaryExpression.getLeft().trim();
            String op = binaryExpression.getOp().trim();
            String right=binaryExpression.getRight().trim();
            String variableType=binaryExpression.getVariableType();
//            System.out.println(left+"  "+op+"  "+right+" "+ variableType);
//    System.out.println(binaryExpression.toString());
//    System.out.println(variableType);
//    System.out.println(left+op+right);
            if (left.contains("e.")&&right.contains("e.")){


                return new SimpleCondition<stockSerializable>() {

                    @Override
                    public boolean filter(stockSerializable event) throws Exception {

                        conditionParser cp=new conditionParser();

                        return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,(String)extractMethod(right).invoke(event),variableType);
                    }


                };
            }
            if(left.contains("e.")){

                return new SimpleCondition<stockSerializable>() {
                    @Override
                    public boolean filter(stockSerializable event) throws Exception {

                        conditionParser cp=new conditionParser();
//                System.out.println((String)extractMethod(left).invoke(event));
//                System.out.println(binaryExpression.getVariableType());
//                System.out.println(cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType()));
//                System.out.println(event.toString());
//                System.out.println((String)extractMethod(left).invoke(event)+"  "+op+"  "+right+" "+ variableType);

//                System.out.println(cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, variableType));

                        return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, variableType);
                    }
                };
            }
            if(right.contains("e.")){
                return new SimpleCondition<stockSerializable>() {
                    @Override
                    public boolean filter(stockSerializable event) throws Exception {
                        conditionParser cp=new conditionParser();
                        return cp.BinaryLogicOperation(left,op,(String)extractMethod(right).invoke(event), variableType);
                    }



                };
            }

            return null;
        }
    }

    public conditionComplier(simpleCondition.BinaryExpression be, Class<?> eventType) {

        this.binaryExpression = be;
        this.eventType = eventType;
//        System.out.println(binaryExpression.toString());
    }

    public  Method extractMethod(String s) throws NoSuchMethodException {

        //e.getSymbol()
//        System.out.println(s.split("\\.")[1]);
        String methodName=s.split("\\.")[1].replace("()","");
//        System.out.println(methodName);
//        System.out.println(eventType);
//        System.out.println(classParser.getMethod(eventType,methodName));
//        System.out.println(classParser.getMethod(eventType,methodName.trim()));
        return classParser.getMethod(eventType,methodName.trim());

    }

public  SimpleCondition<stockSerializable> complie() {
//        System.out.println(this.binaryExpression.toString());
        new conditonKeeper(this.binaryExpression,this.eventType);
        return conditonKeeper.complie();
//    String left=binaryExpression.getLeft().trim();
//    String op = binaryExpression.getOp().trim();
//    String right=binaryExpression.getRight().trim();
//    String variableType=binaryExpression.getVariableType();
////    System.out.println(binaryExpression.toString());
////    System.out.println(variableType);
////    System.out.println(left+op+right);
//    if (left.contains("e.")&&right.contains("e.")){
//
//
//        return new SimpleCondition<stockSerializable>() {
//
//            @Override
//            public boolean filter(stockSerializable event) throws Exception {
//
//                conditionParser cp=new conditionParser();
//
//                return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,(String)extractMethod(right).invoke(event),variableType);
//            }
//
//
//        };
//    }
//    if(left.contains("e.")){
//
//        return new SimpleCondition<stockSerializable>() {
//            @Override
//            public boolean filter(stockSerializable event) throws Exception {
//
//                conditionParser cp=new conditionParser();
////                System.out.println((String)extractMethod(left).invoke(event));
////                System.out.println(binaryExpression.getVariableType());
////                System.out.println(cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType()));
////                System.out.println(event.toString());
////                System.out.println((String)extractMethod(left).invoke(event)+"  "+op+"  "+right+" "+ variableType);
//
////                System.out.println(cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, variableType));
//
//                return cp.BinaryLogicOperation((String)extractMethod(left).invoke(event),op,right, binaryExpression.getVariableType());
//            }
//        };
//    }
//    if(right.contains("e.")){
//        return new SimpleCondition<stockSerializable>() {
//            @Override
//            public boolean filter(stockSerializable event) throws Exception {
//                conditionParser cp=new conditionParser();
//                return cp.BinaryLogicOperation(left,op,(String)extractMethod(right).invoke(event), binaryExpression.getVariableType());
//            }
//
//
//
//        };
//    }
//
//    return null;
}


    public  String staticToString() {
        return "conditionComplier{+"+binaryExpression.toString()+"}";
    }
}
