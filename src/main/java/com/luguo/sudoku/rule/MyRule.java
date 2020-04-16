package com.luguo.sudoku.rule;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;

public interface MyRule {
    /**
     * 查重
     * @param sudoku
     * @return
     */
    boolean checkDuplication(Sudoku sudoku);

    /**
     * 查重
     * @param sudoku
     * @param index
     * @return
     */
    boolean checkDuplication(Sudoku sudoku, int index);

    /**
     * 刷新数据
     * @param sudoku
     * @return
     */
    boolean refershCellValue(Sudoku sudoku) throws Exception;

    /**
     * 刷新数据
     * @param sudoku
     * @param index
     * @return
     * @throws Exception
     */
    boolean refershCellValue(Sudoku sudoku, int index) throws Exception;

    /**
     * 刷新数据
     * @param sudoku
     * @return
     */
    boolean refershCellValueAdvance(Sudoku sudoku) throws Exception;

    /**
     * 刷新数据
     * @param sudoku
     * @param index
     * @return
     * @throws Exception
     */
    boolean refershCellValueAdvance(Sudoku sudoku, int index) throws Exception;

    Cell[] getCellArray(Sudoku sudoku,int index);
}
