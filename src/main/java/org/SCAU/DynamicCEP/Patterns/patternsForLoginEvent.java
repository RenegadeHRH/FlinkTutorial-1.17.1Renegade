package org.SCAU.DynamicCEP.Patterns;

import org.SCAU.cep.LoginEvent;
import org.SCAU.model.stockSerializable;
import org.apache.flink.cep.pattern.GroupPattern;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class patternsForLoginEvent {
    public GroupPattern<LoginEvent ,?> getCompoundPattern()
    {
        return compoundPattern;

    }

    Pattern<LoginEvent, LoginEvent> p1 = Pattern.<LoginEvent>begin("first")     // 以第一个登录失败事件开始
            .where(new SimpleCondition<LoginEvent> () {

                @Override
                public boolean filter(LoginEvent loginEvent)   throws Exception {
                    return loginEvent.eventType.equals("fail");
                }
            });
    Pattern<LoginEvent, LoginEvent> p2 = Pattern.<LoginEvent>begin("second").where(new SimpleCondition<LoginEvent>() {
        @Override
        public boolean filter(LoginEvent loginEvent) throws Exception {
            return loginEvent.eventType.equals("fail");
        }
    });
    Pattern<LoginEvent, LoginEvent> p3 = Pattern.<LoginEvent>begin("third").where(new SimpleCondition<LoginEvent>() {
        @Override
        public boolean filter(LoginEvent loginEvent) throws Exception {
            return loginEvent.eventType.equals("fail");
        }
    });
    GroupPattern<LoginEvent, ?> compoundPattern=p1.next(p2).next(p3);

}
