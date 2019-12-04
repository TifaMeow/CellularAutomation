package edu.neu.csye6200.ca;

public class CACell
{	//every cell has x and y ordinates to correspond the index 
	//of cell array of one crystal
    public int x;
    public int y;
    //whether the position is frozen or not
    public boolean frozen;
    public int step;
    
    public CACell() {
        frozen = false;
        step = 0;
    }
    
    public CACell(final int x, final int y) {
        frozen = false;
        this.x = x;
        this.y = y;
    }
}