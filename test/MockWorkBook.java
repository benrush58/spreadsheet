
import spreadsheets.model.Workbook;
import spreadsheets.model.Worksheet;

/**
 * Represents a MockWorkbook class to demonstrate the communication between the
 * Controller and the Model.
 */
public class MockWorkBook implements Workbook {
  private Workbook book;
  StringBuilder log;

  /**
   * Contructs a Mock object.
   * @param book the book to be tests
   * @param log to append information to
   */
  MockWorkBook(Workbook book, StringBuilder log) {
    this.book = book;
    this.log = log;
  }

  @Override
  public void addPage(String name) throws IllegalArgumentException {
    log.append("Method: addPage, Input: ")
            .append(name);
    book.addPage(name);
  }

  @Override
  public void addPage(String name, Worksheet page) throws IllegalArgumentException {
    log.append("Method: addPage, Input: ")
            .append(name)
            .append(", ")
            .append(page.getName());
    book.addPage(name, page);
  }

  @Override
  public void removePage(String name) throws IllegalArgumentException {
    log.append("Method: removePage, Input: ")
            .append(name);
    book.removePage(name);
  }

  @Override
  public void renamePage(String from, String to) throws IllegalArgumentException {
    log.append("Method: renamePage, Input: ")
            .append(from)
            .append(", ")
            .append(to);
    book.renamePage(from, to);
  }

  @Override
  public String[] getPages() {
    log.append("Method: getPages, Input: ");
    return book.getPages();
  }

  @Override
  public Worksheet getPage(String pageName) throws IllegalArgumentException {
    log.append("Method: getPage, Input: ")
            .append(pageName);
    return book.getPage(pageName);
  }

  @Override
  public Worksheet getCurrentPage() {
    log.append("Method: getCurrentPage, Input: ");
    return book.getCurrentPage();
  }

  @Override
  public void updateCurrentPage(String pageName) throws IllegalArgumentException {
    log.append("Method: updateCurrentPage, Input: ")
            .append(pageName);
    book.updateCurrentPage(pageName);
  }

}
