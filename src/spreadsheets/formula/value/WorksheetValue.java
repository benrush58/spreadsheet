package spreadsheets.formula.value;

import spreadsheets.formula.Formula;

/**
 * Represents a value in a worksheet.
 */
public interface WorksheetValue extends Formula {

  /**
   * Returns the unwrapped value of the WorksheetValue.
   * @param <T> the type you want to return
   * @return the value to return
   */
  <T> T getValue();

}
