
import org.junit.Assert;
import org.junit.Test;

import java.awt.Button;
import java.awt.event.KeyEvent;

import ourreversi.controller.Controller;

import ourreversi.model.AIPlayer;
import ourreversi.model.BasicReversi;
import ourreversi.model.IPlayer;
import ourreversi.model.Player;
import ourreversi.model.PlayerIdentity;
import ourreversi.cell.CellPosition;
import ourreversi.view.IReversiView;
import ourreversi.view.SimpleReversiView;

import static ourreversi.model.AIPlayer.GameLevel.Easy;

/**
 * Test Reversi controller.
 */
public class TestController {

  // Controller test: for handleMousePressed
  @Test
  public void testHandleMousePressed() {
    BasicReversi model = new BasicReversi();
    model.initializeReversi(2);
    IReversiView viewPlayer1 = new SimpleReversiView(model);
    IReversiView viewPlayer2 = new SimpleReversiView(model);
    IPlayer player1 = new Player(PlayerIdentity.BLACKPLAYER);
    IPlayer player2 = new Player(PlayerIdentity.WHITEPLAYER);
    Controller controller1 = new Controller(model, player1, viewPlayer1);
    Controller controller2 = new Controller(model, player2, viewPlayer2);
    CellPosition chosenCellPosition_1 = new CellPosition(0, 0, 0);
    CellPosition chosenCellPosition_2 = new CellPosition(-1, -1, 2);

    // Simulate mouse press.
    controller1.handleMousePressed(chosenCellPosition_1);
    // Check if the model.chosenCell() is null, as the an invalid cell been
    // selected, model does not modify

    Assert.assertEquals(model.getChosenCell(), null);

    // Check the black player choose valid move, model gets modify
    controller1.handleMousePressed(chosenCellPosition_2);
    Assert.assertEquals(model.getChosenCell().getPosition(), chosenCellPosition_2);

    // Black deselected
    controller1.handleMousePressed(chosenCellPosition_1);
    // White player try to select on black turn
    controller2.handleMousePressed(chosenCellPosition_2);
    // White player not be able to modify model, as it is not white turn.
    Assert.assertEquals(model.getChosenCell(), null);
  }

  // Check the key press function correct.
  @Test
  public void testHandleKeyPress() {
    BasicReversi model = new BasicReversi();
    model.initializeReversi(2);
    IReversiView viewPlayer1 = new SimpleReversiView(model);
    IReversiView viewPlayer2 = new SimpleReversiView(model);
    IPlayer player1 = new Player(PlayerIdentity.BLACKPLAYER);
    IPlayer player2 = new Player(PlayerIdentity.WHITEPLAYER);
    Controller controller1 = new Controller(model, player1, viewPlayer1);
    Controller controller2 = new Controller(model, player2, viewPlayer2);
    // Simulate key press event for 'P' key.
    Button button = new Button("Click");

    // Check if the model has been updated correctly
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    controller1.handleKeyPressed(new KeyEvent(button, 1, 20, 0, 80, 'p'));
    // Check if the model has been updated correctly
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    controller2.handleKeyPressed(new KeyEvent(button, 1, 25, 0, 80, 'p'));
    // Check if the model has been updated correctly

    // Both players passed, game over.
    Assert.assertTrue(model.isGameOver());
  }

  // AI play the game. Check AI work correctly.
  @Test
  public void testHandleKeyPress_AI() {
    BasicReversi model = new BasicReversi();
    model.initializeReversi(2);
    IReversiView viewPlayer1 = new SimpleReversiView(model);
    IReversiView viewPlayer2 = new SimpleReversiView(model);
    IPlayer player1 = new Player(PlayerIdentity.BLACKPLAYER);
    IPlayer player2 = new AIPlayer(PlayerIdentity.WHITEPLAYER, Easy);
    Controller controller1 = new Controller(model, player1, viewPlayer1);
    Controller controller2 = new Controller(model, player2, viewPlayer2);
    // Simulate key press event for 'P' key.
    Button button = new Button("Click");

    // Check if the model has been updated correctly
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    controller1.handleKeyPressed(new KeyEvent(button, 1, 20, 0, 80, 'p'));
    // Check if the model has been updated correctly
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
  }


}