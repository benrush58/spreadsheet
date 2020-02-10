import org.junit.Test;

import spreadsheets.formula.value.BooleanValue;
import spreadsheets.formula.value.NumberValue;
import spreadsheets.formula.value.StringValue;
import spreadsheets.model.BasicCell;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Cell;
import spreadsheets.model.Coord;
import spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The tester class for a basic Worksheet.
 */
public class BasicModelTest {
  private Worksheet mtMod;
  private Worksheet intMod;

  private void reset() {
    this.mtMod = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    this.intMod = new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "1") // A1
            .createCell(1, 2, "3") // A2
            .createCell(2, 1, "4") // B1
            .createCell(2, 2, "8") // B2
            .createWorksheet();
  }

  @Test
  public void testCreateEmptyWS() {
    reset();
    Worksheet empty = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    assertTrue(empty instanceof BasicWorksheet);
  }

  @Test
  public void canAddEachType() {
    reset();
    this.mtMod.setCellContents(new Coord(1, 1), "9");
    this.mtMod.setCellContents(new Coord(1, 2), "true");
    this.mtMod.setCellContents(new Coord(1, 3), "\"howdy\"");
    this.mtMod.setCellContents(new Coord(1, 4), "(SUM 3 5)");
    this.mtMod.setCellContents(new Coord(2, 1), "(< A1 10)");
    this.mtMod.setCellContents(new Coord(2, 2), "(PRODUCT 6 4)");
    this.mtMod.setCellContents(new Coord(2, 3), "A1");
    this.mtMod.setCellContents(new Coord(2, 4), "A1:A3");
    this.mtMod.setCellContents(new Coord(2, 4), "(SUM A1:A8 true 7 \"hey\")");
    this.mtMod.setCellContents(new Coord(2, 4), "(SUM 8 (PRODUCT (SUM 4 2) 4) 7)");
    assertNotNull(this.mtMod.getCell(new Coord(1, 1)));
  }

  @Test
  public void testAllCellsThere() {
    reset();
    Cell a1 =
            new BasicCell(this.intMod, new Coord(1, 1), "1");
    Cell a2 = new BasicCell(this.intMod, new Coord(1, 2), "3");
    Cell b1 = new BasicCell(this.intMod, new Coord(2, 1), "4");
    Cell b2 = new BasicCell(this.intMod, new Coord(2, 2), "8");
    assertEquals(a1, this.intMod.getCell(new Coord(1, 1)));
    assertEquals(a2, this.intMod.getCell(new Coord(1, 2)));
    assertEquals(b1, this.intMod.getCell(new Coord(2, 1)));
    assertEquals(b2, this.intMod.getCell(new Coord(2, 2)));
  }

  @Test
  public void testNoSelfReferential() {
    reset();
    this.intMod.setCellContents(new Coord(1, 1), "=A1");
    assertEquals("#CYCLIC", this.intMod.getCell(new Coord(1, 1)).getEvaluated().toString());
  }

  @Test
  public void testNoComplexReferential() {
    reset();
    this.intMod.setCellContents(new Coord(3, 1), "=C3");
    this.intMod.setCellContents(new Coord(3, 2), "=C1");
    this.intMod.setCellContents(new Coord(3, 3), "=C2");
    assertEquals("#CYCLIC", this.intMod.getCell(new Coord(3, 1)).getEvaluated().toString());
  }

  @Test
  public void setCellContents() {
    reset();
    // Changes the contents of an existing cell
    assertEquals("3", this.intMod.getCell(new Coord(1, 2)).getRawData());
    this.intMod.setCellContents(new Coord(1, 2), "100");
    assertEquals("100", this.intMod.getCell(new Coord(1, 2)).getRawData());
  }

  @Test
  public void getCells() {
    reset();
    assertEquals(new BasicCell(intMod, new Coord(1, 1), "1"),
            intMod.getCell(new Coord(1, 1)));
  }


  @Test
  public void getCellsError() {
    reset();
    assertEquals(new BasicCell(mtMod, new Coord(1, 1), ""), mtMod.getCell(new Coord(1, 1)));
  }

  @Test
  public void evaluateCell() {
    reset();
    //Stack overflow for eval of number cell
    Cell a1 = new BasicCell(this.intMod, new Coord(1, 1), "1");
    assertEquals(new NumberValue(1.0), a1.getEvaluated());
    //testing eval of string cell
    Cell a2 = new BasicCell(this.intMod, new Coord(1, 2), "\"hello\"");
    assertEquals(new StringValue("hello"), a2.getEvaluated());
    //testing eval of boolean cell
    Cell a3 = new BasicCell(this.intMod, new Coord(1, 3), "true");
    assertEquals(new BooleanValue(true), a3.getEvaluated());
    //testing eval of cell with a single reference
    Cell a4 = new BasicCell(this.intMod, new Coord(1, 4), "a1");
    assertEquals(new NumberValue(1.0), a4.getEvaluated());
    //testing eval of cell with a multi reference
    //our implementation of multi cell references has it so when a cell with one is evaluated,
    //it evaluates to the value in the top left corner, so this test would evaluate to the
    //value of a1

    Cell a5 = new BasicCell(this.intMod, new Coord(1, 5), "A1:A3");
    assertEquals(new NumberValue(1.0),
            a5.getEvaluated());

    //test for a cell with SumOp
    Cell a6 = new BasicCell(this.intMod, new Coord(1, 6), "(SUM 4 3)");
    assertEquals(new NumberValue(7.0),
            a6.getEvaluated());
    //test for a cell with ProdOp
    Cell a7 = new BasicCell(this.intMod, new Coord(1, 7), "(PRODUCT 4 3)");
    assertEquals(new NumberValue(12.0),
            a7.getEvaluated());
    //test for a cell with LessThanOp
    Cell a8 = new BasicCell(this.intMod, new Coord(1, 8), "(< 4 3)");
    assertEquals(new BooleanValue(false),
            a8.getEvaluated());
    Cell a9 = new BasicCell(this.intMod, new Coord(1, 9), "(< 3 4)");
    assertEquals(new BooleanValue(true),
            a9.getEvaluated());
    //test for a cell with GreaterThanOp
    Cell a10 = new BasicCell(this.intMod, new Coord(1, 10), "(> 4 3)");
    assertEquals(new BooleanValue(true),
            a10.getEvaluated());
    Cell a11 = new BasicCell(this.intMod, new Coord(1, 11), "(> 3 4)");
    assertEquals(new BooleanValue(false),
            a11.getEvaluated());
  }

  @Test
  public void testEvaluateCellError() {
    reset();
    Cell test = new BasicCell(this.intMod, new Coord(1, 1), "HAHA 3 1");
    assertEquals(new StringValue("bad parse"), test.getEvaluated());
  }

  @Test
  public void testAllContents() {
    reset();
    //Test for adding
    Cell a1 = new BasicCell(this.intMod, new Coord(1, 1), "1");
    assertEquals(new Coord(1, 1),
            a1.getCoord());
    assertEquals("1",
            a1.getRawData());
  }

  @Test
  public void testNoCellContents() {
    reset();
    // throws an error when you try to get a cell with not contents
    assertEquals(new BasicCell(this.intMod, new Coord(100, 100), ""),
            this.intMod.getCell(new Coord(100, 100)));
  }

  @Test
  public void getCoord() {
    reset();
    Cell b2 = new BasicCell(this.intMod, new Coord(2, 2), "8");
    assertEquals(new Coord(2, 2), b2.getCoord());
  }

  @Test
  public void getRawData() {
    reset();
    Cell b2 = new BasicCell(this.intMod, new Coord(2, 2), "8");
    assertEquals("8", b2.getRawData());
  }

  @Test(expected = IllegalArgumentException.class)
  public void editCellContentsInvalidCoord() {
    reset();
    this.intMod.setCellContents(new Coord(0, 0), "this cell don't exist");
  }

  @Test
  public void testRender() {
    reset();
    BooleanValue bool = new BooleanValue(true);
    NumberValue num = new NumberValue(6.0);
    StringValue str = new StringValue("howdy");
    assertEquals("true", bool.toString());
    assertEquals("6.0", num.toString());
    assertEquals("howdy", str.toString());
  }

}