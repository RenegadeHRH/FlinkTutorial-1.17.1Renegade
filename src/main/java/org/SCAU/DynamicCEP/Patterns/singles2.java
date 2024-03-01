package org.SCAU.DynamicCEP.Patterns;

import org.SCAU.model.socialMediaStocks2;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.GroupPattern;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class singles2 {

    public GroupPattern<stockSerializable, ?> getcompoundPattern(){
        return compoundPattern;
    }
    Pattern<stockSerializable,stockSerializable> pattern1 = Pattern.<stockSerializable>begin("start").where(
            new SimpleCondition<stockSerializable>() {
                @Override
                public boolean filter(stockSerializable stock) {
//                        System.out.println("here");
                    return Float.valueOf(stock.high)>0;
                }
            }

    );
    Pattern<stockSerializable, stockSerializable> pattern2 = Pattern.<stockSerializable>begin("second").where(
            new SimpleCondition<stockSerializable>() {
                @Override
                public boolean filter(stockSerializable stock) {
//                        System.out.println("here");
                    return Float.valueOf(stock.close)>0;
                }
            }

    );
    Pattern rawPattern2 = pattern2;

    GroupPattern<stockSerializable, ?> compoundPattern=pattern1.next(pattern2);
//    pattern1.next("next").where();










}
