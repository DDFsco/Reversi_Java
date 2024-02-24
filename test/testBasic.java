import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import ourreversi.cell.ICell;
import ourreversi.controller.Controller;
import ourreversi.controller.IActionListener;
import ourreversi.model.BasicReversi;
import ourreversi.model.IPlayer;
import ourreversi.model.Player;
import ourreversi.model.PlayerIdentity;
import ourreversi.cell.CellPosition;
import ourreversi.cell.CellStatus;
import ourreversi.model.IReversi;
import ourreversi.cell.Cell;
import ourreversi.view.SimpleReversiView;


/**
 * Test basic reversi initialization and basic game functions, methods work correctly.
 */
public class TestBasic {


  /**
   * Test initialize the game.
   */
  @Test
  public void testThrowWithTooSmallSize1() {
    IReversi model = new BasicReversi();
    Assert.assertThrows(IllegalArgumentException.class, () -> model.initializeReversi(1));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.initializeReversi(0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.initializeReversi(-1));
  }

  @Test
  public void testGameStartedTwice() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertThrows(IllegalStateException.class, () -> model.initializeReversi(2));
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

  @Test
  public void testThrowGameEndException() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /*End game.*/
    model.playerPass();
    model.playerPass();


    Assert.assertThrows(IllegalStateException.class, () -> model.initializeReversi(2));
    Assert.assertThrows(IllegalStateException.class, () -> model.playerMove());
    Assert.assertThrows(IllegalStateException.class, () ->
            model.playerChooseCell(new CellPosition(0, 0, 0)));
    Assert.assertThrows(IllegalStateException.class, () -> model.playerPass());
    Assert.assertThrows(IllegalStateException.class, () -> model.getNumChessAbleToFlip());
    Assert.assertThrows(IllegalStateException.class, () -> model.getChosenCell());
    Assert.assertThrows(IllegalStateException.class, () -> model.getCurrentPlayer());
  }

  /*Test initialize correctly.*/
  @Test
  public void testInitializeBoardBasic() {
    StringBuilder builder = new StringBuilder();
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    List<ICell> board = model.getAllCells();
    for (ICell cell : board) {
      builder.append("(").append(cell.getPosition().xGetter())
              .append(" ").append(cell.getPosition().yGetter())
              .append(" ").append(cell.getPosition().zGetter())
              .append(")\n");
    }
    Assert.assertEquals(19, board.size());
    Assert.assertEquals("(0 -2 2)\n" +
            "(-1 -1 2)\n" +
            "(-2 0 2)\n" +
            "(1 -2 1)\n" +
            "(0 -1 1)\n" +
            "(-1 0 1)\n" +
            "(-2 1 1)\n" +
            "(2 -2 0)\n" +
            "(1 -1 0)\n" +
            "(0 0 0)\n" +
            "(-1 1 0)\n" +
            "(-2 2 0)\n" +
            "(2 -1 -1)\n" +
            "(1 0 -1)\n" +
            "(0 1 -1)\n" +
            "(-1 2 -1)\n" +
            "(2 0 -2)\n" +
            "(1 1 -2)\n" +
            "(0 2 -2)\n", builder.toString());
  }

  @Test
  public void testInitializeCellsAboundCenter() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
        (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
    (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
        (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
            (2,0,-2) (1,1,-2) (0,2,-2)
    */
    ICell centerCell = model.getCellOnPosition(new CellPosition(0, 0, 0));
    List<ICell> allCells = model.getAllCells();

    /*test Cell Status around center cell contain correct chess.*/
    ICell cell1 = model.getCellOnPosition(new CellPosition(1, -1, 0));
    ICell cell2 = model.getCellOnPosition(new CellPosition(-1, 0, 1));
    ICell cell3 = model.getCellOnPosition(new CellPosition(0, 1, -1));
    ICell cell4 = model.getCellOnPosition(new CellPosition(0, -1, 1));
    ICell cell5 = model.getCellOnPosition(new CellPosition(-1, 1, 0));
    ICell cell6 = model.getCellOnPosition(new CellPosition(1, 0, -1));

    List<ICell> list = List.of(cell1, cell2, cell3, cell4, cell5, cell6);

    Assert.assertEquals(cell1.getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(cell2.getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(cell3.getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(cell4.getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(cell5.getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(cell6.getCellStatus(), CellStatus.BLACK);

    /*test Cell Status of all cells other than the ones around center,
     * including the cell status of center cell.*/
    for (ICell c : allCells) {
      if (!list.contains(c)) {
        Assert.assertEquals(c.getCellStatus(), CellStatus.EMPTY);
      }
    }
  }

  /*Test Getter.*/
  @Test
  public void testGetCellOnPositionBasicValidPosition() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed  cell with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)
    */

    /*InitialState*/
    CellPosition posn1 = new CellPosition(0, -2, 2);
    Cell expectedCell1 = new Cell(posn1, CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(posn1), expectedCell1);

    CellPosition posn2 = new CellPosition(-1, -1, 2);
    Cell expectedCell2 = new Cell(posn2, CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(posn2), expectedCell2);

    CellPosition posn3 = new CellPosition(0, 0, 0);
    Cell expectedCell3 = new Cell(posn3, CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(posn3), expectedCell3);

    CellPosition posn4 = new CellPosition(1, 1, -2);
    Cell expectedCell4 = new Cell(posn4, CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(posn4), expectedCell4);

    /*filled with chess.*/
    model.playerChooseCell(posn2);
    model.playerMove();
    Cell expectedCell2New = new Cell(posn2, CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(posn2), expectedCell2New);

    model.playerChooseCell(posn4);
    model.playerMove();
    Cell expectedCell4New = new Cell(posn4, CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(posn4), expectedCell4New);
  }

  @Test
  public void testGetCellOnPositionBasicInvalidPosition() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed  cell with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)
    */
    CellPosition posn1 = new CellPosition(0, -3, 2);
    Assert.assertNull(model.getCellOnPosition(posn1));

    CellPosition posn2 = new CellPosition(3, -1, 2);
    Assert.assertNull(model.getCellOnPosition(posn2));

    CellPosition posn3 = new CellPosition(1, 1, 1);
    Assert.assertNull(model.getCellOnPosition(posn3));

    CellPosition posn4 = new CellPosition(1, 1, 3);
    Assert.assertNull(model.getCellOnPosition(posn4));
  }

  @Test
  public void testValidMoveFlipSingleRowSingleCell() {
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              ()- - -
               - - -
    */
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    model.getCellOnPosition(new CellPosition(1, 0, -1)).changeStatus(CellStatus.EMPTY);
    model.getCellOnPosition(new CellPosition(0, 1, -1)).changeStatus(CellStatus.EMPTY);

    model.playerChooseCell(new CellPosition(2, -1, -1));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(1, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(
            new CellPosition(0, -1, 1)).getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(
            new CellPosition(1, -1, 0)).getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(
            new CellPosition(2, -1, -1)).getCellStatus(), CellStatus.BLACK);
  }

  @Test
  public void testMoveFlipMultipleChessOneRow() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    /*
       O - white | X - black
       x=0            y=0
           _ _ _ _ _ _
          _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _
        _ _ _ _ _ _ _ _ _
       _ _ _ _ X O _ _ _ _
      _ _ _ _ O _ X _ _ _ _  z=0
       _ _ _ O X O _ _ _ _
        _ _ O _ _ _ _ _ _
         _( )_ _ _ _ _ _
          _ _ _ _ _ _ _
           _ _ _ _ _ _

           () - O O O X
           Contains : 1. Row flip with multiple chess flipped.
     */
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(3, -1, -2)).changeStatus(CellStatus.WHITE);

    model.playerChooseCell(new CellPosition(4, -1, -3));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4

                    , -1, -3))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(3, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.BLACK);
  }

  @Test
  public void testMoveFlipMultipleChessMultiRows() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    /*
       O - white | X - black
       x=0            y=0
           _ _ _ _ _ _
          _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _
        _ _ _ _ _ _ _ _ _
       _ _ _ _ X O _ _ _ _
      _ _ _ _ O _ X _ _ _ _  z=0
       _ _ _ O X O _ _ _ _
        _ _( )O O X _ _ _
         _ _ _ _ _ _ _ _
          _ _ _ _ _ _ _
           _ _ _ _ _ _

          X O O - () - O O X
          Contains : 1. Multiple rows flip with multiple chess flipped.
     */
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, 0, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(1, 1, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(0, 2, -2)).changeStatus(CellStatus.BLACK);

    model.playerChooseCell(new CellPosition(3, -1, -2));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, 0, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, 1, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, 2, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(4, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, 0, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, 1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, 2, -2))
            .getCellStatus(), CellStatus.BLACK);
  }

  @Test
  public void testMoveFlipSomeDirectionsValidFlipSomeNot() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    /*
       O - white | X - black
       x=0            y=0
           _ _ _ _ _ _
          _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _
        _ _ _ _ _ _ _ _ _
       _ _ _ _ X O _ _ _ _
      _ _ _ _ O _ X _ _ _ _  z=0
       _ _ X O X O _ _ _ _
        _ _( )O O X _ _ _
         _ O O _ _ _ _ _
          O _ _ _ _ _ _
           _ _ _ _ _ _

                 X -
           X O O - () - O O X
                    - O O
         Contains : 1. Multiple rows flip with multiple chess flipped.
                    2. Rows start with good color O , unable to flip, end with border.
                    3. Rows start with good color O , unable to flip, end with empty.
                    4. Rows start with bad color X , unable to flip.
     */
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, 0, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(1, 1, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(0, 2, -2)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(3, 0, -3)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(4, -1, -3)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(5, -1, -4)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(3, -2, -1)).changeStatus(CellStatus.BLACK);

    model.playerChooseCell(new CellPosition(3, -1, -2));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, 0, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, 1, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, 2, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, 0, -3))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(5, -1, -4))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -2, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(4, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, 0, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, 1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, 2, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, 0, -3))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(5, -1, -4))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -2, -1))
            .getCellStatus(), CellStatus.BLACK);
  }

  @Test
  public void testValidMoveEndWithDifferentColor() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    /*
       O - white | X - black
       x=0            y=0
           _ _ _ _ _ _
          _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _
        _ _ _ _ O _ _ _ _
       _ _ _ _ X O _ _ _ _
      _ _ _ _ O _ X _ _ _ _  z=0
       _ _ _ O X O _ _ _ _
        _ _ O _ _ _ _ _ _
         _( )_ _ _ _ _ _
          _ _ _ _ _ _ _
           _ _ _ _ _ _

           () - O O O X )
           Contains : 1. Row flip with multiple chess flipped.
                      2. Row valid flip, end with an unflip chess.
     */
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(3, -1, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(-1, -1, 2)).changeStatus(CellStatus.WHITE);

    model.playerChooseCell(new CellPosition(4, -1, -3));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, -1, 2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(3, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, -1, 2))
            .getCellStatus(), CellStatus.WHITE);
  }

  @Test
  public void testValidMoveEndWithEmpty() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    /*
       O - white | X - black
       x=0            y=0
           _ _ _ _ _ _
          _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _
        _ _ _ _ _ _ _ _ _
       _ _ _ _ X O _ _ _ _
      _ _ _ _ O _ X _ _ _ _  z=0
       _ _ _ O X O _ _ _ _
        _ _ O _ _ _ _ _ _
         _( )_ _ _ _ _ _
          _ _ _ _ _ _ _
           _ _ _ _ _ _

           () - O O O X
           contains : 1. valid move, but direction end with empty e.g. (x) o o x -
     */
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(3, -1, -2)).changeStatus(CellStatus.WHITE);

    model.playerChooseCell(new CellPosition(4, -1, -3));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(3, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(3, -1, -2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(4, -1, -3))
            .getCellStatus(), CellStatus.BLACK);
  }

  @Test
  public void testValidMove() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    /*
       O - white | X - black
       x=0            y=0
           _ _ _ _ _ _
          _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _
        _ _ _ _ _ _ _ _ _
       _ _ _ _ X O _ _ _ _
      _ _ _ _ O _ X _ _ _ _  z=0
       _ _ _ O X O _ _ _ _
        _ _ O _ _ _ _ _ _
         _( )_ _ _ _ _ _
          _ _ _ _ _ _ _
           _ _ _ _ _ _

           () - O O O X
           contains : 1. valid move, but direction end with empty e.g. (x) o o x -
     */
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(3, -1, -2)).changeStatus(CellStatus.WHITE);

    Assert.assertTrue(model
            .validMove(model.getCellOnPosition(new CellPosition(4, -1, -3)),
                    PlayerIdentity.BLACKPLAYER));
  }

  @Test
  public void testValidMoveEndWithBorder() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               -( )-
              - O O -
             - O - X -
              X X O -
               - - -

             X O - () - O O X |
               contains: 1. valid move, but direction end with border e.g. (x) o o x |
    */
    model.getCellOnPosition(new CellPosition(0, -1, 1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.BLACK);

    model.playerChooseCell(new CellPosition(-1, -1, 2));
    /* Test game state before verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, -1, 2))
            .getCellStatus(), CellStatus.EMPTY);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.WHITE);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(3, model.getNumChessAbleToFlip());

    model.playerMove();
    /* Test game state after verify move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, -1, 2))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(0, -1, 1))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(1, -1, 0))
            .getCellStatus(), CellStatus.BLACK);
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1))
            .getCellStatus(), CellStatus.BLACK);
  }

  @Test
  public void testThrowWhenPlayerChooseInvalidPositionBlackPlayer() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /* Black Player's turn.*/
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(0, -2, 2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(-2, 0, 2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(-2, 2, 0)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(0, 2, -2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(2, 0, -2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(2, -2, 0)));
  }

  @Test
  public void testThrowWhenPlayerChooseInvalidPositionWhitePlayer() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /* Black Player's turn.*/
    model.playerPass();

    /* White Player's turn.*/
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(0, -2, 2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(-2, 0, 2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(-2, 2, 0)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(0, 2, -2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(2, 0, -2)));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.playerChooseCell(new CellPosition(2, -2, 0)));
  }

  /*Test correct count of numAbleToFlip.*/
  @Test
  public void testChooseCellButPassClearNumAbleToMove() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /* Black Player's turn.*/
    /* 0 choose.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    Assert.assertNull(model.getChosenCell());
    Assert.assertEquals(0, model.getNumChessAbleToFlip());

    /* 1st choose.*/
    model.playerChooseCell(new CellPosition(2, -1, -1));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1)),
            model.getChosenCell());
    Assert.assertEquals(1, model.getNumChessAbleToFlip());

    model.playerPass();
    /*BlackPlayer pass.*/
    /*WhitePlayer's turn.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertNull(model.getChosenCell());
    Assert.assertEquals(0, model.getNumChessAbleToFlip());
  }

  @Test
  public void testNumAbleToFlipStillCountCorrectAfterFewChooses() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /* Black Player's turn.*/
    /* 0 choose.*/
    Assert.assertNull(model.getChosenCell());
    Assert.assertEquals(0, model.getNumChessAbleToFlip());

    /* 1st choose.*/
    model.playerChooseCell(new CellPosition(2, -1, -1));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1)),
            model.getChosenCell());
    Assert.assertEquals(1, model.getNumChessAbleToFlip());

    /* 2nd choose. Still : numABleToFlip = 1*/
    model.playerChooseCell(new CellPosition(-1, 2, -1));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, 2, -1)),
            model.getChosenCell());
    Assert.assertEquals(1, model.getNumChessAbleToFlip());

    /* 3rd choose. Still : numABleToFlip = 1*/
    model.playerChooseCell(new CellPosition(-1, -1, 2));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, -1, 2)),
            model.getChosenCell());
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
  }

  /*Test skip.*/
  @Test
  public void testPlayerSkipOnce() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    model.playerPass();
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testPlayerSkipTwiceConsecutivelyEndGame() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /*Black : Skip.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    model.playerPass();

    /*White : Do Valid Move.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    model.playerChooseCell(new CellPosition(-1, 2, -1));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, 2, -1)),
            model.getChosenCell());
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
    model.playerMove();

    /*Black : Skip.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    model.playerPass();

    /*White : game not end.*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testPlayerSkipTwiceInconsecutive() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    model.playerPass();

    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);


    model.playerPass();

    Assert.assertThrows(IllegalStateException.class, () -> model.getCurrentPlayer());
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testNoPlayerCanMoveSkipTwiceEndGameNotFilledBoard() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              O - - X
             X - - - O
              X - - O
               X - O
    */
    for (ICell c : model.getAllCells()) {
      c.changeStatus(CellStatus.EMPTY);
    }
    model.getCellOnPosition(new CellPosition(1, -2, 1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, -2, 0)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(2, 0, -2)).changeStatus(CellStatus.BLACK);

    model.getCellOnPosition(new CellPosition(-2, 1, 1)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(-2, 2, 0)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(-1, 2, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(0, 2, -2)).changeStatus(CellStatus.WHITE);

    /*BlackPlayer*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.BLACKPLAYER);
    model.playerChooseCell(new CellPosition(0, -2, 2));
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
    model.playerMove();

    /*WhitePlayer*/
    Assert.assertEquals(model.getCurrentPlayer(), PlayerIdentity.WHITEPLAYER);
    model.playerChooseCell(new CellPosition(-2, 0, 2));
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
    model.playerMove();
    model.playerPass();
    model.playerPass();
    Assert.assertTrue(model.isGameOver());
  }


  @Test
  public void testNoPlayerCanMoveSkipTwiceEndGameFilledBoard() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               ()X X
              X O X X
             X X O X X
              X X X X
               X X X
    */
    for (ICell c : model.getAllCells()) {
      c.changeStatus(CellStatus.BLACK);
    }
    model.getCellOnPosition(new CellPosition(0, -2, 2)).changeStatus(CellStatus.EMPTY);
    model.getCellOnPosition(new CellPosition(0, -1, 1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(0, 0, 0)).changeStatus(CellStatus.WHITE);

    /*BlackPlayer.*/
    model.playerChooseCell(new CellPosition(0, -2, 2));
    model.playerMove();
    model.playerPass();
    model.playerPass();
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testOnePlayerSkipOnePlayerCantMoveEndGame() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - - - O
             O - - - X
              O - - X
               O - X
    */
    for (ICell c : model.getAllCells()) {
      c.changeStatus(CellStatus.EMPTY);
    }
    model.getCellOnPosition(new CellPosition(2, -2, 0)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, 0, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(-2, 2, 0)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(-1, 2, -1)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(0, 2, -2)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(-2, 1, 1)).changeStatus(CellStatus.WHITE);

    model.playerPass();
    model.playerPass();
    Assert.assertTrue(model.isGameOver());

  }

  @Test
  public void testOnePlayerCantMoveOnePlayerSkipEndGame() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              O - - X
             X - - X O
              X - - O
               X - O
                 ↓
               X - O
              X - - O
             X - - X O
              X - - O
               X - O
    */
    for (ICell c : model.getAllCells()) {
      c.changeStatus(CellStatus.EMPTY);
    }

    model.getCellOnPosition(new CellPosition(1, -2, 1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(2, -2, 0)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(2, -1, -1)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(2, 0, -2)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(-2, 2, 0)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(-1, 2, -1)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(0, 2, -2)).changeStatus(CellStatus.WHITE);
    model.getCellOnPosition(new CellPosition(-2, 1, 1)).changeStatus(CellStatus.BLACK);
    model.getCellOnPosition(new CellPosition(-1, 1, 0)).changeStatus(CellStatus.BLACK);

    /*BlackPlayer*/
    Assert.assertEquals(PlayerIdentity.BLACKPLAYER, model.getCurrentPlayer());
    model.playerChooseCell(new CellPosition(0, -2, 2));
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
    model.playerMove();

    /*WhitePlayer*/
    Assert.assertEquals(PlayerIdentity.WHITEPLAYER, model.getCurrentPlayer());
    model.playerChooseCell(new CellPosition(-2, 0, 2));
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
    model.playerMove();

    /*BlackPlayer - unable to move, skip*/
    /*WhitePlayer - able to move, but skip.*/
    model.playerPass();
    Assert.assertEquals(PlayerIdentity.WHITEPLAYER, model.getCurrentPlayer());
    model.playerPass();
    Assert.assertTrue(model.isGameOver());
  }


  @Test
  public void testThrowMoveBeforeChooseCell() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertThrows(IllegalStateException.class, () -> model.playerMove());
  }

  @Test
  public void testChooseCellOutBound() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /*Black player*/
    /*       O - white | X - black
              () - -
              - X O -
             - O - X -
              - X O -
               - - -
    */
    CellPosition destPosn0 = new CellPosition(0, -2, 2);
    Assert.assertThrows(IllegalArgumentException.class, () -> model.playerChooseCell(destPosn0));
  }

  @Test
  public void testMoveCorrectly() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /*Black player*/
    /*       O - white | X - black
               - - -
              - X O -
             - O - X -
             () X O -
               - - -
    */
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    ICell expectDestCell1 = model.getCellOnPosition(new CellPosition(2, -1, -1));
    model.playerChooseCell(destPosn1);
    Assert.assertEquals(model.getChosenCell(), expectDestCell1);
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
    Assert.assertEquals(CellStatus.EMPTY, expectDestCell1.getCellStatus());
    Assert.assertEquals(CellStatus.WHITE,
            model.getCellOnPosition(new CellPosition(1, -1, 0)).getCellStatus());
    model.playerMove();

    /*White player*/
    /*       O - white | X - black
               - - -
              - X O -
             - X - X -
              X X O -
               - - -
    */
    Assert.assertEquals(CellStatus.BLACK, expectDestCell1.getCellStatus());
    Assert.assertEquals(CellStatus.BLACK,
            model.getCellOnPosition(new CellPosition(1, 0, -1)).getCellStatus());
    Assert.assertEquals(PlayerIdentity.WHITEPLAYER, model.getCurrentPlayer());
    Assert.assertNull(model.getChosenCell());
  }

  @Test
  public void testGetGameSize() {
    IReversi model1 = new BasicReversi();
    model1.initializeReversi(2);
    Assert.assertEquals(2, model1.getGameSize());

    IReversi model2 = new BasicReversi();
    model2.initializeReversi(5);
    Assert.assertEquals(5, model2.getGameSize());
  }

  @Test
  public void testGetAllCells() {
    /*Test mutation.*/
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    int getAllCellsSize = model.getAllCells().size();
    model.getAllCells().clear();

    Assert.assertEquals(model.getAllCells().size(), getAllCellsSize);
  }


  @Test
  public void testGetCurrentPlayer() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertEquals(PlayerIdentity.BLACKPLAYER, model.getCurrentPlayer());
    model.playerPass();
    Assert.assertEquals(PlayerIdentity.WHITEPLAYER, model.getCurrentPlayer());
    model.playerChooseCell(new CellPosition(-1, -1, 2));
    model.playerMove();
    Assert.assertEquals(PlayerIdentity.BLACKPLAYER, model.getCurrentPlayer());
  }

  @Test
  public void testNullInputPlayerChooseCell() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertThrows(IllegalStateException.class, () -> model.playerChooseCell(null));
  }

  @Test
  public void testNullInputGetCellOnPosition() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertThrows(IllegalStateException.class, () -> model.getCellOnPosition(null));
  }

  @Test
  public void testNullInputGetScore() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertThrows(IllegalStateException.class, () -> model.getScore(null));
  }


  @Test
  public void testPlayerDeselect() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    /* All existed cell & chess with size 2:
           (0,-2,2) (-1,-1,2) (-2,0,2)
         (1,-2,1) (0,-1,1) (-1,0,1) (-2,1,1)
     (2,-2，0) (1,-1，0) (0,0,0) (-1,1，0) (-2,2,0)
         (2,-1,-1) (1,0,-1) (0,1,-1) (-1,2,-1)
              (2,0,-2) (1,1,-2) (0,2,-2)

       O - white | X - black
               - - -
              - X O -
             - O - X -
              - X O -
               - - -
    */

    /* Black Player's turn.*/
    /* 1st choose.*/
    model.playerChooseCell(new CellPosition(2, -1, -1));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(2, -1, -1)),
            model.getChosenCell());
    model.playerDeselectCell();
    Assert.assertNull(model.getChosenCell());

    /* 2nd choose. Still : numABleToFlip = 1*/
    model.playerChooseCell(new CellPosition(-1, 2, -1));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, 2, -1)),
            model.getChosenCell());
    model.playerDeselectCell();
    Assert.assertNull(model.getChosenCell());

    /* 3rd choose. Still : numABleToFlip = 1*/
    model.playerChooseCell(new CellPosition(-1, -1, 2));
    Assert.assertEquals(model.getCellOnPosition(new CellPosition(-1, -1, 2)),
            model.getChosenCell());
    model.playerDeselectCell();
    Assert.assertNull(model.getChosenCell());
  }

  @Test
  public void testGetGameSize1() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    Assert.assertEquals(2, model.getGameSize());
  }

  @Test
  public void testAllCells() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    Assert.assertEquals(19, model.getAllCells().size());
  }


  @Test
  public void testGetCellOnPosition() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    Assert.assertEquals(CellStatus.EMPTY,
            model.getCellOnPosition(new CellPosition(0, 0, 0)).getCellStatus());
  }

  @Test
  public void testGetNumChessAbleToFlip() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    model.playerChooseCell(new CellPosition(-1, -1, 2));
    Assert.assertEquals(1, model.getNumChessAbleToFlip());
  }

  @Test
  public void testGetChosenCell() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    model.playerChooseCell(new CellPosition(-1, -1, 2));

    Assert.assertEquals(new CellPosition(-1, -1, 2),
            model.getChosenCell().getPosition());
  }

  @Test
  public void testGetCurrentPlayer1() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    Assert.assertEquals(PlayerIdentity.BLACKPLAYER, model.getCurrentPlayer());
    model.playerPass();
    Assert.assertEquals(PlayerIdentity.WHITEPLAYER, model.getCurrentPlayer());
  }

  @Test
  public void testGetScore() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    IPlayer playerB = new Player(PlayerIdentity.BLACKPLAYER);
    IPlayer playerW = new Player(PlayerIdentity.WHITEPLAYER);

    Assert.assertEquals(3, model.getScore(playerB));
    Assert.assertEquals(3, model.getScore(playerW));
  }

  @Test
  public void testGetWinner() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    IActionListener controller1 = new Controller(model,
            new Player(PlayerIdentity.BLACKPLAYER),
            new SimpleReversiView(model));

    IActionListener controller2 = new Controller(model,
            new Player(PlayerIdentity.WHITEPLAYER),
            new SimpleReversiView(model));

    Assert.assertNull(model.getWinner());
  }

  @Test
  public void testNumAbleToFlip() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);

    ICell cell = model.getCellOnPosition(new CellPosition(-1, -1, 2));

    Assert.assertEquals(1, model.numAbleToFlip(cell, PlayerIdentity.BLACKPLAYER));
  }

  @Test
  public void tests() {
    IReversi mode = new BasicReversi();
    mode.printBoard();
  }
}
