package ourreversi.cell;


/**
 * Represent the Cell position interface.
 */
public interface ICellPosition {

  /**
   * Retrieves the x-coordinate of this cell position.
   *
   * @return The x-coordinate.
   */
  int xGetter();

  /**
   * Retrieves the y-coordinate of this cell position.
   *
   * @return The y-coordinate.
   */
  int yGetter();

  /**
   * Retrieves the z-coordinate of this cell position.
   *
   * @return The z-coordinate.
   */
  int zGetter();

  /**
   * Compares this CellPosition with another object for equality.
   *
   * @param that The object with which to compare.
   * @return true if the given object represents a CellPosition
   *         equivalent to this position, false otherwise.
   */
  boolean equals(Object that);

  /**
   * Returns a hash code for this CellPosition.
   *
   * @return A hash code for this CellPosition.
   */
  int hashCode();
}
