package spreadsheets.model;

/**
 * Represents a Workbook, which contains at least one Worksheet at a time.
 */
public interface Workbook {

  /**
   * Adds a blank Worksheet to the Workbook.
   * @param name of the new sheet
   * @throws IllegalArgumentException if there is already a page with the given name
   */
  void addPage(String name) throws IllegalArgumentException;

  /**
   * Adds a Worksheet with the given name and an existing model.
   * @param name of the page
   * @param page to be added
   * @throws IllegalArgumentException if there is already a page with the given name
   */
  void addPage(String name, Worksheet page) throws IllegalArgumentException;

  /**
   * Removes the page of the given name from the Workbook.
   * @param name of the page to be removed
   * @throws IllegalArgumentException if page not found in Workbook
   */
  void removePage(String name) throws IllegalArgumentException;

  /**
   * Changes the name of the first page to the second name.
   * @param from the name of the page you want to change
   * @param to the new name
   * @throws IllegalArgumentException if page not found in Workbook
   */
  void renamePage(String from, String to) throws IllegalArgumentException;

  /**
   * Gets the names of all the pages in the workbook.
   * @return a string array of all page names
   */
  String[] getPages();

  /**
   * Gets the page with the given name.
   * @param pageName page to be retrieved
   * @return the desired page
   * @throws IllegalArgumentException if page not found in Workbook
   */
  Worksheet getPage(String pageName) throws IllegalArgumentException;

  /**
   * Gets the currently opened page in the Workbook.
   * @return the current page
   */
  Worksheet getCurrentPage();

  /**
   * Updates the current page in the Workbook.
   * @param pageName of new current page
   * @throws IllegalArgumentException if new current page not found in Workbook.
   */
  void updateCurrentPage(String pageName) throws IllegalArgumentException;

}
