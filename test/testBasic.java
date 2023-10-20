import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import model.BasicReversi;
import model.IReversi;

import static java.lang.Integer.SIZE;

public class testBasic {
  @Test
  public void tests() {
    IReversi mode = new BasicReversi();
    mode.printBoard();
  }
}
