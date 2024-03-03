package org.SCAU.DynamicCEP.POJOs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simpleCondition {
    //简单条件
    public static class BinaryExpression{
        private String op;
        private String right;
        private String left;
        public BinaryExpression(String Expression){
            //匹配：包含空格的左右元素以及中间的操作符
            Pattern pattern = Pattern.compile("\\s*(\\w+)\\s*(>=|<|>|<|=|<=|!=)\\s*(\\w+)\\s*");
            Matcher matcher = pattern.matcher(Expression);
            if (matcher.find()) {
                // 提取操作数和操作符，去除空格
                String operand1 = matcher.group(1).trim();
                String operator = matcher.group(2).trim();
                String operand2 = matcher.group(3).trim();

                // 创建并返回BinaryOperation对象
                this.left=operand1;
                this.op=operator;
                this.right=operand2;
//                System.out.println("BinaryExpression{\"" +operand1+" "+operator+" "+operand2+"\"}");
            } else {

                throw new IllegalArgumentException("Invalid expression: " + Expression);
            }

        }

        public BinaryExpression(String op, String right, String left) {
            this.op = op;
            this.right = right;
            this.left = left;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        @Override
        public String toString() {
            return "BinaryExpression{\"" +left+" "+op+" "+right+"\"}";
        }
    }
}
