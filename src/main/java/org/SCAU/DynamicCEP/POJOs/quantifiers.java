package org.SCAU.DynamicCEP.POJOs;

import java.util.List;

public class quantifiers {
//    (n) :times(n)
//    (n:m):times(n, m)
//    (n:):timesOrMore(n)
    private Integer start;
    private Integer end;
    private boolean continuity;
    public quantifiers(String s){
        continuity=false;
        s=s.replace("(","").replace(")","");
        if (s== null){
            return;
        }
        if ( s.contains(":")){
            continuity = true;
            String[]  res= s.split(":");
            start  = Integer.parseInt(res[0]);
            if ( res.length < 2){
                return;
            }
            else {
                end = Integer.parseInt(res[1]);
            }
            return;
         }
        start = Integer.parseInt(s);
}

    public boolean isContinuity() {
        return continuity;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "quantifiers{" +
                "start=" + start +
                ", end=" + end +
                ", continuity=" + continuity +
                '}';
    }
}
