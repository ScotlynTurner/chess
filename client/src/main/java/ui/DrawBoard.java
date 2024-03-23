package ui;

import chess.ChessBoard;
import chess.ChessPosition;

import java.awt.*;

import static ui.EscapeSequences.*;

public class DrawBoard {
  private ChessBoard board;
  private String drawBoard = "";

  public DrawBoard(ChessBoard board) {
    this.board = board;
  }

  public String drawNormal() {
    drawBoard = "";
    drawBoard += SET_BG_CUSTOM_MINT + SET_TEXT_CUSTOM_WHITE + "    a  b  c  d  e  f  g  h    " + SET_BG_BRIGHT_WHITE + "\n";
    for (int i = 8; i > 0; i--) {
      drawBoard += SET_BG_CUSTOM_MINT + " " + i + " ";
      checkerBoardNormal(i);
      drawBoard += SET_TEXT_CUSTOM_WHITE + SET_BG_CUSTOM_MINT + " " + i + " ";
      drawBoard += SET_BG_BRIGHT_WHITE + "\n";
    }
    drawBoard += SET_BG_CUSTOM_MINT + SET_TEXT_CUSTOM_WHITE + "    a  b  c  d  e  f  g  h    " + SET_BG_BRIGHT_WHITE + "\n";

    return drawBoard;
  }

  private String drawUpsideDown() {
    drawBoard = "";
    drawBoard += SET_BG_CUSTOM_MINT + SET_TEXT_CUSTOM_WHITE + "    h  g  f  e  d  c  b  a    " + SET_BG_BRIGHT_WHITE + "\n";
    for (int i = 1; i < 9; i++) {
      drawBoard += SET_BG_CUSTOM_MINT + " " + i + " ";
      checkerBoardUpsideDown(i);
      drawBoard += SET_TEXT_CUSTOM_WHITE + SET_BG_CUSTOM_MINT + " " + i + " ";
      drawBoard += SET_BG_BRIGHT_WHITE + "\n";
    }
    drawBoard += SET_BG_CUSTOM_MINT + SET_TEXT_CUSTOM_WHITE + "    h  g  f  e  d  c  b  a    " + SET_BG_BRIGHT_WHITE + "\n";

    return drawBoard;
  }

  public String getWhiteDrawings() {
    return drawUpsideDown() + SET_BG_BRIGHT_WHITE + "\n" + drawNormal();
  }

  public String getBlackDrawings() {
    return drawNormal() + SET_BG_BRIGHT_WHITE + "\n" + drawUpsideDown();
  }

  private boolean isEven(int x) {
    if ((x % 2) == 0) {
      return true;
    } else {
      return false;
    }
  }

  private void checkerBoardNormal(int row) {
    ChessPosition position;
    for (int j = 1; j < 9; j++) {
      if ((isEven(row) && isEven(j)) || (!isEven(row) && !isEven(j))) {
        drawBoard += SET_BG_CUSTOM_MAROON + " ";
      } else {
        drawBoard += SET_BG_CUSTOM_PINK + " ";
      }
      position = new ChessPosition(row, j);
      if (board.getPiece(position) != null) {
        drawBoard += SET_TEXT_COLOR_BLACK + board.getPiece(position).toString();
      } else {
        drawBoard += " ";
      }
      drawBoard += " ";
    }
  }

  private void checkerBoardUpsideDown(int row) {
    ChessPosition position;
    for (int j = 8; j > 0; j--) {
      if ((isEven(row) && isEven(j)) || (!isEven(row) && !isEven(j))) {
        drawBoard += SET_BG_CUSTOM_MAROON + " ";
      } else {
        drawBoard += SET_BG_CUSTOM_PINK + " ";
      }
      position = new ChessPosition(row, j);
      if (board.getPiece(position) != null) {
        drawBoard += SET_TEXT_COLOR_BLACK + board.getPiece(position).toString();
      } else {
        drawBoard += " ";
      }
      drawBoard += " ";
    }
  }
}
