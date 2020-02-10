package spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;

import spreadsheets.formula.Formula;
import spreadsheets.formula.value.StringValue;
import spreadsheets.formula.value.WorksheetValue;
import spreadsheets.sexp.Parser;
import spreadsheets.sexp.Sexp;
import spreadsheets.sexp.SexpVisitorToForm;

/**
 * Represents a cell in a basic worksheet. A Cell contains its coordinates and its raw data, as well
 * as storing the model it belongs to and a formula for representing and evaluating raw data.
 */
public class BasicCell implements Cell {
  private Workbook book;
  private final Worksheet model;
  private final Coord coord;
  private final String rawData;
  private Formula formula;
  private WorksheetValue evaluated;

  private HashMap<String, ArrayList<Coord>> directRefs;

  /**
   * Constructs a basic Cell.
   *
   * @param model   the model the cell was created in
   * @param coord   where the cell is in the model
   * @param rawData the raw data input by the user
   */
  public BasicCell(Worksheet model, Coord coord, String rawData) {
    this.model = model;
    this.coord = coord;
    this.rawData = rawData;
    this.formula = null;
    this.evaluated = null;
    this.directRefs = null;
  }

  /**
   * Constructs a basic Cell in the context of a Workbook.
   *
   * @param book    the workbook the cell is contained in
   * @param model   the model the cell was created in
   * @param coord   where the cell is located in the worksheet
   * @param rawData the raw data input by the user
   */
  public BasicCell(Workbook book, Worksheet model, Coord coord, String rawData) {
    this(model, coord, rawData);
    this.book = book;
  }

  // Creates a Cell's formula from its raw data
  private Formula createFormula() {
    Sexp sexp;
    try {
      // Turn the raw data into an Sexp
      if (rawData.length() > 0 && rawData.charAt(0) == '=') {
        sexp = Parser.parse(rawData.substring(1));
      } else {
        sexp = Parser.parse(rawData);
      }
    } catch (IllegalArgumentException e) {
      return new StringValue("");
    }
    // Turn the Sexp to a Formula and return
    SexpVisitorToForm visitor = new SexpVisitorToForm(this.book, this.model, this.coord);
    return sexp.accept(visitor);
  }

  @Override
  public Coord getCoord() {
    return new Coord(this.coord.col, this.coord.row);
  }

  @Override
  public String getRawData() {
    return "" + this.rawData;
  }

  @Override
  public Formula getFormula() {
    if (this.formula == null) {
      this.formula = createFormula();
    }
    return this.formula;
  }

  @Override
  public WorksheetValue getEvaluated() {
    if (this.evaluated == null) {
      this.evaluated = this.getFormula().evaluate();
    }
    return this.evaluated;
  }

  @Override
  public void updateEvaluated() {
    this.evaluated = this.createFormula().evaluate();
  }

  @Override
  public void addDirect(HashMap<String, ArrayList<Coord>> direct) {
    this.directRefs = direct;
  }

  @Override
  public HashMap<String, ArrayList<Coord>> getDirect() {
    return this.directRefs;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BasicCell) {
      Cell given = (BasicCell) obj;
      return given.getCoord().col == (this.coord.col)
              && given.getCoord().row == (this.coord.row)
              && given.getRawData().equals(this.rawData);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return this.coord.toString() + this.getEvaluated().toString();
  }
}