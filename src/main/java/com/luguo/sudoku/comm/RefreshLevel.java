package com.luguo.sudoku.comm;

import java.util.HashMap;

public enum RefreshLevel {

    LOW(1),
    MID(2),
    HIGH(3),
    ;
    private int level;

    RefreshLevel(int level){
        this.level = level;
    }

    public static RefreshLevel getNextLevel(RefreshLevel curLevel){
        if(LOW.equals(curLevel)){
            return MID;
        }else if(LOW.equals(curLevel)){
            return HIGH;
        }else{
            return null;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
