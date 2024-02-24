import org.junit.Assert;
import org.junit.Test;

import ourreversi.model.BasicReversi;
import ourreversi.cell.CellPosition;
import ourreversi.model.IReversi;
import ourreversi.textualview.ITextualView;
import ourreversi.textualview.TextualView;

/**
 * Test the view with rending the model.
 */
public class TestView {

  @Test
  public void testViewing() {
    IReversi model = new BasicReversi();
    model.initializeReversi(5);
    ITextualView view = new TextualView(model);


    Assert.assertEquals(view.toString(), "     _ _ _ _ _ _\n"
            + "    _ _ _ _ _ _ _\n"
            + "   _ _ _ _ _ _ _ _\n"
            + "  _ _ _ _ _ _ _ _ _\n"
            + " _ _ _ _ X O _ _ _ _\n"
            + "_ _ _ _ O _ X _ _ _ _\n"
            + " _ _ _ _ X O _ _ _ _\n"
            + "  _ _ _ _ _ _ _ _ _\n"
            + "   _ _ _ _ _ _ _ _\n"
            + "    _ _ _ _ _ _ _\n"
            + "     _ _ _ _ _ _\n");
  }

  @Test
  public void testViewingPartGame() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    ITextualView view = new TextualView(model);
    System.out.println(view);
    Assert.assertEquals(view.toString(),
            "  _ _ _\n"
                    + " _ X O _\n"
                    + "_ O _ X _\n"
                    + " _ X O _\n"
                    + "  _ _ _\n");
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    model.playerChooseCell(destPosn1);
    model.playerMove();
    System.out.println(view);
    Assert.assertEquals(view.toString(),
            "  _ _ _\n"
                    + " _ X O _\n"
                    + "_ X _ X _\n"
                    + " X X O _\n"
                    + "  _ _ _\n");
  }

  @Test
  public void testViewingWholeGame() {
    IReversi model = new BasicReversi();
    model.initializeReversi(2);
    ITextualView view = new TextualView(model);
    System.out.println(view);
    Assert.assertEquals(view.toString(),
            "  _ _ _\n"
                    + " _ X O _\n"
                    + "_ O _ X _\n"
                    + " _ X O _\n"
                    + "  _ _ _\n");
    CellPosition destPosn1 = new CellPosition(2, -1, -1);
    model.playerChooseCell(destPosn1);
    model.playerMove();
    System.out.println(view);
    Assert.assertEquals(view.toString(),
            "  _ _ _\n"
                    + " _ X O _\n"
                    + "_ X _ X _\n"
                    + " X X O _\n"
                    + "  _ _ _\n");
    CellPosition destPosn2 = new CellPosition(-1, 2, -1);
    model.playerChooseCell(destPosn2);
    model.playerMove();
    System.out.println(view);

    CellPosition destPosn3 = new CellPosition(-2, 1, 1);
    model.playerChooseCell(destPosn3);
    model.playerMove();
    System.out.println(view);

    CellPosition destPosn4 = new CellPosition(-1, -1, 2);
    model.playerChooseCell(destPosn4);
    model.playerMove();
    System.out.println(view);

    CellPosition destPosn5 = new CellPosition(1, 1, -2);
    model.playerChooseCell(destPosn5);
    model.playerMove();
    System.out.println(view);

    CellPosition destPosn6 = new CellPosition(1, -2, 1);
    model.playerChooseCell(destPosn6);
    model.playerMove();
    System.out.println(view);
    Assert.assertEquals(view.toString(),
            "  _ O _\n"
                    + " O O O X\n"
                    + "_ X _ X _\n"
                    + " X X X O\n"
                    + "  _ X _\n");
  }
}
