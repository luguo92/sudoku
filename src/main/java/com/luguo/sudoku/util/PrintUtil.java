package com.luguo.sudoku.util;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.LogLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintUtil {

    public static void printLog(String msg) {
        printLog(LogLevel.INFO,msg);
    }

    public static void printLog(LogLevel logLevel, String msg) {

        switch (logLevel){
            case ERR:
                System.err.println(msg);
                break;
            case INFO:
                System.out.println(msg);
                break;
            default:
                System.out.println(msg);
                break;
        }
    }

//    public static void  printLog(LogLevel logLevel, Cell cell) {
//
//        HashMap<Cell, String> result = cell.getCheckResult();
//        String desc = "";
//        for(HashMap.Entry<Cell,String> entry: result.entrySet()){
//            desc = desc + "  " + entry.getValue() + ":" + "当前cell" + cell + "与目标cell" + entry.getKey() + "重复";
//        }
//
//        printLog(logLevel,desc);
//
//    }

    /**
     * 打印查重命中规则
     * @param logLevel
     * @param entry
     */
    public static void  printLog(LogLevel logLevel, Map.Entry<String, List<Cell>> entry ) {

        String desc = entry.getKey() + " 存在相同值的单元格有:";
        for(Cell cell : entry.getValue()){
            desc = desc + cell + ",";
        }
        desc.substring(0,desc.length()-2);

        printLog(logLevel,desc);
    }

//    public static void  printLog(HashMap<Integer,String > map) {
//
//        for(Map.Entry<Integer,String > entry : map.entrySet()){
//            String str = entry.getValue();
//
//            String desc = str;
//            printLog(LogLevel.INFO,desc);
//        }
//    }


    /**
     * 打印查重结果
     * @param cellMap
     */
    public static void printCheckResult(Map<String, List<Cell>> cellMap) {
        for(Map.Entry<String,List<Cell>> entry : cellMap.entrySet()){
            if(entry.getValue().size() > 1){
                PrintUtil.printLog(LogLevel.ERR, entry);
            }
        }
    }

    public static void printSudokuCell(Sudoku sudoku) {
        Cell curCell = sudoku.sudokuCell;
        Cell colCell = sudoku.sudokuCell;
        while (true){

            System.out.print(String.format("%-18s",curCell));
            curCell = curCell.getRightCell();

            //指定下一行
            if(curCell == null){
                System.out.println();
                Cell tempCell = colCell;
                while(tempCell != null){
                    String temp = tempCell.getPossibleValue().size()>1?tempCell.getPossibleValue().toString():"";
                    System.err.print(String.format("%-18s",temp.replace(" ","")));
                    tempCell = tempCell.getRightCell();
                }
                System.out.println();
                System.out.println();


                colCell = colCell.getDownCell();
                if(colCell == null){
                    break;
                }
                curCell = colCell;
            }
        }
        System.out.println();
        System.out.println();
    }
}
