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
 * A function object that finds the sum of a number of formulas.
 */
public class SumOp implements Operation {
  private ArrayList<Formula> args;

  /**
   * Constructs a sum operation.
   *
   * @param args formulas to be totaled
   */
  public SumOp(ArrayList<Formula> args) {
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
    Double ans = 0.0;

    // add each evaluated formula to the answer if it evaluates to a number
    for (Formula a : args) {
      Object curr = a.evaluate();
      if (curr instanceof NumberValue) {
        ans += (Double) a.evaluate().getValue();
      }
    }
    return new NumberValue(ans);
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

