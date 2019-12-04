package edu.neu.csye6200.ca;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
/*
* This complex rule implements a rule to create random snow flake like pattern
* from center to outer layer into 6 directions
* in one layer, this rule will check every's cell's neighbors in 6 directions
* threshold is a randomly generated value from 0 to 2 during every layer.
* if the number of frozen neighbors is threshold, this cell would be set to frozen.
* I can try multiple times with complex rule and enjoy the power of randomness.
* */
public class ComplexRule implements CARule
{
    private int step;
    
    public ComplexRule() {
        step = 0;
    }
    
    @Override
    public Set<CACell> automateNGetOuterLayerSet(final CACrystal crystal, final Set<CACell> set) {
        ++step;
        final Random seed = new Random();
        final int threshold = seed.nextInt(3);
        //cells that are determined to freeze during this layer
        final Set<CACell> cellsToFree = new HashSet<CACell>();
        final CACell[][] cellArray = crystal.getCellArray();
        //create the nextOuterLayer that I want to return
        final Set<CACell> nextOuterLayer = new HashSet<CACell>();
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
           // if the number of frozen neighbors is threshold, this cell would be set to frozen.
            //if the cell is already frozen, don't need to change to avoid step of the cell change
            if (ctr == threshold  && !cell.frozen) {
                cellsToFree.add(cell);
            }
            ctr = 0;
        }
        for (CACell centerCell : cellsToFree) {
            centerCell.frozen = true;
            centerCell.step = this.step;
            cellArray[centerCell.x][centerCell.y].frozen = true;
            cellArray[centerCell.x][centerCell.y].step = step;
            //System.out.println("Complex Rule");
            //System.out.println("Step: " + centerCell.step);
        }
        crystal.setCellArray(cellArray);
        return nextOuterLayer;
    }
    
    /*
     * after stop button is pressed or one simulation of max steps is finished
     * before next simulation,the step should be reset to 0
     * */
    @Override
    public void reset() {
        this.step = 0;
    }
}