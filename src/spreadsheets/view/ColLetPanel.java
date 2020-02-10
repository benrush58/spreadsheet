package spreadsheets.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;

import spreadsheets.model.Coord;

/**
 * Represents the horizontal panel that will be nested above the
 * grid, labelling the columns by their letter.
 */
public class ColLetPanel extends JPanel {
  private int maxCol;
  private double xPos;

  /**
   * Constructs a ColLetPanel with the initial position of the header at 0.
   * Positioning refers to how much the header has been shifted by scrollbars.
   * @param maxCol the width of the panel.
   */
  ColLetPanel(int maxCol) {
    this.maxCol = maxCol; // the last column in the worksheet
    this.xPos = 0; // the position of the header above the grid
    this.setPreferredSize(new Dimension(this.maxCol * 50, 30));
    this.setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // for each column in the grid, draw the rectangular labels
    for (int col = 0; col <= this.maxCol; col++) {
      String let = Coord.colIndexToName(col);

      // create graphics and translate depending on current xPos
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.translate(this.xPos, 0);

      // changes the background color of the cell and fills it
      g2d.setColor(Color.lightGray);
      g2d.fillRect(col * 50, 0, 50, 30);

      // draws the outline of a cell and its contents
      g2d.setColor(Color.BLACK);
      g2d.drawRect(col * 50, 0, 50, 30);
      g2d.drawString(let, col * 50 + 5, 15);

      // repaint and dispose of graphics object
      this.repaint();
      g2d.dispose();
    }

  }

  /**
   * Updates the position of the header.
   * @param value the value to shift the header.
   */
  void updateXPos(double value) {
    this.xPos = -1 * value;
  }

  /**
   * Adds 20 additional column labels to the header.
   */
  void addColumns() {
    this.maxCol += 20;
  }
}
