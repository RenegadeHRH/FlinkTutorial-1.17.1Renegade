package org.SCAU.DynamicCEP.Register;

import scala.collection.Map;

import java.lang.reflect.Method;
import java.util.HashMap;

public class classRegister {
    private static HashMap<String,Object> registeredObject;

    public  classRegister() {
        this.registeredObject =  new HashMap<>();
    }

    public classRegister(HashMap<String, Object> registeredObject) {
        this.registeredObject = registeredObject;
    }
    public classRegister register(String nameString, Object object) {
//        System.out.println(object.getClass());
//        System.out.println(object.getClass().getMethods());
//        for (Method method: object.getClass().getMethods()) {
//            System.out.println(method.getName());
//        }
        registeredObject.put(nameString, object);
        return this;
    }

    public HashMap<String, Object> getRegisteredObject() {
        return registeredObject;
    }
}
