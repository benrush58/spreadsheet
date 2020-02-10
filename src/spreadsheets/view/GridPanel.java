package spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.WorksheetModelView;

/**
 * Represents a grid of cells in a Worksheet.
 * This panel draws only the grid of cells and the data
 * contained in them.
 */
public class GridPanel extends JPanel {
  private WorksheetModelView model;
  // x and y pos account for shifting due to scrolling
  private int xPos;
  private int yPos;
  private int maxCol;
  private int maxRow;
  private Coord selected;

  /**
   * Constructs the GridPanel based on the parameters passed in.
   * @param model the model to be drawn
   * @param maxCol the farthest column
   * @param maxRow the farthest row
   */
  GridPanel(WorksheetModelView model, int maxCol, int maxRow) {
    this.model = model;
    // the original shifting of the model
    this.xPos = 0;
    this.yPos = 0;
    // the original width and height of the grid
    this.maxCol = maxCol;
    this.maxRow = maxRow;
    // selected cell initialized to (1, 1)
    this.selected = new Coord(1, 1);

    // sets the panel to be visible
    setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // assigns the values of each cell in the model up until the farthest point
    for (int col = 1; col <= this.maxCol; col++) {
      for (int row = 1; row <= this.maxRow; row++) {
        String string = returnValue(col, row);

        // draws a cell for each
        Graphics2D g2d = (Graphics2D) g.create();

        // tells the graphics where to start drawing
        g2d.translate(this.xPos - 50, this.yPos - 30);

        // draws the cell itself in whatever specified background color
        if (col == selected.col && row == selected.row) {
          g2d.setColor(new Color(172, 215, 230));
        } else {
          g2d.setColor(Color.WHITE);
        }
        g2d.fillRect(col * 50, row * 30, 50, 30);

        // draws the outline of a cell and its contents
        g2d.setColor(Color.BLACK);
        g2d.drawRect(col * 50, row * 30, 50, 30);
        g2d.drawString(string, col * 50 + 10, row * 30 + 10);

        // repaint the component
        this.repaint();
      }
    }
  }

  // returns the value of the cell at the given x and y coordinates as a string
  // if the cell can be evaluated, it's data is returned as a string
  // if the cell has an error in evaluation, it's data is set to an error string
  // if the cell is not contained in the model, nothing is drawn over top of it
  private String returnValue(int x, int y) {
    ArrayList<Cell> cells = (ArrayList<Cell>) model.getCells();
    for (Cell cell : cells) {
      if (cell.getCoord().col == x && cell.getCoord().row == y) {
        try {
          return cell.getEvaluated().toString();
        } catch (IllegalArgumentException e) {
          return "#ERROR";
        }
      }
    }
    return "";
  }

  // updates the x position of the origin
  // when you hit the max value, increase it by 20
  void updateXPos(int e) {
    this.xPos = -1 * e;
  }

  // updates the y position of the origin
  // when the you hit the max y value, increase it by 20
  void updateYPos(int e) {
    this.yPos = -1 * e;
  }

  // add 20 more empty rows to the grid
  void addRows() {
    this.maxRow += 20;
  }

  // add 20 more empty columns to the grid
  void addColumns() {
    this.maxCol += 20;
  }

  // sets the cell that is currently selected
  void setSelected(Coord selected) {
    this.selected = selected;
  }

  /**
   * Gets the coordinates of the cell that is currently selected.
   * @return coords of selected cell
   */
  Coord getSelected() {
    return this.selected;
  }

  // takes in the raw data of the clicks and uses scroll positions
  // to find the coordinates of teh cell clicked
  Coord findClick(int x, int y) {
    double col = (x + (-1.0 * this.xPos)) / 50.0;
    double row = (y + (-1.0 * this.yPos)) / 30.0;
    return new Coord((int) col, (int) row);
  }

  /**
   * Sets the model being drawn.
   * @param mv the model being loaded in
   */
  void loadModel(WorksheetModelView mv) {
    this.model = mv;
  }
}