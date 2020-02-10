package spreadsheets.formula.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.FormulaVisitor;
import spreadsheets.formula.value.NumberValue;
import spreadsheets.formula.value.WorksheetValue;
import spreadsheets.model.Coord;

/**
 * A function object for evaluating the product of a list of functions.
 */
public class ProdOp implements Operation {
  private ArrayList<Formula> args;

  /**
   * Constructs a product operation.
   * @param args the list of formulas to apply
   */
  public ProdOp(ArrayList<Formula> args) {
    this.args = args;
  }

  /**
   * Evaluates the Formula. Note: A Formula can take in any non-negative number of inputs, as is
   * dependent on the Formula being evaluated.
   *
   * @return a WorksheetValue that the formula evaluates to
   */
  @Override
  public WorksheetValue evaluate() {
    // if there are no values, return 0
    if (args.size() == 0) {
      return new NumberValue(0.0);
    } else {
      double ans = 1.0;
      // multiply the evaluated formula by the current answer
      for (Formula a : this.args) {
        Object curr = a.evaluate();
        if (curr instanceof NumberValue) {
          ans = ans * (double) a.evaluate().getValue();
        }
      }
      return new NumberValue(ans);
    }
  }

  /**
   * A method that can process a Formula. Processing is done through the Visitor pattern.
   *
   * @param visitor the Visitor to be passed in
   * @return the desired value
   */
  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitOperation(this);
  }

  @Override
  public List<Formula> flatten() {
    return new ArrayList<Formula>(Arrays.asList(this));
  }

  @Override
  public HashMap<String, ArrayList<Coord>> getRefs() {
    return new HashMap<>();
  }


}
