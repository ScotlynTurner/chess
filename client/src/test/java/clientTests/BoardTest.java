package clientTests;

import chess.ChessBoard;
import org.junit.jupiter.api.Test;
import ui.DrawBoard;

public class BoardTest {
  ChessBoard board = new ChessBoard();

  @Test
  public void testDrawing() {
    board.resetBoard();
    DrawBoard drawing = new DrawBoard(board);
    System.out.println(drawing.getWhiteDrawings());
  }
}
