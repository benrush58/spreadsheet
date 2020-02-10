package spreadsheets.controller;

import java.io.FileNotFoundException;

import spreadsheets.model.Coord;
import spreadsheets.model.Worksheet;
import spreadsheets.view.WorksheetView;

/**
 * Represents the features of the Spreadsheet. All of the methods
 * should be acted on the controllers associated model, view, or both.
 * This class is designed to be used in conjunction with a Workbook and
 * and a WorksheetView.
 */
public interface Features {

  /**
   * Adds a new blank page to the Workbook.
   * @param name of the page to be added
   */
  void addBlankPage(String name);

  /**
   * Adds a new page to the Workbook, created by reading in the given file.
   * @param name of the page to be added
   * @param page to be added to the Workbook.
   * @throws FileNotFoundException if the given file cannot be found
   */
  void loadPage(String name, Worksheet page) throws FileNotFoundException;

  /**
   * Removes the page of the given name from the Workbook.
   * @param pageName name of page to be removed
   * @throws IllegalArgumentException if the page does not exist in the Workbook
   */
  void removePage(String pageName) throws IllegalArgumentException;

  /**
   * Renames a page in the Workbook.
   * @param oldName of the page
   * @param newName of the designated page
   * @throws IllegalArgumentException if oldName does not exist in the Workbook
   */
  void renamePage(String oldName, String newName) throws IllegalArgumentException;

  /**
   * Saves the desired page under the given file name.
   * @param pageName page to be saved
   * @param fileName name to save page under
   * @throws IllegalArgumentException if the page is not in the given Workbook
   * @throws FileNotFoundException if there is an issue creating the file
   */
  void savePage(String pageName, String fileName)
          throws IllegalArgumentException, FileNotFoundException;

  /**
   * Attaches a view to this controller and model, and adds this controller
   * as a Features object to the view.
   * @param view the view to be added
   */
  void setView(WorksheetView view);

  /**
   * Sets the cells contents in a given sheet.
   * @param pageName page to be modified
   * @param cell to be filled in
   * @param contents of the cell
   * @throws IllegalArgumentException if the page does not exist in the workbook
   */
  void setCell(String pageName, Coord cell, String contents) throws IllegalArgumentException;

  /**
   * Deletes the given cell at the given coordinate.
   * Note: If the cell does not currently exist at the given location, nothing will happen.
   * @param pageName the page to delete the cell from
   * @param cell to be deleted
   * @throws IllegalArgumentException if the page does not exist in the Workbook.
   */
  void deleteCell(String pageName, Coord cell) throws IllegalArgumentException;

  /**
   * Selects the given cell on the given page.
   * @param pageName name of the page to select the cell in
   * @param cell to be selected
   * @throws IllegalArgumentException if the page does not exist in the Workbook.
   */
  void selectCell(String pageName, Coord cell) throws IllegalArgumentException;

  /**
   * Sets the current page in the Workbook.
   * @param pageName name of the new current page
   * @throws IllegalArgumentException if the page does not exist
   */
  void setCurrentPage(String pageName) throws IllegalArgumentException;

}
