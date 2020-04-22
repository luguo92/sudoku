package com.luguo.sudoku;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.util.PrintUtil;
import com.luguo.sudoku.util.SudokuUtil;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SudokuGenerator {

    // 田
    public static final String TIAN_SHAPE_STR = "111111111100010001100010001100010001111111111100010001100010001100010001111111111";
    // 曰
    public static final String YUE_SHAPE_STR = "111111111100000001100000001100000001111111111100000001100000001100000001111111111";

    public static Sudoku createSudoku() throws Exception {

        Sudoku sudoku = new Sudoku(SudokuUtil.createEmptyArray());

        tryCompleteSudoku(sudoku, sudoku.sudokuCell,sudoku.sudokuCell.getPossibleValue());

        PrintUtil.printSudokuCell(sudoku);

        return sudoku;
    }


    public static Sudoku createSudoku(String shape) throws Exception {
        Sudoku sudoku = createSudoku();

        Cell curCell = sudoku.sudokuCell;
        for(int i=0 ; i<shape.length() ; i++){

            char str = shape.charAt(i);
            if(str == '0' || str == ' '){
                curCell.setValue(0);
            }
            curCell = curCell.getNextCell();
        }

        return sudoku;
    }

    public static boolean tryCompleteSudoku(Sudoku sudoku,Cell curCell,Set<Integer> curPossibleValueSet) throws Exception {

//        PrintUtil.printLog("当前处理Cell: "+ curCell + ",  可选取值" + curPossibleValueSet.toString());

//        PrintUtil.printSudokuCell(sudoku);
        //递归终结条件
        if(null == curCell){
            PrintUtil.printLog("当前处理Cell为空, 返回成功");
            return true;
        }

        if(CollectionUtils.isEmpty(curPossibleValueSet)){
            PrintUtil.printLog("当前处理Cell失败0: "+ curCell + ", 可取值集合" + curCell.getPossibleValue().toString()  + ",  值为" + curCell.getValue() + ", 保留可选取值" +curPossibleValueSet.toString());
            return false;
        }

        Integer curValue = null;
        Iterator<Integer> iterator = curPossibleValueSet.iterator();

        Integer times = SudokuUtil.getRandomInt(curPossibleValueSet.size());
        for (int i = 0; i < times; i++) {
            iterator.next();
        }
        curValue = iterator.next();
        iterator.remove();

        if(SudokuUtil.checkDuplication(sudoku,curCell,curValue)){
            curCell.setValue(curValue);
            Map<Cell,Set<Integer>> cellMap = SudokuUtil.noticeOther(sudoku, curValue, curCell);

            //指定下一递归流程
            Cell nextCell = curCell.getNextNullCell();
            if(null == nextCell){
                return true;
            }
            Set<Integer> nextPossibleValueSet  = new HashSet<>(nextCell.getPossibleValue());
            PrintUtil.printLog("当前处理Cell1: "+ curCell  + ", 可取值集合" + curCell.getPossibleValue().toString() + ",  赋值为" + curValue + ", 保留可选取值" +curPossibleValueSet.toString() + "----->指定下一Cell: "+ nextCell + ", 可取值集合" + nextCell.getPossibleValue().toString() + ",  保留可选取值" +nextPossibleValueSet.toString()  );

            if(!tryCompleteSudoku(sudoku, nextCell, nextPossibleValueSet)){
                PrintUtil.printLog("当前处理Cell2: "+ curCell  + ", 可取值集合" + curCell.getPossibleValue().toString() + ",  撤销赋值" + curValue + ", 保留可选取值" + curPossibleValueSet.toString());

                SudokuUtil.reverseRefreshNotice(curCell, cellMap);

                //TODO 待确认
                if(iterator.hasNext() && !CollectionUtils.isEmpty(curPossibleValueSet)){
                    PrintUtil.printLog("这是什么鬼????? 并发问题???");
                }

                if (iterator.hasNext() || !CollectionUtils.isEmpty(curPossibleValueSet)) {
                    //返回上一流程
                    PrintUtil.printLog("----->处理上一Cell: "+ curCell + ", 可取值集合" + curCell.getPossibleValue().toString()  + ",  已赋值为" + curCell.getValue() + ", 保留可选取值" +curPossibleValueSet.toString());
                    return tryCompleteSudoku(sudoku, curCell, curPossibleValueSet);
                }else {
                    PrintUtil.printLog("当前处理Cell失败1: "+ curCell + ", 可取值集合" + curCell.getPossibleValue().toString() + ",  赋值为" + curValue + ", 保留可选取值" +curPossibleValueSet.toString() );
                    return false;
                }
            }else{
                PrintUtil.printLog("当前处理Cell成功1: "+ curCell  + ", 可取值集合" + curCell.getPossibleValue().toString() + ",  赋值为" + curValue + ", 保留可选取值" +curPossibleValueSet.toString());
                return true;
            }
        }else{
            if(!iterator.hasNext() && CollectionUtils.isEmpty(curPossibleValueSet)){
                PrintUtil.printLog("当前处理Cell失败2: "+ curCell  + ", 可取值集合" + curCell.getPossibleValue().toString()+ ",  赋值为" + curValue + ", 保留可选取值" +curPossibleValueSet.toString() );
                return false;
            }else{
                boolean flag = tryCompleteSudoku(sudoku, curCell, curPossibleValueSet);
                PrintUtil.printLog("当前处理Cell" + (flag?"成功":"失败") +"3: "+ curCell  + ", 可取值集合" + curCell.getPossibleValue().toString()+ ",  赋值为" + curValue + ", 保留可选取值" +curPossibleValueSet.toString() );
                return flag;
            }
        }
    }

    public static void main(String args[]) throws Exception {

        Sudoku sudokuYue = createSudoku(YUE_SHAPE_STR);
        PrintUtil.printLog(sudokuYue.toString());
        Sudoku sudoku = new Sudoku(sudokuYue);
        PrintUtil.printLog(sudoku.toString());
        PrintUtil.printSudokuCell(sudoku);

//        Sudoku sudoku = createEmptySudoku();
//        long startTime = System.currentTimeMillis();
//        if(sudoku.isCompeled() && sudoku.checkDuplication()){
//            PrintUtil.printLog("都被正确填充");
//        }else if(!sudoku.isCompeled()){
//            PrintUtil.printLog("未填充完成");
//        }else{
//            PrintUtil.printLog("存在重复的单元");
//        }
//        long endTime = System.currentTimeMillis();
//        PrintUtil.printLog(LogLevel.ERR, endTime - startTime + "毫秒");

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
