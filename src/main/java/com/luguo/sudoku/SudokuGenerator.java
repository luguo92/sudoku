package com.luguo.sudoku;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.Flow;
import com.luguo.sudoku.util.PrintUtil;
import com.luguo.sudoku.util.SudokuUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class SudokuGenerator {

    public static Sudoku createEmptySudoku() throws Exception {

        Sudoku sudoku = new Sudoku(SudokuUtil.createEmptyArray());

        tryCompleteSudoku(sudoku, sudoku.sudokuCell,sudoku.sudokuCell.getPossibleValue());

        PrintUtil.printSudokuCell(sudoku);

        return sudoku;

    }

    public static boolean tryCompleteSudoku(Sudoku sudoku,Cell curCell,Set<Integer> curPossibleValueSet) throws Exception {

        //递归终结条件
        if(null == curCell){
            return true;
        }

//        if(null == curPossibleValueSet){
//            curPossibleValueSet = new HashSet<>(curCell.getPossibleValue());
//        }

        if(CollectionUtils.isEmpty(curPossibleValueSet) && !CollectionUtils.isEmpty(curCell.getPossibleValue())){
            return false;
        }

        Integer curValue = null;
        Iterator<Integer> iterator = curPossibleValueSet.iterator();

        if(curCell.getValue()<=0) {
            Integer times = SudokuUtil.getRandomInt(curPossibleValueSet.size());
            for (int i = 0; i < times; i++) {
                iterator.next();
            }
            curValue = iterator.next();
            iterator.remove();
        }else{
            curValue = curCell.getValue();
        }
        if(SudokuUtil.checkDuplication(sudoku,curCell,curValue)){
            curCell.setValue(curValue);
            Map<Cell,Set<Integer>> cellMap = SudokuUtil.noticeOther(sudoku, curValue, curCell);

            //指定下一递归流程
            Cell preCell = curCell;
            Set<Integer> prePossibleValueSet = new HashSet<>(curCell.getPossibleValue());
            curCell = curCell.getNextCell();
            if(null == curCell){
                return true;
            }
            curPossibleValueSet = new HashSet<>(curCell.getPossibleValue());

            if(!tryCompleteSudoku(sudoku, curCell, curPossibleValueSet)){
                SudokuUtil.reverseRefreshNotice(curCell, cellMap);

                if (iterator.hasNext()) {
                    return tryCompleteSudoku(sudoku, preCell, prePossibleValueSet);
                }else {
                    return false;
                }
            }
        }else{
            if(!iterator.hasNext()){
                return false;
            }else{
                if(!tryCompleteSudoku(sudoku, curCell, curPossibleValueSet)){
                    if (iterator.hasNext()) {
                        return tryCompleteSudoku(sudoku, curCell, curPossibleValueSet);
                    }else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String args[]) throws Exception {
        Sudoku sudoku = createEmptySudoku();
        if(sudoku.isCompeled() && sudoku.checkDuplication()){
            PrintUtil.printLog("都被正确填充");
        }else if(!sudoku.isCompeled()){
            PrintUtil.printLog("未填充完成");
        }else{
            PrintUtil.printLog("存在重复的单元");
        }

        /*虽然set是无序的， set.iterator().next() 的顺序是可以预期的
        即在set不发生变更的情况下 第一次调用next 获取的值都是一样的， 第二次 第N次都是一样
         */
//        Integer[] arr = new Integer[]{6,123123,5,2,54443};
//        Set<Integer> set = new HashSet<>(Arrays.asList(arr));
//
//        while(true) {
//            Iterator<Integer> iterator = set.iterator();
//            iterator.next();
//            System.out.println(iterator.next());
//        }
    }
}
