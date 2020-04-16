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

public class BlockRule implements MyRule {

    public final static RuleType RULE_TYPE = RuleType.BLOCK;

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

        return SudokuUtil.refershCellValue(RULE_TYPE, cellArray);
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
    public Cell[] getCellArray(Sudoku sudoku,int blockIndex) {
        Cell[][] cells = getBlock(sudoku, blockIndex);

        Cell[] result = SudokuUtil.arrayConvert(cells);
        return result;
    }


    private static Cell[][] getBlock(Sudoku sudoku,int blockIndex) {
        if(blockIndex >= sudoku.MAX_INDEX_VALUE){
            PrintUtil.printLog(LogLevel.ERR,"当前索引[" + blockIndex + "]超出最大值[9]");
            return null;
        }
        Cell[][] block = new Cell[3][3];

        int minColIndex = (blockIndex/3) * 3;
        int maxColIndex = (blockIndex/3) * 3 + 3;
        int rowTemp = (blockIndex + 1)%3 ;
        if(rowTemp == 0){
            rowTemp = 3;
        }
        int minRowIndex = rowTemp * 3 - 3;
        int maxRowIndex = rowTemp * 3;

        Cell curCell = sudoku.sudokuCell;
        Cell colCell = sudoku.sudokuCell;
        while(true){
            if(maxColIndex > curCell.getColIndex() && curCell.getColIndex()>= minColIndex&&
                    maxRowIndex > curCell.getRowIndex() && curCell.getRowIndex() >= minRowIndex){
                block[curCell.getColIndex()%3][curCell.getRowIndex()%3] = curCell;
            }
            curCell = curCell.getRightCell();
            if(null == curCell || curCell.getColIndex() > maxColIndex){
                colCell = colCell.getDownCell();
                if(colCell == null || colCell.getRowIndex() > maxRowIndex){
                    break;
                }
                curCell = colCell;
            }
        }

        return block;
    }


}
