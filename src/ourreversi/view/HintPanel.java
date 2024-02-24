package ourreversi.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Graphics;

import ourreversi.model.IReadOnlyModel;

/**
 * Represent the Hint panel for the view.
 * Hint panel is the hint decoration of the reversi view.
 * Player able to see the number of piece can flip on the selected cell.
 */
public class HintPanel extends JPanel {
  private JSReversiPanel panel;
  private IReadOnlyModel model;
  private int hintNumber;
  private boolean hasHint;
  private JButton hintButton;

  /**
   * Construct the hint panel with a hint displace button.
   */
  public HintPanel(IReadOnlyModel model, JSReversiPanel panel) {
    this.model = model;
    this.panel = panel;
    this.hasHint = false;

    // Create the hint button
    hintButton = new JButton("Show Hint");
    hintButton.addActionListener(e -> toggleHintDisplay());
    add(hintButton); // Add the button to the panel
    setOpaque(false);
    setFocusable(false);
    hintButton.setFocusable(false);
  }

  /**
   * Toggle the hint to display on the board.
   */
  private void toggleHintDisplay() {
    if (!model.isGameOver()) {
      hasHint = !hasHint;
      hintButton.setText(hasHint ? "Hide Hint" : "Show Hint");
      repaint();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (hasHint && !model.isGameOver()) {
      hintNumber = model.getNumChessAbleToFlip();
      drawHint(g);
    }
  }

  /**
   * Draw the hint only if the current player is allowed to play.
   * Play the hint on the cell.
   */
  private void drawHint(Graphics g) {
    if (panel.getPlayerIdentity().equals(model.getCurrentPlayer())) {
      String hintString = String.valueOf(hintNumber);
      if (panel.getChosenPosition() != null) {
        int x = panel.getChosenPosition().x;
        int y = panel.getChosenPosition().y;
        g.drawString(hintString, x - 2, y + 2);
      }
    }
  }
}