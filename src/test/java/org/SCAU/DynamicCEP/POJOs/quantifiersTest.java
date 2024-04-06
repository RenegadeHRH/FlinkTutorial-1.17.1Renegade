package org.SCAU.DynamicCEP.POJOs;

import junit.framework.TestCase;

public class quantifiersTest extends TestCase {

    public void testTestToString() {
        String[] s1 = {"(5)", "(5:)", "(5:7)"};
        for (String s : s1){
            quantifiers q = new quantifiers(s);
            System.out.println(q.toString());
        }

    }
}