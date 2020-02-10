package spreadsheets.formula.reference;

import java.util.ArrayList;
import java.util.HashMap;

import spreadsheets.formula.Formula;
import spreadsheets.model.Coord;

/**
 * Represents a reference in a worksheet.
 */
public interface WorksheetReference extends Formula {

  HashMap<String, ArrayList<Coord>> getRefs();

}
