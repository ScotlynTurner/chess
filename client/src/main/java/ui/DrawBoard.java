package ui;

import chess.ChessBoard;
import chess.ChessPosition;

public class DrawBoard {
  private ChessBoard board;

  public DrawBoard(ChessBoard board) {
    this.board = board;
  }

  private String drawNormal() {
    String drawBoard = "";
    ChessPosition position;
    for (int i = 7; i >= 0; i--) {
      for (int j = 0; j < 8; j++) {
        position = new ChessPosition(i,j);
        if (board.getPiece(position) == null) {
          drawBoard += "| ";
        } else {
          drawBoard += "|" + board.getPiece(position).toString();
        }
      }
      drawBoard += "|\n";
    }

    return "";
  }

  private String drawUpsideDown() {

    return "";
  }

  public String getWhiteDrawings() {
    return drawUpsideDown() + '\n' + drawNormal();
  }

  public String getBlackDrawings() {
    return drawNormal() + '\n' + drawUpsideDown();
  }
}
