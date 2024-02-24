package ourreversi.cell;

import static java.util.Objects.requireNonNull;

/**
 * Represents a single cell within a Reversi game board, holding both its position and status.
 */
public class Cell implements ICell {

  private ICellPosition posn;
  private CellStatus cellStatus;

  /**
   * Constructs a Cell with a given position and status.
   *
   * @param posn       The 3D position of the cell on the game board.
   * @param cellStatus The status of the cell, which can be BLACK, WHITE, or EMPTY.
   * @throws NullPointerException if either posn or cellStatus is null.
   */
  public Cell(ICellPosition posn, CellStatus cellStatus) {
    requireNonNull(posn);
    requireNonNull(cellStatus);

    this.posn = posn;
    this.cellStatus = cellStatus;
  }

  /**
   * Retrieves the position of this cell on the game board.
   *
   * @return The position of the cell.
   */
  @Override
  public ICellPosition getPosition() {
    return this.posn;
  }

  /**
   * Retrieves the status of this cell, indicating whether it's occupied by.
   * a BLACK or WHITE piece, or EMPTY.
   *
   * @return The status of the cell.
   */
  @Override
  public CellStatus getCellStatus() {
    return this.cellStatus;
  }

  /**
   * Checks if this cell and another cell contain pieces of the same color.
   *
   * @param that The cell to compare with this cell.
   * @return true if both cells contain pieces of the same color,
   *         false if they contain different colors or any is empty.
   * @throws NullPointerException if the input cell is null.
   */
  public boolean sameColorChess(ICell that) {
    requireNonNull(that);
    Cell thatCell = (Cell)that;
    if (this.cellStatus == CellStatus.EMPTY || thatCell.cellStatus == CellStatus.EMPTY) {
      return false;
    }
    return this.cellStatus == thatCell.cellStatus;
  }

  /**
   * Changes the status of this cell to the given status.
   *
   * @param cellStatus The new status for the cell.
   * @throws NullPointerException if cellStatus is null.
   */
  public void changeStatus(CellStatus cellStatus) {
    requireNonNull(cellStatus);
    this.cellStatus = cellStatus;
  }

  /**
   * Compares this cell with another object for equality based on cell status and position.
   *
   * @param that The object to compare with this cell.
   * @return true if the provided object is a cell with the same status and position
   *         as this cell, false otherwise.
   */
  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (!(that instanceof Cell)) {
      return false;
    }
    Cell thatCell = (Cell) that;
    return this.cellStatus == thatCell.cellStatus && this.posn.equals(thatCell.posn);
  }

  /**
   * Returns a hash code for this cell based on its status and position.
   *
   * @return A hash code for this cell.
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  
}

