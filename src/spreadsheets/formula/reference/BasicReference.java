package spreadsheets.formula.reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spreadsheets.formula.Formula;
import spreadsheets.formula.FormulaVisitor;
import spreadsheets.formula.value.StringValue;
import spreadsheets.formula.value.WorksheetValue;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.Worksheet;

/**
 * Represents a reference to another Cell in a Worksheet.
 */
public class BasicReference implements WorksheetReference {
  // information about cells that are being referenced
  private HashMap<String, ArrayList<Coord>> refCells;
  private Worksheet refModel;

  // information about where the ref is originating
  private Workbook book;
  private Worksheet model;
  private Coord cell;

  /**
   * Constructs a BasicReference.
   *
   * @param s    the raw string contained in the cell
   * @param b    the workbook the reference belongs to
   * @param m    the model the reference belongs to
   * @param cell the cell the reference is contained in
   */
  public BasicReference(String s, Workbook b, Worksheet m, Coord cell) {
    this.refCells = new HashMap<>();
    this.book = b;
    this.model = m;
    this.cell = cell;

    try {
      // if the reference is to a worksheet outside that of its own...
      String rawString;
      if (s.contains("@")) {
        String[] original = s.split("@", 2);
        // separate into Worksheet name and referenced cells
        this.refModel = this.book.getPage(original[0]);
        rawString = original[1];

      } else {
        this.refModel = m;
        rawString = s;
      }

      // initializes a hashmap of the directly referenced coordinates
      HashMap<String, ArrayList<Coord>> direct = new HashMap<>();

      // initializes the model in the HashMap
      direct.put(this.refModel.getName(), new ArrayList<>());

      // if block reference, flatten into a list of cells
      if (rawString.contains(":")) {
        String[] parts = s.split(":");
        // topLeft and bottomRight refer to corners of referenced rectangle
        Coord topLeft = Coord.strToCoord(parts[0]);
        Coord bottomRight = Coord.strToCoord(parts[1]);

        // add each cell in the rectangle to the list of refCells
        for (int i = topLeft.col; i <= bottomRight.col; i++) {
          for (int j = topLeft.row; j <= bottomRight.row; j++) {
            if (!direct.get(this.refModel.getName()).contains(new Coord(i, j))) {
              ArrayList<Coord> curr = direct.get(this.refModel.getName());
              curr.add(new Coord(i, j));
              direct.put(this.refModel.getName(), curr);
            }
          }
        }
      } else {
        // translate s into a coordinate and add that to the list of coordinates in the reference
        direct.get(this.refModel.getName()).add(Coord.strToCoord(rawString));
      }

      try {
        this.book.getPage(this.model.getName()).getCell(this.cell).addDirect(direct);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      HashMap<String, ArrayList<Coord>> acc = new HashMap<>();
      this.refCells = addIndirect(acc, direct);

    } catch (Exception ignored) {
      // ignore the illegally formatted Sexp
    }
  }

  // Builds up the HashMap of all indirect references in a model
  private HashMap<String, ArrayList<Coord>> addIndirect(HashMap<String, ArrayList<Coord>> acc,
                                                        HashMap<String, ArrayList<Coord>> direct) {
    // run through all the direct references
    if (direct != null) {
      for (String page : direct.keySet()) {
        for (Coord coord : direct.get(page)) {
          // if the accumulator does not have any references to the page...
          if (!acc.containsKey(page)) {
            acc.put(page, new ArrayList<>());
          }
          // if it does contain the page..
          if (!acc.get(page).contains(coord)) {
            acc.get(page).add(coord);
            addIndirect(acc, this.book.getPage(page).getCell(coord).getDirect());
          }
        }
      }
    }
    // if they are not in the accumulator, add them
    // call add indirect of each reference not in the accumulator so far
    return acc;
  }


  /**
   * Evaluates the Formula. Note: A Formula can take in any non-negative number of inputs, as is
   * dependent on the Formula being evaluated.
   *
   * @return a WorksheetValue that the formula evaluates to
   */
  @Override
  public WorksheetValue evaluate() {
    // catch badly formatted sexp's
    if (this.refCells.size() < 1) {
      return new StringValue("#BFS");
    }
    // if referenced cells has this model and this coord, there's a cycle
    if (this.refCells.containsKey(this.model.getName())
            && this.refCells.get(this.model.getName()).contains(this.cell)) {
      return new StringValue("#CYCLIC");
    } else {
      //  referenced model ---> list of referenced cells ---> first in list --> evaluate
      return this.refModel.getCell(this.refCells.get(refModel.getName()).get(0)).getEvaluated();
    }
  }

  @Override
  public HashMap<String, ArrayList<Coord>> getRefs() {
    return this.refCells;
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitReference(this);
  }

  @Override
  public List<Formula> flatten() {
    ArrayList<Formula> forms = new ArrayList<>();
    try {
      for (Coord c : this.refCells.get(this.refModel.getName())) {
        forms.add(this.model.getCell(c).getFormula());
      }
    } catch (Exception e) {
      //
    }
    return forms;
  }

}