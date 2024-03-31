package org.SCAU.DynamicCEP.Patterns;


import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.complier.conditionComplier;
import org.SCAU.DynamicCEP.expose.singlePattern;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class testPaterns {

    public static Pattern<stockSerializable,stockSerializable> Pattern1(){
        conditionComplier c = new conditionComplier(new simpleCondition.BinaryExpression("s:e.getSymbol() =FB"), stockSerializable.class);

        Pattern<stockSerializable,stockSerializable> pattern1 = Pattern.<stockSerializable>begin("1").where(
                c.complie()
        );
        return pattern1;
    }
    public static Pattern<stockSerializable,stockSerializable> Pattern2(){
        Pattern<stockSerializable,stockSerializable> pattern2 = Pattern.<stockSerializable>begin("1").where(
                new SimpleCondition<stockSerializable>() {
                    @Override
                    public boolean filter(stockSerializable e) throws Exception {

//                        System.out.println(e.toString());
                        return Float.parseFloat(e.getHigh()) < 5.5;
                    }
                }
        ).or(
                new SimpleCondition<stockSerializable>() {
                    @Override
                    public boolean filter(stockSerializable e) throws Exception {

//                        System.out.println(e.toString());
                        return Float.parseFloat(e.getClose()) < 5.5;
                    }
                }
        )
                ;
        return pattern2;
    }
    public static Pattern<stockSerializable,stockSerializable> Pattern3(){
        String patternStr = "\"1\":<org.SCAU.model.stockSerializable>[f:e.getHigh() < 5.5 & f:e.getLow() < 5]";
        System.out.println( new singlePattern(patternStr).toString());
        Pattern<stockSerializable,stockSerializable> pattern3 = new singlePattern(patternStr).getPattern();
        return pattern3;

    }
    public static Pattern<stockSerializable,stockSerializable> Pattern4(){
        Pattern<stockSerializable,stockSerializable> pattern4 = Pattern.<stockSerializable>begin("1").where(
                        new SimpleCondition<stockSerializable>() {
                            @Override
                            public boolean filter(stockSerializable e) throws Exception {

//                        System.out.println(e.toString());
                                return Float.parseFloat(e.getClose()) > 379;
                            }
                        }
                )
                .or(
                        new SimpleCondition<stockSerializable>() {
                            @Override
                            public boolean filter(stockSerializable e) throws Exception {

//                        System.out.println(e.toString());
                                return Float.parseFloat(e.getHigh()) > 384;
                            }
                        }
                )
                .where(
                        new SimpleCondition<stockSerializable>() {
                            @Override
                            public boolean filter(stockSerializable e) throws Exception {

//                        System.out.println(e.toString());
                                return e.getSymbol().equals("FB") ;
                            }
                        }
                )
                ;
        return pattern4;
    }
    public static Pattern<stockSerializable,stockSerializable> Pattern5(){
        Pattern<stockSerializable,stockSerializable> pattern = Pattern.<stockSerializable>begin("1");
        pattern= pattern.where(
                new SimpleCondition<stockSerializable>() {
                    @Override
                    public boolean filter(stockSerializable event) throws Exception {
                        return Float.parseFloat(event.getHigh())  <5.5;
                    }
                }
        ).or(
                new SimpleCondition<stockSerializable>() {
                    @Override
                    public boolean filter(stockSerializable event) throws Exception {
                        return Float.parseFloat(event.getLow())  <5.5;
                    }
                }
        )

        ;
        return pattern;
    }

//    public static Pattern<stockSerializable,stockSerializable> Pattern6(){
//        Pattern<stockSerializable,stockSerializable> pattern = Pattern.<stockSerializable>begin("1");
//        pattern= pattern.where(
//
//        ).or(
//                new SimpleCondition<stockSerializable>() {
//                    @Override
//                    public boolean filter(stockSerializable event) throws Exception {
//                        return Float.parseFloat(event.getLow())  <5.5;
//                    }
//                }
//        )
//
//        ;
//        return pattern;
//    }

}
