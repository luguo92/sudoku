package com.luguo.sudoku.bean;

import com.luguo.sudoku.rule.BlockRule;
import com.luguo.sudoku.rule.ColRule;
import com.luguo.sudoku.rule.RowRule;
import com.luguo.sudoku.util.PrintUtil;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Sudoku {

    public static Integer[] allValue = {1,2,3,4,5,6,7,8,9};
    public static Integer MAX_INDEX_VALUE = 9;
    public static Cell sudokuCell;

    public Sudoku(int[][] arr) {

        Cell[] upCellList = new Cell[9];
        Cell[] curCellList = new Cell[9];
        for(int i=0; i<arr.length ; i++) {

            if(i > 0) {
                upCellList = curCellList;
                curCellList = new Cell[9];
            }

            Cell preCell = null;
            for (int j = 0; j < arr[i].length; j++) {

                Integer value = arr[i][j];
                if(StringUtils.isEmpty(value)){
                    value = 0;
                }
                Cell curCell = new Cell();
                curCell.setPossibleValue(new HashSet<>(Arrays.asList(allValue)));
                curCell.setInitValue(value);
                curCell.setColIndex(i);
                curCell.setRowIndex(j);

                curCellList[j] = curCell;

                if(preCell == null){
                    preCell = curCell;
                }

                if( j == 0 && i == 0){
                    sudokuCell = preCell;
                }else{
                    if(j>0){
                        preCell.setRightCell(curCell);
                        preCell = curCell;
                    }

                    Cell upCell = upCellList[j];
                    if(null != upCell){
                        curCell.setUpCell(upCell);
                    }
                }
            }
        }
    }

    public boolean refershCellValue() throws Exception {
        boolean flag = false;
        RowRule rowRule = new RowRule() ;
        if(rowRule.refershCellValue(this)){
            flag = true;
        }


        ColRule colRule = new ColRule();
        if(colRule.refershCellValue(this)){
            flag = true;
        }
        BlockRule blockRule = new BlockRule();
        if(blockRule.refershCellValue(this)){
            flag = true;
        }

        return flag;
    }

    public boolean refershCellValueAdvance() throws Exception {
        boolean flag = false;
        RowRule rowRule = new RowRule() ;
        if(rowRule.refershCellValueAdvance(this)){
            flag = true;
        }


        ColRule colRule = new ColRule();
        if(colRule.refershCellValueAdvance(this)){
            flag = true;
        }
        BlockRule blockRule = new BlockRule();
        if(blockRule.refershCellValueAdvance(this)){
            flag = true;
        }

        return flag;
    }

//    public boolean fillValue() throws Exception {
//        boolean flag = false;
//
//        Cell curCell = this.sudokuCell;
//        Cell colCell = this.sudokuCell;
//        while(curCell != null){
//            Set<Integer> valueSet = curCell.getPossibleValue();
//            if(curCell.getValue()==0 && curCell.getPossibleValue().size() == 1){
//                curCell.setValue(valueSet.iterator().next());
//                flag = true;
//                PrintUtil.printLog(curCell.toString());
//                PrintUtil.printLog((HashMap<Integer, String>) curCell.getImpossibleValue());
//            }
//
//            curCell = curCell.getRightCell();
//            if(curCell == null){
//                colCell = colCell.getDownCell();
//                curCell = colCell;
//            }
//        }
//        return flag;
//    }

    public boolean isAllsetValue(){
        boolean flag = true;

        Cell curCell = this.sudokuCell;
        Cell colCell = this.sudokuCell;
        while(curCell != null){
            if(curCell.getValue()==0){
                return false;
            }

            curCell = curCell.getRightCell();
            if(curCell == null){
                colCell = colCell.getDownCell();
                curCell = colCell;
            }
        }

        return flag;
    }


    public boolean checkDuplication(){
        RowRule rowRule = new RowRule() ;
        if(!rowRule.checkDuplication(this)){
            return false;
        }

        ColRule colRule = new ColRule();
        if(!colRule.checkDuplication(this)){
            return false;
        }

        BlockRule blockRule = new BlockRule();
        if(!blockRule.checkDuplication(this)){
            return false;
        }

        return true;
    }

//    private static void initSudoku(int[][] arr) {
//        for(int i=0; i<arr.length ; i++){
//            for(int j=0; j<arr[i].length; j++){
//
//                Integer value = arr[i][j];
//                if(StringUtils.isEmpty(value)){
//                    value = 0;
//                }
//                Cell cell = new Cell();
//                cell.setPossibleValue(new HashSet<>(Arrays.asList(allValue)));
//                cell.setInitValue(value);
//                cell.setColIndex(i);
//                cell.setRowIndex(j);
//                sudoku[i][j] = cell;
//
//                if(i>0 && null!=sudoku[i-1][j]){
//                    cell.setLeftCell(sudoku[i-1][j]);
//                }
//                if(i<arr.length-1 && null!=sudoku[i+1][j]){
//                    cell.setRightCell(sudoku[i+1][j]);
//                }
//                if(j>0 && null!=sudoku[i][j-1]){
//                    cell.setUpCell(sudoku[i][j-1]);
//                }
//                if(j<arr.length-1 && null!=sudoku[i][j+1]){
//                    cell.setDownCell(sudoku[i][j+1]);
//                }
//            }
//        }
//    }


}
