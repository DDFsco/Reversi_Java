package OurReversi.model;

import OurReversi.model.AIPlayer;
import OurReversi.model.IPlayer;
import OurReversi.model.Player;
import OurReversi.model.PlayerIdentity;

import static OurReversi.model.AIPlayer.GameLevel.Easy;
import static OurReversi.model.AIPlayer.GameLevel.Hard;
import static OurReversi.model.AIPlayer.GameLevel.Medium;

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
    HARD;

  }

  /**
   * Create the player base on provided type.
   */
  public static IPlayer createPlayer(GameType type, PlayerIdentity identity) {
    switch (type) {
      case HUMAN:
        return new Player(identity);
      case EASY:
        return new AIPlayer(identity, Easy);
      case MEDIUM:
        return new AIPlayer(identity, Medium);
      case HARD:
        return new AIPlayer(identity, Hard);
      default:
        throw new IllegalArgumentException("Invalid player type");
    }
  }

}
