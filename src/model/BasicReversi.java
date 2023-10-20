package model;

import java.util.List;

import static java.lang.Integer.SIZE;

public class BasicReversi implements IReversi {
  private Player[][] board;
  private static final int SIZE = 7; // You can adjust this as needed.

  public BasicReversi() {
    board = new Player[SIZE][SIZE];
    initializeBoard();
  }

  @Override
  public void initializeBoard() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        board[i][j] = Player.EMPTY;
      }
    }

    // Setting up the initial state
    board[SIZE / 2][SIZE / 2] = Player.WHITE;
    board[SIZE / 2 - 1][SIZE / 2 - 1] = Player.WHITE;
    board[SIZE / 2][SIZE / 2 - 1] = Player.BLACK;
    board[SIZE / 2 - 1][SIZE / 2] = Player.BLACK;
  }
    public void printBoard() {
      for (int i = 0; i < SIZE; i++) {
        // To make hexagonal pattern, we print spaces to offset rows.
        for (int space = 0; space < i; space++) {
          System.out.print(" ");
        }

        for (int j = 0; j < SIZE; j++) {
          switch (this.board[i][j]) {
            case BLACK:
              System.out.print("X ");
              break;
            case WHITE:
              System.out.print("O ");
              break;
            default:
              System.out.print("- ");
              break;
          }
        }
        System.out.println();
      }
    }


  @Override
  public boolean isValidMove(int x, int y, Player player) {
    return false;
  }

  @Override
  public List<int[]> getValidMoves(Player player) {
    return null;
  }

  @Override
  public void playMove(int x, int y, Player player) {

  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int[] getScore() {
    return new int[0];
  }

  // Remaining methods from IBoard to be implemented...
}
