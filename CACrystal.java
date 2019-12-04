package edu.neu.csye6200.ca;

import java.util.HashSet;
import java.util.Set;

public class CACrystal
{
	//every crystal has a cell array 
    private CACell[][] cellArray;
    private Set<CACell> initializedOuterLayer;
    public static final int ROW_NUM = 60;
    public static final int COL_NUM = 120;
    
    public CACrystal() {
        cellArray = null;
        initializedOuterLayer = null;
        initializedOuterLayer = new HashSet<CACell>();
        cellArray = new CACell[60][120];
        for (int i = 0; i < 60; ++i) {
            for (int j = 0; j < 120; ++j) {
                cellArray[i][j] = new CACell(i, j);
                cellArray[i][j].frozen = false;
            }
        }
    }
    
    public void setCellArray(final CACell[][] cellArray) {
        this.cellArray = cellArray;
    }
    /* initialized the crystal with the seed in the center
     * */
    public void initializeCrystal() {
        cellArray[30][60].frozen = true;
        initializedOuterLayer.add(cellArray[30][60]);
    }
    
    public Set<CACell> getInitializedOuterLayer() {
        return this.initializedOuterLayer;
    }
    
    public CACell getCACellByIndex(final int x, final int y) {
        return this.cellArray[x][y];
    }
    /*
     * check the validity of one cell's index
     * */
    public boolean IndexAreValid(final int x, final int y) {
        final int rowNum = this.cellArray.length;
        final int colNum = this.cellArray[0].length;
        return x >= 0 && x < rowNum && y >= 0 && y < colNum;
    }
    
    public CACell[][] getCellArray() {
        return cellArray;
    }
}