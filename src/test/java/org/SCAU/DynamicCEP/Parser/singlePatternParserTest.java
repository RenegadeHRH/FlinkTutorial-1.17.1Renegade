package org.SCAU.DynamicCEP.Parser;

import junit.framework.TestCase;
import org.SCAU.DynamicCEP.complier.conditionComplier;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

import java.util.List;
import java.util.Map;

public class singlePatternParserTest extends TestCase {
    singlePatternParser spp=new singlePatternParser();
    conditionParser cp = new conditionParser();
//    conditionComplier ccmp=new conditionComplier();
    public void SimpleCondition() {
        SimpleCondition<stockSerializable> sc = new SimpleCondition<stockSerializable>() {
            @Override
            public boolean filter(stockSerializable value) throws Exception {
                return false;
            }
        };
        }
    public void testGetPatternName() {
        String[] patterns = {
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
                "\"first\":<org.SCAU.model.stockSerializable>[s:e.Symbol=\"FB\"]",
                "\"second\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100]",
                "\"third\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100]·(1:3)",
                "\"fourth\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100]·(1:3;g)",
                "\"fifth\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100]·(1:3;o)",
                "\"7th\":<org.SCAU.model.stockSerializable>[f:e.adjclose>100]·(1:3;og)",
                "\"eighth\":<org.SCAU.model.stockSerializable>[f:e.adjclose>100]~(1:3)",
                "\"9th\":<org.SCAU.model.stockSerializable>[f:e.adjclose>100]~~(1:3)",
                "\"10th\":<org.SCAU.model.stockSerializable>[f:e.adjclose>100]·(3:)",
                "\"11th\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100 | s:e.adjclose>100 | s:e.adjclose>100 ]·(3:)_[e.adjclose<100]",
                "\"11th\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100 | s:e.adjclose>100 | s:e.adjclose>100 ]·(3:)_[e.adjclose<100]~~(3:)"
        };
        System.out.println(stockSerializable.class);
        for (String s:patterns){
            singlePatternParser spp=new singlePatternParser(s);
            System.out .println(spp.toString());
            System.out.println(new conditionParser(spp.getCondition()));

//            try {
//                cp.parse(spp.getCondition());
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }

        }

    }

    public void testParser() {
        String pattern= "\"11th\":<stockSerializable>[i:e.adjclose>100 | s:e.adjclose>100 | e.adjclose>100 ]·(3:)_[e.adjclose<100]";
        Map<String,String> res=spp.parseByDelimiters('\"','\"',pattern);
        for(var entry:res.entrySet()){
            String key = entry.getKey();
            // 获得value
            String value = entry.getValue();
            System.out.println(key+":                 "+value);
        }
        Map<String,String> res2=spp.parseByDelimiters('<','>',res.get("rest"));
        for(var entry:res2.entrySet()){
            String key = entry.getKey();
            // 获得value
            String value = entry.getValue();
            System.out.println(key+":                "+value);
        }
        Map<String,String> res3=spp.parseByDelimiters('[',']',res2.get("rest"));
        for(var entry:res3.entrySet()){
            String key = entry.getKey();
            // 获得value
            String value = entry.getValue();
            System.out.println(key+":                "+value);
        }
        char c = res3.get("rest").charAt(0);
        String rest = res.get("rest").substring(1);
        System.out.println(c);
        System.out.println(rest);
        Map<String,String> res4=spp.parseByDelimiters('(',')',rest);
        for(var entry:res4.entrySet()){
            String key = entry.getKey();
            // 获得value
            String value = entry.getValue();
            System.out.println(key+":                "+value);
        }
        System.out.println("?");
        Map<String,String> res5=spp.parseByDelimiters('_',']',rest);
        for(var entry:res5.entrySet()){
            String key = entry.getKey();
            // 获得value
            String value = entry.getValue();
            System.out.println(key+":                "+value);
        }

    }
}