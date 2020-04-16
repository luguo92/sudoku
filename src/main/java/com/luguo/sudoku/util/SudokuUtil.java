package com.luguo.sudoku.util;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.LogLevel;
import com.luguo.sudoku.comm.RuleType;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class SudokuUtil {

//    public static boolean checkDuplication(RuleType ruleType, Cell[] rowArray) {
//        boolean flag = true;
//        HashMap<String,List<Cell>> cellMap = new HashMap<>();
//        for(Cell cell : rowArray){
//            Integer value = cell.getValue();
//            if(null != value && value > 0){
//                String key = getSign(cell,ruleType);
//                if(cellMap.keySet().contains(key)){
//                    List<Cell> cellList = cellMap.get(key);
//                    cellList.add(cell);
//                    flag = false;
//                }else {
//                    List<Cell> cellList = new ArrayList<>();
//                    cellList.add(cell);
//                    cellMap.put(key,cellList);
//                }
//            }
//        }
//
//        for(Map.Entry<String,List<Cell>> entry : cellMap.entrySet()){
//            if(entry.getValue().size() > 1){
//                PrintUtil.printLog(LogLevel.ERR, entry);
//            }
//        }
//
//        return flag;
//    }

    public static Map<String,List<Cell>> checkDuplication(RuleType ruleType, Cell[] rowArray) {
        HashMap<String,List<Cell>> cellMap = new HashMap<>();
        for(Cell cell : rowArray){
            Integer value = cell.getValue();
            if(null != value && value > 0){
                String key = getSign(cell,ruleType);
                if(cellMap.keySet().contains(key)){
                    List<Cell> cellList = cellMap.get(key);
                    cellList.add(cell);
                }else {
                    List<Cell> cellList = new ArrayList<>();
                    cellList.add(cell);
                    cellMap.put(key,cellList);
                }
            }
        }

        return cellMap;
    }

    /**
     * 基础刷新
     * @param ruleType
     * @param rowArray
     * @return
     * @throws Exception
     */
    public static boolean refershCellValue(RuleType ruleType, Cell[] rowArray) throws Exception {
        boolean flag = false;

        for(Cell curCell : rowArray) {
            Integer curValue = curCell.getValue();
            if (null != curValue && curValue > 0) {

                for (Cell cell : rowArray) {
                    Integer value = cell.getValue();
                    if (null != value && value > 0) {
                        continue;
                    }
                    Set<Integer> possibleValue = cell.getPossibleValue();
                    if(possibleValue.contains(curValue)){
                        flag = true;
                        String cellSign = getCellSign(curCell,ruleType);
                        cell.addImpossibleValue(curValue, cellSign);

                        Set<Integer> valueSet = cell.getPossibleValue();
                        if(!CollectionUtils.isEmpty(valueSet) && valueSet.size() == 1){
                            cell.setValue(valueSet.iterator().next());
                        }
                    }
                }
            }
        }

        return flag;
    }

    /**
     * 高级刷新
     * @param ruleType
     * @param rowArray
     * @return
     * @throws Exception
     */
    public static boolean refershCellValueAdvance(RuleType ruleType, Cell[] rowArray) throws Exception {
        boolean flag = false;

        for(Cell curCell : rowArray) {
            Integer curValue = curCell.getValue();
            if (null == curValue || curValue == 0) {
                Set<Integer> curCellPossibleValue = curCell.getPossibleValue();
                if(curCellPossibleValue.size() !=2){
                    continue;
                }

                for (Cell cell : rowArray) {
                    Integer value = cell.getValue();
                    if (value > 0 || curCell.equals(cell)) {
                        continue;
                    }
                    Set<Integer> possibleValue = cell.getPossibleValue();
                    if(curCellPossibleValue.equals(possibleValue)){

                        for(Cell entry : rowArray) {
                            if(entry.equals(curCell) || entry.equals(cell)){
                                continue;
                            }
                            Integer entryValue = entry.getValue();
                            if (entryValue > 0) {
                                continue;
                            }
                            Set<Integer> entryPossibleValue = entry.getPossibleValue();

                            Set<Integer> result = new HashSet<>(entryPossibleValue);
                            result.retainAll(possibleValue);

                            if(!CollectionUtils.isEmpty(result)){
                                for(Integer impossibleValue : result ){
                                    if(!entryPossibleValue.contains(impossibleValue)){
                                        continue;
                                    }
                                    flag = true;
                                    String cellSign = getCellSign(curCell,ruleType);
                                    entry.addImpossibleValue(impossibleValue, cellSign);

                                    Set<Integer> valueSet = entry.getPossibleValue();
                                    if(!CollectionUtils.isEmpty(valueSet) && valueSet.size() == 1){
                                        entry.setValue(valueSet.iterator().next());
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        return flag;
    }




    public static Cell[] arrayConvert(Cell[][] oldArr){

        int colLength = oldArr.length;
        int rowLength = oldArr[0].length;

        int index = colLength * rowLength;

        Cell[] newArr = new Cell[index];
        int temp = 0;
        for(int i=0; i<oldArr.length; i++){
            for(int j=0; j<oldArr[i].length; j++){
                newArr[temp] = oldArr[i][j];
                temp++;
            }
        }

        return newArr;
    }

    public static String getSign(Cell cell, RuleType ruleType){
        Integer index = null;
        switch (ruleType){
            case COL:
                index = cell.getRowIndex() + 1;
                break;
            case ROW:
                index = cell.getColIndex() + 1;
                break;
            case BLOCK:
                index = cell.getBlockIndex();
                break;
            default:
                break;
        }
        Integer value = cell.getValue();
        return "第"+index + ruleType.getDesc() + ",取值为:" + value ;
    }


    public static String getCellSign(Cell cell, RuleType ruleType){
        Integer index = null;
        switch (ruleType){
            case COL:
                index = cell.getRowIndex() + 1;
                break;
            case ROW:
                index = cell.getColIndex() + 1;
                break;
            case BLOCK:
                index = cell.getBlockIndex();
                break;
            default:
                break;
        }
        Integer value = cell.getValue();
        return "第"+index + ruleType.getDesc() + "存在"+cell+"取值为:" + value ;
    }

}
