package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class DrawBoard {
  private ChessBoard board;
  private String drawBoard = "";
  private ChessGame game;
  private ChessPosition position;

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

  public String getWhiteDrawings(ChessGame game, ChessPosition position) {
    this.game = game;
    this.position = position;
    return drawUpsideDown() + SET_BG_BRIGHT_WHITE + "\n" + drawNormal();
  }

  public String getBlackDrawings(boolean highlight, ChessGame game, ChessPosition position) {
    this.game = game;
    this.position = position;
    return drawNormal() + SET_BG_BRIGHT_WHITE + "\n" + drawUpsideDown();
  }

  public String getListVersion(ChessGame game) {
    this.game = game;
    this.position = null;
    return drawNormal();
  }

  private boolean isEven(int x) {
    if ((x % 2) == 0) {
      return true;
    } else {
      return false;
    }
  }

  private void checkerBoardNormal(int row) {
    for (int j = 1; j < 9; j++) {
      checkerboardHelper(row, j);
    }
  }

  private void checkerBoardUpsideDown(int row) {
    for (int j = 8; j > 0; j--) {
      checkerboardHelper(row, j);
    }
  }

  private void checkerboardHelper(int row, int j) {
    HashSet<ChessMove> validMoves;
    HashSet<ChessPosition> endPositions = new HashSet<>();
    if (position != null) {
      validMoves = (HashSet<ChessMove>) game.validMoves(position);
      for (ChessMove move : validMoves) {
        endPositions.add(move.getEndPosition());
      }
    }

    ChessPosition coordinate;
    coordinate = new ChessPosition(row, j);

    if (position != null && position.equals(coordinate)) {
      drawBoard+=SET_BG_CUSTOM_WHITE + " ";
    } else if ((isEven(row) && isEven(j)) || (!isEven(row) && !isEven(j))) {
      if (endPositions != null && endPositions.contains(coordinate)) {
        drawBoard+=SET_BG_CUSTOM_MAROON_FADED + " ";
      } else {
        drawBoard+=SET_BG_CUSTOM_MAROON + " ";
      }
    } else {
      if (endPositions != null && endPositions.contains(coordinate)) {
        drawBoard+=SET_BG_CUSTOM_PINK_FADED + " ";
      } else {
        drawBoard+=SET_BG_CUSTOM_PINK + " ";
      }
    }

    if (board.getPiece(coordinate) != null) {
      drawBoard += SET_TEXT_COLOR_BLACK + board.getPiece(coordinate).toString();
    } else {
      drawBoard += " ";
    }
    drawBoard += " ";
  }
}
