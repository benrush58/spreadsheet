import spreadsheets.controller.Controller;
import spreadsheets.model.BasicWorkbook;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.WorkbookModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.view.EditView;
import spreadsheets.view.WorksheetView;

/**
 * A class to test the EditView. The main method pops up
 * a window demonstrating the view that can be interacted with.
 */
public class TestEditView {

  /**
   * The main entry-point into the program. Runs an EditView.
   * @param args the command-line inputs
   */
  public static void main(String[] args) {
    Worksheet model = new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "500000000000000000")
            .createCell(1, 2, "\"howdytherefellah\"")
            .createCell(2, 1, "\"howdy\"")
            .createCell(2, 2, "=A1")
            .createCell(5, 5,"100")
            .createCell(10, 2, "\"test\"")
            .createCell(2, 15, "123")
            .createCell(100, 100, "\"hi\"")
            .createCell(3, 1, "2")
            .createCell(3, 2, "3")
            .createCell(3, 3, "=(SUM C1 C2)")
            .createWorksheet();
    Controller controller = new Controller(new BasicWorkbook());
    controller.addBlankPage("hi");
    WorksheetView view = new EditView(new WorkbookModelView(new BasicWorkbook(model)));
    controller.setView(view);
  }
}
