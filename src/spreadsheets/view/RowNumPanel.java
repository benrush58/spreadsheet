package spreadsheets.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;

/**
 * Represents the vertical panel that will be nested to the left of
 * the grid, labelling the rows by their number.
 */
public class RowNumPanel extends JPanel {
  private int maxRow;
  private double yPos;

  /**
   * Constructs a row header with its initial positioning at 0.
   * Positioning refers to how much the header has been shifted by scrollbars.
   * @param maxRow the height of the header
   */
  RowNumPanel(int maxRow) {
    this.yPos = 0; // position next to the grid
    this.maxRow = maxRow; // height of the header
    this.setPreferredSize(new Dimension(50, maxRow * 30));
    this.setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // for each row in the grid, draw the rectangular label for it
    for (int row = 0; row <= this.maxRow - 1; row++) {
      String num = String.valueOf(row + 1);

      // create the graphics and then shift them the necessary distance
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.translate(0, this.yPos);

      // changes the color of the cell and fills it
      g2d.setColor(Color.lightGray);
      g2d.fillRect(0, row * 30, 50, 30);

      // draws the outline of a cell and the String on top of it
      g2d.setColor(Color.BLACK);
      g2d.drawRect(0, row * 30, 50, 30);
      g2d.drawString(num, 5, (row * 30) + 15);

      // repaint and dispose of graphics object
      this.repaint();
      g2d.dispose();
    }

  }

  /**
   * Updates the position of the header.
   * @param value the value to shift the header.
   */
  void updateYPos(double value) {
    this.yPos = -1 * value;
  }

  /**
   * Adds 20 additional rows to the header.
   */
  void addRows() {
    this.maxRow += 20;
  }
}
