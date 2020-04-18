package com.luguo.sudoku.rule;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.LogLevel;
import com.luguo.sudoku.comm.RuleType;
import com.luguo.sudoku.util.PrintUtil;
import com.luguo.sudoku.util.SudokuUtil;
import com.sun.istack.internal.NotNull;
import com.sun.javafx.css.Rule;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        Cell headCell = sudoku.getRowHeadCell(index);
        Map<String, List<Cell>> checkResult = SudokuUtil.checkDuplication(sudoku,RULE_TYPE,headCell);
        if(!CollectionUtils.isEmpty(checkResult)){
            PrintUtil.printCheckResult(checkResult);
            return false;
        }else{
            return true;
        }
    }

}
