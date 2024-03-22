package org.SCAU.DynamicCEP.Patterns;

import junit.framework.TestCase;
import org.apache.flink.cep.pattern.conditions.BooleanConditions;
public class singles2Test extends TestCase {

    public void testGetcompoundPattern() {
        singles2 singles2 = new singles2();
        String patString = singles2.getcompoundPattern().toString();
        System.out.println(patString);
        System.out.println(singles2.getcompoundPattern().getCondition().toString());
    }
}