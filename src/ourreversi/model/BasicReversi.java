package ourreversi.model;

import java.util.ArrayList;
import java.util.List;

import ourreversi.cell.ICell;
import ourreversi.cell.ICellPosition;
import ourreversi.cell.Cell;
import ourreversi.cell.CellPosition;
import ourreversi.cell.CellStatus;

/**
 * BasicReversi class defines the logic and state for a game of Reversi.
 */
public class BasicReversi extends AbstractReversi implements IReversi {

  /**
   * Constructor for BasicReversi game.
   */
  public BasicReversi() {
    /* Creating a game state of basic reversi game.
     * Specific settings and rules will be updated with method :
     * initializeReversi(int).
     */
  }

  /**
   * Initialize the game with a given size.
   *
   * @param size : Size is number of row on the board - 1 / 2;
   *             e.g A Reversi with size 2 is having its longest row with 5 cells.
   * @return
   */
  @Override
  public void initializeReversi(int size) {
    throwGameIsEnd();
    // Check game started?
    if (gameStarted) {
      throw new IllegalStateException("Game already started");
    }
    this.size = size;
    if (size <= 1) {
      throw new IllegalArgumentException("The size need to be at least 2 " +
              "(with demeter of 5) being able to play.");
    }

    this.gameStarted = true;
    this.currentPlayerIdentity = PlayerIdentity.BLACKPLAYER;
    this.controllers = new ArrayList<>();
    initializeBoard(size);
    initializeCellRelation();
    initializeChess();
    allCellsCanGoInThisTurn();
  }


  /**
   * A helper to only initialize the board with all the cells required in the game.
   * Initialize the position of each cell, and initialize the cellStatus with EMPTY.
   */
  private void initializeBoard(int size) {
    int demeter = size * 2 + 1;

    /* create an empty board*/
    List<List<ICell>> board = new ArrayList<>();
    /* add empty rows to board*/
    for (int row = 0; row < (demeter); row++) {
      board.add(new ArrayList<>());
    }
    /* initialize the cells in each row of the board*/
    for (int rowIndex = 0; rowIndex <= size; rowIndex++) {
      List<ICell> currentRow = board.get(rowIndex);
      int oppositeRowIndex = demeter - 1 - rowIndex;
      List<ICell> oppositeRow = board.get(oppositeRowIndex);

      /* initialize the rolls include and above the middle row.*/
      int rowSize = size + 1 + rowIndex;
      /*the list yPosnInOrder will include all the positions in this row for y in order*/
      List<Integer> yPosnInOrder = new ArrayList<>();
      for (int cellIndex = 0; cellIndex < rowSize; cellIndex++) {
        int yNum = -size + cellIndex;
        yPosnInOrder.add(yNum);
      }
      /* initialize the posn of current cell above the middle row.*/
      for (int rowCellIndex = 0; rowCellIndex < rowSize; rowCellIndex++) {
        int yCurrent = yPosnInOrder.get(rowCellIndex);
        int reverseIndex = rowSize - 1 - rowCellIndex;
        int xCurrent = yPosnInOrder.get(reverseIndex);
        int zCurrent = size - rowIndex;
        ICellPosition posn = new CellPosition(xCurrent, yCurrent, zCurrent);
        currentRow.add(new Cell(posn, CellStatus.EMPTY));

        /* if it is not the middle row*/
        if (rowIndex != oppositeRowIndex) {
          CellPosition oppositePosn = new CellPosition(-xCurrent, -yCurrent, -zCurrent);
          oppositeRow.add(0, new Cell(oppositePosn, CellStatus.EMPTY));
        }
      }
    }
    /* add all cells to a single list.*/
    for (List<ICell> list : board) {
      this.allCells.addAll(list);
    }
  }


  /**
   * Initialize the hashMap of relationship between cells.
   * The hashMap include key -a cell, value -a list of cells
   */
  private void initializeCellRelation() {
    for (ICell currentCell : allCells) {
      /* current cell position divided in X Y and Z.*/
      int thisX = currentCell.getPosition().xGetter();
      int thisY = currentCell.getPosition().yGetter();
      int thisZ = currentCell.getPosition().zGetter();

      /* on left :          x+1 y-1 z=
         on left up :       x=  y-1 z+1
         on right up :      x-1 y=  z+1
         on right :         x-1 y+1 z=
         on right down :  x=  y+1 z-1
         on left down :   x+1 y=  z-1
       */
      ICell leftCell = getCellOnPosition(new CellPosition(thisX + 1, thisY - 1, thisZ));
      ICell leftUpCell = getCellOnPosition(new CellPosition(thisX, thisY - 1, thisZ + 1));
      ICell rightUpCell = getCellOnPosition(new CellPosition(thisX - 1, thisY, thisZ + 1));
      ICell rightCell = getCellOnPosition(new CellPosition(thisX - 1, thisY + 1, thisZ));
      ICell rightDownCell = getCellOnPosition(new CellPosition(thisX, thisY + 1, thisZ - 1));
      ICell leftDownCell = getCellOnPosition(new CellPosition(thisX + 1, thisY, thisZ - 1));

      /* add the surrounding cells above into the list.*/
      /* if cell exists, it is a cell; it no cell exists, it is null.*/
      List<ICell> surroundingCell = new ArrayList<>();
      surroundingCell.add(leftCell);
      surroundingCell.add(leftUpCell);
      surroundingCell.add(rightUpCell);
      surroundingCell.add(rightCell);
      surroundingCell.add(rightDownCell);
      surroundingCell.add(leftDownCell);

      /* add current cell as key, and an arrayList of all surrounding cells.*/
      this.cellRelation.put(currentCell, surroundingCell);
    }
  }

  // Initialize the chess surrounding the middle cell with position (0,0,0).
  // The left cell is White, and  each cell is having two different color chess around it.
  private void initializeChess() {
    CellPosition centerPosn = new CellPosition(0,0,0);
    ICell centerCell = getCellOnPosition(centerPosn);
    List<ICell> cellsAroundCenter = getSurroundingCells(centerCell);

    for (int i = 0 ; i < cellsAroundCenter.size() / 2; i++) {
      cellsAroundCenter.get(i * 2).changeStatus(CellStatus.WHITE);
      cellsAroundCenter.get(i * 2 + 1).changeStatus(CellStatus.BLACK);
    }
  }
}
