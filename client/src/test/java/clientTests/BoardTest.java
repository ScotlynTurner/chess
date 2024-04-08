package clientTests;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import org.junit.jupiter.api.Test;
import ui.DrawBoard;

public class BoardTest {
  ChessGame game = new ChessGame();
  ChessPosition position;

  @Test
  public void testDrawing() {
    ChessBoard board = game.getBoard();
    board.resetBoard();
    DrawBoard drawing = new DrawBoard(game.getBoard());
    position = new ChessPosition(2, 2);
    System.out.println(drawing.getWhiteDrawings(game, position));
  }
}
