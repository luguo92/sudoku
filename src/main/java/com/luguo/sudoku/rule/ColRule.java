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

public class ColRule implements MyRule {


    public final static RuleType RULE_TYPE = RuleType.COL;

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
        Cell[] cellArray = getCellArray(sudoku,index);

        return SudokuUtil.refershCellValue( RULE_TYPE, cellArray);
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
    public Cell[] getCellArray(Sudoku sudoku,int colIndex) {
        if(colIndex >= sudoku.MAX_INDEX_VALUE){
            PrintUtil.printLog(LogLevel.ERR,"当前索引[" + colIndex + "]超出最大值[9]");
            return null;
        }
        Cell[] col = new Cell[sudoku.MAX_INDEX_VALUE];
        Cell curCell = sudoku.sudokuCell;
        Cell rowCell = sudoku.sudokuCell;
        while (true){
            if(curCell.getRowIndex() == colIndex){
                col[curCell.getColIndex()] = curCell;
                curCell = curCell.getDownCell();

                if(curCell ==null){
                    break;
                }
            }else{
                rowCell = rowCell.getRightCell();
                if(rowCell == null){
                    break;
                }
                curCell = rowCell;
            }
        }

        return col;
    }
}
