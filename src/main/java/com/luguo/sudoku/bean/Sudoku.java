package com.luguo.sudoku.bean;

import com.luguo.sudoku.rule.BlockRule;
import com.luguo.sudoku.rule.ColRule;
import com.luguo.sudoku.rule.RowRule;
import com.sun.istack.internal.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Sudoku {

    public static Integer[] allValue = {1,2,3,4,5,6,7,8,9};
    public static Integer MAX_INDEX_VALUE = 9;
    public static Integer MAX_CELL_VALUE = 81;
    public static Cell sudokuCell;

    public Sudoku(Integer[][] arr) throws Exception {
        List<Integer> list = new ArrayList<>();
        for(Integer[] array : arr){
            list.addAll(Arrays.asList(array));
        }

        init(list);
    }

    public Sudoku(List<Integer> list) throws Exception {
        init(list);
    }

    /**
     * 初始化数独， 同时初始化数独内单元格的关联关系
     * @param list
     * @throws Exception
     */
    private void init(List<Integer> list) throws Exception {
        if(CollectionUtils.isEmpty(list) || list.size() != MAX_CELL_VALUE){
            throw new Exception("初始化数据不符合要求");
        }

        Iterator<Integer> iterator = list.iterator();

        Cell curCell = null;
        for(int rowIndex=0 ; rowIndex<MAX_INDEX_VALUE ; rowIndex++){
            for(int colIndex=0 ; colIndex<MAX_INDEX_VALUE ; colIndex++){
                int blockIndex = rowIndex/3 * 3 + colIndex/3;
                Cell newCell = new Cell(rowIndex,colIndex,blockIndex,allValue);
                newCell.setInitValue(iterator.next());

                if(null == curCell){
                    curCell = newCell;
                    sudokuCell = curCell;
                }else{
                    curCell.setNextCell(newCell);
                    curCell = newCell;
                }
            }
        }

        curCell = sudokuCell;
        Cell upCell = null;
        Cell colCell = curCell;
        while(null != curCell.getNextCell()){
            Cell nextCell = curCell.getNextCell();

            if(null != upCell){
                upCell = upCell.getNextCell();
            }

            //新的一行
            if(nextCell.getColIndex() == 0){
                upCell = colCell;
                nextCell.setUpCell(upCell);
                colCell = nextCell;
            }else{
                nextCell.setLeftCell(curCell);

                if(null != upCell){
                    nextCell.setUpCell(upCell);
                }
            }
            curCell = nextCell;
        }
    }

    /**
     * 检查数独是否填写完成
     * @return
     */
    public boolean isCompeled(){
        Cell curCell = sudokuCell;
        while(curCell != null){
            if(curCell.getValue()==0){
                return false;
            }
            curCell = curCell.getNextCell();
        }

        return true;
    }


    /**
     * 检查是否存在重复
     * @return
     */
    public boolean checkDuplication(){
        RowRule rowRule = new RowRule() ;
        if(!rowRule.checkDuplication(this)){
            return true;
        }

        ColRule colRule = new ColRule();
        if(!colRule.checkDuplication(this)){
            return true;
        }

        BlockRule blockRule = new BlockRule();
        if(!blockRule.checkDuplication(this)) {
            return true;
        }

        return false;
    }

    /**
     * 获取第index 行的首个Cell
     * @param index
     * @return
     */
    public static Cell getRowHeadCell(Integer index){
        return getColCell(sudokuCell,index);
    }

    /**
     * 获取当前行内的下一Cell
     * @param preCell
     * @return
     */
    public static Cell getNextRowCell(Cell preCell){
        return preCell.getRightCell();
    }

    /**
     * 获取当前行的指定(第index个)Cell
     * @param headCell
     * @param index
     * @return
     */
    public static Cell getRowCell(Cell headCell, @NotNull Integer index){
        Cell curCell = headCell;
        int i = 0;;
        while(index > i && curCell != null){
            curCell = curCell.getRightCell();
            i++;
        }
        return curCell;
    }

    /**
     * 获取第index 列的首个Cell
     * @param index
     * @return
     */
    public static Cell getColHeadCell(@NotNull Integer index){
        return getRowCell(sudokuCell,index);
    }

    /**
     * 获取当前列内的下一Cell
     * @param preCell
     * @return
     */
    public static Cell getNextColCell(Cell preCell){
        return preCell.getDownCell();
    }

    /**
     * 获取当前行的指定(第index个)Cell
     * @param headCell
     * @param index
     * @return
     */
    public static Cell getColCell(Cell headCell, @NotNull Integer index){
        Cell curCell = headCell;
        int i = 0;;
        while(index > i && curCell != null){
            curCell = curCell.getDownCell();
            i++;
        }
        return curCell;
    }

    /**
     * 获取第index 宫的首个Cell
     * @param index
     * @return
     */
    public static Cell getBlockHeadCell( @NotNull Integer index){
        return getBlockCell(index, 0);
    }

    /**
     * 获取当前宫内的下一Cell
     * @param index
     * @param indexInBlock
     * @return
     */
    public static Cell getBlockCell(@NotNull Integer index, @NotNull Integer indexInBlock){

        int rowIndex = index / 3 * 3 + indexInBlock / 3;
        int colIndex = index % 3 * 3 + indexInBlock % 3;
        return getCell(rowIndex, colIndex);
    }

    /**
     * 获取当前宫内的下一Cell
     * @param preCell
     * @return
     */
    public static Cell getNextBlockCell(Cell preCell){
        int rowIndex = preCell.getRowIndex();
        int colIndex = preCell.getColIndex();
        int blockIndex = preCell.getBlockIndex();
        int indexInBlock = (rowIndex%3)*3 + colIndex%3 ;
        if(indexInBlock >= 8){
            return null;
        }

        int nextIndexInBlock = indexInBlock + 1;
        return getBlockCell(blockIndex,nextIndexInBlock);
    }

    /**
     * 获取指定的Cell
     * @param rowIndex
     * @param colIndex
     * @return
     */
    private static Cell getCell(Integer rowIndex, Integer colIndex) {
        return getRowCell(getRowHeadCell(rowIndex),colIndex);
    }


}
