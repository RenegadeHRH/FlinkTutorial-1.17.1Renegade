package org.SCAU.DynamicCEP.expose;

import junit.framework.TestCase;

public class singlePatternTest extends TestCase {

    public void testSetNameAndClass() {
    }

    public void testSetCondition() {
        singlePattern sp = new singlePattern("\"11th\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100 | s:e.adjclose>100 | s:e.adjclose>100 ]·(3:)_[e.adjclose<100]~~(3:)");

    }

}