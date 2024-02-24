package ourreversi.textualview;

import java.util.List;

import ourreversi.cell.ICell;
import ourreversi.model.IReversi;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class TextualView implements ITextualView {
  private final IReversi model;
  private final Appendable ap;

  /**
   * For testing purpose that takes only model to check the view.
   */
  public TextualView(IReversi model) {
    this.model = model;
    this.ap = null;
  }

  /**
   * For user purpose that takes append output from model changes.
   */
  public TextualView(IReversi model, Appendable appendable) {
    this.model = model;
    this.ap = appendable;

  }


  /**
   * Convert the model into string.
   * @return String
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    // Draw top half of the graph
    int size = this.model.getGameSize();
    List<ICell> cellList = this.model.getAllCells();
    printFront(size, result, cellList);
    printMiddle(size, cellList, result);
    printBottom(size, result, cellList);
    return result.toString();
  }

  // Print bottom of the grid.
  private void printBottom(int size, StringBuilder result, List<ICell> cellList) {
    // Print bottom part of grid
    for (int line = 1; line < size + 1; line++) {
      // Space at front
      result.append(" ".repeat(line));
      // Cells in between
      for (int cell = size * 2 - line + 1; cell > 0; cell--) {
        printCell(cellList, result);
        if (cell > 1) {
          result.append(" ");
        }
      }
      appendLineBreak(result);
    }
  }

  // Print middle of the grid.
  private void printMiddle(int size, List<ICell> cellList, StringBuilder result) {
    // Print middle part of grid
    for (int cell = 0; cell < size * 2 + 1; cell++) {
      printCell(cellList, result);
      if (cell < size * 2) {
        result.append(" ");
      }
    }
    appendLineBreak(result);
  }

  // Print top of the grid.
  private void printFront(int size, StringBuilder result, List<ICell> cellList) {
    for (int line = size; line > 0; line--) {
      // Space at front
      result.append(" ".repeat(line));
      // Cells in between
      for (int cell = 0; cell < size * 2 - line + 1; cell++) {
        printCell(cellList, result);
        if (cell < size * 2 - line) {
          result.append(" ");
        }
      }
      appendLineBreak(result);
    }
  }

  // Print the cell into string
  private void printCell(List<ICell> cellList, StringBuilder result) {
    switch (cellList.get(0).getCellStatus()) {
      case BLACK:
        result.append("X");
        cellList.remove(0);
        return;
      case WHITE:
        result.append("O");
        cellList.remove(0);
        return;
      case EMPTY:
        result.append("_");
        cellList.remove(0);
        return;
      default:
        return;
    }
  }

  // Append a line break at the end.
  private void appendLineBreak(StringBuilder result) {

    result.append("\n");
  }
}