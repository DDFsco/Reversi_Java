package OurReversi.model.controller;

import java.awt.event.KeyEvent;
import OurReversi.model.cell.CellPosition;

/**
 * Represent the listener of Reversi for all feature actions.
 * The controller is able to process mouse click and key press.
 * Everytime the command been executed, will inform all listener.
 */
public interface IActionListener {

  /**
   * Handle key pressed for key event.
   *
   * @param e KeyEvent been pressed.
   */
  void handleKeyPressed(KeyEvent e);

  /**
   * Handle Mouse pressed for key event.
   *
   * @param cell Cell position been pressed.
   */
  void handleMousePressed(CellPosition cell);

  /**
   * Run if the controller is an AI player.
   * Let the AI player play a move.
   */
  void runIfComputer();

  /**
   * Update the game view.
   */
  void updateView();

  /**
   * Shows the game over scene.
   */
  void letPanelShowGameOver();
}
