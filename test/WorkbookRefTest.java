import org.junit.Test;

import java.util.ArrayList;

import spreadsheets.controller.Controller;
import spreadsheets.formula.value.StringValue;
import spreadsheets.model.BasicWorkbook;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.WorkbookModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.view.EditView;

import static org.junit.Assert.assertEquals;

/**
 * Tests the references across Worksheets in a Workbook.
 */
public class WorkbookRefTest {
  private Workbook book;
  private Worksheet page1;
  private Worksheet page2;

  private Controller controller;

  private void reset() {
    this.book = new BasicWorkbook();

    this.page1 = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    this.page2 = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    Worksheet page3 = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();

    this.book.addPage("page1", this.page1);
    this.book.addPage("page2", this.page2);
    this.book.addPage("page3", page3);

    this.controller = new Controller(this.book);
    this.controller.setView(new EditView(new WorkbookModelView(this.book)));
  }

  @Test
  public void testRefs() {
    reset();
    // checks that all the pages were added correctly
    assertEquals(4, this.book.getPages().length);
    assertEquals("untitled", this.book.getPages()[0]);
    assertEquals("page3", this.book.getPages()[1]);
    assertEquals("page2", this.book.getPages()[2]);
    assertEquals("page1", this.book.getPages()[3]);

    //renaming
    this.book.renamePage("untitled", "renamed");
    assertEquals("renamed", this.book.getPages()[3]);

    //removing
    this.book.removePage("renamed");
    assertEquals(3, this.book.getPages().length);

    //select page through controller and testing getCurrentPage and updateCurrentPage
    controller.setCurrentPage("page1");
    assertEquals(this.page1, this.book.getCurrentPage());
    this.book.updateCurrentPage("page2");
    assertEquals(this.page2, this.book.getCurrentPage());

    //testing getPages and getPage
    String[] answer = {"page3", "page2", "page1"};
    assertEquals(answer, this.book.getPages());
    assertEquals(this.page1, this.book.getPage("page1"));

    //testing adding a page with just a name
    this.book.addPage("page4");
    assertEquals(4, this.book.getPages().length);
    assertEquals(new ArrayList<Cell>(),
            this.book.getPage("page4").getCells());
  }


  //Time to test errors, including cyclic references
  @Test
  public void refTestSelfRef() {
    reset();
    this.page1.setCellContents(new Coord(1,1), "=page1@A1");
    assertEquals(new StringValue("#CYCLIC"),
            this.page1.evalCell(new Coord(1, 1)));
  }


  @Test
  public void refTestCyclicWithinSheet() {
    reset();
    this.page1.setCellContents(new Coord(1,1), "=page1@A2");
    this.page1.setCellContents(new Coord(1,2), "=page1@A3");
    this.page1.setCellContents(new Coord(1,3), "=page1@A1");

    assertEquals(new StringValue("#CYCLIC"),
            this.page1.evalCell(new Coord(1, 1)));
    assertEquals(new StringValue("#CYCLIC"),
            this.page1.evalCell(new Coord(1, 2)));
    assertEquals(new StringValue("#CYCLIC"),
            this.page1.evalCell(new Coord(1, 3)));
  }

  @Test
  public void refTestCyclesAcrossSheets() {
    reset();
    this.page1.setCellContents(new Coord(1,1), "=page2@A1");
    this.page2.setCellContents(new Coord(1,1), "=page1@A1");
    assertEquals(new StringValue("#CYCLIC"),
            this.page1.evalCell(new Coord(1, 1)));
    assertEquals(new StringValue("#CYCLIC"),
            this.page2.evalCell(new Coord(1, 1)));
  }

  // cell that references something with a cyclic reference!!!

}
