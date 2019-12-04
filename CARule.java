package edu.neu.csye6200.ca;

import java.util.Set;
/*
 * This is an interface of simulation rule
 * which has the basic function of getting next layer from this layer
 * since the snow flake spread from the center to outer layer
 * one step by step, a little bit like Breadth First Search pattern
 */

public interface CARule

{	
	/* In simulation display panel, odd and even rows are slightly staggered,
	 * when getting the ordinate of six neighbors of a cell
	 * the offset vary between odd rows and even rows
	 * so directions4OddRow and directions4EvenRows are defined separately
	 *     
	 *     		   +   +
	 *     +  ->  +  +  +
	 *     		   +   +
	 *     
	 * */
    public static final int[][] directions4OddRow = { 
    		{ -1, -1 },
    		{ -1, 0 },
    		{ 0, -1 }, 
    		{ 0, 1 }, 
    		{ 1, -1 },
    		{ 1, 0 } 
    		};
    public static final int[][] directions4EvenRow = {
    		{ -1, 0 },
    		{ -1, 1 },
    		{ 0, -1 }, 
    		{ 0, 1 },
    		{ 1, 0 }, 
    		{ 1, 1 } 
    		};

   /* Get new outer layer from last layer
   * @param crystal  is the crystal object which is being simulated
   * @param set is the set of last outer layer
   */
    Set<CACell> automateNGetOuterLayerSet(CACrystal crystal, final Set<CACell> set);
    
    /*
     * after stop button is pressed or one simulation of max steps is finished
     * before next simulation,the step should be reset to 0
     * */
    void reset();
}
