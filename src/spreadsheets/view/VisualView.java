package spreadsheets.view;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.BorderLayout;

import spreadsheets.controller.Features;
import spreadsheets.model.Coord;
import spreadsheets.model.WorksheetModelView;

/**
 * A visual GUI representation of a Worksheet.
 */
public class VisualView extends JFrame implements WorksheetView {
  private ScrollGrid grid;

  /**
   * Constructs a visual view of a Worksheet.
   * @param model the model to be drawn
   */
  public VisualView(WorksheetModelView model) {
    super("Microsoft Excel ^(TM)");

    // sets the frames basic size, positioning, and layout
    setSize(getPreferredSize());
    setLocation(250, 250);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // set the layout to be a grid bag layout
    this.setLayout(new BorderLayout());

    // nest the grid panel within the scroll panel
    this.grid = new ScrollGrid(model);
    this.getContentPane().add(this.grid, BorderLayout.CENTER);

    // need to do at end so view is displayed!
    this.setLocationRelativeTo(null);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500, 500);
  }

  @Override
  public void addFeatures(Features features) {
    // The VisualView does not currently have any features to add
  }

  @Override
  public void setPage() {
    // not used
  }

  @Override
  public void highlightCell(Coord coord) {
    this.grid.setSelected(coord);
  }

  @Override
  public void resetFocus() {
    // This method is not needed for the VisualView.
  }

}
