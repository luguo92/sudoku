package com.luguo.sudoku.bean;

import com.sun.istack.internal.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class Cell {

    private static Integer[] allValue = null;
    private Integer initValue;

    private Integer value;
    private Set<Integer> possibleValue;
    private Map<Integer, String> impossibleValue;

    private Cell upCell;
    private Cell downCell;
    private Cell leftCell;
    private Cell rightCell;
    private Cell nextCell;

    private boolean isChanged;

    @NotNull
    private Integer rowIndex;
    @NotNull
    private Integer colIndex;
    @NotNull
    private Integer blockIndex;

    private HashMap<Cell,String> checkResult;

    public Cell(){
        this.impossibleValue = new HashMap<>();
        this.checkResult = new HashMap<>();
        this.isChanged = false;
    }

    public Cell(Integer rowIndex, Integer colIndex, Integer blockIndex,Integer[] allValue){
        this();
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.blockIndex = blockIndex;
        this.allValue = allValue;
        this.possibleValue = new HashSet(Arrays.asList(allValue));
    }

    public Cell(@NotNull Cell oldCell){

        this.allValue = oldCell.allValue;
        this.initValue = oldCell.initValue;
        this.value = oldCell.value;
        this.possibleValue = oldCell.possibleValue;
        this.impossibleValue = oldCell.impossibleValue;

        this.isChanged = oldCell.isChanged;

        this.rowIndex = oldCell.rowIndex;
        this.colIndex = oldCell.colIndex;
        this.blockIndex = oldCell.blockIndex;

        this.checkResult = oldCell.checkResult;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(@NotNull Integer value) throws Exception {
        if(initValue > 0 && initValue.compareTo(value) != 0){
            System.out.println("当前值不可变更");
            throw new Exception();
        }
        this.value = value;

//        if(value > 0 ) {
//            Set<Integer> impossibleValueSet = new HashSet<>(possibleValue);
//            impossibleValueSet.remove(value);
//            this.addImpossibleValue(impossibleValueSet, "尝试指定赋值");
//        }else{
//            removeImpPossibleValue(value);
//        }

        this.isChanged = true;
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
        this.isChanged = true;
    }

    public void addImpossibleValue(Set<Integer> impossibleValueSet, String cellSign) {
        for(Integer impossibleValue : impossibleValueSet) {
            addImpossibleValue(impossibleValue,cellSign);
        }
    }

    public void addImpossibleValue(Map<Integer,String > valueMap) {
        for(Map.Entry<Integer,String > entry : valueMap.entrySet()) {
            addImpossibleValue(entry.getKey(),entry.getValue());
        }
    }

    public void removeImpPossibleValue(Integer possibleValue) {
        this.impossibleValue.remove(possibleValue);
        this.possibleValue.add(possibleValue);
        this.isChanged = true;
    }

    public void removeImpPossibleValue(Set<Integer> valueSet) {
        for(Integer value : valueSet) {
            if(impossibleValue.containsKey(value)) {
                removeImpPossibleValue(value);
            }
        }
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
        if(null == downCell.getUpCell()){
            downCell.setUpCell(this);
        }
    }

    public Cell getLeftCell() {
        return leftCell;
    }

    public void setLeftCell(Cell leftCell) {
        if(null == this.leftCell) {
            this.leftCell = leftCell;
        }
        if(null == leftCell.getRightCell()) {
            leftCell.setRightCell(this);
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

        if(initValue > 0 ) {
            Set<Integer> impossibleValueSet = new HashSet<>(possibleValue);
            impossibleValueSet.remove(initValue);
            this.addImpossibleValue(impossibleValueSet, "初始化已赋值");
        }

        this.value = initValue;
        this.isChanged = true;
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
        String initvalueStr = this.getInitValue()==null? "*" : String.valueOf(this.getInitValue());
        return "[" + (this.getRowIndex()+1) + "," + (this.getColIndex()+1) + "," + (this.getBlockIndex()+1) +"] " + initvalueStr +"  " + valueStr;
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

    public Integer getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(Integer blockIndex) {
        this.blockIndex = blockIndex;
    }

    public Cell getNextCell() {
        return nextCell;
    }

    public Cell getNextNullCell() {
        Cell curCell = nextCell;
        while(curCell != null){
            if(curCell.getValue() > 0){
                curCell = curCell.getNextCell();
            }else{
                break;
            }
        }
        return curCell;
    }

    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
