package spreadsheets.model;

import java.util.Objects;

/**
 * A value type representing coordinates in a {@link Worksheet}.
 */
public class Coord {
  public final int row;
  public final int col;

  /**
   * Constructs a coordinate.
   * @param col column number
   * @param row row number
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Turns a string representation of a coord into a Coord.
   * @param str the given string
   * @return the string as a coord
   */
  public static Coord strToCoord(String str) {
    int count = 0;
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (Character.isLetter(c)) {
        count++;
      } else {
        break;
      }
    }
    String col = str.substring(0, count);
    String row = str.substring(count, str.length());
    return new Coord(colNameToIndex(col), Integer.parseInt(row));
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
        && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}
