package adpater;

import ourreversi.cell.CellStatus;
import ourreversi.cell.ICell;
import ourreversi.cell.ICellPosition;
import theirreversi.model.AxialCoordinate;

/**
 * Represent the adapter connect from ICell(our cell) to AxialCoordinate(their cell).
 */
public class CellAdapter implements ICell, AxialCoordinate {

  private ICell adaptee;
  private int q;
  private int r;

  /**
   * Constructor for CellAdapter.
   * Construct an AxialCoordinate.
   */
  public CellAdapter(ICell cell) {
    this.adaptee = cell;
    this.q = - this.adaptee.getPosition().xGetter();
    this.r = - this.adaptee.getPosition().zGetter();

  }


  // Our methods
  @Override
  public ICellPosition getPosition() {
    return this.adaptee.getPosition();
  }

  @Override
  public CellStatus getCellStatus() {
    return this.adaptee.getCellStatus();
  }

  @Override
  public void changeStatus(CellStatus cellStatus) {
    this.adaptee.changeStatus(cellStatus);
  }

  @Override
  public boolean sameColorChess(ICell that) {
    return this.adaptee.sameColorChess(that);
  }


  // Their methods
  @Override
  public int getQ() {
    return this.q;
  }

  @Override
  public int getR() {
    return this.r;
  }


  // Useless method
  @Override
  public AxialCoordinate add(AxialCoordinate other) {
    return null;
  }
}
