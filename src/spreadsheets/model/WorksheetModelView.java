package spreadsheets.model;

import java.util.List;

import spreadsheets.formula.value.WorksheetValue;

/**
 * A middle-man class to ensure that the view cannot modify the model.
 */
public class WorksheetModelView implements Worksheet {
  private Worksheet bw;

  /**
   * Constructs a ModelView.
   * @param bw the BasicModel to be created
   */
  public WorksheetModelView(Worksheet bw) {
    this.bw = bw;
  }

  @Override
  public WorksheetValue evalCell(Coord coord) {
    return bw.evalCell(coord);
  }

  @Override
  public void setCellContents(Coord coord, String contents) {
    throw new UnsupportedOperationException("Cannot set a cell's contents in the view!");
  }

  @Override
  public void deleteCell(Coord coord) {
    throw new UnsupportedOperationException("Cannot remove a cell's contents in the view!");
  }

  @Override
  public Cell getCell(Coord coord) {
    return this.bw.getCell(coord);
  }

  @Override
  public List<Cell> getCells() {
    return this.bw.getCells();
  }

  @Override
  public void updateReference(Coord coord) {
    throw new UnsupportedOperationException("Cannot update a cell's references in the view!");
  }

  @Override
  public void nameSheet(String name) {
    throw new UnsupportedOperationException("Cannot modify the name from the view.");
  }

  @Override
  public String getName() {
    return this.bw.getName();
  }

  @Override
  public void addToBook(Workbook book) {
    throw new UnsupportedOperationException("Cannot make modifications from view!");
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (! (o instanceof WorksheetModelView)) {
      return false;
    }
    WorksheetModelView mv = (WorksheetModelView) o;
    return this.bw.equals(mv.bw);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
