package ourreversi.model;


/**
 * Represents a read-only Reversi model.
 * Designed to provide a view of the game state without allowing modifications.
 * If view trying to modify model will result in a RuntimeException.
 */
public class ReadOnlyModel extends BasicReversi implements IReadOnlyModel {

  /**
   * Constructs a new ReadOnlyModel with default settings.
   */
  public ReadOnlyModel() {
    super();
  }

  /**
   * Unsupported operation in ReadOnlyBasicBoard. Attempting to call this method will
   * result in a RuntimeException.
   *
   * @throws RuntimeException Always thrown to indicate that this operation is not supported.
   */
  @Override
  public void playerMove() {
    throw new RuntimeException("Read only should not make move");
  }

  /**
   * Unsupported operation in ReadOnlyBasicBoard. Attempting to call this method will
   * result in a RuntimeException.
   *
   * @throws RuntimeException Always thrown to indicate that this operation is not supported.
   */
  @Override
  public void playerPass() {
    throw new RuntimeException("Read only should not pass the turn");
  }

}