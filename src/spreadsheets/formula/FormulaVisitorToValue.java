package spreadsheets.formula;

import spreadsheets.formula.operation.Operation;
import spreadsheets.formula.reference.WorksheetReference;
import spreadsheets.formula.value.WorksheetValue;

/**
 * Represents a concrete class which acts on a Formula
 * and turns it into a WorksheetValue.
 */
public class FormulaVisitorToValue implements FormulaVisitor<WorksheetValue> {
  /**
   * Process an Operation.
   *
   * @param op the operation
   * @return the desired result
   */
  @Override
  public WorksheetValue visitOperation(Operation op) {
    return op.evaluate();
  }

  /**
   * Process a WorksheetReference.
   *
   * @param ref the reference
   * @return the desired result
   */
  @Override
  public WorksheetValue visitReference(WorksheetReference ref) {
    return ref.evaluate();
  }

  /**
   * Process a WorksheetValue.
   *
   * @param val the value
   * @return the desired result
   */
  @Override
  public WorksheetValue visitValue(WorksheetValue val) {
    return val;
  }
}
