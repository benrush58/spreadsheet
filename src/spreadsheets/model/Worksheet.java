package spreadsheets.model;

import java.util.List;

import spreadsheets.formula.value.WorksheetValue;

/**
 * Represents a worksheet of cells that contain data.
 * Cells can be evaluated, set/created, and retrieved.
 */
public interface Worksheet {

  /**
   * Evaluates the Cell at the given Coordinates.
   * @param coord coordinates of the cell to be evaluated
   * @return the evaluated contents of the cell
   * @throws IllegalArgumentException if cell is blank
   */
  WorksheetValue evalCell(Coord coord) throws IllegalArgumentException;

  /**
   * Sets the cell at the given coordinates to contain the given contents.
   * Sets the contents regardless of the Cell that was there before, if any.
   * @param coord coordinates of the cell to be set
   * @param contents of the cell to be set
   */
  void setCellContents(Coord coord, String contents);

  /**
   * Removes the contents of the cell at the specified location.
   * @param coord location of cell to be removed
   */
  void deleteCell(Coord coord);

  /**
   * Returns a cell at the given coordinate.
   * If the cell has value, a cell will be created with
   * a placeholder string "" as the cell's value.
   * @param coord coordinates of the cell
   * @return the desired cell
   */
  Cell getCell(Coord coord);

  /**
   * Gives the user a list of non-empty cells in the model.
   * @return the list of cells
   */
  List<Cell> getCells();

  /**
   * Updates all of the cells that reference the given coordinate.
   * @param coord of the cell being referenced
   */
  void updateReference(Coord coord);

  /**
   * Sets the name of the Worksheet.
   * @param name to be given
   */
  void nameSheet(String name);

  /**
   * Gives the current name of the Worksheet.
   * @return the name
   */
  String getName();

  /**
   * Adds this Worksheet to the given book.
   * Passes the Workbook into the sheet.
   * @param book to be added to
   */
  void addToBook(Workbook book);

}
