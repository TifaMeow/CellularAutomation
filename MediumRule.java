package edu.neu.csye6200.ca;

import java.util.HashSet;
import java.util.Set;
/*
 * This medium rule implements a rule to create a snow flake like pattern
 * from center to outer layer into 6 directions
 * in one layer, this rule will check every's cell's neighbors in 6 directions
 * if there exist exactly one neighbor is frozen, this cell would be set to frozen.
 * */

public class MediumRule implements CARule
{
    private int step;
    
    public MediumRule() {
        this.step = 0;
    }
    
    public Set<CACell> automateNGetOuterLayerSet(final CACrystal crystal, final Set<CACell> set) {
        ++step;
        final CACell[][] cellArray = crystal.getCellArray();
      //create the nextOuterLayer that I want to return
        final Set<CACell> nextOuterLayer = new HashSet<CACell>();
        //cells that are determined to freeze during this layer
        final Set<CACell> cellsToFreeze = new HashSet<CACell>();
      //for each cell in last layer, check how many neighbors are already frozen
        for (final CACell cell : set) {
            int ctr = 0;
            final int[][] directions = (cell.x % 2 == 0) ? CARule.directions4EvenRow : CARule.directions4OddRow;
            for (int i = 0; i < directions.length; ++i) {
                final int[] direction = directions[i];
                final int neighbor_x = cell.x + direction[0];
                final int neighbor_y = cell.y + direction[1];
              //check the validity of the index I generated for neighbors
                if (crystal.IndexAreValid(neighbor_x, neighbor_y)) {
                    final CACell neighbor = crystal.getCACellByIndex(neighbor_x, neighbor_y);
                    if (!nextOuterLayer.contains(neighbor) && !set.contains(neighbor)) {
                        nextOuterLayer.add(neighbor);
                    }
                    if (neighbor.frozen) {
                        ++ctr;
                    }
                }
            }
            // if there exist exactly one neighbor is frozen, this cell would be set to frozen.
            //if the cell is already frozen, don't need to change to avoid step of the cell change
            if (ctr == 1 && !cell.frozen) {
                cellsToFreeze.add(cell);
            }
            ctr = 0;
        }
        for (CACell centerCell : cellsToFreeze) {
            centerCell.frozen = true;
            centerCell.step = this.step;
            cellArray[centerCell.x][centerCell.y].frozen = true;
            cellArray[centerCell.x][centerCell.y].step = this.step;
            //System.out.println("Medium Rule");
            //System.out.println("Step: " + centerCell.step);
        }
        crystal.setCellArray(cellArray);
        return nextOuterLayer;
    }
    /*
     * after stop button is pressed or one simulation of max steps is finished
     * before next simulation,the step should be reset to 0
     * */
    public void reset() {
        this.step = 0;
    }
}
