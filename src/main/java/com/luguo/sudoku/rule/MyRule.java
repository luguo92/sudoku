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

}
