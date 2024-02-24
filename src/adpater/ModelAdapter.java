package adpater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ourreversi.cell.CellPosition;
import ourreversi.cell.CellStatus;
import ourreversi.cell.ICell;
import ourreversi.cell.ICellPosition;
import ourreversi.model.IPlayer;
import ourreversi.model.IReadOnlyModel;
import ourreversi.model.Player;
import ourreversi.model.PlayerIdentity;
import theirreversi.features.ModelStatusListener;
import theirreversi.model.AxialCoordinate;
import theirreversi.model.PlayerColor;
import theirreversi.model.ReadonlyReversiModel;

/**
 * Represent the adapter.
 * Connect from IReadOnlyModel(our model) to ReadonlyReversiModel(their model).
 */
public class ModelAdapter implements IReadOnlyModel, ReadonlyReversiModel {

  private IReadOnlyModel adaptee;
  private List<AxialCoordinate> list = new ArrayList<>();

  /**
   * Constructor for ModelAdapter.
   * Construct an ReadonlyReversiModel.
   */
  public ModelAdapter(IReadOnlyModel model) {
    this.adaptee = model;
    for (ICell cell : model.getAllCells()) {
      AxialCoordinate newCell = new CellAdapter(cell);
      list.add(newCell);
    }

  }


  // Their methods
  @Override
  public PlayerColor getCellAt(AxialCoordinate coord) throws IllegalArgumentException {
    int x = -coord.getQ();
    int y = coord.getR() + coord.getQ();
    int z = -coord.getR();

    CellStatus cellColor = null;
    try {

      cellColor = this.adaptee.getCellOnPosition(new CellPosition(x, y, z)).getCellStatus();

    } catch (IllegalStateException e) {
      List<ICell> allCells = adaptee.getAllCells();
      for (ICell c : allCells) {
        if (c.getPosition().equals(new CellPosition(x, y, z))) {
          cellColor = c.getCellStatus();
        }
      }
    }

    if (cellColor == CellStatus.BLACK) {
      return PlayerColor.BLACK;
    } else if (cellColor == CellStatus.WHITE) {
      return PlayerColor.WHITE;
    } else {
      return PlayerColor.NONE;
    }

  }

  @Override
  public PlayerColor getCurrentPlayerColor() {
    PlayerIdentity identity = adaptee.getCurrentPlayer();

    if (identity == PlayerIdentity.BLACKPLAYER) {
      return PlayerColor.BLACK;
    } else if (identity == PlayerIdentity.WHITEPLAYER) {
      return PlayerColor.WHITE;
    } else {
      return PlayerColor.NONE;
    }
  }

  @Override
  public int getBoardSize() {
    return adaptee.getGameSize();
  }

  @Override
  public boolean isGameOver() {
    return adaptee.isGameOver();
  }

  @Override
  public int getScoreC(PlayerColor playerColor) throws IllegalArgumentException {
    PlayerIdentity identity;
    if (playerColor == PlayerColor.BLACK) {
      identity = PlayerIdentity.BLACKPLAYER;
    } else if (playerColor == PlayerColor.WHITE) {
      identity = PlayerIdentity.WHITEPLAYER;
    } else {
      identity = null;
    }

    if (identity != null) {
      return adaptee.getScore(new Player(identity));
    }
    throw new IllegalArgumentException("None player color provided.");
  }

  @Override
  public List<AxialCoordinate> getCoordinates() {
    return list;
  }

  @Override
  public List<AxialCoordinate> getCoordsToFlipFromMove(PlayerColor playerColor,
                                                       AxialCoordinate coord) {
    int x = -coord.getQ();
    int y = coord.getR() + coord.getQ();
    int z = -coord.getR();
    ICellPosition cell = new CellPosition(x, y, z);

    PlayerIdentity identity;
    if (playerColor == PlayerColor.BLACK) {
      identity = PlayerIdentity.BLACKPLAYER;
    } else if (playerColor == PlayerColor.WHITE) {
      identity = PlayerIdentity.WHITEPLAYER;
    } else {
      return new ArrayList<>();
    }

    int num = this.adaptee.numAbleToFlip(adaptee.getCellOnPosition(cell), identity);

    List<AxialCoordinate> list = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      list.add(null);
    }
    return list;
  }

  @Override
  public Map<AxialCoordinate, PlayerColor> getBoardCopy() {
    return null;
  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {
    // We do not have their model, so there is no implementation on this method.
  }

  // Our methods
  @Override
  public int getGameSize() {
    return this.adaptee.getGameSize();
  }

  @Override
  public List<ICell> getAllCells() {
    return this.adaptee.getAllCells();
  }

  @Override
  public ICell getCellOnPosition(ICellPosition posn) {
    return this.adaptee.getCellOnPosition(posn);
  }

  @Override
  public int getNumChessAbleToFlip() {
    return this.adaptee.getNumChessAbleToFlip();
  }

  @Override
  public ICell getChosenCell() {
    return this.adaptee.getChosenCell();
  }

  @Override
  public PlayerIdentity getCurrentPlayer() {
    return this.adaptee.getCurrentPlayer();
  }

  @Override
  public int getScore(IPlayer player) {
    return this.adaptee.getScore(player);
  }

  @Override
  public IPlayer getWinner() {
    return this.adaptee.getWinner();
  }

  @Override
  public int numAbleToFlip(ICell destCell, PlayerIdentity player) {
    return this.adaptee.numAbleToFlip(destCell, player);
  }

  @Override
  public List<ICell> getAllCellsCanGo() {
    return this.adaptee.getAllCellsCanGo();
  }

  @Override
  public List<ICell> getSurroundingCells(ICell c) {
    return this.adaptee.getSurroundingCells(c);
  }

  @Override
  public boolean validMove(ICell destCell, PlayerIdentity player) {
    return this.adaptee.validMove(destCell, player);
  }

  @Override
  public void startGame() {
    this.adaptee.startGame();
  }
}
