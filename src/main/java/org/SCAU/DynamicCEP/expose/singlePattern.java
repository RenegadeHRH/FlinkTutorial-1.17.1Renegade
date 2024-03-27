package org.SCAU.DynamicCEP.expose;

import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.Parser.classParser;
import org.SCAU.DynamicCEP.Parser.conditionParser;
import org.SCAU.DynamicCEP.Parser.endConditionParser;
import org.SCAU.DynamicCEP.Parser.singlePatternParser;
import org.SCAU.DynamicCEP.complier.conditionComplier;
import org.SCAU.model.stockSerializable;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

import java.lang.reflect.ParameterizedType;
import java.util.List;
//        Pattern<stockSerializable,stockSerializable> pattern1 = Pattern.<stockSerializable>begin("start").where(
//                new SimpleCondition<stockSerializable>() {
//                    @Override
//                    public boolean filter(stockSerializable stock) {
////                        System.out.println("here");
//                        return Float.valueOf(stock.high)>0;
//                    }
//                }
//
//        );

public class singlePattern {
    //传入String , getpattern（）获取pattern


    //解析class
    //解析连续性
    //解析条件
    //解析量词
    //private singlePatternParser spp;
    //解析endcondition
    //private endConditionParser ecp;



    //返回值
    private Pattern<stockSerializable,stockSerializable> pattern;

    public void setNameAndClass(String name, String className){
        //setNameAndClass 1.0 由于java擦除机制，动态类型暂时搁置，固定类型为stockSerializable
        // 后续可能的解决办法：
        //1. 编译时罗列所有事件类型，运行时切换
        //2.
        Pattern<stockSerializable,stockSerializable> p = Pattern.<stockSerializable>begin(name);
        this.pattern=p;
    }
    public void setCondition(String condition){
        //conditionParser
        //conditionComplier
        //conditionFilter
        conditionParser cp=new conditionParser(condition);
        this.pattern=this.pattern.where(new SimpleCondition<stockSerializable>() {
            @Override
            public boolean filter(stockSerializable value) throws Exception {
                return false;
            }
        });
    }
    public singlePattern(String s) {

        //"\"11th\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100 | s:e.adjclose>100 | s:e.adjclose>100 ]·(3:)_[e.adjclose<100]~~(3:)"

        //解析原始语句
        singlePatternParser spp=new singlePatternParser(s);
        //开始构建规则




        //设置模式名和事件类型
        setNameAndClass(spp.getPatternName(),spp.getEventType());

        //设置条件
        conditionParser cndtP=new conditionParser(spp.getCondition());
        List<simpleCondition.BinaryExpression> bes= cndtP.getBinaryExpressions();

        for(simpleCondition.BinaryExpression be : bes){
            new conditionComplier(be,stockSerializable.class);
            SimpleCondition<stockSerializable> condition = conditionComplier.complie();
            if (be.isOptional()){
                this.pattern=this.pattern.or(
                        condition
                );
            }
            else{
                this.pattern.where(
                        condition
                );
            }

        }

        System.out.println(this.pattern.toString());
        //解析endcondition
        endConditionParser ecp=new endConditionParser(spp.getEndCondition());

        //singlePatternParser
        //conditionParser





    }
    //动态类

}
