package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.HashSet;

public class KnightMovesCalculator {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor thisColor;

  public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisColor) {
    this.board=board;
    this.myPosition=myPosition;
    this.thisColor=thisColor;
  }

  public HashSet<ChessPosition> pieceMoves() {
    HashSet<ChessPosition> endPositions=new HashSet<ChessPosition>();
    ChessPosition coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() + 2, coordinate.getColumn() - 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() + 2, coordinate.getColumn() + 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 2);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() + 2);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() - 2, coordinate.getColumn() + 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() - 2, coordinate.getColumn() - 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() - 2);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }

    coordinate=myPosition;
    coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() - 2);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate=new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      }
    }
      return endPositions;
  }
}