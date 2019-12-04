package edu.neu.csye6200.ca;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;

public class CAPanel extends JPanel
{
    int boxSize;
    private double padding;
    private CACell[][] cellArray;
    
    public CAPanel() {
        boxSize = 8;
        padding = 0.5;
    }
    
    public void setCrystal(final CACrystal crystal) {
        this.cellArray = crystal.getCellArray();
    }
    
    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2d = (Graphics2D)g;
        final Dimension size = this.getSize();
        g2d.setColor(Color.BLACK);
        //System.out.println("Dimension's width: " + size.width);
        //System.out.println("Dimension's height: " + size.height);
        g2d.fillRect(0, 0, size.width, size.height);
        if (this.cellArray == null) {
  //      	System.out.println("The cell Array is null.");
            return;
        }
        int rowNum = 60;
        int colNum = 120;
        Color color = Color.white;
        //display the cell array a crystal holds
        for (int row = 0; row < rowNum; ++row) {
            final boolean evenRow = row % 2 == 0;
            for (int col = 0; col < colNum; ++col) {
                int step = cellArray[row][col].step;
                //color changes with step, color changes from blue to greenish blue
                color = new Color(step * 4, 120 + step * 5, 255 - step * 5);
                g2d.setColor(color);
                //used staggered rows to simulate 6 directions of hexagons
                int shift = evenRow ? 0 : -10;
                if (this.cellArray[row][col].frozen) {
                	//fill a small rectangle with the corresponding color 
                    g2d.fillRect((int)(col * 18 + shift - 0.5 * size.width + 150), (int)(row * 18 - 0.5 * size.height + 80), this.boxSize, this.boxSize);
                }
            }
        }
    }
}