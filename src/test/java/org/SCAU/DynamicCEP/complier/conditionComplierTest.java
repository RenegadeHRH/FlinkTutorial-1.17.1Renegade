package org.SCAU.DynamicCEP.complier;

import junit.framework.TestCase;
import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class conditionComplierTest extends TestCase {

    public void testExtractMethod() throws NoSuchMethodException {

        conditionComplier c = new conditionComplier(new simpleCondition.BinaryExpression("s:e.getSymbol() = aapl"), stockSerializable.class);
        SimpleCondition<?> condition = c.complie();
        System.out.println(condition.getClass());
//        System.out.println(c.extractMethod("e.getSymbol()"));
    }
}