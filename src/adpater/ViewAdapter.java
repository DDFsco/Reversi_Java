package adpater;

import ourreversi.model.IPlayer;
import ourreversi.model.IReadOnlyModel;
import ourreversi.controller.IActionListener;
import ourreversi.model.PlayerIdentity;
import ourreversi.view.IReversiView;
import theirreversi.features.PlayerActionListener;
import theirreversi.view.ReversiView;

/**
 * Represent the adapter.
 * Connect from ReversiView(their view) to IReversiView(our view).
 */
public class ViewAdapter implements IReversiView, ReversiView {
  //private IReversiView ourView;
  private ReversiView theirView; // Reference to the client's view
  private IReadOnlyModel model;

  /**
   * Constructor for ViewAdapter.
   * Construct an IReversiView.
   */
  public ViewAdapter(ReversiView theirView, IReadOnlyModel model) {
    this.theirView = theirView;
    this.model = model;
  }

  // Our methods
  @Override
  public void setVisible(boolean visible) {
    theirView.makeVisible();
  }

  @Override
  public void setKeyPressHandler(IActionListener controller) {
    // Their view do not have handler usage.
  }

  @Override
  public void setMousePressHandler(IActionListener controller) {
    // Their view do not have handler usage.
  }

  @Override
  public void update() {
    theirView.refresh();
  }

  @Override
  public void setTitle(IPlayer player, IReadOnlyModel model) {
    try {
      theirView.setPlayerTurn(player.getPlayerIdentity()
              == model.getCurrentPlayer());
    } catch (IllegalStateException e) {
      this.showGameOver(player);
    }
  }

  @Override
  public void showUnableTOMove() {
    theirView.displayErrorMessage("Unable to move");
  }

  @Override
  public void showGameOver(IPlayer player) {
    theirView.setGameOver(player.equals(model.getWinner()));
  }

  @Override
  public void showCellError() {
    theirView.displayErrorMessage("Error cell chosen");
  }

  @Override
  public void setPlayer(PlayerIdentity identity) {

  }


  // Their methods
  @Override
  public void makeVisible() {
    theirView.makeVisible();
  }

  @Override
  public void refresh() {
    theirView.refresh();
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    theirView.addPlayerActionListener(listener);
  }

  @Override
  public void displayErrorMessage(String message) {
    theirView.displayErrorMessage(message);
  }

  @Override
  public void setGameOver(boolean isWinner) {
    theirView.setGameOver(isWinner);
  }

  @Override
  public void setPlayerTurn(boolean isPlayerTurn) {
    theirView.setPlayerTurn(isPlayerTurn);
  }


}