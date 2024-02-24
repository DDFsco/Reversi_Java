package ourreversi.cell;

/**
 * Represent the square reversi cell position.
 * x is the horizontal coordinate.
 * y is the vertical coordinate.
 */
public class SquareCellPosition implements ICellPosition {

  private int x;
  private int y;

  /**
   * Constructs a CellPosition with specified x, y, and z coordinates.
   *
   * @param x The x-coordinate of the cell, from the upper right to the lower left.
   * @param y The y-coordinate of the cell, from the upper left to the lower right.
   */
  public SquareCellPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int xGetter() {
    return this.x;
  }

  @Override
  public int yGetter() {
    return this.y;
  }

  @Override
  public int zGetter() {
    return 0;
  }


  /**
   * Compares this CellPosition with another object for equality.
   *
   * @param that The object with which to compare.
   * @return true if the given object represents a CellPosition
   *        equivalent to this position, false otherwise.
   */
  @Override
  public boolean equals(Object that) {
    if (!(that instanceof SquareCellPosition)) {
      return false;
    }
    SquareCellPosition thatPosn = (SquareCellPosition) that;
    return this.x == thatPosn.x && this.y == thatPosn.y;
  }

  /**
   * Returns a hash code for this CellPosition.
   *
   * @return A hash code for this CellPosition.
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
