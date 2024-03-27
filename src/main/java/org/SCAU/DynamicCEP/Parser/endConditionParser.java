package org.SCAU.DynamicCEP.Parser;

import java.util.HashMap;
import java.util.Map;

public class endConditionParser {
    private String condition;
    private String quantifier;
    private String continuousType;
    public Map<String,String> parseByDelimiters(char start, char end, String s){
        Map<String,String> result= new HashMap<>();
        String rest = "";
        StringBuilder content= new StringBuilder();
        boolean recordFlag=false;
        if(s == null){
            return result;
        }
        int maxLen=s.length();
        for(int i =0;i<maxLen;i++){
            char c = s.charAt(i);
            if(recordFlag==true){
                if(c==end){
                    recordFlag=false;
                    rest=s.substring(i+1);
                    result.put("content", content.toString());
                    result.put("rest",rest);
                    break;
                }
                content.append(c);
            }
            if(c == start){

                recordFlag = true;
                if (end == ' '){
                    rest = "";
                    result.put("content", s.substring(i+1));
                    result.put("rest",rest);
                    return result;
                }
            }
        }

        return result;
    }

    public endConditionParser(String s){

        //条件
        Map<String,String> res= this.parseByDelimiters('[',']',s);
        this.condition=res.get("content").trim();
        //连续性
        String rest= res.get("rest").trim();

        if(rest.startsWith("~")){

            if (rest.contains("~~")){
                this.continuousType="~~";
                rest=rest.substring(2);

            }
            else{
                this.continuousType="~";
                rest=rest.substring(1);
            }

        }
        else if (rest.startsWith("·")){

            this.continuousType="·";
            rest=rest.substring(1);
        }
        //量词
        res= this.parseByDelimiters('(',')',rest.trim());
        try {
            this.quantifier=res.get("content").trim();
        }
        catch (NullPointerException e){
            this.quantifier="";
        }
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getQuantifier() {
        return quantifier;
    }

    public void setQuantifier(String quantifier) {
        this.quantifier = quantifier;
    }

    public String getContinuousType() {
        return continuousType;
    }

    public void setContinuousType(String continuousType) {
        this.continuousType = continuousType;
    }

    @Override
    public String toString() {
        return "endConditionParser{" +
                "condition='" + condition + '\'' +
                ", quantifier='" + quantifier + '\'' +
                ", continuousType='" + continuousType + '\'' +
                '}';
    }
}
