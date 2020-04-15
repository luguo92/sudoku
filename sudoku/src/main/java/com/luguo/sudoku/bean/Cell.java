package com.luguo.sudoku.bean;

import com.luguo.sudoku.comm.RuleType;
import com.luguo.sudoku.rule.MyRule;

import java.util.*;

public class Cell {

    private final static int[] allValue = {1,2,3,4,5,6,7,8,9};
    private Integer initValue;
    private Integer value;
    private Set<Integer> possibleValue;
    private Map<Integer, String> impossibleValue;

//    private int rowNum;
//    private int colNum;

    private Cell upCell;
    private Cell downCell;
    private Cell leftCell;
    private Cell rightCell;

    private Integer rowIndex;
    private Integer colIndex;

    private HashMap<Cell,String> checkResult;


    public Integer getBlockIndex() {
        int rowIndex = this.getRowIndex();
        int colIndex = this.getColIndex();

        return rowIndex/3 + (colIndex/3)*3 + 1;
    }

    Cell(){
        this.possibleValue = new HashSet<>();
        this.impossibleValue = new HashMap<>();
        this.checkResult = new HashMap<>();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) throws Exception {
        if(null == initValue || initValue == 0){
            this.value = value;
        }else if(initValue.compareTo(value) == 0 && null == this.value){
            this.value = value;
        }else{
            System.out.println("当前值不可变更");
            throw new Exception();
        }
    }

    public Set<Integer> getPossibleValue() {
        return possibleValue;
    }

    public void setPossibleValue(Set<Integer> possibleValue) {
        this.possibleValue = possibleValue;
    }

    public void addPossibleValue(Integer possibleValue) {
        this.possibleValue.add(possibleValue);
        this.impossibleValue.remove(possibleValue);
    }

    public void removePossibleValue(Integer possibleValue, String cellSign) {
        this.possibleValue.remove(possibleValue);
        this.impossibleValue.put(possibleValue,cellSign);
    }

    public Map<Integer, String> getImpossibleValue() {
        return impossibleValue;
    }

    public void setImpossibleValue(Map<Integer,String> impossibleValue) {
        this.impossibleValue = impossibleValue;
    }

    public void addImpossibleValue(Integer impossibleValue, String cellSign) {
        this.impossibleValue.put(impossibleValue,cellSign);
        this.possibleValue.remove(impossibleValue);
    }

    public void removeImpPossibleValue(Integer impossibleValue) {
        this.impossibleValue.remove(impossibleValue);
        this.possibleValue.add(impossibleValue);
    }

    public Cell getUpCell() {
        return upCell;
    }

    public void setUpCell(Cell upCell) {
        if(null == this.upCell){
            this.upCell = upCell;
        }
        if(null == upCell.getDownCell()){
            upCell.setDownCell(this);
        }
    }

    public Cell getDownCell() {
        return downCell;
    }

    public void setDownCell(Cell downCell) {
        if(null == this.downCell) {
            this.downCell = downCell;
        }
    }

    public Cell getLeftCell() {
        return leftCell;
    }

    public void setLeftCell(Cell leftCell) {
        if(null == this.leftCell) {
            this.leftCell = leftCell;
        }
    }

    public Cell getRightCell() {
        return rightCell;
    }

    public void setRightCell(Cell rightCell) {
        if(null == this.rightCell){
            this.rightCell = rightCell;
        }
        if(null == rightCell.getLeftCell()) {
            rightCell.setLeftCell(this);
        }
    }

    public Integer getInitValue() {
        return initValue;
    }

    public void setInitValue(Integer initValue) {
        this.initValue = initValue;
        for(int entry : allValue){
            if(initValue != 0 && Integer.valueOf(entry).compareTo(initValue) != 0){
                this.addImpossibleValue(entry, "初始化已赋值");
            }
        }
        this.value = initValue;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    @Override
    public String toString() {
        Integer value = this.getValue();
        if(null == value || value <= 0){
            value = null;
        }
        String valueStr = value==null? "*" : String.valueOf(value);
        return "[" + (this.getColIndex()+1) + "," + (this.getRowIndex()+1) + "] " + valueStr;
    }

    public HashMap<Cell, String> getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(HashMap<Cell, String> checkResult) {
        this.checkResult = checkResult;
    }

    public void addCheckResult(String ruleDesc, Cell cell) {
        this.checkResult.put(cell,ruleDesc);
    }
}
