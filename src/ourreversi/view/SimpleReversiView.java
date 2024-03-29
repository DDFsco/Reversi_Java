package ourreversi.view;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import ourreversi.controller.IActionListener;
import ourreversi.model.IReadOnlyModel;
import ourreversi.model.IPlayer;
import ourreversi.model.Player;
import ourreversi.model.PlayerIdentity;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * This class represents a simple JFrame-based view for the Reversi game.
 * It extends JFrame and implements the IReversiView interface, providing
 * a graphical interface for the game using a JSReversiPanel to display the game state.
 */
public class SimpleReversiView extends JFrame implements IReversiView {

  public JSReversiPanel panel;
  private IReadOnlyModel model;

  /**
   * Constructor for SimpleReversiView. It initializes the JFrame with
   * a JSReversiPanel for the specified game model.
   *
   * @param model The IReversi game model used to display the current state of the game.
   */
  public SimpleReversiView(IReadOnlyModel model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.setSize(800, 800); // Set the size before calculating bounds
    JLayeredPane layer = new JLayeredPane();
    layer.setPreferredSize(new Dimension(getWidth(), getHeight())); // Set the preferred size

    JSReversiPanel panel = new JSReversiPanel(model);
    panel.setBounds(new Rectangle(getWidth(), getHeight()));
    panel.setOpaque(true); // If you want your panel to paint its background

    HintPanel hint = new HintPanel(model, panel);
    hint.setBounds(new Rectangle(getWidth(), getHeight()));
    layer.add(hint, JLayeredPane.PALETTE_LAYER);
    this.model = model;
    this.panel = panel;

    layer.add(panel, JLayeredPane.DEFAULT_LAYER);
    this.add(layer);

    this.setVisible(true); // Set the JFrame visible
  }

  /**
   * Pass the controller to panel's key press, connect them.
   */
  public void setKeyPressHandler(IActionListener controller) {
    this.panel.setKeyPressHandler(controller);
  }

  /**
   * Pass the controller to panel's mouse press, connect them.
   */
  public void setMousePressHandler(IActionListener controller) {
    this.panel.setMouseClickHandler(controller);
  }

  /**
   * Update the view.
   */
  public void update() {
    this.panel.update();
  }

  /**
   * Inform the player the game is over.
   */
  public void showGameOver(IPlayer player) {
    setTitle(player,model);

    StringBuilder message = new StringBuilder("Game Over!\n");
    // Customize this message based on the game result
    IPlayer winner = model.getWinner();
    int whiteScore = model.getScore(new Player(PlayerIdentity.WHITEPLAYER));
    int blackScore = model.getScore(new Player(PlayerIdentity.BLACKPLAYER));

    if (player.equals(winner)) {
      message.append("YOU WIN:)\n");
    } else {
      message.append("YOU LOSE:(\n");
    }

    message.append("BlackPlayer : ").append(blackScore).append("\n");
    message.append("WhitePlayer : ").append(whiteScore).append("\n");

    showMessageDialog(this, message.toString(), "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Overrides the setVisible method of JFrame. Sets the visibility of this frame.
   *
   * @param visible if true, the frame is made visible; if false, the frame is hidden.
   */
  @Override
  public void setVisible(boolean visible) {
    super.setVisible(true);
  }


  /**
   * Set the title for the given player.
   * Inform the player which turn are they.
   *
   * @param player this controller's player.
   * @param model the model this view oberseved.
   */
  public void setTitle(IPlayer player, IReadOnlyModel model) {
    StringBuilder builder = new StringBuilder();
    PlayerIdentity identity = player.getPlayerIdentity();
    if (player.getGameLevel() != null) {
      builder.append("Computer ");
    }

    if (identity == PlayerIdentity.BLACKPLAYER) {
      builder.append("Black Player");
    } else {
      builder.append("White Player");
    }

    if (!model.isGameOver()) {
      if (model.getCurrentPlayer() == player.getPlayerIdentity()) {
        builder.append(" : it's your turn!");
      } else {
        builder.append(" : wait for the other player move...");
      }
    } else {
      builder.append(" : Game is Over!");
    }

    this.setTitle(builder.toString());
  }

  /**
   * Inform the player if an invalid move been made.
   */
  public void showUnableTOMove() {
    String message = "No valid place to move!";
    showMessageDialog(this, message, "No valid place to move",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Inform the player if there is no cell been chosen.
   */
  @Override
  public void showCellError() {
    String message = "Chosen cell is invalid to move to.";
    showMessageDialog(this, message, "Chosen error",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Set the player identity.
   */
  public void setPlayer(PlayerIdentity identity) {
    panel.setPlayerIdentity(identity);
  }
}