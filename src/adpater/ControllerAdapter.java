package adpater;

import java.awt.Button;
import java.awt.event.KeyEvent;

import ourreversi.cell.CellPosition;
import ourreversi.cell.ICellPosition;
import ourreversi.controller.IActionListener;
import ourreversi.model.PlayerIdentity;
import theirreversi.features.PlayerActionListener;
import theirreversi.model.AxialCoordinate;

/**
 * Represent the adapter.
 * Connect from IActionListener(our controller) to PlayerActionListener(their controller).
 */
public class ControllerAdapter implements IActionListener, PlayerActionListener {

  private IActionListener ourController;

  /**
   * Constructor for ControllerAdapter.
   * Construct an PlayerActionListener.
   */
  public ControllerAdapter(IActionListener ourController) {
    this.ourController = ourController;

  }

  @Override
  public void handleKeyPressed(KeyEvent e) {
    this.ourController.handleKeyPressed(e);

  }

  @Override
  public void handleMousePressed(ICellPosition cell) {
    this.ourController.handleMousePressed(cell);
  }

  @Override
  public void runIfComputer() {
    this.ourController.runIfComputer();
  }

  @Override
  public void updateView() {
    this.ourController.updateView();
  }

  @Override
  public void letPanelShowGameOver() {
    this.ourController.letPanelShowGameOver();
  }

  @Override
  public void moveRequested(AxialCoordinate coord) {
    int x = - coord.getQ();
    int y = coord.getR() + coord.getQ();
    int z = - coord.getR();
    ICellPosition cell = new CellPosition(x, y, z);
    this.ourController.handleMousePressed(cell);

    Button button = new Button("Click");
    KeyEvent keyEnter = new KeyEvent(button, 1, 20, 0,
            KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
    this.ourController.handleKeyPressed(keyEnter);
  }

  @Override
  public void passRequested() {
    Button button = new Button("Click");
    KeyEvent keyP = new KeyEvent(button, 1, 20, 0, 80, 'p');
    this.ourController.handleKeyPressed(keyP);
  }
}
