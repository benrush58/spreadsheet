import java.util.List;

import spreadsheets.formula.value.WorksheetValue;
import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.Worksheet;

/**
 * This class represents a mock Worksheet meant to test interactions
 * with the Controller. Return values should be ignored, all testing outputs
 * will be written to the log.
 */
public class MockModel implements Worksheet {
  private Worksheet model;
  StringBuilder log;

  /**
   * Constructs the mock model.
   * @param log the log to write interactions to
   */
  MockModel(Worksheet model, StringBuilder log) {
    this.model = model;
    this.log = log;
  }

  @Override
  public WorksheetValue evalCell(Coord coord) throws IllegalArgumentException {
    log.append("Method: evalCell, Input: ")
            .append(coord.toString());
    return model.evalCell(coord);
  }

  @Override
  public void setCellContents(Coord coord, String contents) {
    log.append("Method: setCellContents, Inputs: ")
            .append(coord.toString())
            .append(", ")
            .append(contents);
    model.setCellContents(coord, contents);
  }

  @Override
  public void deleteCell(Coord coord) {
    log.append("Method: deleteCell, Input: ")
            .append(coord.toString());
    model.deleteCell(coord);
  }

  @Override
  public Cell getCell(Coord coord) {
    log.append("Method: getCell, Input: ")
            .append(coord.toString());
    return model.getCell(coord);
  }

  @Override
  public List<Cell> getCells() {
    log.append("Method: getCells()");
    return model.getCells();
  }

  @Override
  public void updateReference(Coord coord) {
    log.append("Method: updateReference, Input: ")
            .append(coord.toString());
    model.updateReference(coord);
  }

  @Override
  public void nameSheet(String name) {
    log.append("Method: nameSheet, Input: ")
            .append(name);
    model.nameSheet(name);
  }

  @Override
  public String getName() {
    log.append("Method: getName");
    return model.getName();
  }

  @Override
  public void addToBook(Workbook book) {
    log.append("Method: addToBook Input: ")
            .append(book.toString());
    model.addToBook(book);
  }
}
