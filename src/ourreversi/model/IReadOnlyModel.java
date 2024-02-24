package ourreversi.model;

import java.util.List;

import ourreversi.cell.ICell;
import ourreversi.cell.ICellPosition;

/**
 * Represent the read only model.
 * For View to observe only.
 */
public interface IReadOnlyModel {

  /**
   * Checks if the game is over. The game is over when there are no valid
   * moves left for either player or both players pass consecutively.
   *
   * @return true if the game is over, false otherwise.
   * @throws IllegalStateException if the game has not been started.
   */
  boolean isGameOver();

  /**
   * Retrieves the current size of the game. The size is defined as
   * (number of rows on the board - 1) / 2.
   *
   * @return The size of the game.
   * @throws IllegalStateException if the game has not been started.
   */
  int getGameSize();

  /**
   * Retrieves a list of all cells on the game board. The cells are provided
   * in a specific order, from left to right
   * and top to bottom as they appear on the game board.
   *
   * @return A list of all cells on the game board.
   * @throws IllegalStateException if the game has not been started.
   */
  List<ICell> getAllCells();

  /**
   * Retrieves the list of cells surrounding a given cell. The cells are
   * provided in a clockwise order starting from
   * the direct left side cell.
   *
   * @param posn The cell position
   * @return The cell on the position
   * @throws IllegalStateException if the game has not been started.
   * @throws NullPointerException  if the provided cell is null.
   */
  ICell getCellOnPosition(ICellPosition posn);


  /**
   * Retrieves the number of opponent pieces that would be flipped if a piece
   * were placed in the currently chosen cell.
   *
   * @return The number of pieces that can be flipped.
   * @throws IllegalStateException if the game has not been started.
   */
  int getNumChessAbleToFlip();

  /**
   * Retrieves the cell that the current player has chosen for their next move.
   * This is the cell where the player will
   * place their piece if they decide to make a move.
   *
   * @return The chosen cell or null if no cell has been chosen.
   * @throws IllegalStateException if the game has not been started.
   */
  ICell getChosenCell();

  /**
   * Retrieves the current player who is to make the next move.
   *
   * @return The current player's PlayerIdentity.
   * @throws IllegalStateException if the game has not been started.
   */
  PlayerIdentity getCurrentPlayer();

  /**
   * Get the current score of the player.
   *
   * @param player One of the player.
   * @return Score of the player.
   */
  int getScore(IPlayer player);

  /**
   * Determines the winner of the game based on the current scores.
   * If the game is not over, the result is undefined.
   *
   * @return The player who has won the game or null if there is no winner.
   * @throws IllegalStateException if the game has not been started.
   */
  IPlayer getWinner();

  /**
   * Number of cells that is able to flip.
   *
   * @param destCell The destination cell to validate.
   * @param player   The player who is making the move.
   * @return int Amount of cell that is able to flip.
   */
  int numAbleToFlip(ICell destCell, PlayerIdentity player);

  /**
   * Return a reference type of all cells can go in this turn.
   *
   * @return a list of cell
   * @throws IllegalStateException if game not started yet.
   */
  List<ICell> getAllCellsCanGo();

  /**
   * Retrieves the list of cells surrounding a given cell.
   * The cells are provided in a clockwise order starting from the direct left side cell.
   *
   * @param c The cell for which to retrieve the surrounding cells.
   * @return A List of cells surrounding the given cell.
   * @throws IllegalStateException if the game has not been started.
   * @throws NullPointerException  if the provided cell is null.
   */
  List<ICell> getSurroundingCells(ICell c);

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
  boolean validMove(ICell destCell, PlayerIdentity player);

  /**
   * Start the game, run the game.
   */
  void startGame();


}


