package org.SCAU.DynamicCEP.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class singlePatternParser {
    //个体模式：ipat单例模式/lpat循环模式
    //ipat: "first":<stockSerializable>[e.Symbol="FB"]
    //ipat: "second":<stock1>[e.adjclose>100]
    //lpat: "third":<stock2>[e.adjclose>100]·(1:3)
    //lpat: "fourth":<stockSerializable>[e.adjclose>100]·(1:3;g)
    //lpat: "fifth":<stockSerializable>[e.adjclose>100]·(1:3;o)
    //lpat: "7th":<stock3>[e.adjclose>100]·(1:3;og)
    //lpat: "eighth":<stockSerializable>[e.adjclose>100]~(1:3)
    //lpat: "9th":<stockSerializable>[e.adjclose>100]~~(1:3)
    //lpat: "10th":<stockSerializable>[e.adjclose>100]·(3:)
    //lpat: "11th":<stockSerializable>[e.adjclose>100]·(3:)_[e.adjclose<100]
    private String patternName;
    private String eventType;
    private String condition;
    private String continuousType;
    private String quantifier;
    private String endCondition;
    public singlePatternParser(){
        this.patternName = "";
        this.eventType = "";
        this.condition = "";
        this.continuousType = "";
        this.quantifier="";
        this.endCondition = "";
    }
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

    public singlePatternParser(String s){

//        Pattern pattern = Pattern.compile("\"(.+?)\":<(\\w+)>\\[(.*?)\\]");
//        Matcher matcher = pattern.matcher(s);
//        List<String> parts = new ArrayList<>();
//        if (matcher.find()) {
//            parts.add(matcher.group(1)); // patternName
//            parts.add(matcher.group(2)); // eventType
//            parts.add(matcher.group(3)); // condition
//
//            String remaining = s.substring(matcher.end());
//            String[] separators = {"·", "~", "~~"};
//            String quantifier = "";
//            String endCondition = "";
//
//            for (String separator : separators) {
//                if (remaining.contains(separator)) {
//                    String[] segments = remaining.split("\\\\" + separator);
//                    quantifier = segments[0].trim();
//                    parts.add(separator.replaceFirst("\\.", "")); // continuousType
//                    parts.add(quantifier); // quantifier
//                    if (segments.length > 2) {
//                        endCondition = segments[2].trim();
//                        parts.add(endCondition); // endCondition
//                    } else {
//                        parts.add(""); // endCondition
//                    }
//                    break;
//                }
//            }
//
//            if (parts.size() < 6) {
//                parts.add(""); // continuousType
//                parts.add(""); // quantifier
//                parts.add(""); // endCondition
//            }
//
//        }
//        this.patternName= parts.get(0);
//        this.eventType= parts.get(1);
//        this.condition= parts.get(2);
//        this.continuousType= parts.get(3);
//        this.quantifier= parts.get(4);
//        this.endCondition= parts.get(5);
        s=s.trim();
        //模式名 用""分割
        Map<String,String> res= this.parseByDelimiters('\"','\"',s);
        this.patternName=res.get("content").trim();
        //事件类型
        res= this.parseByDelimiters('<','>',res.get("rest").trim());
        this.eventType = res.get("content").trim();
        //条件
        res= this.parseByDelimiters('[',']',res.get("rest").trim());
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


        //结束条件
        try {


            res = this.parseByDelimiters('_', ' ', res.get("rest"));//' '表示取到末尾
            this.endCondition = res.get("content").trim();
        }
        catch (NullPointerException e ){
            this.endCondition="";
        }


    }


    public singlePatternParser(String patternName, String eventType,
                               String condition, String continuousType,
                               String quantifier, String endCondition)
    {
        this.patternName = patternName;
        this.eventType = eventType;
        this.condition = condition;
        this.continuousType = continuousType;
        this.quantifier = quantifier;
        this.endCondition = endCondition;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getContinuousType() {
        return continuousType;
    }

    public void setContinuousType(String continuousType) {
        this.continuousType = continuousType;
    }

    public String getRepeatTimes() {
        return quantifier;
    }

    public void setRepeatTimes(String repeatTimes) {
        this.quantifier = repeatTimes;
    }

    public String getEndCondition() {
        return endCondition;
    }

    public void setEndCondition(String endCondition) {
        this.endCondition = endCondition;
    }

    @Override
    public String toString() {
        return "singlePatternParser{" +
                "patternName='" + patternName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", condition='" + condition + '\'' +
                ", continuousType='" + continuousType + '\'' +
                ", quantifier='" + quantifier + '\'' +
                ", endCondition='" + endCondition + '\'' +
                '}';
    }
}
