package spreadsheets.formula.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.FormulaVisitor;
import spreadsheets.model.Coord;

/**
 * Represents a Boolean value in a worksheet.
 */
public class BooleanValue implements WorksheetValue {
  private Boolean value;

  /**
   * Constructs the boolean value.
   *
   * @param b the value of the boolean to be created
   */
  public BooleanValue(Boolean b) {
    this.value = b;
  }

  /**
   * Evaluates the Formula. Note: A Formula can take in any non-negative number of inputs, as is
   * dependent on the Formula being evaluated.
   *
   * @return a WorksheetValue that the formula evaluates to
   */
  @Override
  public WorksheetValue evaluate() {
    return new BooleanValue(this.value);
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
  public <Boolean> Boolean getValue() {
    return (Boolean) this.value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  @Override
  public boolean equals(Object boolval) {
    if (boolval == this) {
      return true;
    }
    if (!(boolval instanceof BooleanValue)) {
      return false;
    }
    BooleanValue bool = (BooleanValue) boolval;
    return bool.value.equals(this.value);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
