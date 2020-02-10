package spreadsheets.formula;

import spreadsheets.formula.operation.Operation;
import spreadsheets.formula.reference.WorksheetReference;
import spreadsheets.formula.value.WorksheetValue;

/**
 * An abstracted function object for processing any Formulas.
 * @param <R> The return type of this function
 */
public interface FormulaVisitor<R> {

  /**
   * Process an Operation.
   * @param op the operation
   * @return the desired result
   */
  R visitOperation(Operation op);

  /**
   * Process a WorksheetReference.
   * @param ref the reference
   * @return the desired result
   */
  R visitReference(WorksheetReference ref);

  /**
   * Process a WorksheetValue.
   * @param val the value
   * @return the desired result
   */
  R visitValue(WorksheetValue val);
}
