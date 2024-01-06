package org.SCAU.DynamicCEP.POJOs;

import java.util.List;

public class PatternPOJO {
    public class Condition {

        private String type;

        private String attr;

        private String value;

        private Quantifier quantifier;

        private Condition[] children;

        //  getter/setter 方法

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Quantifier getQuantifier() {
            return quantifier;
        }

        public void setQuantifier(Quantifier quantifier) {
            this.quantifier = quantifier;
        }

        public Condition[] getChildren() {
            return children;
        }

        public void setChildren(Condition[] children) {
            this.children = children;
        }
    }
    public enum QuantifierType {
        GREEDY,
        RELUCTANT,
        PERMISSIVE
    }
    public class Quantifier {

        private QuantifierType type; // PERMISSIVE, GREEDY等

        private int max;

        // getter/setter

        public QuantifierType getType() {
            return type;
        }

        public void setType(QuantifierType type) {
            this.type = type;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }

    public class RuleConfig {

        private String name;

        private Pattern pattern;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }
    }

    public class Pattern {

        private List<Condition> conditions;

        private Quantifier quantifier;

        public List<Condition> getConditions() {
            return conditions;
        }

        public void setConditions(List<Condition> conditions) {
            this.conditions = conditions;
        }

        public Quantifier getQuantifier() {
            return quantifier;
        }

        public void setQuantifier(Quantifier quantifier) {
            this.quantifier = quantifier;
        }
    }
}
