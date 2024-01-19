package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.ArrayList;

public class PawnMovesCalculator {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor thisColor;

  public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisColor) {
    this.board=board;
    this.myPosition=myPosition;
    this.thisColor=thisColor;
  }
  public ArrayList<ChessPosition> pieceMoves() {
    ArrayList<ChessPosition> endPositions=new ArrayList<ChessPosition>();
    ChessPosition coordinate=myPosition;
    if(thisColor.equals(ChessGame.TeamColor.WHITE)) {
      return whiteMoves();
    } else {
      return blackMoves();
    }
  }

  private ArrayList<ChessPosition> whiteMoves() {
    ArrayList<ChessPosition> endPositions=new ArrayList<ChessPosition>();
    ChessPosition coordinate=myPosition;

    if (coordinate.getRow() == 2) {
      coordinate=new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
      if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
        if (board.getPiece(coordinate) == null) {
          endPositions.add(coordinate);
        }
      }
    }

    coordinate=new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null) {
        endPositions.add(coordinate);
      }
    }
    coordinate=new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) != null && board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }

    coordinate=new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) != null && board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    return endPositions;
  }

  private ArrayList<ChessPosition> blackMoves() {
    ArrayList<ChessPosition> endPositions=new ArrayList<ChessPosition>();
    ChessPosition coordinate=myPosition;

    if (coordinate.getRow() == 7) {
      coordinate=new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
      if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
        if (board.getPiece(coordinate) == null) {
          endPositions.add(coordinate);
        }
      }
    }
    coordinate=new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null) {
        endPositions.add(coordinate);
      }
    }
    coordinate=new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) != null && board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }

    coordinate=new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) != null && board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    return endPositions;
  }

}