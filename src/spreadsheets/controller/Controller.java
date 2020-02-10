package spreadsheets.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.Worksheet;
import spreadsheets.model.WorksheetModelView;
import spreadsheets.view.TextualView;
import spreadsheets.view.WorksheetView;

/**
 * Represents a Controller to communicate between a Worksheet and WorksheetView.
 * The Controller cannot be constructed without a model, but it starts with a null view.
 * The controller is connected to a view when the setView method is called.
 */
public class Controller implements Features {
  private Workbook model;
  private WorksheetView view;

  /**
   * Constructs a controller.
   * The view field is not initialized in the constructor.
   * @param book to be instantiated
   */
  public Controller(Workbook book) {
    if (book == null) {
      throw new IllegalArgumentException("Cannot have a null model");
    } else {
      this.model = book;
      this.view = null;
    }
  }

  @Override
  public void addBlankPage(String name) {
    this.model.addPage(name);
    this.view.setPage();
  }

  @Override
  public void loadPage(String name, Worksheet page) {
    this.model.addPage(name, page);
    this.model.updateCurrentPage(name);
    this.view.setPage();
    this.update();
  }

  @Override
  public void removePage(String pageName) throws IllegalArgumentException {
    this.model.removePage(pageName);
  }

  @Override
  public void renamePage(String oldName, String newName) throws IllegalArgumentException {
    this.model.renamePage(oldName, newName);
  }

  @Override
  public void savePage(String pageName, String fileName) throws FileNotFoundException {
    PrintWriter toSave = new PrintWriter(new File(fileName));
    TextualView saved = new TextualView(new WorksheetModelView(
            this.model.getPage(pageName)), toSave);
    toSave.close();
  }

  @Override
  public void setView(WorksheetView view) {
    this.view = view;
    view.addFeatures(this);
  }

  @Override
  public void setCell(String pageName, Coord cell, String contents)
          throws IllegalArgumentException {
    this.model.getPage(pageName).setCellContents(cell, contents);
    this.update();
  }

  @Override
  public void deleteCell(String pageName, Coord cell) throws IllegalArgumentException {
    this.model.getPage(pageName).deleteCell(cell);
  }

  @Override
  public void selectCell(String pageName, Coord cell) throws IllegalArgumentException {
    this.view.highlightCell(cell);
  }

  @Override
  public void setCurrentPage(String pageName) throws IllegalArgumentException {
    this.model.updateCurrentPage(pageName);
    this.view.setPage();
  }

  private void update() {
    for (String page : this.model.getPages()) {
      for (Cell c : this.model.getPage(page).getCells()) {
        c.updateEvaluated();
      }
    }
  }

}
