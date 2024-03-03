package org.SCAU.DynamicCEP.Parser;

import org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class conditionParser {
    private BinaryExpression binaryExpression;

    public BinaryExpression getBinaryExpression() {
        return binaryExpression;
    }

    public void setBinaryExpression(BinaryExpression binaryExpression) {
        this.binaryExpression = binaryExpression;
    }

    public conditionParser() {
        this.binaryExpression = null;
    }

    //    把不等式左右分析出来，把
    public static boolean isNumeric(Class<?> clazz) {
        return clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Short.class ||
                clazz == Byte.class ||
                clazz == BigDecimal.class ||
                clazz == BigInteger.class;
    }
    public static boolean isNumeric(String s) {

        return Pattern.matches("^-?\\d+(\\.\\d+)?$", s);
    }
    public boolean isValidOperator(String op) {
//        System.out.println(op);
//        System.out.println("=,!=,>=,<=,>,<".contains(op));
        return "=,!=,>=,<=,>,<".contains(op);
//        return "=,!=,>=,<=,>,<".contains(op);
    }

    //        op=>,<,=,!=,>=,<=

//    public boolean BinaryLogicOperation(String left,String op, String right){
//        //        op:: =,!=
//        if (left == null || right == null){
//            throw new IllegalArgumentException("等式左边或者右边为空");
//        }
//        if (op == "!="){
//            return !(left.equals(right));
//        }
//        if (op == "="){
//            return left.equals(right);
//        }
//        if (op == ">=" || op == "<=" || op == ">" || op == "<"){
//            System.out.println("String 之间不可以使用 >=,<=,>,<进行比较 ");
//            return false;
//        }
//        System.out.println("非法操作符");
//        return false;
//    }
    public  <T extends Comparable<T>>
    boolean BinaryLogicOperation(T left,String op, T right) throws Exception
    {

        //        op:: =,!=,>=,<=,>,<
        if (!isValidOperator(op))
        {
            throw new IllegalArgumentException("非法操作符");
        }
        if (left == null || right == null){
            throw new IllegalArgumentException("公式左边或者右边为空");
        }

        if(left.getClass()!=right.getClass()){
            throw new IllegalArgumentException("等公边或者右边类型不一致");
        }
        boolean isNumber=isNumeric(left.getClass())&&isNumeric(right.getClass());
        if (left.getClass() == String.class && right.getClass()==String.class){
            if(isNumeric((String) left) && isNumeric((String) right)){
                isNumber=true;
            }
        }

        if (!isNumber){
            switch (op) {
                case "=":
                    return left.equals(right);
                case "!=":
                    return !left.equals(right);
                default:
                    throw new IllegalArgumentException("String 类型不支持除了=、!=以外的操作");
        }

        }
//        System.out.println(isNumber);
//        System.out.println(op);
        if (isNumber) {
            if (Objects.equals(op, "!=") || Objects.equals(op, "=")){
                return left.equals(right);
            }
            if (op.equals(">=")) {
                return left.compareTo(right)>=0;
            }
            if (op.equals("<=")) {
                return left.compareTo(right)<=0;
            }
            if (op.equals(">")) {
                return left.compareTo(right)>0;
            }
            if (op.equals("<")) {
                return left.compareTo(right)<0;
            }
        }else {
            throw new IllegalArgumentException("String 类型不支持除了=、!=以外的操作");
        }
        throw new Exception("未定义错误");
    }


    public BinaryExpression string2Expression(String s){
        return new BinaryExpression(s);
    }
    public conditionParser parse(String expression) throws Exception{
        BinaryExpression binaryExpression = string2Expression(expression);
        this.setBinaryExpression(binaryExpression);

        return this;
    }
    public boolean calculate() throws Exception {
        if (this.getBinaryExpression()==null){
            throw new Exception("表达式为空");
        }
        return this.BinaryLogicOperation(
                this.getBinaryExpression().getLeft(),
                this.getBinaryExpression().getOp(),
                this.getBinaryExpression().getRight()
        );

    }
}
