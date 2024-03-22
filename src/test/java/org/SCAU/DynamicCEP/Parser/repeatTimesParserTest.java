package org.SCAU.DynamicCEP.Parser;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class repeatTimesParserTest extends TestCase {

    public void testCheck() {
        List<String> testList = new ArrayList<>();
        testList.add("(1,2)");
        testList.add("(2,3)");
        testList.add("(3,4)");
        testList.add("(5,4)");
        testList.add("(,)");
        testList.add("(5,");
        testList.add("(0,4)");
        testList.add("(4,4)");
        testList.add("(5,1)");
        testList.add("(54)");
        testList.add("(,53)");
        testList.add("(54,)");
        testList.add("(54  , )");
        testList.add("(  , )");
        testList.add("(54, 1)");
        testList.add("(a, 1)");
        for(String i : testList){
            repeatTimesParser parser = new repeatTimesParser(i);
            System.out.println(parser.toString());
        }
    }
}