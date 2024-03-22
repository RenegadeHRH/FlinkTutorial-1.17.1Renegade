package org.SCAU.DynamicCEP.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class repeatTimesParser {
    private String start;
    private String end;
    public repeatTimesParser(){
        this.start = "1";
        this.end   = "1";
    }
    public repeatTimesParser(String s){
        String regex = "\\((\\s*\\d*),\\s*(\\d*)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            start = matcher.group(1).trim();
            end = matcher.group(2).trim();
    }

    }
    public boolean checkAndFix(){
        if (start==null && end != null) {
            start = "0";
            return  true;
        }
        if(start== null && end == null){
            return false;
        }

        return true;
    }

    public repeatTimesParser(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "repeatTimesParser{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
