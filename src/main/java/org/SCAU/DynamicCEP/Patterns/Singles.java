package org.SCAU.DynamicCEP.Patterns;
import org.SCAU.model.socialMediaStocks2;
import org.apache.flink.cep.pattern.GroupPattern;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class Singles {
    public GroupPattern<socialMediaStocks2, ?> getcompoundPattern(){
        return compoundPattern;
    }
    Pattern<socialMediaStocks2,?> pattern1 = Pattern.<socialMediaStocks2>begin("start").where(
    new SimpleCondition<socialMediaStocks2>() {
        @Override
        public boolean filter(socialMediaStocks2 stock) {
//                        System.out.println("here");
            return Float.valueOf(stock.high)>10;
        }
    }

    );
    Pattern<socialMediaStocks2, ?> pattern2 = Pattern.<socialMediaStocks2>begin("second").where(
            new SimpleCondition<socialMediaStocks2>() {
                @Override
                public boolean filter(socialMediaStocks2 stock) {
//                        System.out.println("here");
                    return Float.valueOf(stock.close)>10;
                }
            }

    );
    Pattern rawPattern2 = pattern2;

    GroupPattern<socialMediaStocks2, ?> compoundPattern=pattern1.next(rawPattern2);
//    pattern1.next("next").where();








}
