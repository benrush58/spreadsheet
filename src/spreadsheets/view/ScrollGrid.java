package spreadsheets.view;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import java.awt.Adjustable;
import java.awt.Dimension;

import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.WorksheetModelView;

/**
 * Represents a class that arranges the GridPanel, cell headers (ColLetPanel and RowNumPanel), and
 * two scroll bars in order to depict the contents of a Worksheet.
 */
class ScrollGrid extends JPanel {
  private JScrollBar horizontal;
  private JScrollBar vertical;
  private GridPanel grid;
  private WorksheetModelView model;
  private ColLetPanel colLetHeader;
  private RowNumPanel rowNumHeader;
  private int maxCol;
  private int maxRow;

  /**
   * Constructs a ScrollGrid to lay out the necessary components. Note: this class is receiving the
   * ModelView from the View, with all setter methods muted.
   *
   * @param model the Worksheet to be drawn
   */
  ScrollGrid(WorksheetModelView model) {
    this.model = model;

    // Columns and rows are initialized to 20, so that all worksheets
    // are made with at least 20 visible cells.
    this.maxCol = 20;
    this.maxRow = 20;

    // if there are more than 20 columns and rows initially,
    // they will be calculated and assigned
    this.setMaxRowCol();

    // sets the preferred size of the window
    this.setPreferredSize(new Dimension(400, 400));

    // Creates all the components to be contained in the grid
    this.grid = new GridPanel(model, this.maxCol, this.maxRow);
    this.colLetHeader = new ColLetPanel(this.maxCol);
    this.rowNumHeader = new RowNumPanel(this.maxRow);
    this.horizontal = new JScrollBar(Adjustable.HORIZONTAL, 10, 25, 0, (maxCol - 7) * 50);
    this.vertical = new JScrollBar(Adjustable.VERTICAL, 10, 25, 0, (maxRow - 10) * 30);

    // Adds a listener to the vertical scroll bar.
    // Every time the scroll bar is adjusted, the X value of
    // both the grid and the column header are adjusted.
    this.horizontal.addAdjustmentListener(e -> {
      grid.updateXPos(e.getValue());
      colLetHeader.updateXPos(e.getValue());
    });

    // Adds a listener to the vertical scroll bar.
    // Every time the scroll bar is adjusted, the Y value of
    // both the grid and the row header are adjusted.
    this.vertical.addAdjustmentListener(e -> {
      grid.updateYPos(e.getValue());
      rowNumHeader.updateYPos(e.getValue());
    });

    // sets the layout of the panel and eliminates gaps between
    // the headers, grid, and scrollbars
    BorderLayout lay = new BorderLayout();
    lay.setHgap(0);
    lay.setVgap(0);
    this.setLayout(lay);

    // adds each of the components to the panel
    this.add(colLetHeader, BorderLayout.NORTH);
    this.add(rowNumHeader, BorderLayout.WEST);
    this.add(grid, BorderLayout.CENTER);
    this.add(horizontal, BorderLayout.SOUTH);
    this.add(vertical, BorderLayout.EAST);

    // set the pane to be visible
    this.setVisible(true);
  }

  // determines the max row and column represented in the current model
  // cells will only be drawn until the max row and col numbers
  private void setMaxRowCol() {
    for (Cell cell : this.model.getCells()) {
      if (cell.getCoord().col > this.maxCol) {
        this.maxCol = cell.getCoord().col;
      }
      if (cell.getCoord().row > this.maxRow) {
        this.maxRow = cell.getCoord().row;
      }
    }
  }

  // adds more columns to the grid and header
  // also increases the length of the scrollbar to ensure fit
  void addColumns() {
    this.grid.addColumns();
    this.colLetHeader.addColumns();
    this.horizontal.setMaximum(this.horizontal.getMaximum() + 50 * 20);
  }

  // adds more rows to the grid and header
  // also increases the length of the scrollbar to ensure fit
  void addRows() {
    this.grid.addRows();
    this.rowNumHeader.addRows();
    this.vertical.setMaximum(this.vertical.getMaximum() + 30 * 20);
  }

  /**
   * Sets the cell that is currently "selected" in the grid. Selected cells are highlighted and can
   * be edited by the user.
   *
   * @param coord of the cell to be selected
   */
  void setSelected(Coord coord) {
    this.grid.setSelected(coord);
  }

  /**
   * Gets the cell that is currently "selected" in the grid.
   *
   * @return the currently selected cell
   */
  Coord getSelected() {
    return this.grid.getSelected();
  }

  /**
   * Gets the number of the last row in the spreadsheet.
   *
   * @return last row
   */
  int getMaxRow() {
    return this.maxRow;
  }

  /**
   * Gets the number of the last column in the spreadsheet.
   *
   * @return last column
   */
  int getMaxCol() {
    return this.maxCol;
  }

  /**
   * Determines which cell was clicked based on the coordinates that the user clicked.
   *
   * @param x the x value of the click
   * @param y the y value of the click
   * @return the Coord of the cell that was clicked
   */
  Coord findClick(int x, int y) {
    return this.grid.findClick(x, y);
  }

  /**
   * Sets this model and the model that the grid is drawing.
   *
   * @param mv the model to be set
   */
  void loadModel(WorksheetModelView mv) {
    this.model = mv;
    this.grid.loadModel(mv);
  }
}
