package org.SCAU.DynamicCEP.expose;

import org.SCAU.DynamicCEP.POJOs.simpleCondition;
import org.SCAU.DynamicCEP.Parser.classParser;
import org.SCAU.DynamicCEP.Parser.conditionParser;
import org.SCAU.DynamicCEP.Parser.endConditionParser;
import org.SCAU.DynamicCEP.Parser.singlePatternParser;
import org.SCAU.DynamicCEP.complier.conditionComplier;
import org.SCAU.model.stockSerializable;
import org.SCAU.utils.TimerAnnotation;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.SCAU.DynamicCEP.POJOs.quantifiers;
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
    private String patternStr;
    public void setNameAndClass(String name, String className){
        //setNameAndClass 1.0 由于java擦除机制，动态类型暂时搁置，固定类型为stockSerializable
        // 后续可能的解决办法：
        //1. 编译时罗列所有事件类型，运行时切换
        //2.
        Pattern<stockSerializable,stockSerializable> p = Pattern.<stockSerializable>begin(name);
        this.pattern=p;
    }
    public void setCondition(String cndtString){
        conditionParser cndtP=new conditionParser(cndtString);
        List<simpleCondition.BinaryExpression> bes= cndtP.getBinaryExpressions();

        for(simpleCondition.BinaryExpression be : bes){
//            System.out.println(be.toString());
            conditionComplier cndtp=new conditionComplier(be,stockSerializable.class);
//            System.out.print(conditionComplier.staticToString());
            SimpleCondition<stockSerializable> condition;
            condition = cndtp.complie();
            if (be.isOptional()){
//                System.out.println("    or()");
                this.pattern=this.pattern.or(
                        condition
                );
            }
            else{
//                System.out.println("    where()");
                this.pattern.where(

                        condition
                );
            }

        }
    }
    public void setQuantifier(String qString){
        //todo:TEST
        if (qString.equals("") || qString == null){
            return;
        }
        quantifiers q=new quantifiers(qString);
        if (q.getStart()!= null && q.getEnd()!=null){
//            有头有尾 times(n,m)
            this.pattern=this.pattern.times(q.getStart(),q.getEnd());
        }
        if (q.getStart()!=null && q.getEnd()==null && q.isContinuity()){
//            有头无尾,连续符号 timesOrMore(n)
            this.pattern=this.pattern.timesOrMore(q.getStart());
        }
        if (q.getStart() != null && q.getEnd()==null && !q.isContinuity()){
//            仅有头，仅表示 times(n)
            this.pattern=this.pattern.times(q.getStart());
        }

    }
    public void setContinuity(String cString){
        //todo:TEST

        // ~ :松散连续(默认)
        //~~ ：allowCombinations() 不确定松散连续性
        // · ：consecutive() 严格连续性
        if (cString.isEmpty() || cString.trim().equals("~")){
            return;
        }
        if (cString.trim().equals("·")){
            this.pattern=this.pattern.consecutive();
        }
        else if (cString.trim().equals("~~")){
            this.pattern=this.pattern.allowCombinations();
        }

    }

    public void setEndCondition(String ecString){
        //TODO:TEST

        //_[e.adjclose<100]~~(3:)
        endConditionParser ecp=new endConditionParser(ecString);


    }
    public singlePattern(String s) {


        long stime = System.nanoTime();

        //"\"11th\":<org.SCAU.model.stockSerializable>[i:e.adjclose>100 | s:e.adjclose>100 | s:e.adjclose>100 ]·(3:)_[e.adjclose<100]~~(3:)"
        this.patternStr = s;
        //解析原始语句
        singlePatternParser spp=new singlePatternParser(s);
        //开始构建规则




        //设置模式名和事件类型
        setNameAndClass(spp.getPatternName(),spp.getEventType());

        //设置条件
        setCondition(spp.getCondition());


        //设置量词
        setQuantifier(spp.getRepeatTimes());
        //设置连续性
        setContinuity(spp.getContinuousType());
        long etime = System.nanoTime();
        //设置endCondition
        setEndCondition(spp.getEndCondition());
        System.out.print("正在编译pattern : ");
        System.out.printf(s+"\t");
        System.out.printf("  执行时长：%d 纳秒.", (etime - stime));
        System.out.println();


    }
    //动态类

    public Pattern<stockSerializable, stockSerializable> getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        singlePatternParser spp=new singlePatternParser(this.patternStr);
        return spp.toString();
    }




}
