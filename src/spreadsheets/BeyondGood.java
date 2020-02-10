package spreadsheets;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import spreadsheets.controller.Controller;
import spreadsheets.model.BasicWorkbook;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.WorkbookModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.model.WorksheetModelView;
import spreadsheets.model.WorksheetReader;
import spreadsheets.view.EditView;
import spreadsheets.view.TextualView;
import spreadsheets.view.VisualView;
import spreadsheets.view.WorksheetView;

/**
 * This class represents the entry point into the BeyondGood program. When the main method
 * is run, it can call several different combinations of models, views, and controllers
 * to achieve whatever it is the user calls from the command line.
 */
public class BeyondGood {
  /**
   * The main entry point into the program.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    // our implementation variables
    WorksheetView view;
    Worksheet model;
    Workbook book;
    WorksheetModelView modelView;
    FileReader file;
    WorksheetReader.WorksheetBuilder<Worksheet> builder
            = new BasicWorksheet.BasicWorksheetBuilder();
    Controller controller;

    // blank model
    Worksheet blank = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    Workbook blankBook = new BasicWorkbook();

    switch (args[0]) {
      // if the first argument is -gui or -edit, it will create a new blank VisualView or
      // EditView respectively, then end the method
      case "-gui":
        view = new VisualView(new WorksheetModelView(blank));
        return;
      case "-edit":
        controller = new Controller(blankBook);
        view = new EditView(new WorkbookModelView(blankBook));
        controller.setView(view);
        return;
        // if -gui, -provider or -edit is not called, then the first argument should be -in
      // -in should be followed by a file name and a command to do with that file
      case "-in":
        try { // set the model and modelView based on the file
          file = new FileReader(args[1]);
          model = WorksheetReader.read(builder, file);
          book = new BasicWorkbook(model);
          modelView = new WorksheetModelView(model);
        } catch (FileNotFoundException e) {
          System.out.println("File could not be found.");
          return;
        }
        switch (args[2]) {
          case "-eval": // evaluate the given cell in the spreadsheet
            try {
              System.out.println(model.evalCell(Coord.strToCoord(args[3])));
            } catch (Exception e) {
              System.out.println("There was an issue evaluating the cell. Problem: " + e);
            }
            return;
          case "-save": // save the model under the given file name
            PrintWriter p = new PrintWriter(args[3]);
            view = new TextualView(modelView, p);
            p.close();
            return;
          case "-gui": // render the model as a gui view
            view = new VisualView(modelView);
            return;
          case "-edit": // render the model in an editable view
            controller = new Controller(book);
            view = new EditView(new WorkbookModelView(book));
            controller.setView(view);
            return;
          default:
            throw new IllegalArgumentException("Malformed command line!");
        }
      default:
        throw new IllegalArgumentException("Malformed command line!");
    }
  }
}