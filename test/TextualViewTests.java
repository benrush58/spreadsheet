import org.junit.Test;

import java.io.StringReader;

import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.WorksheetModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.model.WorksheetReader;
import spreadsheets.view.TextualView;

import static org.junit.Assert.assertEquals;

/**
 * A tester class for the textual view of the model.
 */
public class TextualViewTests {
  private WorksheetModelView intMod;
  private WorksheetModelView emptyMod;
  private String out1;
  private TextualView view1;
  private WorksheetReader.WorksheetBuilder builder;

  private void reset() {
    this.intMod = new WorksheetModelView(new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "3")
            .createCell(1, 2, "4")
            .createCell(1, 3, "9")
            .createCell(1, 4, "12")
            .createWorksheet());
    this.emptyMod = new WorksheetModelView(
            new BasicWorksheet.BasicWorksheetBuilder().createWorksheet());
    this.out1 = "A2 4\nA1 3\nA4 12\nA3 9\n";
    this.view1 = new TextualView(this.intMod, new StringBuilder());
    this.builder = new BasicWorksheet.BasicWorksheetBuilder();
  }

  @Test
  public void testOutputToOutput() {
    reset();
    // Tests that an empty model will output an empty string
    assertEquals("", new TextualView(this.emptyMod, new StringBuilder()).ap.toString());
    // Tests that an basic model will output the expected string version
    assertEquals(this.out1, this.view1.ap.toString().trim());
  }

  @Test
  public void testModelToModel() {
    reset();
    // Tests that the same model can be recreated from the output of the textual view
    assertEquals(this.intMod, new WorksheetModelView((Worksheet) WorksheetReader.read(this.builder,
            new StringReader(this.view1.ap.toString()))));
  }

}
