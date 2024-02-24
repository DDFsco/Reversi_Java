package ourreversi.view;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import ourreversi.cell.ICell;
import ourreversi.controller.IActionListener;
import ourreversi.model.IReadOnlyModel;
import ourreversi.cell.CellPosition;
import ourreversi.cell.CellStatus;
import ourreversi.model.PlayerIdentity;

/**
 * This class represents a custom JPanel for the Reversi game.
 * It handles rendering of the game board,
 * processing mouse events for hexagon clicks, and key events for game controls.
 */
public class JSReversiPanel extends JPanel {

  /**
   * Our view will need to display a model, so it needs to get the current cells from the model.
   */
  private final IReadOnlyModel model;
  // All the cells in the model. Only for observation
  private List<ICell> allCells;
  // All the hexagon been in View
  protected Map<Polygon, CellPosition> allHexagon = new HashMap<>();

  // Chosen Position been selected.
  private CellPosition chosenPosition = null;

  private final int HEX_SIZE = 30; // The size of the hexagon
  // Color of thw cell with status.
  private final static Map<CellStatus, Color> cellColor = Map.of(
          CellStatus.WHITE, Color.WHITE,
          CellStatus.BLACK, Color.BLACK
  );
  private IActionListener keyPressHandler;
  private IActionListener mouseClickHandler;

  private PlayerIdentity playerIdentity;


  /**
   * Constructor that initializes the panel with a given Reversi game model.
   * It sets up mouse and key listeners and makes the panel focusable.
   *
   * @param model The game model to be used for displaying game state.
   */
  public JSReversiPanel(IReadOnlyModel model) {
    this.model = Objects.requireNonNull(model);
    this.allCells = model.getAllCells();
    MouseEventsListener listener = new MouseEventsListener();
    // Key event listener
    KeyEventsListener keyEvent = new KeyEventsListener();
    this.addMouseListener(listener);
    this.addKeyListener(keyEvent);
    setFocusable(true);
    requestFocusInWindow();
  }

  /**
   * Set the key press link to controller.
   */
  public void setKeyPressHandler(IActionListener handler) {
    this.keyPressHandler = handler;
  }

  /**
   * Set the mouse click link to controller.
   */
  public void setMouseClickHandler(IActionListener handler) {
    this.mouseClickHandler = handler;
  }

  /**
   * Inner class to handle key events. This listener responds to specific key presses
   * to perform game actions such as passing the turn or placing a piece.
   */
  public class KeyEventsListener implements KeyListener {

    /**
     * No key released event.
     */
    @Override
    public void keyReleased(KeyEvent e) {
      // Override key released,
      // No implementation as there is no key released event.
    }

    /**
     * No key typed event.
     */
    @Override
    public void keyTyped(KeyEvent e) {
      // Override key Typed,
      // No implementation as there is no key typed event.
    }

    /**
     * Method called when a key is pressed. Handles game-related key presses.
     *
     * @param e The KeyEvent object containing details about the key event.
     */
    @Override
    public void keyPressed(KeyEvent e) {
      if (keyPressHandler != null) {
        System.out.println("Key pressed");
        keyPressHandler.handleKeyPressed(e);
      }
    }
  }


  /**
   * Inner class to handle mouse events. This listener responds to mouse presses
   * to interact with hexagons on the game board.
   */
  private class MouseEventsListener extends MouseInputAdapter {

    /**
     * Method called when the mouse is pressed. Used to detect clicks on hexagons.
     *
     * @param e The MouseEvent object containing details about the mouse event.
     */
    public void mousePressed(MouseEvent e) {
      Graphics2D g2d = (Graphics2D) getGraphics();
      Point physicalP = e.getPoint();

      for (Map.Entry<Polygon, CellPosition> entry : allHexagon.entrySet()) {
        Polygon hex = entry.getKey();

        CellPosition hexPosition = entry.getValue();

        if (hex.contains(physicalP)) {
          printHexPosition(hexPosition);
          handleHexagonClick(hex, hexPosition);
          return; // Stop the search once we find the hexagon
        }
      }
      chosenPosition = null;
      mouseClickHandler.handleMousePressed(null);
      repaint();
    }


    /**
     * Prints the position of the clicked hexagon.
     * This is for self-testing purpose.
     *
     * @param hexPosition The position of the clicked hexagon.
     */
    private void printHexPosition(CellPosition hexPosition) {
      System.out.println("Hexagon clicked at: "
              + hexPosition.xGetter() + ", "
              + hexPosition.yGetter() + ", "
              + hexPosition.zGetter() + ", ");
    }

    /**
     * Handles the logic for clicking on a hexagon, including selecting or deselecting it.
     *
     * @param hex         The hexagon that was clicked.
     * @param hexPosition The position of the clicked hexagon.
     */
    private void handleHexagonClick(Polygon hex, CellPosition hexPosition) {
      Polygon hexToChange = null;

      if (chosenPosition == null || !chosenPosition.equals(hexPosition)) {
        for (ICell c : model.getAllCells()) {
          if (c.getPosition().equals(hexPosition) && c.getCellStatus()
                  == CellStatus.EMPTY) {
            hexToChange = hex;
            break;
          }
        }
      }

      if (hexToChange != null) {
        chosenPosition = hexPosition;
        mouseClickHandler.handleMousePressed(hexPosition);

      } else {
        chosenPosition = null;
        mouseClickHandler.handleMousePressed(null);
      }
      // If none of the above conditions are true, chosenHex remains unchanged

      repaint();
    }
  }

  /**
   * Paints the game board component. This method is called automatically by the Swing framework
   * whenever the component needs to be redrawn.
   *
   * @param g The Graphics object to be used for painting.
   */
  @Override
  protected void paintComponent(Graphics g) {
    System.out.println("Print");
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    this.allHexagon = new HashMap<>();
    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    // Drawing hexagons based on the coordinate system
    for (ICell cell : this.allCells) {
      if (cell.getCellStatus() == CellStatus.EMPTY) {
        drawHexagon(g2d, (CellPosition) cell.getPosition());
      } else {
        drawHexagon(g2d, (CellPosition) cell.getPosition());
        drawCircle(g2d, (CellPosition) cell.getPosition()
                , cellColor.get(cell.getCellStatus()));
      }
    }
    g2d.dispose();
  }

  /**
   * Initialise the setting of view and repaint.
   */
  public void update() {
    this.allCells = model.getAllCells();
    this.chosenPosition = null;
    repaint();
  }

  /**
   * Draws a circle representing a game piece on the board.
   *
   * @param g2d      The Graphics2D object used for drawing.
   * @param position The position where the circle should be drawn.
   * @param color    The color of the circle to be drawn.
   */
  private void drawCircle(Graphics2D g2d, CellPosition position, Color color) {
    g2d.setColor(color);
    int circleX = (position.yGetter() - position.xGetter()) *
            (HEX_SIZE / 2 + 10) + getWidth() / 2 - 15;
    int circleY = (-position.zGetter() * (HEX_SIZE + 15)) + getHeight() / 2 - 15;
    g2d.fillOval(circleX, circleY, 30, 30);
  }

  /**
   * Draws a hexagon at a specified position on the board.
   *
   * @param g2d      The Graphics2D object used for drawing.
   * @param position The position where the hexagon should be drawn.
   */
  private void drawHexagon(Graphics2D g2d, CellPosition position) {
    // For a horizontal hexagon, the width
    int x = position.xGetter();
    int y = position.yGetter();
    int z = position.zGetter();
    // Adjust the center x and y based on the new hex width and height
    int hexagonX = (y - x)
            * (HEX_SIZE / 2 + 10) + getWidth() / 2;
    int hexagonY = (-z * (HEX_SIZE + 15)) + getHeight() / 2;
    Polygon hex = new Polygon();
    // The angle starts from 30 degrees for a flat top hexagon
    for (int i = 0; i < 6; i++) {
      int angle = 30 + 60 * i;
      hex.addPoint((int) (hexagonX + HEX_SIZE * Math.cos(Math.toRadians(angle))),
              (int) (hexagonY + HEX_SIZE * Math.sin(Math.toRadians(angle))));
    }
    g2d.setColor(Color.GRAY);
    g2d.fillPolygon(hex);
    g2d.setColor(Color.BLACK);
    g2d.drawPolygon(hex);
    // Add the hexagon and its CellPosition to the map
    allHexagon.put(hex, position);
    if (this.chosenPosition == position) {
      g2d.setColor(Color.CYAN);
      g2d.fillPolygon(hex);
      g2d.setColor(Color.BLACK);
      g2d.drawPolygon(hex);
    }
  }

  /**
   * Get the chosen position.
   */
  public Point getChosenPosition() {
    for (Map.Entry<Polygon, CellPosition> entry : allHexagon.entrySet()) {
      Polygon hex = entry.getKey();
      CellPosition hexPosition = entry.getValue();

      if (hexPosition.equals(this.chosenPosition)) {
        int centerX = 0;
        int centerY = 0;
        int numPoints = hex.npoints;

        for (int i = 0; i < numPoints; i++) {
          centerX += hex.xpoints[i];
          centerY += hex.ypoints[i];
        }

        centerX /= numPoints;
        centerY /= numPoints;

        return new Point(centerX, centerY);
      }
    }
    return null; // Return null if no chosen position is found
  }

  /**
   * Set the player identity.
   */
  public void setPlayerIdentity(PlayerIdentity identity) {
    this.playerIdentity = identity;
  }

  /**
   * Get the player identity.
   */
  public PlayerIdentity getPlayerIdentity() {
    return this.playerIdentity;
  }
}