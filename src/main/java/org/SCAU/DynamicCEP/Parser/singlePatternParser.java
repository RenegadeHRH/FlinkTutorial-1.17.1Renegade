package org.SCAU.DynamicCEP.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class singlePatternParser {
    //个体模式：ipat单例模式/lpat循环模式
    //ipat: "first":{stockSerializable}[e.Symbol="FB"]
    //ipat: "second":{stockSerializable}[e.adjclose>100]
    //lpat: "third":{stockSerializable}[e.adjclose>100]·(1,3)
    //lpat: "fourth":{stockSerializable}[e.adjclose>100]~(1,3)
    //lpat: "fifth":{stockSerializable}[e.adjclose>100]~~(1,3)
    //lpat: "sixth":{stockSerializable}[e.adjclose>100]·(3,)
    //lpat: "seventh":{stockSerializable}[e.adjclose>100]·(3,)_[e.close<100]
    private String patternName;
    private String eventType;
    private String condition;
    private String continuousType;
    private String repeatTimes;
    private String endCondition;
    public singlePatternParser(){
        this.patternName = "";
        this.eventType = "";
        this.condition = "";
        this.continuousType = "";
        this.repeatTimes="";
        this.endCondition = "";
    }
    public singlePatternParser(String s){
        String pattern = "";;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(s);

    }
    public singlePatternParser(String patternName, String eventType,
                               String condition, String continuousType,
                               String repeatTimes, String endCondition)
    {
        this.patternName = patternName;
        this.eventType = eventType;
        this.condition = condition;
        this.continuousType = continuousType;
        this.repeatTimes = repeatTimes;
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
        return repeatTimes;
    }

    public void setRepeatTimes(String repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public String getEndCondition() {
        return endCondition;
    }

    public void setEndCondition(String endCondition) {
        this.endCondition = endCondition;
    }
}
