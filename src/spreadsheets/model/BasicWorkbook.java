package spreadsheets.model;

import java.util.HashMap;

/**
 * Represents a Workbook, or a collection of WorksheetModels.
 */
public class BasicWorkbook implements Workbook {
  private HashMap<String, Worksheet> pages;
  private Worksheet currentPage;

  /**
   * Constructs a new Workbook with an empty Worksheet.
   */
  public BasicWorkbook() {
    this.pages = new HashMap<String, Worksheet>();
    Worksheet blank = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    blank.nameSheet("untitled");
    blank.addToBook(this);
    this.pages.put("untitled", blank);
    this.currentPage = blank;
  }

  /**
   * Constructs a new Workbook with the given Worksheet.
   * @param page to be initialized in the Workbook.
   */
  public BasicWorkbook(Worksheet page) {
    this.pages = new HashMap<String, Worksheet>();
    page.nameSheet("untitled");
    page.addToBook(this);
    this.pages.put("untitled", page);
    this.currentPage = page;
  }

  @Override
  public void addPage(String name) throws IllegalArgumentException {
    if (this.pages.containsKey(name)) {
      throw new IllegalArgumentException("A page with this name already exists.");
    } else {
      Worksheet blank = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
      // gives the worksheet an identifiable name
      blank.nameSheet(name);
      blank.addToBook(this);
      this.pages.put(name, blank);
    }
  }

  @Override
  public void addPage(String name, Worksheet page) throws IllegalArgumentException {
    page.nameSheet(name);
    page.addToBook(this);
    this.pages.put(name, page);
  }

  @Override
  public void removePage(String name) throws IllegalArgumentException {
    if (this.pages.containsKey(name)) {
      this.pages.remove(name);
    } else {
      throw new IllegalArgumentException("This page does not exist in the Workbook.");
    }
  }

  @Override
  public void renamePage(String from, String to) throws IllegalArgumentException {
    if (this.pages.containsKey(to)) {
      throw new IllegalArgumentException("A page with this name already exists.");
    } else if (!this.pages.containsKey(from)) {
      throw new IllegalArgumentException("The first page does not exist in the Workbook.");
    } else {
      Worksheet toAdd = this.pages.get(from);
      toAdd.nameSheet(to);
      toAdd.addToBook(this);
      this.pages.remove(from);
      this.pages.put(to, toAdd);
    }
  }

  @Override
  public String[] getPages() {
    return this.pages.keySet().toArray(new String[0]);
  }

  @Override
  public Worksheet getPage(String pageName) throws IllegalArgumentException {
    if (this.pages.containsKey(pageName)) {
      return this.pages.get(pageName);
    } else {
      throw new IllegalArgumentException("Page not found in Workbook.");
    }
  }

  @Override
  public Worksheet getCurrentPage() {
    return this.currentPage;
  }

  @Override
  public void updateCurrentPage(String pageName) throws IllegalArgumentException {
    if (this.pages.containsKey(pageName)) {
      this.currentPage = this.pages.get(pageName);
    } else {
      throw new IllegalArgumentException("New current page not in the Workbook.");
    }
  }

}
