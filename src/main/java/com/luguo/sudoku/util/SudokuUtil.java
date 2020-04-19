package com.luguo.sudoku.util;

import com.luguo.sudoku.bean.Cell;
import com.luguo.sudoku.bean.Sudoku;
import com.luguo.sudoku.comm.CellStatus;
import com.luguo.sudoku.comm.RefreshLevel;
import com.luguo.sudoku.comm.RuleType;
import com.sun.istack.internal.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class SudokuUtil {

    private static final class RandomHolder{
        static final Random randomGenerator = new Random();
    }

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
                    curCell = sudoku.getNextColCell(curCell);
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

    public static boolean checkDuplication(Sudoku sudoku, Cell cell) {
        return checkDuplication(sudoku,cell,cell.getValue());
    }
    /**
     * 为当前cell检查其所在的行、列、宫 是否与指定值重复
     * @param sudoku
     * @param cell
     * @param value
     * @return
     */
    public static boolean checkDuplication(Sudoku sudoku, Cell cell, Integer value) {

        Map<Integer,String> impossibleValueMap = getImpossibleForCell(sudoku, cell);

        if(impossibleValueMap.keySet().contains(value)){
            return false;
        }

        return true;
    }

    public static Map<Integer,String> getImpossibleForCell(Sudoku sudoku, Cell cell) {
        Map<Integer,String> impossibleValueMap = new HashMap();
        for(RuleType ruleType : RuleType.values()) {
            Cell curCell = null;
            switch (ruleType) {
                case ROW:
                    curCell = sudoku.getRowHeadCell(cell.getRowIndex());
                    break;
                case COL:
                    curCell = sudoku.getColHeadCell(cell.getColIndex());
                    break;
                case BLOCK:
                    curCell = sudoku.getBlockHeadCell(cell.getBlockIndex());
                    break;
                default:
                    break;
            }

            while (curCell != null) {

                Integer curValue = curCell.getValue();
                Set<Integer> possibleValueSet = curCell.getPossibleValue();
                if (curValue > 0 && cell != curCell) {
                    if(!impossibleValueMap.containsKey(curValue)){
                        impossibleValueMap.put(curValue,getRuleSign(ruleType,curCell));
                    }
                }else if(possibleValueSet.size() == 1 && cell != curCell){
                    Integer possibleValue = possibleValueSet.iterator().next();
                    if(!impossibleValueMap.containsKey(possibleValue)){
                        impossibleValueMap.put(possibleValue,getRuleSign(ruleType,curCell));
                    }
                }


                switch (ruleType) {
                    case ROW:
                        curCell = sudoku.getNextRowCell(curCell);
                        break;
                    case COL:
                        curCell = sudoku.getNextColCell(curCell);
                        break;
                    case BLOCK:
                        curCell = sudoku.getNextBlockCell(curCell);
                        break;
                    default:
                        break;
                }
            }
        }
        return impossibleValueMap;
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


    public static HashMap<Cell,Set<Integer>> noticeOther(Sudoku sudoku, Integer value,Cell...cell) throws Exception {
        Set<Integer> impossibleValueSet = new HashSet<>();
        impossibleValueSet.add(value);
        return noticeOther(sudoku,impossibleValueSet,null,cell);
    }

    public static HashMap<Cell,Set<Integer>> noticeOther(Sudoku sudoku, Set<Integer> valueSet, Cell...cell) throws Exception {
        return noticeOther(sudoku,valueSet,null,cell);
    }

    public static HashMap<Cell,Set<Integer>> noticeOther(Sudoku sudoku,  Set<Integer> impossibleValueSet, RuleType ruleType,Cell...cell) throws Exception {

        HashMap<Cell,Set<Integer>> cellMap = new HashMap<>();
        for(RuleType rule : RuleType.values()){
            if(null!=ruleType && !rule.equals(ruleType)){
                continue;
            }
            switch (rule){
                case ROW:
                    cellMap.putAll(refreshRow(sudoku, impossibleValueSet, cell));
                    break;
                case COL:
                    cellMap.putAll(refreshCol(sudoku, impossibleValueSet, cell));
                    break;
                case BLOCK:
                    cellMap.putAll(refreshBlock(sudoku, impossibleValueSet, cell));
                    break;
                default:
                    break;
            }
        }

        return cellMap;
    }

    private static HashMap<Cell,Set<Integer>>  refreshBlock(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell... cell) throws Exception {
        HashMap<Cell,Set<Integer>> cellMap = new HashMap<>();
        String blockSign = SudokuUtil.getRuleSign(RuleType.BLOCK,cell);
        Cell curBlockCell = sudoku.getBlockHeadCell(cell[0].getBlockIndex());
        while(curBlockCell != null){
            if(Arrays.asList(cell).contains(curBlockCell)){
                curBlockCell = sudoku.getNextBlockCell(curBlockCell);
                continue;
            }

            if(refreshCell(impossibleValueSet, blockSign, curBlockCell)){
                cellMap.put(curBlockCell,impossibleValueSet);
            }

            curBlockCell = sudoku.getNextBlockCell(curBlockCell);
        }
        return cellMap;
    }

    private static HashMap<Cell,Set<Integer>> refreshCol(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell... cell) throws Exception {
        HashMap<Cell,Set<Integer>> cellMap = new HashMap<>();
        String colSign = SudokuUtil.getRuleSign(RuleType.COL,cell);
        Cell curColCell = sudoku.getColHeadCell(cell[0].getColIndex());
        while(curColCell != null){
            if(Arrays.asList(cell).contains(curColCell)){
                curColCell = sudoku.getNextColCell(curColCell);
                continue;
            }

            if(refreshCell(impossibleValueSet, colSign, curColCell)){
                cellMap.put(curColCell,impossibleValueSet);
            }

            curColCell = sudoku.getNextColCell(curColCell);
        }
        return cellMap;
    }

    private static HashMap<Cell,Set<Integer>> refreshRow(Sudoku sudoku, Set<Integer> impossibleValueSet, Cell... cell) throws Exception {
        HashMap<Cell,Set<Integer>> cellMap = new HashMap<>();
        String rowSign = SudokuUtil.getRuleSign(RuleType.ROW,cell);
        Cell curRowCell = sudoku.getRowHeadCell(cell[0].getRowIndex());
        while(curRowCell != null){
            if(Arrays.asList(cell).contains(curRowCell)){
                curRowCell = sudoku.getNextRowCell(curRowCell);
                continue;
            }
            if(refreshCell(impossibleValueSet, rowSign, curRowCell)){
                cellMap.put(curRowCell,impossibleValueSet);
            }

            curRowCell = sudoku.getNextRowCell(curRowCell);
        }
        return cellMap;
    }

    /**
     * 刷新Cell
     * @param valueSet
     * @param cellSign
     * @param curCell
     * @return
     * @throws Exception
     */
    private static boolean refreshCell(Set<Integer> valueSet, String cellSign, Cell curCell) throws Exception {
        Set<Integer> valueSetTemp = new HashSet<>(curCell.getPossibleValue());
        if (valueSetTemp.retainAll(valueSet) && !CollectionUtils.isEmpty(valueSetTemp)) {
            curCell.addImpossibleValue(valueSetTemp, cellSign);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 撤销因某字段被指定值而带来的一系列修改
     * @param curCell
     * @param cellMap
     * @throws Exception
     */
    public static void reverseRefreshNotice(Cell curCell, Map<Cell, Set<Integer>> cellMap) throws Exception {
        for(Map.Entry<Cell,Set<Integer>> entry : cellMap.entrySet()){
            Set<Integer> valueSet = entry.getValue();
            for(Integer value : valueSet){
                entry.getKey().removeImpPossibleValue(value);
            }
        }
        if(curCell.getInitValue() == 0){
            curCell.setValue(0);
        }
    }

    /**
     * 更新数独
     * @param sudoku
     * @return
     * @throws Exception
     */
    public static boolean refresh(Sudoku sudoku) throws Exception {
        boolean isChanged = true;
        RefreshLevel refreshLevel = RefreshLevel.LOW;
        int retryTime = 0;
        while(retryTime <= 1 || isChanged){

            isChanged = refresh(sudoku,refreshLevel);

            if(!isChanged){
                retryTime++;
                if(retryTime > 1){
                    retryTime = 0;
                    refreshLevel = RefreshLevel.getNextLevel(refreshLevel);
                    if(null == refreshLevel){
                        break;
                    }
                }
            }
        }
        return isChanged;
    }

    public static boolean refresh(Sudoku sudoku, RefreshLevel refreshLevel) throws Exception {
        boolean isChanged = false;
        if(null == refreshLevel){
            return isChanged;
        }

        Cell curCell = sudoku.sudokuCell;
        while(null!= curCell){

            if(refreshByCell(sudoku, curCell,refreshLevel) && !isChanged){
                isChanged = true;
            }

            curCell = curCell.getNextCell();
        }

        return isChanged;
    }

    public static boolean refreshByCell(Sudoku sudoku, Cell curCell, RefreshLevel refreshLevel) throws Exception {
        boolean flag = false;
        Set<Integer> possibleValueSet = curCell.getPossibleValue();
        if(refreshLevel.getLevel() >= RefreshLevel.LOW.getLevel()) {
            CellStatus cellStatus = getCellStatus(curCell);
            if (CellStatus.REFRESH.equals(cellStatus) || CellStatus.INIT.equals(cellStatus)) {
                if (CellStatus.INIT.equals(cellStatus)) {
                    curCell.setValue(possibleValueSet.iterator().next());
                }

                noticeOther(sudoku, curCell.getValue(), curCell);
                curCell.setChanged(false);
                flag = true;
            }
        }

        if(refreshLevel.getLevel() >= RefreshLevel.MID.getLevel()) {
            if (possibleValueSet.size() == 2) {
                flag = refreshByCellPossibleValue(sudoku, curCell);
            }
        }

        if(refreshLevel.getLevel() >= RefreshLevel.HIGH.getLevel()) {
            if (possibleValueSet.size() > 2 && possibleValueSet.size() < sudoku.MAX_INDEX_VALUE -1) {
                flag = refreshByCellPossibleValue(sudoku, curCell);
            }
        }


        return flag;
    }

    private static boolean refreshByCellPossibleValue(Sudoku sudoku, Cell curCell) throws Exception {
        boolean isChanged = false;
        if(!curCell.isChanged()){
            return false;
        }

        Set<Integer> possibleValueSet = curCell.getPossibleValue();
        Iterator<Integer> it = possibleValueSet.iterator();

        for (RuleType ruleType : RuleType.values()) {
            List<Cell> cellList = new ArrayList<>();
            Cell nextCell = curCell;

            while (it.hasNext() && null != nextCell) {

                Set<Integer> nextRowCellPossibleValueSet = nextCell.getPossibleValue();
                if (!cellList.contains(nextCell) && possibleValueSet.equals(nextRowCellPossibleValueSet)) {
                    cellList.add(nextCell);
                    if (nextCell != curCell) {
                        it.next();
                    }
                }
                switch (ruleType) {
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
                HashMap<Cell,Set<Integer>> cellMap= noticeOther(sudoku, possibleValueSet, ruleType, cellList.toArray(new Cell[cellList.size()]));
                if(!CollectionUtils.isEmpty(cellMap)){
                    isChanged = true;
                }
            }
        }
        return isChanged;
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

    /**
     * 创建一个空的二维数组
     * @return
     */
    public static Integer[][] createEmptyArray(){
        Integer[][] arr = new Integer[9][9];
        for(Integer[] entry : arr){
            Arrays.fill(entry, 0);
        }
        return arr;
    }

    public static Integer getRandomInt(Integer maxLimit){
        return RandomHolder.randomGenerator.nextInt(maxLimit);
    }

    /**
     * 复制数独
     * @param oldSudoku
     * @param copyAsTemplate
     *         true  将原数独作为模板进行填充（value当作initValue填充）
     *         false 与原数独完全保持一致
     * @return
     */
    public static Sudoku copy(Sudoku oldSudoku,boolean copyAsTemplate){

        Sudoku newSudoku = new Sudoku();

        newSudoku.sudokuCell = new Cell(oldSudoku.sudokuCell);
        copyCell(newSudoku.sudokuCell ,oldSudoku.sudokuCell,copyAsTemplate);

        newSudoku.genCellRelation();

        return newSudoku;
    }

    public static void copyCell(Cell newCell, Cell oldCell, boolean copyAsTemplate) {

        if(copyAsTemplate && newCell.getValue() > 0 && newCell.getInitValue() == 0) {
            newCell.setInitValue(newCell.getValue());
        }

        if(oldCell.getNextCell() == null){
            return;
        }

        Cell nextCell = new Cell(oldCell.getNextCell());
        newCell.setNextCell(nextCell);
        copyCell(nextCell, oldCell.getNextCell(), copyAsTemplate);

    }


    public static void main(String args[]) throws Exception {

//        while(true) {
//            Random random = new Random();
//            PrintUtil.printLog(random.nextInt(Sudoku.MAX_INDEX_VALUE) + 1 + "");
//        }



        Set<Integer> a = new HashSet<>();
        a.add(1);
        a.add(3);
        a.add(4);
        a.add(5);

        Set<Integer> b = new HashSet<>();
        b.add(2);
        b.add(4);
        b.add(5);
        b.add(8);
        System.out.println(a);
//        a.addAll(b);
//        System.out.println(a);
        a.retainAll(b);
        System.out.println(a);

//        Sudoku sudoku = new Sudoku(createEmptyArray());
//
//        PrintUtil.printSudokuCell(sudoku);

//        for (int index = 0; index < 9; index++){
//            for(int indexInBlock=0; indexInBlock<9; indexInBlock ++) {
//                int rowIndex = index / 3 * 3 + indexInBlock / 3;
//                int colIndex = index % 3 * 3 + indexInBlock % 3;
//
//                System.out.println(index + "宫" + indexInBlock + "个单元格: [" + rowIndex + "," + colIndex + "]");
//
//            }
//        }


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
