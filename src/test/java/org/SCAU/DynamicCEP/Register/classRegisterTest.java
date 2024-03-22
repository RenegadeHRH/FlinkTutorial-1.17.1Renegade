package org.SCAU.DynamicCEP.Register;

import junit.framework.TestCase;
import org.SCAU.model.stockSerializable;

import java.util.Map;

public class classRegisterTest extends TestCase {
    classRegister cr= new classRegister();
    classRegister cr2 = new classRegister();

    final stockSerializable stock=new stockSerializable("2018-01-01","aapl","1","2","3","0.9","1","5");
    public void testRegister() throws NoSuchMethodException {
//        cr.register("stock",stock);
        cr.register("getSymbol",stock.getClass().getMethod("getSymbol"));
        cr.register("stockEvent",stock.getClass());
        cr2.register("getDate",stock.getClass().getMethod("getDate"));
//        cr2.register("getDate",stock.getDate());
//        System.out.println(cr.getRegisteredObject());
        for (Map.Entry<String,Object> entry:cr.getRegisteredObject().entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
            entry.getValue();
        }
//        cr.register("stock",stock.getSymbol());
    }
}