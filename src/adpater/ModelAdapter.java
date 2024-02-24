package Adpater;

import java.util.List;
import java.util.Map;

import OurReversi.cell.Cell;
import OurReversi.cell.CellPosition;
import OurReversi.cell.ICell;
import OurReversi.model.IPlayer;
import OurReversi.model.IReadOnlyModel;
import OurReversi.model.PlayerIdentity;
import TheirReversi.features.ModelStatusListener;
import TheirReversi.model.AxialCoordinate;
import TheirReversi.model.PlayerColor;
import TheirReversi.model.ReadonlyReversiModel;

public class ModelAdapter implements IReadOnlyModel, ReadonlyReversiModel {

  private IReadOnlyModel adaptee;

  @Override
  public PlayerColor getCellAt(AxialCoordinate coord) throws IllegalArgumentException {
    return null;
  }

  @Override
  public PlayerColor getCurrentPlayerColor() {
    return null;
  }

  @Override
  public int getBoardSize() {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getScore(PlayerColor playerColor) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public List<AxialCoordinate> getCoordinates() {
    return null;
  }

  @Override
  public List<AxialCoordinate> getCoordsToFlipFromMove(PlayerColor playerColor, AxialCoordinate coord) {
    return null;
  }

  @Override
  public Map<AxialCoordinate, PlayerColor> getBoardCopy() {
    return null;
  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {

  }

  @Override
  public int getGameSize() {
    return 0;
  }

  @Override
  public List<Cell> getAllCells() {
    return null;
  }

  @Override
  public Cell getCellOnPosition(CellPosition posn) {
    return null;
  }

  @Override
  public int getNumChessAbleToFlip() {
    return 0;
  }

  @Override
  public Cell getChosenCell() {
    return null;
  }

  @Override
  public PlayerIdentity getCurrentPlayer() {
    return null;
  }

  @Override
  public int getScore(IPlayer player) {
    return 0;
  }

  @Override
  public IPlayer getWinner() {
    return null;
  }

  @Override
  public int numAbleToFlip(Cell destCell, PlayerIdentity player) {
    return 0;
  }

  @Override
  public List<Cell> getAllCellsCanGo() {
    return null;
  }

  @Override
  public List<Cell> getSurroundingCells(Cell c) {
    return null;
  }

  @Override
  public boolean validMove(Cell destCell, PlayerIdentity player) {
    return false;
  }

  @Override
  public void startGame() {

  }
}
