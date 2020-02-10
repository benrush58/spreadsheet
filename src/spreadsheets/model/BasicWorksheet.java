package spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.value.WorksheetValue;

/**
 * A basic implementation of the Worksheet interface. This class represents a spreadsheet that
 * has knowledge of a Cell's coordinates and their data.
 */
public class BasicWorksheet implements Worksheet {
  private final HashMap<Coord, Cell> data;
  private Workbook book;
  private String name;

  // constructor is private because it can be accessed by the nested builder
  private BasicWorksheet(HashMap<Coord, Cell> data, String name) {
    this.data = data;
    this.book = null;
    this.name = name;
  }

  @Override
  public WorksheetValue evalCell(Coord coord) throws IllegalArgumentException {
    if (!data.containsKey(coord)) {
      throw new IllegalArgumentException("This cell has no contents!");
    } else {
      return data.get(coord).getEvaluated();
    }
  }

  @Override
  public void setCellContents(Coord coord, String contents) {
    // if the contents make a cycle, throw an error
    if (contents.equals(Coord.colIndexToName(coord.col) + coord.row)) { // self-referential
      data.put(coord, new BasicCell(this.book, this, coord, "\"#SELF-REF\""));
    } else {
      data.put(coord, new BasicCell(this.book, this, coord, contents));
    }
  }

  @Override
  public void deleteCell(Coord coord) {
    this.data.remove(coord);
  }

  @Override
  public Cell getCell(Coord coord) {
    if (this.data.containsKey(coord)) {
      return data.get(coord);
    } else {
      return new BasicCell(this.book, this, coord, "");
    }
  }

  @Override
  public List<Cell> getCells() {
    ArrayList<Cell> cells = new ArrayList<Cell>();
    for (Coord coord : this.data.keySet()) {
      cells.add(this.data.get(coord));
    }
    return cells;
  }

  @Override
  public void updateReference(Coord coord) {
    // This method should figure out what cells are dependent on this coordinate
    // and re-evaluate its contents by calling cell.updateEvaluated();
    for (Cell c : this.getCells()) {
      c.updateEvaluated();
    }
  }

  @Override
  public void nameSheet(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addToBook(Workbook book) {
    this.book = book;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (! (o instanceof BasicWorksheet)) {
      return false;
    }
    BasicWorksheet bw = (BasicWorksheet) o;
    for (Cell c : bw.getCells()) {
      if (!this.getCells().contains(c)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Builds a BasicWorksheet. Because this class is nested inside of BasicWorksheet,
   * it can access its private constructor.
   */
  public static class BasicWorksheetBuilder implements
          WorksheetReader.WorksheetBuilder<Worksheet> {
    // private builder fields
    private HashMap<Coord, Cell> data = new HashMap<>();
    private Worksheet model = new BasicWorksheet(this.data, "untitled");

    @Override
    public WorksheetReader.WorksheetBuilder<Worksheet> createCell(
            int col, int row, String contents) {
      Coord place = new Coord(col, row);
      model.setCellContents(place, contents);
      return this;
    }

    @Override
    public Worksheet createWorksheet() {
      return this.model;
    }

  }

}
