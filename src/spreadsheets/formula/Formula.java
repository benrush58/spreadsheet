package spreadsheets.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.value.WorksheetValue;
import spreadsheets.model.Coord;

/**
 * A Formula is one of... a) Value b) Reference c) Function (operation) All of these can be
 * evaluated to a WorksheetValue
 */
public interface Formula {

  /**
   * Evaluates the Formula. Note: A Formula can take in any non-negative number of inputs, as is
   * dependent on the Formula being evaluated.
   *
   * @return a WorksheetValue that the formula evaluates to
   */
  WorksheetValue evaluate();

  /**
   * A method that can process a Formula. Processing is done through the Visitor pattern.
   *
   * @param visitor the Visitor to be passed in
   * @param <R>     The return type of the Formula
   * @return the desired value
   */
  <R> R accept(FormulaVisitor<R> visitor);

  /**
   * Turns a Formula into a flat list of Formulas.
   * @return  The flattened list
   */
  List<Formula> flatten();

  HashMap<String, ArrayList<Coord>> getRefs();
}
