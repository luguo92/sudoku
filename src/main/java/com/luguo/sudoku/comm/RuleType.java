package com.luguo.sudoku.comm;

public enum RuleType {

    ROW("行"),
    COL("列"),
    BLOCK("宫"),

    ;

    private String desc;

    RuleType(String desc) {
        this.desc = desc;
    }

    public String getDesc(int index){
        return this.getDesc() + "校验----第" + (index+1) + this.getDesc();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
