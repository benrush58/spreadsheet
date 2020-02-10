package spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;

import spreadsheets.formula.Formula;
import spreadsheets.formula.value.WorksheetValue;

/**
 * Represents a Cell in a spreadsheet, that can evaluate its
 * contents, return its coordinates in the spreadsheet, and
 * get the raw data that was input by the user.
 */
public interface Cell {

  /*
   * Gives the user a copy of the Cell's coordinates.
   * @return the Cell's Coords
   */
  Coord getCoord();

  /*
   * Gives the user a copy of the raw data in a Cell.
   * @return a copy of the raw data
   */
  String getRawData();

  /**
   * Gets the Formula of a Cell.
   * @return the Formula
   */
  Formula getFormula();

  /**
   * Gets the evaluated value of a Cell.
   * @return the evaluated WorksheetValue
   */
  WorksheetValue getEvaluated();

  /**
   * Updates the evaluated data of cells.
   */
  void updateEvaluated();

  void addDirect(HashMap<String, ArrayList<Coord>> direct);

  HashMap<String, ArrayList<Coord>> getDirect();

}
