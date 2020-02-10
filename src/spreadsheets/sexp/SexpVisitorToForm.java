package spreadsheets.sexp;

import java.util.ArrayList;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.operation.GreaterThanOp;
import spreadsheets.formula.operation.LessThanOp;
import spreadsheets.formula.operation.OperationType;
import spreadsheets.formula.operation.ProdOp;
import spreadsheets.formula.operation.SumOp;
import spreadsheets.formula.reference.BasicReference;
import spreadsheets.formula.value.BooleanValue;
import spreadsheets.formula.value.NumberValue;
import spreadsheets.formula.value.StringValue;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.Worksheet;

/**
 * Represents a visitor that acts on an Sexp and turns it into a Worksheet.
 */
public class SexpVisitorToForm implements SexpVisitor<Formula> {
  private Workbook book;
  private Worksheet model;
  private Coord cell;

  /**
   * Turns an S-Expression into a Formula that is compatible with Worksheet
   * and associated classes.
   * @param book created in
   * @param model created in
   * @param cell contained in
   */
  public SexpVisitorToForm(Workbook book, Worksheet model, Coord cell) {
    this.book = book;
    this.model = model;
    this.cell = cell;
  }

  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  @Override
  public Formula visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  @Override
  public Formula visitNumber(double d) {
    return new NumberValue(d);
  }

  /**
   * Process a symbol.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Formula visitSymbol(String s) {
    return new BasicReference(s, book, model, cell);
  }

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  @Override
  public Formula visitString(String s) {
    return new StringValue(s);
  }

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  @Override
  public Formula visitSList(List l) {

    // We assume the first item of a list is always an operation
    // performed on a list of formulas
    OperationType op = symbolToOp(l.get(0));

    // the list of formulas evaluated by the operation
    List<Sexp> rest = l.subList(1, l.size());

    // must transform the rest of the list of Sexp's to Formula
    ArrayList<Formula> arguments = new ArrayList<Formula>();
    for (Sexp sexp : rest) {
      arguments.addAll(sexp.accept(this).flatten());
    }

    // creates a new Operation item with the list of arguments
    switch (op) {
      case SUM:
        return new SumOp(arguments);
      case PRODUCT:
        return new ProdOp(arguments);
      case LESS:
        return new LessThanOp(arguments);
      case GREATER:
        return new GreaterThanOp(arguments);
      default:
        throw new IllegalArgumentException("Bad cell form");
    }
  }

  // Turns a symbol into an operation
  private OperationType symbolToOp(Object o) {
    SSymbol symbol = (SSymbol) o;
    switch (symbol.toString()) {
      case "SUM":
        return OperationType.SUM;
      case "PRODUCT":
        return OperationType.PRODUCT;
      case "<":
        return OperationType.LESS;
      case ">":
        return OperationType.GREATER;
      default:
        throw new IllegalArgumentException("Bad cell form");
    }
  }

}
