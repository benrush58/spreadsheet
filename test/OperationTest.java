import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import spreadsheets.formula.Formula;
import spreadsheets.formula.operation.GreaterThanOp;
import spreadsheets.formula.operation.LessThanOp;
import spreadsheets.formula.operation.ProdOp;
import spreadsheets.formula.operation.SumOp;
import spreadsheets.formula.value.BooleanValue;
import spreadsheets.formula.value.NumberValue;
import spreadsheets.formula.value.StringValue;
import spreadsheets.model.BasicWorkbook;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Coord;
import spreadsheets.model.Workbook;
import spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;

/**
 * A tester class for all operations performed on a spreadsheet.
 */
public class OperationTest {
  private Formula zero;
  private Formula two;
  private Formula three;
  private Formula five;
  private Formula six;
  private Formula ten;
  private Formula sixteen;
  private Formula tru;
  private Formula nah;
  private Formula refA1;
  private Formula refA1B2;

  private ArrayList<Formula> list15neg4;
  private ArrayList<Formula> list123;
  private ArrayList<Formula> listAllTypes;

  private ArrayList<Formula> listNeg42;
  private ArrayList<Formula> list2neg4;
  private ArrayList<Formula> list33;

  private Worksheet intMod;

  private void reset() {
    Workbook book = new BasicWorkbook();

    intMod = new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "1") // A1
            .createCell(1, 2, "3") // A2
            .createCell(2, 1, "4") // B1
            .createCell(2, 2, "8") // B2
            .createWorksheet();

    this.zero = new NumberValue(0.0);
    Formula one = new NumberValue(1.0);
    this.two = new NumberValue(2.0);
    this.three = new NumberValue(3.0);
    Formula negFour = new NumberValue(-4.0);
    this.five = new NumberValue(5.0);
    this.six = new NumberValue(6.0);
    this.ten = new NumberValue(10.0);
    this.sixteen = new NumberValue(16.0);
    Formula hello = new StringValue("hello");
    this.tru = new BooleanValue(true);
    this.nah = new BooleanValue(false);

    this.list15neg4 = new ArrayList<Formula>(Arrays.asList(one, this.five, negFour));
    this.list123 = new ArrayList<Formula>(Arrays.asList(one, this.two, this.three));
    this.listAllTypes = new ArrayList<Formula>(
            Arrays.asList(this.three, negFour, hello, this.nah));

    this.listNeg42 = new ArrayList<Formula>(Arrays.asList(negFour, this.two));
    this.list2neg4 = new ArrayList<Formula>(Arrays.asList(this.two, negFour));
    this.list33 = new ArrayList<Formula>(Arrays.asList(this.three, this.three));
  }

  @Test
  public void testSumOp() {
    reset();
    // tests that basic lists of numbers can be summed
    assertEquals(this.zero, new SumOp(new ArrayList<>()).evaluate());
    assertEquals(this.six, new SumOp(this.list123).evaluate());
    assertEquals(this.two, new SumOp(this.list15neg4).evaluate());

    // tests that nested lists can be summed
    // "=(SUM 2 (SUM 1 2 3) (SUM 1 5 -4) )
    assertEquals(this.ten, new SumOp(new ArrayList<Formula>(
            Arrays.asList(this.two, new SumOp(list123).evaluate(),
                    new SumOp(this.list15neg4).evaluate()))).evaluate());

    // tests that adding strings and booleans doesn't mess up the sum
    assertEquals(new NumberValue(-1.0), new SumOp(this.listAllTypes).evaluate());

    // tests formulas that refer to the same cell multiple times
    // "=(SUM A1 A1 3)"
    assertEquals(this.five, new SumOp(new ArrayList<Formula>(
            Arrays.asList(this.refA1, this.refA1, this.three))).evaluate());
  }

  @Test
  public void testProdOp() {
    reset();
    // tests that a basic list of numbers can be multiplied
    assertEquals(this.zero, new ProdOp(new ArrayList<>()).evaluate());
    assertEquals(this.six, new ProdOp(this.list123).evaluate());
    assertEquals(new NumberValue(-20.0), new ProdOp(this.list15neg4).evaluate());

    // tests that nested lists can be evaluated
    assertEquals(new NumberValue(24.0), new ProdOp(new ArrayList<Formula>(
            Arrays.asList(this.two, new SumOp(list123).evaluate(),
                    new SumOp(this.list15neg4).evaluate()))).evaluate());

    // tests that bogus inputs are ignored
    assertEquals(new NumberValue(-12.0), new ProdOp(this.listAllTypes).evaluate());

  }

  //Test for using a block reference in a function
  @Test
  public void testBlockReferences() {
    reset();
    assertEquals(new NumberValue(1.0), this.refA1B2.evaluate());
    this.intMod.setCellContents(new Coord(3, 1), "(SUM A1:B2)");
    assertEquals(this.sixteen, this.intMod.evalCell(new Coord(3,1 )));
    this.intMod.setCellContents(new Coord(4, 1), "(PRODUCT C1 (SUM A1:B2))");
    assertEquals(new NumberValue(256.0), this.intMod.evalCell(new Coord(4,1 )));
  }

  //////////////////////////// TESTS FOR THE LESS THAN OPERATION /////////////////////////

  @Test
  public void testLessThanOp() {
    reset();
    // tests that basic functionality works
    assertEquals(this.tru, new LessThanOp(this.listNeg42).evaluate());
    assertEquals(this.nah, new LessThanOp(this.list2neg4).evaluate());
    assertEquals(this.nah, new LessThanOp(this.list33).evaluate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void lessThanErrorZeroInput() {
    reset();
    Formula lessThan = new LessThanOp(new ArrayList<>());
    lessThan.evaluate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void lessThanErrorOneInput() {
    reset();
    Formula lessThan = new LessThanOp(new ArrayList<Formula>(Arrays.asList(this.three)));
    lessThan.evaluate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void lessThanErrorThreeInput() {
    reset();
    Formula lessThan = new LessThanOp(this.list123);
    lessThan.evaluate();
  }

  //////////////////////////// TESTS FOR THE GREATER THAN OPERATION /////////////////////////

  @Test
  public void testGreaterThanOp() {
    reset();
    // tests that basic functionality works
    assertEquals(this.nah, new GreaterThanOp(this.listNeg42).evaluate());
    assertEquals(this.tru, new GreaterThanOp(this.list2neg4).evaluate());
    assertEquals(this.nah, new GreaterThanOp(this.list33).evaluate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void greaterThanErrorZeroInput() {
    reset();
    Formula greaterThan = new GreaterThanOp(new ArrayList<>());
    greaterThan.evaluate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void greaterThanErrorOneInput() {
    reset();
    Formula greaterThan = new GreaterThanOp(new ArrayList<Formula>(Arrays.asList(this.three)));
    greaterThan.evaluate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void greaterThanErrorThreeInput() {
    reset();
    Formula greaterThan = new GreaterThanOp(this.list123);
    greaterThan.evaluate();
  }


}
