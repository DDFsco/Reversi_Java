package OurReversi.model;

import OurReversi.model.controller.Controller;
import OurReversi.model.view.IReversiView;
import OurReversi.model.view.SimpleReversiView;

/**
 * This is the main class for reversi.
 */
public final class Reversi {

  /**
   * This is the main runner for reversi, which run the game of Reversi.
   */
  public static void main(String[] args) {
    BasicReversi model = new BasicReversi();
    model.initializeReversi(5); // Or any other initial setup

    ReversiCreator.GameType player1Type = args.length > 0 ?
            ReversiCreator.GameType.valueOf(args[0].toUpperCase()) : ReversiCreator.GameType.HUMAN;
    ReversiCreator.GameType player2Type = args.length > 1 ?
            ReversiCreator.GameType.valueOf(args[1].toUpperCase()) : ReversiCreator.GameType.HUMAN;

    IPlayer player1 = ReversiCreator.createPlayer(player1Type, PlayerIdentity.BLACKPLAYER);
    IPlayer player2 = ReversiCreator.createPlayer(player2Type, PlayerIdentity.WHITEPLAYER);

    IReversiView viewPlayer1 = new SimpleReversiView(model);
    IReversiView viewPlayer2 = new SimpleReversiView(model);

    Controller controller1 = new Controller(model, player1, viewPlayer1);
    Controller controller2 = new Controller(model, player2, viewPlayer2);

    viewPlayer1.setVisible(true);
    viewPlayer2.setVisible(true);

    model.startGame();
  }

}