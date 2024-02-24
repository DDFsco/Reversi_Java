import ourreversi.cell.ICell;
import ourreversi.model.BasicReversi;
import ourreversi.model.PlayerIdentity;
import ourreversi.cell.CellPosition;
import ourreversi.cell.CellStatus;
import ourreversi.model.IReversi;
import ourreversi.model.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test the model's functionality.
 */
public class TestBro {
  BasicReversi game;

  @Before
  public void setUp() {
    game = new BasicReversi();
  }

  @Test
  public void testInitializeReversi() {
    Assert.assertThrows(IllegalArgumentException.class, () -> game.initializeReversi(1));

    game.initializeReversi(2);
    ICell centerCell = game.getCellOnPosition(new CellPosition(0, 0, 0));
    Assert.assertNotNull(centerCell);
    Assert.assertEquals(CellStatus.EMPTY, centerCell.getCellStatus());
  }

  // To ensure that the game initializes correctly for valid sizes.
  @Test
  public void testInitializeForValidSize() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertNotNull(model.getAllCells());
    Assert.assertEquals(19, model.getAllCells().size());
    // Ensure the board has the correct number of cells for size 2
  }

  // To ensure that the game state is correctly set after initialization.
  @Test
  public void testGameStateAfterInitialization() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertFalse(model.isGameOver());
    // Game should not be over right after initialization
    Assert.assertEquals(PlayerIdentity.BLACKPLAYER, model.getCurrentPlayer());
    // The first player should be BLACK by default
  }

  // To ensure that passing the turn changes the current player.
  @Test
  public void testPlayerPass() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    model.playerPass(); // Current player should pass the turn
    Assert.assertEquals(PlayerIdentity.WHITEPLAYER, model.getCurrentPlayer());
    // The next player should be WHITE
  }

  // To ensure that the game ends after consecutive passes.
  @Test
  public void testConsecutivePlayerPassEndsGame() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    model.playerPass(); // First player passes
    model.playerPass(); // Second player passes
    Assert.assertTrue(model.isGameOver()); // Game should be over after two consecutive passes
  }

  // To ensure that the cell status is updated correctly after a move.
  @Test
  public void testCellStatusAfterValidMove() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    CellPosition validPos = new CellPosition(-1, 2, -1);
    model.playerChooseCell(validPos);
    model.playerMove();
    Assert.assertEquals(CellStatus.BLACK, model.getCellOnPosition(validPos).getCellStatus());
  }

  @Test
  public void testgetScore() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    model.playerChooseCell(destPosn1);
    model.playerMove();
    CellPosition destPosn2 = new CellPosition(-1, 2, -1);
    model.playerChooseCell(destPosn2);
    model.playerMove();
    CellPosition destPosn3 = new CellPosition(-2, 1, 1);
    model.playerChooseCell(destPosn3);
    model.playerMove();
    CellPosition destPosn4 = new CellPosition(-1, -1, 2);
    model.playerChooseCell(destPosn4);
    model.playerMove();
    Assert.assertEquals(model.getScore(new Player(PlayerIdentity.WHITEPLAYER)), 5);
    Assert.assertEquals(model.getScore(new Player(PlayerIdentity.BLACKPLAYER)), 5);
  }

  @Test
  public void testgetWinnerDraw() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    model.playerChooseCell(destPosn1);
    model.playerMove();
    CellPosition destPosn2 = new CellPosition(-1, 2, -1);
    model.playerChooseCell(destPosn2);
    model.playerMove();
    CellPosition destPosn3 = new CellPosition(-2, 1, 1);
    model.playerChooseCell(destPosn3);
    model.playerMove();
    CellPosition destPosn4 = new CellPosition(-1, -1, 2);
    model.playerChooseCell(destPosn4);
    model.playerMove();
    Assert.assertEquals(model.getScore(new Player(PlayerIdentity.WHITEPLAYER)), 5);
    Assert.assertEquals(model.getScore(new Player(PlayerIdentity.BLACKPLAYER)), 5);

    Assert.assertNull(model.getWinner());
  }

  @Test
  public void testgetWinnerWin() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    model.playerChooseCell(destPosn1);
    model.playerMove();
    CellPosition destPosn2 = new CellPosition(-1, 2, -1);
    model.playerChooseCell(destPosn2);
    model.playerMove();
    CellPosition destPosn3 = new CellPosition(-2, 1, 1);
    model.playerChooseCell(destPosn3);
    model.playerMove();
    CellPosition destPosn4 = new CellPosition(-1, -1, 2);
    model.playerChooseCell(destPosn4);
    model.playerMove();
    CellPosition destPosn5 = new CellPosition(1, 1, -2);
    model.playerChooseCell(destPosn5);
    model.playerMove();
    CellPosition destPosn6 = new CellPosition(1, -2, 1);
    model.playerChooseCell(destPosn6);
    model.playerMove();
    Assert.assertEquals(model.getScore(new Player(PlayerIdentity.WHITEPLAYER)), 5);
    Assert.assertEquals(model.getScore((new Player(PlayerIdentity.BLACKPLAYER))), 7);
  }

  // GameOver not able to make move.
  @Test
  public void testGameOver() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    model.playerChooseCell(destPosn1);
    model.playerMove();
    CellPosition destPosn2 = new CellPosition(-1, 2, -1);
    model.playerChooseCell(destPosn2);
    model.playerMove();
    CellPosition destPosn3 = new CellPosition(-2, 1, 1);
    model.playerChooseCell(destPosn3);
    model.playerMove();
    CellPosition destPosn4 = new CellPosition(-1, -1, 2);
    model.playerChooseCell(destPosn4);
    model.playerMove();
    CellPosition destPosn5 = new CellPosition(1, 1, -2);
    model.playerChooseCell(destPosn5);
    model.playerMove();

    model.playerPass();
    model.playerPass();

    Assert.assertTrue(model.isGameOver());
    Assert.assertThrows(IllegalStateException.class, () -> model.playerMove());
  }


  @Test
  public void testThrowGameNotStarted() {
    IReversi model = new BasicReversi();
    Assert.assertThrows(IllegalStateException.class, () -> model.playerMove());
    Assert.assertThrows(IllegalStateException.class, () ->
            model.playerChooseCell(new CellPosition(0, 0, 0)));
    Assert.assertThrows(IllegalStateException.class, () -> model.playerPass());
    Assert.assertThrows(IllegalStateException.class, () -> model.getGameSize());
    Assert.assertThrows(IllegalStateException.class, () -> model.getAllCells());
    Assert.assertThrows(IllegalStateException.class, () ->
            model.getCellOnPosition(new CellPosition(0, 0, 0)));
    Assert.assertThrows(IllegalStateException.class, () -> model.getNumChessAbleToFlip());
    Assert.assertThrows(IllegalStateException.class, () -> model.getChosenCell());
    Assert.assertThrows(IllegalStateException.class, () -> model.getCurrentPlayer());
  }
}
