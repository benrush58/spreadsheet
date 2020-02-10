package spreadsheets.view;

import java.io.IOException;

import spreadsheets.controller.Features;
import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.WorksheetModelView;

/**
 * A textual representation of a Worksheet.
 * Renders a Worksheet as a text file listing each non-empty
 * cell by its name (coordinates) and raw contents.
 */
public class TextualView implements WorksheetView {
  public Appendable ap;

  /**
   * Constructs a textual view of a Worksheet.
   * @param model the model to be drawn
   * @param ap the appendable to add onto
   */
  public TextualView(WorksheetModelView model, Appendable ap) {
    // Worksheet is the model interface, to avoid tight coupling
    this.ap = ap;

    // for each of the non-empty cells in the model, try to
    // append it to the given output
    for (Cell cell : model.getCells()) {
      try {
        ap.append(cell.getCoord().toString()) // the coordinate
                .append(" ")                  // a space
                .append(cell.getRawData())    // the cell's raw data
                .append("\n");                // a new line afterwards
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  @Override
  public void addFeatures(Features features) {
    // There are no features to be added at this time
  }

  @Override
  public void setPage() {
    // not used
  }

  @Override
  public void highlightCell(Coord coord) {
    // There are no cells to highlight at this time
  }

  @Override
  public void resetFocus() {
    // This method is not necessary in the TextualView
  }

}
