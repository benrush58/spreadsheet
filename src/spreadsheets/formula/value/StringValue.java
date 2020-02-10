package spreadsheets.formula.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.FormulaVisitor;
import spreadsheets.model.Coord;

/**
 * Represents a string value in a worksheet.
 */
public class StringValue implements WorksheetValue {
  private String value;

  /**
   * Constructs the string value.
   *
   * @param s the value of the string to be constructed
   */
  public StringValue(String s) {
    this.value = s;
  }

  /**
   * Evaluates the Formula. Note: A Formula can take in any non-negative number of inputs, as is
   * dependent on the Formula being evaluated.
   *
   * @return a WorksheetValue that the formula evaluates to
   */
  @Override
  public WorksheetValue evaluate() {
    return new StringValue(this.value);
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
  public <String> String getValue() {
    return (String) this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  @Override
  public boolean equals(Object strval) {
    if (strval == this) {
      return true;
    }
    if (!(strval instanceof StringValue)) {
      return false;
    }
    StringValue str = (StringValue) strval;
    return str.value.equals(this.value);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
