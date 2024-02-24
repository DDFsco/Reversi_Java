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
import ourreversi.cell.ICellPosition;
import ourreversi.controller.IActionListener;
import ourreversi.model.IReadOnlyModel;
import ourreversi.cell.CellStatus;

/**
 * This class represents a custom JPanel for the Reversi game.
 * It handles rendering of the game board,
 * processing mouse events for square clicks, and key events for game controls.
 */
public class SquareJSReversiPanel extends JPanel {

  /**
   * Our view will need to display a model, so it needs to get the current cells from the model.
   */
  private final IReadOnlyModel model;
  // All the cells in the model. Only for observation
  private List<ICell> allCells;
  // All the Square been in View
  protected Map<Polygon, ICellPosition> allSquare = new HashMap<>();

  // Chosen Position been selected.
  private ICellPosition chosenPosition = null;

  // Color of thw cell with status.
  private final static Map<CellStatus, Color> cellColor = Map.of(
          CellStatus.WHITE, Color.WHITE,
          CellStatus.BLACK, Color.BLACK
  );
  private IActionListener keyPressHandler;
  private IActionListener mouseClickHandler;


  /**
   * Constructor that initializes the panel with a given Reversi game model.
   * It sets up mouse and key listeners and makes the panel focusable.
   *
   * @param model The game model to be used for displaying game state.
   */
  public SquareJSReversiPanel(IReadOnlyModel model) {
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
  private class KeyEventsListener implements KeyListener {

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
     * Method called when the mouse is pressed. Used to detect clicks on square.
     *
     * @param e The MouseEvent object containing details about the mouse event.
     */
    public void mousePressed(MouseEvent e) {
      Graphics2D g2d = (Graphics2D) getGraphics();
      Point physicalP = e.getPoint();

      for (Map.Entry<Polygon, ICellPosition> entry : allSquare.entrySet()) {
        Polygon square = entry.getKey();

        ICellPosition sqrPosition = entry.getValue();

        if (square.contains(physicalP)) {
          printSqrPosition(sqrPosition);
          handleSqrClick(square, sqrPosition);
          break; // Stop the search once we find the square.
        }
      }
    }


    /**
     * Prints the position of the clicked square.
     * This is for self-testing purpose.
     *
     * @param sqrPosn The position of the clicked square.
     */
    private void printSqrPosition(ICellPosition sqrPosn) {
      System.out.println("Square clicked at: "
              + sqrPosn.xGetter() + ", "
              + sqrPosn.yGetter() + ", ");
    }

    /**
     * Handles the logic for clicking on a square, including selecting or deselecting it.
     *
     * @param sqr         The square that was clicked.
     * @param hexPosition The position of the clicked hexagon.
     */
    private void handleSqrClick(Polygon sqr, ICellPosition hexPosition) {
      Polygon sqrToChange = null;

      if (chosenPosition == null || !chosenPosition.equals(hexPosition)) {
        for (ICell c : model.getAllCells()) {
          if (c.getPosition().equals(hexPosition) && c.getCellStatus()
                  == CellStatus.EMPTY) {
            sqrToChange = sqr;
            break;
          }
        }
      }

      if (sqrToChange != null) {
        chosenPosition = hexPosition;
        mouseClickHandler.handleMousePressed(hexPosition);
      } else if (chosenPosition != null && chosenPosition.equals(hexPosition)) {
        chosenPosition = null;
        mouseClickHandler.handleMousePressed(null);
      } // If none of the above conditions are true, chosenPosition remains unchanged

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
    this.allSquare = new HashMap<>();
    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    // Drawing square based on the coordinate system
    for (ICell cell : this.allCells) {
      ICellPosition squarePosn = cell.getPosition();

      if (cell.getCellStatus() == CellStatus.EMPTY) {
        drawSquare(g2d, squarePosn);
      } else {
        drawSquare(g2d, squarePosn);
        drawCircle(g2d, squarePosn
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
  private void drawCircle(Graphics2D g2d, ICellPosition position, Color color) {
    g2d.setColor(color);

    int width = getWidth();
    int height = getHeight();
    int gameDiameter = model.getGameSize() * 2;
    // Calculate the size of each square based on the panel size
    double squareSize = Math.min(width, height) / (double) gameDiameter;

    double topLeftX = 0;
    double topLeftY = 0;

    // Calculate the top-left corner of the square
    if (width > height) {
      int castPositionX = (width - height) / 2;
      topLeftX = (position.xGetter()) * squareSize + castPositionX;
      topLeftY = (position.yGetter()) * squareSize;
    } else {  //width < height
      int castPositionY = (height - width) / 2;
      topLeftX = (position.xGetter()) * squareSize;
      topLeftY = (position.yGetter()) * squareSize + castPositionY;
    }

    // Calculate the center of the square
    double centerX = topLeftX + squareSize / 2;
    double centerY = topLeftY + squareSize / 2;

    // Circle diameter
    double circleDiameter = squareSize * 0.8;

    // Calculate the top-left corner of the circle
    double circleX = centerX - circleDiameter / 2;
    double circleY = centerY - circleDiameter / 2;

    // Draw the circle
    g2d.fillOval((int) circleX, (int) circleY, (int) circleDiameter, (int) circleDiameter);
  }

  /**
   * Draws a square at a specified position on the board.
   *
   * @param g2d      The Graphics2D object used for drawing.
   * @param position The position where the square should be drawn.
   */
  private void drawSquare(Graphics2D g2d, ICellPosition position) {
    int width = getWidth();
    int height = getHeight();
    int gameDiameter = model.getGameSize() * 2;
    // Calculate the size of each square based on the panel size
    double squareSize = Math.min(width, height) / (double) gameDiameter;

    double squareX = 0;
    double squareY = 0;
    if (width > height) {
      int castPositionX = (width - height) / 2;
      squareX = position.xGetter() * squareSize + castPositionX;
      squareY = position.yGetter() * squareSize;
    } else { //width < height
      int castPositionY = (height - width) / 2;
      squareX = position.xGetter() * squareSize;
      squareY = position.yGetter() * squareSize + castPositionY;
    }

    // Create a square polygon
    Polygon square = new Polygon();
    square.addPoint((int) squareX, (int) squareY);
    square.addPoint((int) (squareX + squareSize), (int) squareY);
    square.addPoint((int) (squareX + squareSize), (int) (squareY + squareSize));
    square.addPoint((int) squareX, (int) (squareY + squareSize));

    // Draw the square
    g2d.setColor(Color.GRAY);
    g2d.fillPolygon(square);
    g2d.setColor(Color.BLACK);
    g2d.drawPolygon(square);

    this.allSquare.put(square, position);
    // Highlight the selected square if applicable
    if (this.chosenPosition == position) {
      g2d.setColor(Color.CYAN);
      g2d.fillPolygon(square);
      g2d.setColor(Color.BLACK);
      g2d.drawPolygon(square);
    }
  }

}