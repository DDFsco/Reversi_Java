package ourreversi;

import ourreversi.controller.Controller;
import ourreversi.model.BasicReversi;
import ourreversi.model.IPlayer;
import ourreversi.model.IReversi;
import ourreversi.model.PlayerIdentity;
import ourreversi.model.SquareReversi;
import ourreversi.view.IReversiView;
import ourreversi.view.SimpleReversiView;
import ourreversi.view.SquareReversiView;

/**
 * Represent the main class of the Reversi game.
 */
public final class Reversi {

  /**
   * The main runner of the reversi game.
   * Takes 4 arguments maximum.
   * - Game mode: basic , square
   * - Size: (Default: 5 for basic, 4 for square)
   * - Player1: human or AI
   * - Player2: human or AI
   */
  public static void main(String[] args) {
    // Default values
    String gameMode = args.length > 0 ? args[0].toLowerCase() : "basic";
    int gameSize = args.length > 1 ? Integer.parseInt(args[1]) : (gameMode.equals("basic") ? 5 : 4);
    ReversiCreator.GameType player1Type = args.length > 2 ?
            ReversiCreator.GameType.valueOf(args[2].toUpperCase())
            : ReversiCreator.GameType.HUMAN;
    ReversiCreator.GameType player2Type = args.length > 3 ?
            ReversiCreator.GameType.valueOf(args[3].toUpperCase())
            : ReversiCreator.GameType.HUMAN;

    IReversi model = gameMode.equals("square") ? new SquareReversi() : new BasicReversi();
    model.initializeReversi(gameSize);

    IPlayer player1 = ReversiCreator.createPlayer(player1Type, PlayerIdentity.BLACKPLAYER);
    IPlayer player2 = ReversiCreator.createPlayer(player2Type, PlayerIdentity.WHITEPLAYER);

    IReversiView viewPlayer1 = gameMode.equals("square") ?
            new SquareReversiView(model) : new SimpleReversiView(model);
    IReversiView viewPlayer2 = gameMode.equals("square") ?
            new SquareReversiView(model) : new SimpleReversiView(model);

    Controller controller1 = new Controller(model, player1, viewPlayer1);
    Controller controller2 = new Controller(model, player2, viewPlayer2);

    viewPlayer1.setVisible(true);
    viewPlayer2.setVisible(true);

    model.startGame();
  }
}