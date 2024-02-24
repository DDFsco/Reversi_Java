package ourreversi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ourreversi.cell.Cell;
import ourreversi.cell.CellStatus;
import ourreversi.cell.ICell;
import ourreversi.cell.ICellPosition;
import ourreversi.controller.IActionListener;

import static java.util.Objects.requireNonNull;

/**
 * Represent the abstract class of the reversi game.
 * Holds all the foundation functionality for the reversi game.
 */
public abstract class AbstractReversi implements IReversi {

  // Game State related fields.
  // Game Started?
  protected boolean gameStarted = false;
  // Game Over?
  protected boolean gameOver = false;
  // All cells in the game.
  // Arrange from left to right, and top to bottom.
  protected final List<ICell> allCells = new ArrayList<>();
  /* hashMap with key of a cell, value of a list of surrounding cells.*/
  /* starting from the direct left side cell, go clockwise.
     e.g   1  2
         0 cell 3
           5  4    [all index in value list.]

   * Add cell for existing cells, add null for no cell.
   */
  protected final HashMap<ICell, List<ICell>> cellRelation = new HashMap<>();
  // Size of the game.
  // Invaniance: Size is number of row on the board - 1 / 2;
  // Row number = size * 2 + 1
  // Column number = size * 2 + 1
  protected int size;
  // Current player
  protected PlayerIdentity currentPlayerIdentity;
  // Previous player passed the turn or not
  protected boolean prevPlayerPassOrNot;
  // Place chess related fields.
  protected List<ICell> allCellsCanGo;
  // The cell that player chooses to place the chess.
  protected ICell chosenCell;
  // After having a chosenCell to place the chess
  // Number of chess able to flip
  protected int numChessAbleToFlip;
  // The index of surrounding cells that able to flip.
  // Invariance: Index from 0 to 5, starting from the left side, go clockwise.
  protected List<Integer> indexOfDirectionAbleToFlip = new ArrayList<>();

  protected List<IActionListener> controllers;
  protected List<IPlayer> players = new ArrayList<>();


  /**
   * Clean out the move helper files before everytime a new cell is selected.
   * Related Fields : numChessAbleToFlip
   * indexOfDirectionAbleToFlip
   */
  protected void clearMoveHelperFields() {
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
   *
   * @param destCell The destination cell to validate.
   * @param player   The player who is making the move.
   * @return true if the move is valid, false otherwise.
   */
  public boolean validMove(ICell destCell, PlayerIdentity player) {
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
   *
   * @param destCell The destination cell to validate.
   * @param player   The player who is making the move.
   * @return int Amount of cell that is able to flip.
   */
  public int numAbleToFlip(ICell destCell, PlayerIdentity player) {
    List<ICell> surroundingCells = getSurroundingCells(destCell);
    int countNumFlip = 0;

    for (ICell c : surroundingCells) {
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
        ICell checkAt = c;
        int ableFlipNum = 1;
        ICell nextAt = getSurroundingAt(checkAt, index);
        /* While the next cell is not null (out of bond) ,
         * and it is still different color chess as the starter chess, go to the next.
         * If the next is null,
         * or it goes to a same color chess as the beginning one, exit the <while loop>.*/
        while (nextAt != null && nextAt.sameColorChess((Cell) checkAt)) {
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
  protected void flipARow(int rowIndex, ICell startingCell) {
    ICell nextCellInRow = getSurroundingAt(startingCell, rowIndex);
    while (!nextCellInRow.sameColorChess((Cell) startingCell)) {
      nextCellInRow.changeStatus(startingCell.getCellStatus());
      ICell currentCell = nextCellInRow;
      nextCellInRow = getSurroundingAt(currentCell, rowIndex);
    }
  }

  /**
   * This method handles all the flips around a chosen cell to place the chess piece.
   *
   * @param startingCell The cell from where the flipping starts.
   */
  protected void flipCells(ICell startingCell) {
    throwGameNotStartedException();
    for (int rowIndex : indexOfDirectionAbleToFlip) {
      flipARow(rowIndex, startingCell);
    }
  }

  /**
   * Player passes their turn, and the turn goes to the other player.
   */
  public void playerPass() {
    throwGameIsEnd();
    throwGameNotStartedException();
    // If both player passed the turn, the game end
    if (prevPlayerPassOrNot) {
      setGameOver();
    } else {
      this.prevPlayerPassOrNot = true;
    }
    nextTurn();
  }

  /**
   * Passes the turn to the next player and updates the game state.
   */
  protected void nextTurn() {
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
  public void playerChooseCell(ICellPosition cellPosition) {
    throwGameNotStartedException();
    throwGameIsEnd();
    throwNullInput(cellPosition);

    clearMoveHelperFields();
    ICell expectedChosenCell = getCellOnPosition(cellPosition);
    if (validMove(expectedChosenCell, this.currentPlayerIdentity)) {
      this.chosenCell = expectedChosenCell;
    } else {
      throw new IllegalArgumentException("The chosen cell is invalid to place any chess.");
    }
  }

  /**
   * Player deselect the cell they chose.
   */
  public void playerDeselectCell() {
    throwGameNotStartedException();
    throwGameIsEnd();
    this.chosenCell = null;
  }

  /**
   * Places the chess piece on the chosen cell.
   */
  protected void placeChess() {
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
    for (ICell c : this.allCells) {
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
  protected boolean isThisPlayerMovable() {
    return !this.allCellsCanGo.isEmpty();
  }


  // Return the color of chess the given player is holding.
  protected CellStatus playerChessColor(PlayerIdentity player) {
    int p = player.ordinal();
    return CellStatus.values()[p];
  }

  // Return true if the player's chess color is different from the given cell's chess color.
  // Return false if their color are different or the given cell doesn't contain any chess.
  protected boolean sameColorPlayerChess(PlayerIdentity player, ICell cell) {
    return playerChessColor(player) == cell.getCellStatus();
  }

  // Update the game state for current player.
  protected void updateGameState() {
    allCellsCanGoInThisTurn();
    clearMoveHelperFields();
  }

  /**
   * return the size of the game.
   *
   * @return int
   */
  public int getGameSize() {
    throwGameNotStartedException();
    return this.size;
  }

  /**
   * return the list of all cells in this game.
   *
   * @return a list of Cell.
   */
  public List<ICell> getAllCells() {
    throwGameNotStartedException();
    List<ICell> allCells = new ArrayList<>(this.allCells);
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
  public List<ICell> getSurroundingCells(ICell c) {
    throwGameIsEnd();
    throwGameNotStartedException();
    requireNonNull(c);
    List<ICell> copy = new ArrayList<>(this.cellRelation.get(c));
    return copy;
  }

  /**
   * get the number of cells able to flip by placing player's chess selected cell.
   *
   * @return int
   */
  public int getNumChessAbleToFlip() {
    throwGameIsEnd();
    throwGameNotStartedException();
    return this.numChessAbleToFlip;
  }

  /**
   * Given a surrounding index, return the surrounding cell at that position.
   *
   * @return Cell
   */
  protected ICell getSurroundingAt(ICell centerCell, int index) {
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
  public ICell getCellOnPosition(ICellPosition posn) {
    throwGameIsEnd();
    throwGameNotStartedException();
    throwNullInput(posn);

    for (ICell c : allCells) {
      if (c.getPosition().equals(posn)) {
        return c;
      }
    }
    return null;
  }

  /**
   * Return the player score.
   *
   * @return Player
   */
  public int getScore(IPlayer player) {
    throwGameNotStartedException();
    throwNullInput(player);

    int score = 0;

    for (ICell cell : this.allCells) {
      if (sameColorPlayerChess(player.getPlayerIdentity(), cell)) {
        score++;
      }
    }
    return score;
  }

  /**
   * The game is over?.
   *
   * @return boolean
   */
  public boolean isGameOver() {
    throwGameNotStartedException();
    return this.gameOver;
  }

  /**
   * Return the winner of the game.
   *
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
   *
   * @return Cell or null
   */
  public ICell getChosenCell() {
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
   *
   * @return a list of cell
   * @throws IllegalStateException if game not started yet.
   */
  public List<ICell> getAllCellsCanGo() {
    throwGameIsEnd();
    throwGameNotStartedException();
    List<ICell> copy = new ArrayList<>(this.allCellsCanGo);
    return copy;
  }

  /**
   * Return the current player.
   *
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
  protected void throwGameNotStartedException() {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game not started yet!");
    }
  }

  /**
   * Throw exception to using methods when game it over.
   */
  protected void throwGameIsEnd() {
    if (this.gameOver) {
      throw new IllegalStateException("Game is over!");
    }
  }

  /**
   * Throw exception if the null object been input.
   */
  protected void throwNullInput(Object input) {
    if (input == null) {
      throw new IllegalStateException("Null input is not allowed.");
    }
  }

  /**
   * Set the player with provided player identity.
   * Only user for AI strategy.
   *
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
      System.out.println("Print controller");
      System.out.println(this.controllers);
      System.out.println(observer);
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
  public void addPlayer(IPlayer player) {
    this.players.add(player);
  }

  /**
   * When game over, set the game into game over state.
   */
  protected void setGameOver() {
    this.gameOver = true;
    notifyAllControllerGameOver();
  }
}
