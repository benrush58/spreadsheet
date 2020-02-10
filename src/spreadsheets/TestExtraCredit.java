package spreadsheets;

import java.io.FileNotFoundException;
import java.io.FileReader;

import spreadsheets.controller.Controller;
import spreadsheets.model.BasicWorkbook;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Workbook;
import spreadsheets.model.WorkbookModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.model.WorksheetReader;
import spreadsheets.view.EditView;
import spreadsheets.view.WorksheetView;

/**
 * A main function to test the Workbook model functionality.
 */
public class TestExtraCredit {

  /**
   * The main method that runs the program.
   * @param args no arguments taken in the command line
   * @throws FileNotFoundException if the file cannot be found
   */
  public static void main(String[] args) throws FileNotFoundException {
    WorksheetReader.WorksheetBuilder<Worksheet> builder
            = new BasicWorksheet.BasicWorksheetBuilder();
    FileReader file = new FileReader("resources/BasicTestFile.txt");
    Worksheet sheet = WorksheetReader.read(builder, file);
    Workbook model = new BasicWorkbook(sheet);
    Controller controller = new Controller(model);
    WorksheetView view = new EditView(new WorkbookModelView(model));
    controller.setView(view);
  }
}
