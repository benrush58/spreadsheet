package spreadsheets.view;

import spreadsheets.controller.Features;
import spreadsheets.model.Coord;

/**
 * Represents a view of a Worksheet. The view must display
 * the contents of a Worksheet in some way.
 */
public interface WorksheetView {

  /**
   * Adds listeners to a view and calls methods on the given Features object,
   * so that events can be communicated between the view and controller.
   */
  void addFeatures(Features features);

  /**
   * Sets the model that the view is associated with.
   * Note: if displaying a Workbook with multiple pages, this method
   * will set the current page being displayed.
   */
  void setPage();

  /**
   * Highlights and selects the cell at the given coordinate.
   * @param coord of the cell to be highlighted
   */
  void highlightCell(Coord coord);

  /**
   * Resets the focus of the view. This method ensures
   * that the view can always be seen/interacted with properly.
   */
  void resetFocus();

}
