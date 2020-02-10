package spreadsheets.model;

/**
 * This class mutes all setter functions in a Workbook so that it can be passed to a
 * WorksheetView without the view being able to manipulate setter functions.
 */
public class WorkbookModelView implements Workbook {
  private Workbook wb;

  /**
   * Constructs a WorkbookModelView.
   * @param wb the Workbook whose setter functions are to be muted
   */
  public WorkbookModelView(Workbook wb) {
    this.wb = wb;
  }

  @Override
  public void addPage(String name) throws IllegalArgumentException {
    throw new UnsupportedOperationException("addPage is not supported in the view!");
  }

  @Override
  public void addPage(String name, Worksheet page) throws IllegalArgumentException {
    throw new UnsupportedOperationException("addPage is not supported in the view!");
  }

  @Override
  public void removePage(String name) throws IllegalArgumentException {
    throw new UnsupportedOperationException("removePage is not supported in the view!");
  }

  @Override
  public void renamePage(String from, String to) throws IllegalArgumentException {
    throw new UnsupportedOperationException("renamePage is not supported in the view!");
  }

  @Override
  public String[] getPages() {
    return wb.getPages();
  }

  @Override
  public Worksheet getPage(String pageName) throws IllegalArgumentException {
    return new WorksheetModelView(wb.getPage(pageName));
  }

  @Override
  public Worksheet getCurrentPage() {
    return new WorksheetModelView(wb.getCurrentPage());
  }

  @Override
  public void updateCurrentPage(String pageName) throws IllegalArgumentException {
    throw new UnsupportedOperationException("updateCurrentPage is not supported in the view!");
  }

}
