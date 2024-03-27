package org.SCAU.DynamicCEP.Parser;

import junit.framework.TestCase;
import org.apache.activemq.filter.BinaryExpression;
import org.apache.hadoop.tracing.TraceAdminPB;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class conditionParserTest extends TestCase {
    conditionParser cp=new conditionParser();
    public void testIsNumeric() {
        String s = "123";
        Byte b = 1;
        Integer i = 1;
        Float f = 1.0f;
        Double d = 1.0;
        Long l = 1L;
        Short sh = 1;

        Assertions.assertEquals(false, cp.isNumeric(s.getClass()));
        System.out.println(cp.isNumeric(s.getClass()));
        Assertions.assertEquals(true, cp.isNumeric(b.getClass()));
        System.out.println(cp.isNumeric(b.getClass()));
        Assertions.assertEquals(true, cp.isNumeric(i.getClass()));
        System.out.println(cp.isNumeric(i.getClass()));
        Assertions.assertEquals(true, cp.isNumeric(f.getClass()));
        System.out.println(cp.isNumeric(f.getClass()));
        Assertions.assertEquals(true, cp.isNumeric(d.getClass()));
        System.out.println(cp.isNumeric(d.getClass()));
        Assertions.assertEquals(true, cp.isNumeric(l.getClass()));
        System.out.println(cp.isNumeric(l.getClass()));
        Assertions.assertEquals(true, cp.isNumeric(sh.getClass()));
        System.out.println(cp.isNumeric(sh.getClass()));
        Assertions.assertEquals(false, cp.isNumeric(conditionParser.class));
        System.out.println(cp.isNumeric(conditionParser.class));

    }

    public void testBinaryLogicOperation() {
        try {
        Assertions.assertEquals(false, cp.BinaryLogicOperation(1, "<", 1));
        Assertions.assertEquals(false, cp.BinaryLogicOperation(1, ">", 1));
        Assertions.assertEquals(true, cp.BinaryLogicOperation(1, "=", 1));
        Assertions.assertEquals(true, cp.BinaryLogicOperation(1, ">=", 1));
        Assertions.assertEquals(true, cp.BinaryLogicOperation(1, "<=", 1));
        Assertions.assertEquals(true, cp.BinaryLogicOperation(1, "<=", 1));
        Assertions.assertEquals(true, cp.BinaryLogicOperation("1", "=", "1"));
        Assertions.assertEquals(false, cp.BinaryLogicOperation("1", "=", "2"));
        Assertions.assertEquals(true, cp.BinaryLogicOperation("1", "!=", "2"));
        Assertions.assertEquals(false, cp.BinaryLogicOperation("1", "!=", "1"));
        Assertions.assertEquals(false, cp.BinaryLogicOperation(2, "<=", 1));
        Assertions.assertEquals(false, cp.BinaryLogicOperation(2, "=", 1));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->cp.BinaryLogicOperation("1", ">", "1"));
            Assertions.assertThrows(IllegalArgumentException.class,
                    ()->cp.BinaryLogicOperation("1", ">", "1"));
        }
        catch (Exception e){
            System.out.println(e.toString());
            Assertions.fail();
        }
    }

    public void testString2Expression() throws Exception {
        String expression = "1>2";

        org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression BE =cp.string2Expression(expression);
        System.out.println(BE.toString());
        String expression2 = "1=2";
        org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression BE2 =cp.string2Expression(expression2);
        System.out.println(BE2.toString());
        String expression3 = "1>=2";
        org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression BE3 =cp.string2Expression(expression3);
        System.out.println(BE3.toString());
        String expression4 = "1<=2";
        org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression BE4 =cp.string2Expression(expression4);
        System.out.println(BE4.toString());
        String expression5 = "awdagas2233!=awd5e4qw5e12";
        org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression BE5 =cp.string2Expression(expression5);
        System.out.println(BE5.toString());
        List<String> test = new ArrayList<>();
        test.add("awdagas2233!=awd5e4qw5e12");
        test.add("awdagas2233=awd5e4qw5e12");
        test.add("awdagas2233>=awd5e4qw5e12");
        test.add("awdagas2233<=awd5e4qw5e12");
        test.add("awdagas2233>awd5e4qw5e12");
        test.add("awdagas2233<awd5e4qw5e12");
        test.add("awdagas 2233<awd5e4qw5e 12");
        for (String i : test){
            System.out.println(
                    cp.string2Expression(i).toString()+"\n"
                    +"left:"+cp.string2Expression(i).getLeft()
                    +"\n"+"right:"+cp.string2Expression(i).getRight()
                    +"\n"+"op:"+cp.string2Expression(i).getOp()
            );
        }
    }

    public void testParse() {
        try {
            List<String> testList=new ArrayList<>();
            testList.add("i:1=1");
            testList.add("s:1  <    2");
            testList.add("f:2  <    1");
            testList.add("i:abcd  >    weqq");
            testList.add("s:abcd  >    weqq |i:1 < 2");
            testList.add("s:e.abcd()  =    weqq |i:1<2");
//            System.out.println(cp.parse(testList.get(1)).getBinaryExpression().toString());
            for (String s : testList){
                System.out.println(cp.parse(s).getBinaryExpressions().toString());
            }


        }catch (Exception e){
            System.out.println(e.toString());

        }
    }

//    public void testCalculate() {
//        try {
////            Assertions.assertEquals(true,cp.parse("1=1").calculate());
////            Assertions.assertEquals(true,cp.parse("1  <    2").calculate());
////            Assertions.assertEquals(false,cp.parse("2  <    1").calculate());
////            Assertions.assertEquals(true,cp.parse("2  >    1").calculate());
////            Assertions.assertEquals(false,cp.parse("1<1").calculate());
////            Assertions.assertEquals(false,cp.parse("1  <    1").calculate());
////            Assertions.assertEquals(true,cp.parse("1  <=  1").calculate());
////            Assertions.assertEquals(true,cp.parse("1  >=  1").calculate());
////            Assertions.assertEquals(true,cp.parse("abcd  =   abcd").calculate());
////            Assertions.assertEquals(false,cp.parse("abcd  =   abcc").calculate());
////            Assertions.assertEquals(false,cp.parse("abcd  =   abdc").calculate());
////            Assertions.assertEquals(false,cp.parse("abcd  >   abdc").calculate());
////            Assertions.assertEquals(false,cp.parse("1  >   abdc").calculate());
//        }
//        catch (Exception e){
//            System.out.println(e.toString());
//        }
//    }

    public void testBinaryLogicOperation2() {
        try {
            Assertions.assertEquals(false, cp.BinaryLogicOperation("1", "<", "1","i"));
            Assertions.assertEquals(true, cp.BinaryLogicOperation("1", "<", "2","i"));
            Assertions.assertEquals(true, cp.BinaryLogicOperation("1", "!=", "2","s"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}