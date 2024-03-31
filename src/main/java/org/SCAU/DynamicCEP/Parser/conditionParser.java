package org.SCAU.DynamicCEP.Parser;

import org.SCAU.DynamicCEP.POJOs.simpleCondition.BinaryExpression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class conditionParser {
    private List<BinaryExpression> binaryExpressions;

    public List<BinaryExpression> getBinaryExpressions() {
        return binaryExpressions;
    }

    public void setBinaryExpression(List<BinaryExpression> binaryExpressions) {
        this.binaryExpressions = binaryExpressions;
    }
    public List<String> splitExpression(String s){

        List<String> list = new ArrayList<>();
        String tmp = "";
        for (int i = 0; i < s.length(); i++){

            if ( s.charAt(i)== '&' || s.charAt(i) =='|' ){


                list.add(tmp.trim());
                tmp="";

            }
            tmp+=s.charAt(i);
        }
        list.add(tmp);

        return list;
    }
    public conditionParser() {
        this.binaryExpressions = null;
    }
    public conditionParser(String s) {
        this.binaryExpressions= new ArrayList<>();
        List<String> exps=splitExpression(s);
        for (String str :exps){


            this.binaryExpressions.add(new BinaryExpression(str.trim()));
        }

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



    public  <T extends Comparable<T>>
    boolean BinaryLogicOperation(T left,String op, T right) throws Exception
    {
//        System.out.println(left);
//        System.out.println(right);

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
//            System.out.println(left);
//            System.out.println(right);
            switch (op) {
                case "=":
//                    System.out.println(left.equals(right));
                    return left.equals(right);
                case "!=":
//                    System.out.println(!left.equals(right));
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
    boolean StringOperation(String left,String op, String right) {
        System.out.println(
                "left : "+ left+
                        " right : "+right+ " op: "+op +"result :"
        );
        switch (op) {
            case "=":
                System.out.println(left.equals(right));
                return left.equals(right);
            case "!=":
                System.out.println(!left.equals(right));
                return !left.equals(right);
            default:
                throw new IllegalArgumentException("String 类型不支持除了=、!=以外的操作");
        }
    }
    boolean NumOeration(String left,String op, String right) {
        Double left1=Double.valueOf(left);
        Double right1=Double.valueOf(right);
        if (Objects.equals(op, "!=") || Objects.equals(op, "=")){
            return left.equals(right);
        }
        if (op.equals(">=")) {
            return left1>=right1;
        }
        if (op.equals("<=")) {
            return left1<=right1;
        }
        if (op.equals(">")) {
            return left1>right1;
        }
        if (op.equals("<")) {



            return left1 <right1;
        }
        return false;
    }
    public boolean BinaryLogicOperation(String left,String op, String right,String type) throws Exception{
//        System.out.println(type);
//        System.out.println(this.binaryExpressions.get(0).toString());
//        System.out.println(left+op+right);
        if (type.equals( "s")){

            System.out.println("here");
            return StringOperation(left,op,right);
        }else {

            return NumOeration(left,op,right);
        }
    }
    public BinaryExpression string2Expression(String s){
        return new BinaryExpression(s);
    }
    public conditionParser parse(String expression) throws Exception{
        String[] segments = expression.split("\\|");


        binaryExpressions = new ArrayList<>();

        for (String s :segments){
            this.binaryExpressions.add(string2Expression(s));
        }



        return this;
    }

    @Override
    public String toString() {
        StringBuilder expressionString= new StringBuilder();
        for(BinaryExpression be : this.binaryExpressions){
            expressionString.append(be.toString()).append("\n");
        }
        return "conditionParser{\n" +
                expressionString+
                '}';
    }

//    public boolean calculate(){
//
//    }
//    public boolean calculate() throws Exception {
//        if (this.getBinaryExpression()==null){
//            throw new Exception("表达式为空");
//        }
//        return this.BinaryLogicOperation(
//                this.getBinaryExpression().getLeft(),
//                this.getBinaryExpression().getOp(),
//                this.getBinaryExpression().getRight()
//        );

//    }
}
