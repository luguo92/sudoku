package com.luguo.sudoku;

import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.util.PrintUtil;
import com.luguo.sudoku.util.SudokuUtil;

public class SudokuTest {


    public static void main(String args[]) throws Exception {

//        int[][] arr = { {5,9,3,4,1,6,2,8,7},
//                        {1,6,8,7,9,2,4,5,3},
//                        {7,2,4,3,5,8,9,1,6},
//                        {8,4,2,1,6,7,3,9,5},
//                        {3,7,5,9,2,4,8,6,1},
//                        {6,1,9,5,8,3,7,2,4},
//                        {4,8,1,2,7,5,6,3,9},
//                        {9,3,6,8,4,1,5,7,2},
//                        {2,5,7,6,3,9,1,4,8}};

//        int[][] arr = { {5,9,3,4,1,6,0,0,0},
//                        {1,6,0,7,0,2,4,0,3},
//                        {7,2,4,3,5,8,0,1,6},
//                        {8,0,0,1,0,0,3,9,0},
//                        {3,0,5,0,2,4,8,6,1},
//                        {6,0,0,5,8,3,0,2,4},
//                        {4,8,1,0,7,0,6,3,0},
//                        {9,0,6,0,4,1,0,7,2},
//                        {2,5,0,6,3,9,1,0,0}};

//        int[][] arr = { {5,9,3,4,1,6,0,0,0},
//                        {1,0,0,0,0,0,0,5,3},
//                        {7,0,0,0,0,0,0,0,0},
//                        {8,0,0,1,0,0,3,9,0},
//                        {3,0,5,0,2,4,8,6,1},
//                        {6,0,0,5,8,3,0,2,4},
//                        {4,8,1,0,7,0,6,3,0},
//                        {9,0,6,0,4,1,0,7,2},
//                        {2,5,0,6,3,9,1,0,0}};



        Integer[][] arr = { {0,5,2,0,8,1,0,6,0},
                            {0,0,0,0,0,6,5,0,0},
                            {0,0,9,2,0,0,0,0,0},
                            {4,0,0,5,0,0,0,0,1},
                            {0,0,0,0,9,0,0,3,0},
                            {6,3,1,0,0,0,0,0,0},
                            {5,0,0,0,0,0,4,0,8},
                            {9,0,6,0,0,0,7,0,0},
                            {0,4,0,3,7,5,0,0,0}};

        Sudoku sudoku = new Sudoku(arr);
        PrintUtil.printSudokuCell(sudoku);
        if(sudoku.isCompeled() && sudoku.checkDuplication()){
            PrintUtil.printLog("都被正确填充");
        };

        int count = 0;
        while(SudokuUtil.refersh(sudoku)){
           count++;
        }
        PrintUtil.printSudokuCell(sudoku);

        if(sudoku.isCompeled() && sudoku.checkDuplication()){
            PrintUtil.printLog("都被正确填充");
        }else if(!sudoku.isCompeled()){
            PrintUtil.printLog("未填充完成");
        }else{
            PrintUtil.printLog("存在重复的单元");
        }
        System.err.println(count);
    }

}
