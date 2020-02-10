package spreadsheets.formula.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.FormulaVisitor;
import spreadsheets.model.Coord;

/**
 * Represents a Double value in a worksheet.
 */
public class NumberValue implements WorksheetValue {
  private Double value;

  /**
   * Constructs a number.
   *
   * @param d the value of the number
   */
  public NumberValue(Double d) {
    this.value = d;
  }

  /**
   * Evaluates the Formula. Note: A Formula can take in any non-negative number of inputs, as is
   * dependent on the Formula being evaluated.
   *
   * @return a WorksheetValue that the formula evaluates to
   */
  @Override
  public WorksheetValue evaluate() {
    return new NumberValue(this.value);
  }

  /**
   * A method that can process a Formula. Processing is done through the Visitor pattern.
   *
   * @param visitor the Visitor to be passed in
   * @return the desired value
   */
  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitValue(this);
  }

  @Override
  public List<Formula> flatten() {
    return new ArrayList<Formula>(Arrays.asList(this));

  }

  @Override
  public HashMap<String, ArrayList<Coord>> getRefs() {
    return new HashMap<>();
  }


  /**
   * Returns the unwrapped value of the WorksheetValue.
   *
   * @return the value
   */
  @Override
  public <Double> Double getValue() {
    return (Double) this.value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  @Override
  public boolean equals(Object numval) {
    if (numval == this) {
      return true;
    }
    if (!(numval instanceof NumberValue)) {
      return false;
    }
    NumberValue num = (NumberValue) numval;
    return num.value.equals(this.value);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
