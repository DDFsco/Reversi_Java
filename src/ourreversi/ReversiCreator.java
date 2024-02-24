package ourreversi;

import ourreversi.model.AIPlayer;
import ourreversi.model.IPlayer;
import ourreversi.model.Player;
import ourreversi.model.PlayerIdentity;

/**
 * Represent Reversi game creator.
 * Base on main argument, construct different player.
 */
public class ReversiCreator {

  /**
   * Player could be human.
   * Or Easy, medium, hard AI.
   */
  public enum GameType {
    HUMAN,
    EASY,
    MEDIUM,
    HARD,
    OTHERANY,
    OTHERMAX,

  }

  /**
   * Create the player base on provided type.
   */
  public static IPlayer createPlayer(GameType type, PlayerIdentity identity) {
    switch (type) {
      case HUMAN:
        return new Player(identity);
      case EASY:
        return new AIPlayer(identity, AIPlayer.GameLevel.Easy);
      case MEDIUM:
        return new AIPlayer(identity, AIPlayer.GameLevel.Medium);
      case HARD:
        return new AIPlayer(identity, AIPlayer.GameLevel.Hard);
      case OTHERANY:
        return new AIPlayer(identity, AIPlayer.GameLevel.OtherAny);
      case OTHERMAX:
        return new AIPlayer(identity, AIPlayer.GameLevel.OtherMax);

      default:
        throw new IllegalArgumentException("Invalid player type");
    }
  }

}
