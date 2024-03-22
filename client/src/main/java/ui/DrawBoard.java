package ui;

import chess.ChessBoard;
import chess.ChessPosition;

import java.awt.*;

import static ui.EscapeSequences.*;

public class DrawBoard {
  private ChessBoard board;
  private String drawBoard = "";
  //private static final Font BOARD_FONT = new Font("Arial", Font.PLAIN, 24);

  public DrawBoard(ChessBoard board) {
    this.board = board;
  }

  private String drawNormal() {
    drawBoard += SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE + "    a  b  c  d  e  f  g  h    " + SET_BG_COLOR_WHITE + "\n";
    for (int i = 8; i > 0; i--) {
      drawBoard += SET_BG_COLOR_LIGHT_GREY + " " + i + " ";
      checkerBoard(i);
      drawBoard += SET_BG_COLOR_LIGHT_GREY + " " + i + " ";
      drawBoard += SET_BG_COLOR_WHITE + "\n";
    }
    drawBoard += SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE + "    1  2  3  4  5  6  7  8    " + SET_BG_COLOR_WHITE + "\n";

    return drawBoard;
  }

  private String drawUpsideDown() {

    return "v";
  }

  public String getWhiteDrawings() {
    return drawUpsideDown() + '\n' + drawNormal();
  }

  public String getBlackDrawings() {
    return drawNormal() + '\n' + drawUpsideDown();
  }

  private boolean isEven(int x) {
    if ((x % 2) == 0) {
      return true;
    } else {
      return false;
    }
  }

  private void checkerBoard(int row) {
    ChessPosition position;
    for (int j = 1; j < 9; j++) {
      if ((isEven(row) && isEven(j)) || (!isEven(row) && !isEven(j))) {
        drawBoard += SET_BG_COLOR_MAGENTA + " ";
      } else {
        drawBoard += SET_BG_COLOR_WHITE + " ";
      }
      position = new ChessPosition(row, j);
      if (board.getPiece(position) != null) {
        drawBoard += board.getPiece(position).toString();
      } else {
        drawBoard += " ";
      }
      drawBoard += " ";
    }
  }
}
