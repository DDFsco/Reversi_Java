package ourreversi.model;

import ourreversi.controller.IActionListener;

/**
 * Represent the model listener triggered when model gets modified.
 * Inform all controller model has changed.
 */
public interface ModelStatusListener {

  /**
   * Add the player into model.
   *
   * @param player player for the game.
   */
  void addPlayer(IPlayer player);

  /**
   * Add the controller to listener list.
   *
   */
  void addController(IActionListener controller);

  /**
   * When the game is over, inform all controller it is game over.
   */
  void notifyAllControllerGameOver();

  /**
   * Notify all the controller to update their view.
   * For AI player to update their move.
   */
  void notifyAllController();
}
