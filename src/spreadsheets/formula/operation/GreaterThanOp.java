package spreadsheets.formula.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.FormulaVisitor;
import spreadsheets.formula.value.BooleanValue;
import spreadsheets.formula.value.NumberValue;
import spreadsheets.formula.value.WorksheetValue;
import spreadsheets.model.Coord;

/**
 * An operation for determining if one formula is greater than another operation.
 */
public class GreaterThanOp implements Operation {
  private ArrayList<Formula> args;

  /**
   * Constructs a greater than operation.
   *
   * @param args the formulas to be compared
   */
  public GreaterThanOp(ArrayList<Formula> args) {
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
    if (args.size() != 2 || !(args.get(0).evaluate() instanceof NumberValue)
            || !(args.get(1).evaluate() instanceof NumberValue)) {
      throw new IllegalArgumentException("Can only compare two number values");
    }
    return new BooleanValue((Double) args.get(0).evaluate().getValue()
            > (Double) args.get(1).evaluate().getValue());
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
