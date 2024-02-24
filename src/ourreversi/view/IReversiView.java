package ourreversi.view;

import ourreversi.controller.IActionListener;
import ourreversi.model.IReadOnlyModel;
import ourreversi.model.IPlayer;
import ourreversi.model.PlayerIdentity;

/**
 * Represent the Reversi View, which only observe the model.
 */
public interface IReversiView {

  /**
   * Overrides the setVisible method of JFrame. Sets the visibility of this frame.
   *
   * @param visible if true, the frame is made visible; if false, the frame is hidden.
   */
  void setVisible(boolean visible);

  /**
   * Set the key press handler connect with its own controller.
   * @param controller controller control this view.
   */
  void setKeyPressHandler(IActionListener controller);

  /**
   * Set the mouse press handler connect with its own controller.
   * @param controller controller control this view.
   */
  void setMousePressHandler(IActionListener controller);

  /**
   * Update the view, repaint the world scene.
   */
  void update();

  /**
   * Set the title for the given player.
   * Inform the player which turn are they.
   *
   * @param player this controller's player.
   * @param model the model this view oberseved.
   */
  void setTitle(IPlayer player, IReadOnlyModel model);

  /**
   * Inform the player if an invalid move been made.
   */
  void showUnableTOMove();

  /**
   * Inform the player the game is over.
   */
  void showGameOver(IPlayer player);

  /**
   * Inform the player if there is no cell been chosen.
   */
  void showCellError();

  void setPlayer(PlayerIdentity identity);
}
