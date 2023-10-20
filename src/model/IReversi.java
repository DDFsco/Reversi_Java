package model;

import java.util.List;

public interface IReversi {
  /**
   * Enumeration for players and the empty state.
   */
  enum Player {
    BLACK, WHITE, EMPTY
  }

  /**
   * Initializes the board to the starting position.
   */
  void initializeBoard();

  /**
   * Prints the current state of the board.
   */
  void printBoard();

  /**
   * Checks if a move is valid for the given player at the specified position.
   *
   * @param x      The x-coordinate of the position.
   * @param y      The y-coordinate of the position.
   * @param player The player making the move.
   * @return true if the move is valid, false otherwise.
   */
  boolean isValidMove(int x, int y, Player player);

  /**
   * Returns a list of all valid moves for the given player.
   *
   * @param player The player to check for.
   * @return A list of valid moves as an array of integers (x, y).
   */
  List<int[]> getValidMoves(Player player);

  /**
   * Places a disc of the given player at the specified position.
   * Also handles flipping the opponent's discs.
   *
   * @param x      The x-coordinate of the position.
   * @param y      The y-coordinate of the position.
   * @param player The player making the move.
   */
  void playMove(int x, int y, Player player);

  /**
   * Checks if the game is over (i.e., no valid moves for either player).
   *
   * @return true if the game is over, false otherwise.
   */
  boolean isGameOver();

  /**
   * Returns the current score of the game.
   *
   * @return An array where the first element is the score for BLACK and the second is for WHITE.
   */
  int[] getScore();
}

