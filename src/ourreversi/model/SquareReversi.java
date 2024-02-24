package ourreversi.model;

import java.util.ArrayList;
import java.util.List;

import ourreversi.cell.Cell;
import ourreversi.cell.CellStatus;
import ourreversi.cell.ICell;
import ourreversi.cell.ICellPosition;
import ourreversi.cell.SquareCellPosition;

/**
 * Represent the square game model of the reversi.
 */
public class SquareReversi extends AbstractReversi implements IReversi {

  /**
   * Construct the model of the square reversi game.
   */
  public SquareReversi() {
    // Square Reversi construct without argument.
  }

  @Override
  public void initializeReversi(int size) {
    super.throwGameIsEnd();
    // Check game started?
    if (gameStarted) {
      throw new IllegalStateException("Game already started");
    }

    this.size = size;
    if (size <= 1) {
      throw new IllegalArgumentException("The size need to be at least 2 " +
              "(with demeter of 4) being able to play.");
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
    int demeter = size * 2;

    /* create an empty board*/
    List<List<ICell>> board = new ArrayList<>();
    for (int i = 0; i < demeter; i++) {
      board.add(new ArrayList<>());
    }
    /*initialize each cell in each roll*/
    for (int rowNum = 0; rowNum < demeter; rowNum++) {
      for (int colNum = 0; colNum < demeter; colNum++) {
        int x = colNum;
        int y = rowNum;
        ICellPosition position = new SquareCellPosition(x, y);
        ICell thisCell = new Cell(position, CellStatus.EMPTY);
        board.get(rowNum).add(thisCell);
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

      /* on left :        x-1 y=
         on left-up :     x-1 y-1
         on up :          x=  y-1
         on right-up :    x+1 y-1
         on right :       x+1 y=
         on right-down :  x+1 y+1
         on down :        x=  y+1
         on left-down :   x-1 y+1
       */
      ICell leftCell = getCellOnPosition(new SquareCellPosition(thisX - 1, thisY));
      ICell leftUpCel = getCellOnPosition(new SquareCellPosition(thisX - 1, thisY - 1));
      ICell upCell = getCellOnPosition(new SquareCellPosition(thisX, thisY - 1));
      ICell rightUpCell = getCellOnPosition(new SquareCellPosition(thisX + 1, thisY - 1));
      ICell rightCell = getCellOnPosition(new SquareCellPosition(thisX + 1, thisY));
      ICell rightDownCell = getCellOnPosition(new SquareCellPosition(thisX + 1, thisY + 1));
      ICell downCell = getCellOnPosition(new SquareCellPosition(thisX, thisY + 1));
      ICell leftDownCell = getCellOnPosition(new SquareCellPosition(thisX - 1, thisY + 1));

      /* add the surrounding cells above into the list.*/
      /* if cell exists, it is a cell; it no cell exists, it is null.*/
      List<ICell> surroundingCell = new ArrayList<>();
      surroundingCell.add(leftCell);
      surroundingCell.add(leftUpCel);
      surroundingCell.add(upCell);
      surroundingCell.add(rightUpCell);
      surroundingCell.add(rightCell);
      surroundingCell.add(rightDownCell);
      surroundingCell.add(downCell);
      surroundingCell.add(leftDownCell);

      /* add current cell as key, and an arrayList of all surrounding cells.*/
      this.cellRelation.put(currentCell, surroundingCell);
    }
  }


  // Initialize the 4 chess in the middle of the board.
  // The left-down and right-up cells are White
  // The left-up and right-down cells are black
  private void initializeChess() {
    int boardSize = super.getGameSize();

    //left up : black
    ICellPosition leftUpPosn = new SquareCellPosition(boardSize - 1, boardSize - 1);
    getCellOnPosition(leftUpPosn).changeStatus(CellStatus.BLACK);
    //right down :black
    ICellPosition rightDownPosn = new SquareCellPosition(boardSize, boardSize);
    getCellOnPosition(rightDownPosn).changeStatus(CellStatus.BLACK);
    //left down : white
    ICellPosition leftDownPosn = new SquareCellPosition(boardSize - 1, boardSize);
    getCellOnPosition(leftDownPosn).changeStatus(CellStatus.WHITE);
    //right up :white
    ICellPosition rightUpPosn = new SquareCellPosition(boardSize, boardSize - 1);
    getCellOnPosition(rightUpPosn).changeStatus(CellStatus.WHITE);
  }

}
