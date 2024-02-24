package OurReversi.model.controller;

import java.awt.event.KeyEvent;

import OurReversi.model.IPlayer;
import OurReversi.model.IReversi;
import OurReversi.model.cell.CellPosition;
import OurReversi.model.view.IReversiView;

/**
 * Represent the controller of the Reversi.
 * Each controller is an action listener for GUI.
 * Base on the action given from View.
 * Controller modify the model.
 */
public class Controller implements IActionListener {

  private IReversi model;
  private IPlayer player;

  protected IReversiView view;


  /**
   * Construct a controller for the player.
   */
  public Controller(IReversi model, IPlayer player, IReversiView viewPlayer) {
    this.model = model;
    this.player = player;
    this.view = viewPlayer;
    viewPlayer.setKeyPressHandler(this);
    viewPlayer.setMousePressHandler(this);
    model.addController(this);
    model.addPlayer(player);
    view.setVisible(true);
    view.setTitle(player, model);

  }

  /**
   * Ask this controller update its view.
   */
  public void updateView() {
    view.setTitle(player, model);
    view.update();
  }

  /**
   * Handle the key press event.
   * Modify the model.
   */
  @Override
  public void handleKeyPressed(KeyEvent e)  {
    if (model.isGameOver()) {
      System.out.println("Game is Over.");
      return;
    }

    // when it is this player's turn, and it is a human player.
    if (this.player.getPlayerIdentity() == model.getCurrentPlayer()
            && this.player.getGameLevel() == null) {
      if (Character.toUpperCase(e.getKeyChar()) == KeyEvent.VK_P) {
        System.out.println("Pass the turn");
        model.playerPass();
      }
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        System.out.println();
        if (model.getChosenCell() != null) {
          System.out.println("Place the piece");
          model.playerMove();
        } else {
          view.showCellError();
        }
      }
    }
  }

  /**
   * Handle the mouse press event.
   * Base on the cell position provided. Modify the model.
   */
  @Override
  public void handleMousePressed(CellPosition cell) {
    if (model.isGameOver()) {
      System.out.println("Game is Over.");
      return;
    }

    // when it is this player's turn, and it is a human player.
    if (this.player.getPlayerIdentity() == model.getCurrentPlayer()
            && this.player.getGameLevel() == null) {
      if (cell == null) {
        System.out.println("Deselect cell");
        model.playerDeselectCell();
      } else {
        try {
          model.playerChooseCell(cell);
          System.out.println("Select cell");
        } catch (IllegalStateException exception) {
          System.out.println("Catch deselect");
          model.playerDeselectCell();
        }
      }
    }
  }

  /**
   * if the player is an AI player.
   * Base on AI strategy make a move.
   */
  public void runIfComputer() {
    if (!model.isGameOver() &&
            this.player.getPlayerIdentity() == this.model.getCurrentPlayer()) {
      if (this.player.getGameLevel() != null) {
        if (!this.model.getAllCellsCanGo().isEmpty()) {
          CellPosition choseCell = this.player.getGameLevel().chooseCell(this.model, this.player);
          this.model.playerChooseCell(choseCell);
          this.model.playerMove();
        } else {
          this.model.playerPass();
        }
      } else {
        if (this.model.getAllCellsCanGo().isEmpty()) {
          view.showUnableTOMove();
        }
      }
    }
  }

  /**
   * Ask this controller's view show game over state.
   * Pop up window.
   */
  public void letPanelShowGameOver() {
    this.view.showGameOver(player);
  }

}