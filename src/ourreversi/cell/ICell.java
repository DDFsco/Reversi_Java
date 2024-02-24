package OurReversi.model.cell;


/**
 * Represents a single cell within a Reversi game board, holding.
 * both its position and status.
 */
public interface ICell {

  /**
   * Retrieves the position of this cell on the game board.
   *
   * @return The position of the cell.
   */
  CellPosition getPosition();

  /**
   * Retrieves the status of this cell, indicating whether it's occupied.
   * by a BLACK or WHITE piece, or EMPTY.
   *
   * @return The status of the cell.
   */
  CellStatus getCellStatus();

  /**
   * Changes the status of this cell to the given status.
   *
   * @param cellStatus The new status for the cell.
   * @throws NullPointerException if cellStatus is null.
   */
  void changeStatus(CellStatus cellStatus);

  /**
   * Checks if this cell and another cell contain pieces of the same color.
   *
   * @param that The cell to compare with this cell.
   * @return true if both cells contain pieces of the same color,
   *          false if they contain different colors or any is empty.
   * @throws NullPointerException if the input cell is null.
   */
  boolean sameColorChess(Cell that);

  /**
   * Compares this cell with another object for equality based on cell.
   * status and position.
   *
   * @param that The object to compare with this cell.
   * @return true if the provided object is a cell with the same status
   *          and position as this cell, false otherwise.
   */
  boolean equals(Object that);

  /**
   * Returns a hash code for this cell based on its status and position.
   *
   * @return A hash code for this cell.
   */
  int hashCode();
}
