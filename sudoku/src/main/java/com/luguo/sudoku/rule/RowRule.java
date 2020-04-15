package com.luguo.sudoku.rule;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.LogLevel;
import com.luguo.sudoku.comm.RuleType;
import com.luguo.sudoku.util.PrintUtil;
import com.luguo.sudoku.util.SudokuUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class RowRule implements MyRule {

    public final static RuleType RULE_TYPE = RuleType.ROW;

    @Override
    public boolean checkDuplication(Sudoku sudoku) {
        boolean flag = true;
        for(int i=0 ; i<sudoku.MAX_INDEX_VALUE ; i++){
            if(!checkDuplication(sudoku, i)){
                flag = false;
            };
        }

        return flag;
    }

    @Override
    public boolean checkDuplication(Sudoku sudoku, int index) {
        Cell[] cellArray = getCellArray(sudoku,index);

        Map<String, List<Cell>> checkResult = SudokuUtil.checkDuplication(RULE_TYPE, cellArray);
        if(!CollectionUtils.isEmpty(checkResult)){
            PrintUtil.printCheckResult(checkResult);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean refershCellValue(Sudoku sudoku) throws Exception {
        boolean flag = false;
        for(int i=0 ; i<sudoku.MAX_INDEX_VALUE ; i++){
            if(refershCellValue(sudoku, i)){
                flag = true;
            };
        }

        return flag;
    }

    @Override
    public boolean refershCellValue(Sudoku sudoku, int index) throws Exception {
        Cell[] rowArray = getCellArray(sudoku,index);

        return SudokuUtil.refershCellValue(RULE_TYPE, rowArray);
    }

    @Override
    public boolean refershCellValueAdvance(Sudoku sudoku) throws Exception {
        boolean flag = false;
        for(int i=0 ; i<sudoku.MAX_INDEX_VALUE ; i++){
            if(refershCellValueAdvance(sudoku, i)){
                flag = true;
            };
        }

        return flag;
    }

    @Override
    public boolean refershCellValueAdvance(Sudoku sudoku, int index) throws Exception {
        Cell[] rowArray = getCellArray(sudoku,index);

        return SudokuUtil.refershCellValueAdvance(RULE_TYPE, rowArray);
    }

    @Override
    public Cell[] getCellArray(Sudoku sudoku,int rowIndex) {
        if(rowIndex >= sudoku.MAX_INDEX_VALUE){
            PrintUtil.printLog(LogLevel.ERR,"当前索引[" + rowIndex + "]超出最大值[9]");
            return null;
        }
        Cell[] row = rowRraversal(sudoku, rowIndex);

        return row;
    }

    /**
     * 行遍历
     * @param sudoku
     * @param rowIndex
     * @return
     */
    private Cell[] rowRraversal(Sudoku sudoku, int rowIndex) {
        Cell[] row = new Cell[sudoku.MAX_INDEX_VALUE];
        Cell curCell = sudoku.sudokuCell;
        Cell colCell = sudoku.sudokuCell;
        while (true){
            if(curCell.getColIndex() == rowIndex){
                row[curCell.getRowIndex()] = curCell;
                curCell = curCell.getRightCell();

                if(curCell ==null){
                    break;
                }
            }else{
                colCell = colCell.getDownCell();
                if(colCell == null){
                    break;
                }
                curCell = colCell;
            }
        }
        return row;
    }

}
