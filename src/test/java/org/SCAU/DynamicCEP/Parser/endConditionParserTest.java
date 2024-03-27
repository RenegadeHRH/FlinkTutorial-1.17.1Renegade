package org.SCAU.DynamicCEP.Parser;

import junit.framework.TestCase;

import java.util.List;

public class endConditionParserTest extends TestCase {

    public void testGetCondition() {
        List<String> testArr= List.of("[e.adjclose<100]~~(3:)","[e.adjclose<100]·(3:)","[e.adjclose<100|e.adjclose<120]·(3:4)");

        for (String s:testArr){
            endConditionParser endConditionParser = new endConditionParser(s);
            System.out.println(endConditionParser.toString());

        }


    }
}