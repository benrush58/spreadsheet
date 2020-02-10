import org.junit.Test;

import java.io.FileNotFoundException;

import spreadsheets.controller.Controller;
import spreadsheets.model.BasicWorkbook;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.WorkbookModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.view.EditView;

import static org.junit.Assert.assertEquals;

/**
 * Tests the interactions between the Controller, Workbook, and EditView.
 */
public class ControllerTest {

  private MockModel mockModel;
  private Controller controller;
  private MockWorkBook mockWorkBook;
  private MockView mockView;

  private void reset() {
    // A1
    // A2
    // B1
    // B2
    Worksheet model = new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "1") // A1
            .createCell(1, 2, "3") // A2
            .createCell(2, 1, "4") // B1
            .createCell(2, 2, "8") // B2
            .createWorksheet();
    mockModel = new MockModel(model, new StringBuilder());
    Workbook book = new BasicWorkbook(mockModel);
    mockWorkBook = new MockWorkBook(book, new StringBuilder());
    controller = new Controller(mockWorkBook);
    mockView = new MockView(new EditView(new WorkbookModelView(book)), new StringBuilder());
    controller.setView(mockView);
  }

  @Test
  public void testAddBlankPage() {
    reset();
    controller.addBlankPage("test");
    assertEquals("Method: addPage, Input: test", mockWorkBook.log.toString());
  }

  @Test
  public void testLoadPage() throws FileNotFoundException {
    reset();
    Worksheet sheet = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    controller.loadPage("name", sheet);
    assertEquals("Method: addPage, Input: name, untitledMethod: updateCurrentPage, Input: name",
            mockWorkBook.log.toString());
  }


  @Test
  public void testRemovePage() {
    reset();
    controller.removePage("untitled");
    assertEquals("Method: removePage, Input: untitled",
            mockWorkBook.log.toString());
  }


  @Test
  public void testRenamePage() {
    reset();
    controller.renamePage("untitled", "newTest");
    assertEquals("Method: renamePage, Input: untitled, newTest",
            mockWorkBook.log.toString());
  }


  @Test
  public void testSavePage() throws FileNotFoundException {
    reset();
    controller.savePage("untitled", "testFile");
    assertEquals("Method: getPage, Input: untitled",
            mockWorkBook.log.toString());
  }

  @Test
  public void testSetView() {
    reset();
    // we called the below line in the reset method!
    //controller.setView(mockView);
    assertEquals("Features added.",
            mockView.log.toString());
  }


  @Test
  public void testSetCell() {
    reset();
    controller.setCell("untitled", new Coord(1, 1), "123");
    assertEquals("Method: getCells()Method: getCell, Input: A1Method: setCellContents, "
                    + "Inputs: A1, 123Method: updateReference, Input: A1",
            mockModel.log.toString());
  }


  @Test
  public void testDeleteCell() {
    reset();
    controller.deleteCell("untitled", new Coord(3, 2));
    assertEquals("Method: getCells()Method: getCell, Input: A1Method: deleteCell, Input: C2",
            mockModel.log.toString());
  }

  @Test
  public void testSelectCell() {
    reset();
    controller.selectCell("untitled", new Coord(1, 1));
    assertEquals("Method: getCells()Method: getCell, Input: A1Method: getCell, Input: A1",
            mockModel.log.toString());
    assertEquals("Features added.Cell highlighted: A1", mockView.log.toString());
  }

  @Test
  public void testSetCurrentPage() {
    reset();
    controller.setCurrentPage("untitled");
    assertEquals("Method: updateCurrentPage, Input: untitled",
            mockWorkBook.log.toString());
    assertEquals("Features added.New model set.", mockView.log.toString());
  }
}
