package com.luguo.sudoku.util;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.CellStatus;
import com.luguo.sudoku.comm.RuleType;
import com.sun.istack.internal.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class SudokuUtil {

    public static Map<String,List<Cell>> checkDuplication(Sudoku sudoku,RuleType ruleType, Cell headCell) {
        HashMap<String,List<Cell>> cellMap = new HashMap<>();

        Cell curCell = headCell;

        while(curCell != null){

            Integer value = curCell.getValue();
            if(value > 0){
                String key = getSign(curCell,ruleType);
                if(cellMap.keySet().contains(key)){
                    List<Cell> cellList = cellMap.get(key);
                    cellList.add(curCell);
                }else {
                    List<Cell> cellList = new ArrayList<>();
                    cellList.add(curCell);
                    cellMap.put(key,cellList);
                }
            }

            switch (ruleType){
                case ROW:
                    curCell = sudoku.getNextRowCell(curCell);
                    break;
                case COL:
                    curCell = sudoku.getNextRowCell(curCell);
                    break;
                case BLOCK:
                    curCell = sudoku.getNextBlockCell(curCell);
                    break;
                default:
                    break;
            }

        }

        return cellMap;
    }

    public static String getSign(Cell cell, RuleType ruleType){
        Integer index = null;
        switch (ruleType){
            case COL:
                index = cell.getRowIndex() + 1;
                break;
            case ROW:
                index = cell.getColIndex() + 1;
                break;
            case BLOCK:
                index = cell.getBlockIndex() + 1;
                break;
            default:
                break;
        }
        Integer value = cell.getValue();
        return "第"+index + ruleType.getDesc() + ",取值为:" + value ;
    }

    public static String getRuleSign(RuleType ruleType, @NotNull Cell...cells){
        Integer index = null;
        switch (ruleType){
            case COL:
                index = cells[0].getRowIndex() + 1;
                break;
            case ROW:
                index = cells[0].getColIndex() + 1;
                break;
            case BLOCK:
                index = cells[0].getBlockIndex() + 1;
                break;
            default:
                break;
        }

        String valueStr = null;
        Integer value = cells[0].getValue();
        if(value == 0){
            valueStr = cells[0].getPossibleValue().toString();
        }else{
            valueStr = value.toString();
        }

        return "第"+index + ruleType.getDesc() + "存在"+cells+"限定取值为:" + valueStr ;
    }


    public static void noticeOther(Sudoku sudoku, Integer impossibleValue, Cell...cell) throws Exception {
        Set<Integer> impossibleValueSet = new HashSet<>();
        impossibleValueSet.add(impossibleValue);
        noticeOther(sudoku,impossibleValueSet,null,cell);
    }

    public static void noticeOther(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell...cell) throws Exception {
        noticeOther(sudoku,impossibleValueSet,null,cell);
    }

    public static void noticeOther(Sudoku sudoku,  Set<Integer> impossibleValueSet, RuleType ruleType, Cell...cell) throws Exception {

//        PrintUtil.printLog(impossibleValueSet.toString() + ruleType + Arrays.asList(cell).toString());
        for(RuleType rule : RuleType.values()){
            if(null!=ruleType && !rule.equals(ruleType)){
                continue;
            }
            switch (rule){
                case ROW:
                    refreshRow(sudoku, impossibleValueSet, cell);
                    break;
                case COL:
                    refreshCol(sudoku, impossibleValueSet, cell);
                    break;
                case BLOCK:
                    refreshBlock(sudoku, impossibleValueSet, cell);
                    break;
                default:
                    break;
            }
        }

//        PrintUtil.printSudokuCell(sudoku);
        return ;
    }

    private static void refreshBlock(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell...cell) throws Exception {
        String blockSign = SudokuUtil.getRuleSign(RuleType.BLOCK,cell);
        Cell curBlockCell = sudoku.getBlockHeadCell(cell[0].getBlockIndex());
        while(curBlockCell != null){
            if(Arrays.asList(cell).contains(curBlockCell)){
                curBlockCell = sudoku.getNextBlockCell(curBlockCell);
                continue;
            }

            curBlockCell.addImpossibleValue(impossibleValueSet, blockSign);

            Set<Integer> valueSet = curBlockCell.getPossibleValue();
            if(!CollectionUtils.isEmpty(valueSet) && valueSet.size() == 1 && curBlockCell.getValue() == 0){
                curBlockCell.setValue(valueSet.iterator().next());
                noticeOther(sudoku,curBlockCell.getValue(),curBlockCell);
            }

            curBlockCell = sudoku.getNextBlockCell(curBlockCell);
        }
    }

    private static void refreshCol(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell...cell) throws Exception {
        String colSign = SudokuUtil.getRuleSign(RuleType.COL,cell);
        Cell curColCell = sudoku.getColHeadCell(cell[0].getColIndex());
        while(curColCell != null){
            if(Arrays.asList(cell).contains(curColCell)){
                curColCell = sudoku.getNextColCell(curColCell);
                continue;
            }

            curColCell.addImpossibleValue(impossibleValueSet, colSign);
            Set<Integer> valueSet = curColCell.getPossibleValue();
            if(!CollectionUtils.isEmpty(valueSet) && valueSet.size() == 1 && curColCell.getValue() == 0){
                curColCell.setValue(valueSet.iterator().next());
                noticeOther(sudoku,curColCell.getValue(),curColCell);
            }

            curColCell = sudoku.getNextColCell(curColCell);
        }
    }

    private static void refreshRow(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell...cell) throws Exception {
        String rowSign = SudokuUtil.getRuleSign(RuleType.ROW,cell);
        Cell curRowCell = sudoku.getRowHeadCell(cell[0].getRowIndex());
        while(curRowCell != null){
            if(Arrays.asList(cell).contains(curRowCell)){
                curRowCell = sudoku.getNextRowCell(curRowCell);
                continue;
            }

            curRowCell.addImpossibleValue(impossibleValueSet, rowSign);
            Set<Integer> valueSet = curRowCell.getPossibleValue();
            if(!CollectionUtils.isEmpty(valueSet) && valueSet.size() == 1 && curRowCell.getValue() == 0){
                curRowCell.setValue(valueSet.iterator().next());
                noticeOther(sudoku,curRowCell.getValue(),curRowCell);
            }

            curRowCell = sudoku.getNextRowCell(curRowCell);

        }
    }

    /**
     * 更新数独
     * @param sudoku
     * @return
     * @throws Exception
     */
    public static boolean refersh(Sudoku sudoku) throws Exception {
        boolean flag = false;

//        PrintUtil.printSudokuCell(sudoku);
        Cell curCell = sudoku.sudokuCell;
        while(null!= curCell){

            Set<Integer> possibleValueSet = curCell.getPossibleValue();
            CellStatus cellStatus = getCellStatus(curCell);
            if(CellStatus.REFRESH.equals(cellStatus) || CellStatus.INIT.equals(cellStatus)){
                if(CellStatus.INIT.equals(cellStatus)){
                    curCell.setValue(possibleValueSet.iterator().next());
                }

                noticeOther(sudoku,curCell.getValue(),curCell);
                curCell.setChanged(false);
                flag = true;
            }

            if(possibleValueSet.size() >= 2 && possibleValueSet.size() < 8){
                Iterator<Integer> it = possibleValueSet.iterator();

                for(RuleType ruleType : RuleType.values()) {
                    List<Cell> cellList = new ArrayList<>();
                    Cell nextCell = curCell;

                    while (it.hasNext() && null!=nextCell) {

                        Set<Integer> nextRowCellPossibleValueSet = nextCell.getPossibleValue();
                        if (!cellList.contains(nextCell) && possibleValueSet.equals(nextRowCellPossibleValueSet)) {
                            cellList.add(nextCell);
                            if(nextCell != curCell){
                                it.next();
                            }
                        }
                        switch (ruleType){
                            case ROW:
                                nextCell = sudoku.getNextRowCell(nextCell);
                                break;
                            case COL:
                                nextCell = sudoku.getNextColCell(nextCell);
                                break;
                            case BLOCK:
                                nextCell = sudoku.getNextBlockCell(nextCell);
                                break;
                        }
                    }

                    if (cellList.size() == possibleValueSet.size()) {
                        noticeOther(sudoku, possibleValueSet, ruleType, cellList.toArray(new Cell[cellList.size()]));
                        flag = true;
                    }
                }

            }

            curCell = curCell.getNextCell();

        }

        return flag;
    }

    private static CellStatus getCellStatus(Cell cell) {
        Set<Integer> possibleValueSet = cell.getPossibleValue();
        Integer value = cell.getValue();
        if(possibleValueSet.size() == 1 && value ==0){
            return CellStatus.INIT;
        }else if(possibleValueSet.size() == 1 && value > 0){
            if(cell.isChanged()){
                return CellStatus.REFRESH;
            }else{
                return CellStatus.END;
            }
        }else{
            return CellStatus.UNINIT;
        }
    }


    public static void main(String args[]) {

        for (int index = 0; index < 9; index++){
            for(int indexInBlock=0; indexInBlock<9; indexInBlock ++) {
                int rowIndex = index / 3 * 3 + indexInBlock / 3;
                int colIndex = index % 3 * 3 + indexInBlock % 3;

                System.out.println(index + "宫" + indexInBlock + "个单元格: [" + rowIndex + "," + colIndex + "]");

            }
        }


//        for (int rowIndex = 0; rowIndex < 9; rowIndex++){
//            for(int colIndex=0; colIndex<9; colIndex ++) {
//
//                int blockindex = rowIndex/3 * 3 + colIndex/3;
//
//                System.out.println(blockindex + "宫" +  "的单元格: [" + rowIndex + "," + colIndex + "]");
//
//            }
//        }

    }
}
