import spreadsheets.controller.Features;
import spreadsheets.model.Coord;
import spreadsheets.view.WorksheetView;

/**
 * This class is intended to Mock a WorksheetView, and to check
 * that the Controller is calling the correct methods on the model. Return values
 * should be ignored, all testing outputs will be written to the log.
 */
public class MockView implements WorksheetView {
  private WorksheetView view;
  StringBuilder log;

  /**
   * Constructs a mock view with a real view and a log.
   * @param view to be called
   * @param log to be recording
   */
  MockView(WorksheetView view, StringBuilder log) {
    this.view = view;
    this.log = log;
  }

  @Override
  public void addFeatures(Features features) {
    log.append("Features added.");
    view.addFeatures(features);
  }

  @Override
  public void setPage() {
    log.append("New model set.");
    view.setPage();
  }

  @Override
  public void highlightCell(Coord coord) {
    log.append("Cell highlighted: ")
            .append(coord.toString());
    view.highlightCell(coord);
  }

  @Override
  public void resetFocus() {
    log.append(" Focus reset.");
  }
}
