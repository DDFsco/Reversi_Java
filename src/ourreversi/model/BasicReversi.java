package OurReversi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import OurReversi.model.controller.IActionListener;
import OurReversi.model.cell.Cell;
import OurReversi.model.cell.CellPosition;
import OurReversi.model.cell.CellStatus;

import static java.util.Objects.requireNonNull;

/**
 * BasicReversi class defines the logic and state for a game of Reversi.
 */
public class BasicReversi implements IReversi {

  // Game State related fields.
  // Game Started?
  private boolean gameStarted = false;
  // Game Over?
  private boolean gameOver = false;
  // All cells in the game. 
  // Arrange from left to right, and top to bottom.
  private final List<Cell> allCells = new ArrayList<>();
  /* hashMap with key of a cell, value of a list of surrounding cells.*/
  /* starting from the direct left side cell, go clockwise.
     e.g   1  2
         0 cell 3
           5  4    [all index in value list.]

   * Add cell for existing cells, add null for no cell.
   */
  private final HashMap<Cell, List<Cell>> cellRelation = new HashMap<>();
  // Size of the game.
  // Invaniance: Size is number of row on the board - 1 / 2;
  // Row number = size * 2 + 1
  // Column number = size * 2 + 1
  private int size;
  // Current player
  private PlayerIdentity currentPlayerIdentity;
  // Previous player passed the turn or not
  private boolean prevPlayerPassOrNot;
  // Place chess related fields. 
  private List<Cell> allCellsCanGo;
  // The cell that player chooses to place the chess.
  private Cell chosenCell;
  // After having a chosenCell to place the chess
  // Number of chess able to flip
  private int numChessAbleToFlip;
  // The index of surrounding cells that able to flip.
  // Invariance: Index from 0 to 5, starting from the left side, go clockwise.
  private List<Integer> indexOfDirectionAbleToFlip = new ArrayList<>();

  private List<IActionListener> controllers;
  private List<IPlayer> players = new ArrayList<>();

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
   * @param size : Size is number of row on the board - 1 / 2;
   *             e.g A Reversi with size 2 is having its longest row with 5 cells.
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
   * Clean out the move helper files before everytime a new cell is selected.
   * Related Fields : numChessAbleToFlip
   *                  indexOfDirectionAbleToFlip
   */
  private void clearMoveHelperFields() {
    this.chosenCell = null;
    this.numChessAbleToFlip = 0;
    this.indexOfDirectionAbleToFlip.clear();
  }

  /**
   * Check whether the destination Cell is a valid Cell for current player to put a chess.
   * If it is a valid move: - Return true.
   * - Record all the directions that contains flippable chess,
   * in field indexOfDirectionAbleToFlip, based on surrounding Cell index.
   * - Record the number of chess can be flipped,
   * in field numChessAbleToFlip.
   * If it is a valid move: - Return false.
   * - When : 1. The dest Position already have chess in it.
   * 2. It is not next to any differ color chess.
   * 3. No chess will be flipped by placing this chess.
   * @param destCell The destination cell to validate.
   * @param player   The player who is making the move.
   * @return true if the move is valid, false otherwise.
   */
  public boolean validMove(Cell destCell, PlayerIdentity player) {
    throwGameIsEnd();
    throwGameNotStartedException();
    requireNonNull(destCell);
    requireNonNull(player);
    clearMoveHelperFields();
    /* if the destination Cell is already filled with chess, return false.*/
    if (destCell.getCellStatus() != CellStatus.EMPTY) {
      return false;
    }

    this.numChessAbleToFlip = numAbleToFlip(destCell, player);

    return this.numChessAbleToFlip > 0;
  }

  /**
   * Number of cells that is able to flip.
   * @param destCell The destination cell to validate.
   * @param player   The player who is making the move.
   * @return int Amount of cell that is able to flip.
   */
  public int numAbleToFlip(Cell destCell, PlayerIdentity player) {
    List<Cell> surroundingCells = getSurroundingCells(destCell);
    int countNumFlip = 0;

    for (Cell c : surroundingCells) {
      /* Break if the surrounding cell is border.*/
      if (c == null) {
        continue;
      }
      /* Break if the surrounding cell is empty.*/
      if (c.getCellStatus().equals(CellStatus.EMPTY)) {
        continue;
      }
      /* Break if the surrounding cell is same color as player's chess.*/
      if (sameColorPlayerChess(player, c)) {
        continue;
      }
      /* Continue only if player's chess is having different color as this surrounding chess.*/
      if (!sameColorPlayerChess(player, c)) {
        /* get position index of c.*/
        int index = surroundingCells.indexOf(c);
        Cell checkAt = c;
        int ableFlipNum = 1;
        Cell nextAt = getSurroundingAt(checkAt, index);
        /* While the next cell is not null (out of bond) ,
         * and it is still different color chess as the starter chess, go to the next.
         * If the next is null,
         * or it goes to a same color chess as the beginning one, exit the <while loop>.*/
        while (nextAt != null && nextAt.sameColorChess(checkAt)) {
          checkAt = nextAt;
          nextAt = getSurroundingAt(checkAt, index);
          ableFlipNum++;
        }
        /*Break the <for loop> for this c doesn't have player's chess color till the border.*/
        if (nextAt == null) {
          continue;
        }
        /* if the next cell contains chess with same color of player's chess,
         * update the number of chess able to flip,
         * and update the direction that can be flipped.
         */
        if (sameColorPlayerChess(player, nextAt)) {
          countNumFlip += ableFlipNum;
          this.indexOfDirectionAbleToFlip.add(index);
        }
      }
    }
    return countNumFlip;
  }

  /**
   * Helper for flipCells.This method flips all the flippable chess pieces in a single row of cells.
   *
   * @param rowIndex     The index of the row to flip.
   * @param startingCell The cell from where the flipping starts.
   */
  private void flipARow(int rowIndex, Cell startingCell) {
    Cell nextCellInRow = getSurroundingAt(startingCell,rowIndex);
    while (!nextCellInRow.sameColorChess(startingCell)) {
      nextCellInRow.changeStatus(startingCell.getCellStatus());
      Cell currentCell = nextCellInRow;
      nextCellInRow = getSurroundingAt(currentCell,rowIndex);
    }
  }

  /**
   * This method handles all the flips around a chosen cell to place the chess piece.
   *
   * @param startingCell The cell from where the flipping starts.
   */
  private void flipCells(Cell startingCell) {
    throwGameNotStartedException();
    for (int rowIndex : indexOfDirectionAbleToFlip) {
      flipARow(rowIndex, startingCell);
    }
  }

  /**
   * Player passes their turn, and the turn goes to the other player.
   */
  @Override
  public void playerPass()  {
    throwGameIsEnd();
    throwGameNotStartedException();
    // If both player passed the turn, the game end
    if (prevPlayerPassOrNot) {
      setGameOver();
    }
    else {
      this.prevPlayerPassOrNot = true;
    }
    nextTurn();
  }

  /**
   * Passes the turn to the next player and updates the game state.
   */
  private void nextTurn() {
    if (this.gameOver) {
      notifyAllController();
      return;
    }
    int currentPlayerOrdinal = this.currentPlayerIdentity.ordinal();
    if (currentPlayerOrdinal < 1) {
      currentPlayerOrdinal++;
    } else {
      currentPlayerOrdinal = 0;
    }
    this.currentPlayerIdentity = PlayerIdentity.values()[currentPlayerOrdinal];

    updateGameState();
    notifyAllController();
    System.out.println("Notify");
  }

  /**
   * Player chooses a cell to place the chess piece.
   *
   * @param cellPosition The position of the cell where the player wants to place the chess piece.
   */
  @Override
  public void playerChooseCell(CellPosition cellPosition) {
    throwGameNotStartedException();
    throwGameIsEnd();
    throwNullInput(cellPosition);

    clearMoveHelperFields();
    Cell expectedChosenCell = getCellOnPosition(cellPosition);
    if (validMove(expectedChosenCell,this.currentPlayerIdentity)) {
      this.chosenCell = expectedChosenCell;
    }
    else {
      throw new IllegalStateException("The chosen cell is invalid to place any chess.");
    }
  }

  /**
   * Player deselect the cell they chose.
   */
  @Override
  public void playerDeselectCell() {
    throwGameNotStartedException();
    throwGameIsEnd();
    this.chosenCell = null;
  }

  /**
   * Places the chess piece on the chosen cell.
   */
  private void placeChess() {
    if (this.currentPlayerIdentity == PlayerIdentity.BLACKPLAYER) {
      this.chosenCell.changeStatus(CellStatus.BLACK);
    }
    if (this.currentPlayerIdentity == PlayerIdentity.WHITEPLAYER) {
      this.chosenCell.changeStatus(CellStatus.WHITE);
    }
  }

  /**
   * Confirms the player's move, places the chess piece, and flips the necessary pieces.
   */
  @Override
  public void playerMove() {
    throwGameNotStartedException();
    throwGameIsEnd();
    // Chosen cell should not be null
    if (chosenCell == null) {
      throw new IllegalStateException("No cell chosen yet.");
    }
    this.prevPlayerPassOrNot = false;
    placeChess();
    flipCells(this.chosenCell);
    nextTurn();
  }

  /**
   * Adds all possible cells that can go in this turn to the list.
   */
  protected void allCellsCanGoInThisTurn() {
    this.allCellsCanGo = new ArrayList<>();
    for (Cell c : this.allCells) {
      if (validMove(c, this.currentPlayerIdentity)) {
        this.allCellsCanGo.add(c);
      }
    }
  }

  /**
   * Checks if the current player can make a move.
   *
   * @return true if the player can move, false otherwise.
   */
  private boolean isThisPlayerMovable() {
    return !this.allCellsCanGo.isEmpty();
  }

  /**
   * A helper to only initialize the board with all the cells required in the game.
   * Initialize the position of each cell, and initialize the cellStatus with EMPTY.
   */
  private void initializeBoard(int size) {
    int demeter = size * 2 + 1;

    /* create an empty board*/
    List<List<Cell>> board = new ArrayList<>();
    /* add empty rows to board*/
    for (int row = 0; row < (demeter); row++) {
      board.add(new ArrayList<>());
    }
    /* initialize the cells in each row of the board*/
    for (int rowIndex = 0; rowIndex <= size; rowIndex++) {
      List<Cell> currentRow = board.get(rowIndex);
      int oppositeRowIndex = demeter - 1 - rowIndex;
      List<Cell> oppositeRow = board.get(oppositeRowIndex);

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
        CellPosition posn = new CellPosition(xCurrent, yCurrent, zCurrent);
        currentRow.add(new Cell(posn, CellStatus.EMPTY));

        /* if it is not the middle row*/
        if (rowIndex != oppositeRowIndex) {
          CellPosition oppositePosn = new CellPosition(-xCurrent, -yCurrent, -zCurrent);
          oppositeRow.add(0, new Cell(oppositePosn, CellStatus.EMPTY));
        }
      }
    }
    /* add all cells to a single list.*/
    for (List<Cell> list : board) {
      this.allCells.addAll(list);
    }
  }


  /**
   * Initialize the hashMap of relationship between cells.
   * The hashMap include key -a cell, value -a list of cells
   */
  private void initializeCellRelation() {
    for (Cell currentCell : allCells) {
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
      Cell leftCell = getCellOnPosition(new CellPosition(thisX + 1, thisY - 1, thisZ));
      Cell leftUpCell = getCellOnPosition(new CellPosition(thisX, thisY - 1, thisZ + 1));
      Cell rightUpCell = getCellOnPosition(new CellPosition(thisX - 1, thisY, thisZ + 1));
      Cell rightCell = getCellOnPosition(new CellPosition(thisX - 1, thisY + 1, thisZ));
      Cell rightDownCell = getCellOnPosition(new CellPosition(thisX, thisY + 1, thisZ - 1));
      Cell leftDownCell = getCellOnPosition(new CellPosition(thisX + 1, thisY, thisZ - 1));

      /* add the surrounding cells above into the list.*/
      /* if cell exists, it is a cell; it no cell exists, it is null.*/
      List<Cell> surroundingCell = new ArrayList<>();
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
    Cell centerCell = getCellOnPosition(centerPosn);
    List<Cell> cellsAroundCenter = getSurroundingCells(centerCell);

    for (int i = 0 ; i < cellsAroundCenter.size() / 2; i++) {
      cellsAroundCenter.get(i * 2).changeStatus(CellStatus.WHITE);
      cellsAroundCenter.get(i * 2 + 1).changeStatus(CellStatus.BLACK);
    }
  }

  // Return the color of chess the given player is holding.
  private CellStatus playerChessColor(PlayerIdentity player) {
    int p = player.ordinal();
    return CellStatus.values()[p];
  }

  // Return true if the player's chess color is different from the given cell's chess color.
  // Return false if their color are different or the given cell doesn't contain any chess.
  private boolean sameColorPlayerChess(PlayerIdentity player, Cell cell) {
    return playerChessColor(player) == cell.getCellStatus();
  }

  // Update the game state for current player.
  private void updateGameState() {
    allCellsCanGoInThisTurn();
    clearMoveHelperFields();
  }

  /**
   * return the size of the game.
   * @return int
   */
  public int getGameSize() {
    throwGameNotStartedException();
    return this.size;
  }

  /**
   * return the list of all cells in this game.
   * @return a list of Cell.
   */
  public List<Cell> getAllCells() {
    throwGameNotStartedException();
    List<Cell> allCells = new ArrayList<>(this.allCells);
    return allCells;
  }


  /**
   * Retrieves the list of cells surrounding a given cell.
   * The cells are provided in a clockwise order starting from the direct left side cell.
   *
   * @param c The cell for which to retrieve the surrounding cells.
   * @return A List of cells surrounding the given cell.
   * @throws IllegalStateException if the game has not been started.
   * @throws NullPointerException  if the provided cell is null.
   */
  public List<Cell> getSurroundingCells(Cell c) {
    throwGameIsEnd();
    throwGameNotStartedException();
    requireNonNull(c);
    List<Cell> copy = new ArrayList<>(this.cellRelation.get(c));
    return copy;
  }

  /**
   * get the number of cells able to flip by placing player's chess selected cell.
   * @return int
   */
  public int getNumChessAbleToFlip() {
    throwGameIsEnd();
    throwGameNotStartedException();
    return  this.numChessAbleToFlip;
  }

  /**
   * Given a surrounding index, return the surrounding cell at that position.
   * @return Cell
   */
  private Cell getSurroundingAt(Cell centerCell, int index) {
    throwGameNotStartedException();
    throwNullInput(centerCell);
    return getSurroundingCells(centerCell).get(index);
  }

  /**
   * Return the cell with the given position.
   * return null if no cell exists with the giving position.
   *
   * @return Cell or null
   */
  public Cell getCellOnPosition(CellPosition posn) {
    throwGameIsEnd();
    throwGameNotStartedException();
    throwNullInput(posn);

    for (Cell c : allCells) {
      if (c.getPosition().equals(posn)) {
        return c;
      }
    }
    return null;
  }

  /**
   * Return the player score.
   * @return Player
   */
  public int getScore(IPlayer player) {
    throwGameNotStartedException();
    throwNullInput(player);

    int score = 0;

    for (Cell cell: this.allCells) {
      if (sameColorPlayerChess(player.getPlayerIdentity(), cell)) {
        score++;
      }
    }
    return score;
  }

  /**
   * The game is over?.
   * @return boolean
   */
  public boolean isGameOver() {
    throwGameNotStartedException();
    return this.gameOver;
  }

  /**
   * Return the winner of the game.
   * @return Player if there is winner ; null if draw.
   */
  public IPlayer getWinner() {
    throwGameNotStartedException();
    int winnerScore = 0;
    IPlayer winner = null;

    for (IPlayer p : this.players) {
      int score = getScore(p);
      if (score > winnerScore) {
        winnerScore = score;
        winner = p;
      }
    }

    for (IPlayer p : this.players) {
      int score = getScore(p);
      if (winnerScore != score) {
        return winner;
      }
    }
    return null;
  }

  /**
   * Return a reference type of chosen cell.
   * @return Cell or null
   */
  public Cell getChosenCell() {
    throwGameIsEnd();
    throwGameNotStartedException();
    if (this.chosenCell == null) {
      return null;
    }
    Cell chosenCell = new Cell(this.chosenCell.getPosition(),
            this.chosenCell.getCellStatus());
    return chosenCell;
  }

  /**
   * Return a reference type of all cells can go in this turn.
   * @return a list of cell
   * @throws IllegalStateException if game not started yet.
   */
  public List<Cell> getAllCellsCanGo() {
    throwGameIsEnd();
    throwGameNotStartedException();
    List<Cell> copy = new ArrayList<>(this.allCellsCanGo);
    return copy;
  }

  /**
   * Return the current player.
   * @return Player
   */
  public PlayerIdentity getCurrentPlayer() {
    throwGameIsEnd();
    throwGameNotStartedException();
    return this.currentPlayerIdentity;
  }

  /**
   * Throw exception to using methods when game it not started.
   */
  private void throwGameNotStartedException() {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started yet!");
    }
  }

  /**
   * Throw exception to using methods when game it over.
   */
  private void throwGameIsEnd() {
    if (this.gameOver) {
      throw new IllegalStateException("Game is over!");
    }
  }

  /**
   * Throw exception if the null object been input.
   */
  private void throwNullInput(Object input) {
    if (input == null) {
      throw new IllegalStateException("Null input is not allowed.");
    }
  }

  /**
   * Set the player with provided player identity.
   * Only user for AI strategy.
   * @param identity The identity of the player.
   */
  public void setCurrentPlayerIdentity(PlayerIdentity identity) {
    this.currentPlayerIdentity = identity;
  }

  /**
   * Notify all the controller to update their view.
   * For AI player to update their move.
   */
  public void notifyAllController() {
    // Throw if there is no controller
    for (IActionListener observer : controllers) {
      observer.updateView();
    }
    for (IActionListener observer : controllers) {
      observer.runIfComputer();
    }
  }

  /**
   * When the game is over, inform all controller it is game over.
   */
  public void notifyAllControllerGameOver() {
    // Throw if there is no controller
    for (IActionListener observer : controllers) {
      observer.letPanelShowGameOver();
    }
  }

  /**
   * Add a controller to notify list.
   */
  public void addController(IActionListener controller) {
    this.controllers.add(controller);
  }

  /**
   * Notify all controller the game has started.
   */
  public void startGame() {
    for (IActionListener observer : controllers) {
      observer.runIfComputer();
    }
  }

  /**
   * Add new player to the game.
   */
  @Override
  public void addPlayer(IPlayer player) {
    this.players.add(player);
  }

  /**
   * When game over, set the game into game over state.
   */
  private void setGameOver() {
    this.gameOver = true;
    notifyAllControllerGameOver();
  }
}
