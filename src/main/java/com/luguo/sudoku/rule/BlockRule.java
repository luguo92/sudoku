package com.luguo.sudoku.rule;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
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

        Cell headCell = sudoku.getBlockHeadCell(index);
        Map<String, List<Cell>> checkResult = SudokuUtil.checkDuplication(sudoku,RULE_TYPE,headCell);
        if(!CollectionUtils.isEmpty(checkResult)){
            PrintUtil.printCheckResult(checkResult);
            return false;
        }else{
            return true;
        }
    }


}
