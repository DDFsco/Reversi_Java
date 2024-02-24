package OurReversi.model;

import OurReversi.model.cell.Cell;
import OurReversi.model.cell.CellPosition;

/**
 * Represent the interface of Reversi model.
 * Defines the logic and state for a game of Reversi.
 * When BaiscModel get constructed, it initializes a board that contain a hexagon
 * shape with (2 * "size" + 1) number of rows and columns.
 * All the cells will be set to "Empty" at the beginning of the game, except the
 * middle six cells will be set to "Black" and "White" respectively.
 * All the cells' position will be base on x, y and z postion. (0,0,0) will be the
 * position of the location.
 */
public interface IReversi extends IReadOnlyModel, ModelStatusListener {

  /**
   * Initializes the Reversi game board with the given size.
   * The size determines the number of cells on the board.
   * The game cannot be reinitialized once started.
   *
   * @param size The size of the game, defined as (number of rows on the board - 1) / 2.
   * @throws IllegalStateException    if the game has already been started.
   * @throws IllegalArgumentException if the provided size is less than the minimum
   *                                  required to play.
   */
  void initializeReversi(int size);


  /**
   * Allows a player to pass their turn. If both players pass consecutively,
   * the game is considered over.
   *
   * @throws IllegalStateException if the game has not been started.
   */
  void playerPass();

  /**
   * A player chooses a cell to place their piece. The cell must be a
   * valid move according to the game rules.
   * Choosing a cell is a prerequisite for making a move.
   *
   * @param cellPosition The position of the cell where the player wants to
   *                     place their piece.
   * @throws IllegalStateException if the chosen cell is invalid for placing a
   *                               piece or the game has not been started.
   * @throws NullPointerException  if cellPosition is null.
   */
  void playerChooseCell(CellPosition cellPosition);

  /**
   * Player deselect the cell they chose.
   *
   * @throws IllegalStateException if the game has not been started.
   */
  void playerDeselectCell();

  /**
   * Finalizes the player's move, places the piece on the chosen cell,
   * and flips any flippable opponent pieces.
   * This method must be called after a valid cell has been chosen.
   *
   * @throws IllegalStateException if no cell has been chosen or the
   *                               game has not been started.
   */
  void playerMove();

  /**
   * Checks if the game is over. The game is over when there are no valid
   * moves left for either player or both players pass consecutively.
   *
   * @return true if the game is over, false otherwise.
   * @throws IllegalStateException if the game has not been started.
   */
  boolean isGameOver();

  /**
   * Number of cells that is able to flip.
   *
   * @param destCell The destination cell to validate.
   * @param player   The player who is making the move.
   * @return int Amount of cell that is able to flip.
   */
  int numAbleToFlip(Cell destCell, PlayerIdentity player);

  /**
   * Set the player with provided player identity.
   * Only user for AI strategy.
   *
   * @param identity The identity of the player.
   */
  void setCurrentPlayerIdentity(PlayerIdentity identity);

}

