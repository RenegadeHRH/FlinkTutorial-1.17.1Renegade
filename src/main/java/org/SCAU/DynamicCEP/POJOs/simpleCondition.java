package org.SCAU.DynamicCEP.POJOs;

import weka.core.pmml.jaxbbindings.True;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simpleCondition {
    //简单条件
    public static class BinaryExpression{
        private boolean isOptional;
        private String op;
        private String right;
        private String left;


        private String variableType;
        public boolean isOptional() {
            return isOptional;
        }

        public void setOptional(String s){
            if (s.contains("|")){
                this.isOptional= true;
            }
            else{
                this.isOptional=false;
            }
        }
        public BinaryExpression(String Expression){
            //匹配：包含空格的左右元素以及中间的操作符
            setOptional(Expression);

            this.variableType=Expression.split("\\:")[0].replace("|","").replace("&","").trim();
            String rest = Expression.split("\\:")[1];
            this.left=rest.split("(>=|<|>|<=|=|!=)")[0];
            this.right=rest.split("(>=|<|>|<=|=|!=)")[1];
            this.op=rest.replace(this.left,"").replace(this.right,"");

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

        public String getVariableType() {
            return variableType;
        }

        public void setVariableType(String variableType) {
            this.variableType = variableType;
        }

        @Override
        public String toString() {
            return "BinaryExpression{\"" +left+" "+op+" "+right+"\", variableType:"+variableType+ ",isOptional:"+isOptional+ "}";
        }
    }
}
