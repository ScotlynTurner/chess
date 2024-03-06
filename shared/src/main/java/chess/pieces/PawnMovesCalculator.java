package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.HashSet;

public class PawnMovesCalculator {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor thisColor;

  public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisColor) {
    this.board=board;
    this.myPosition=myPosition;
    this.thisColor=thisColor;
  }
  public HashSet<ChessPosition> pieceMoves() {
    HashSet<ChessPosition> endPositions=new HashSet<ChessPosition>();
    ChessPosition coordinate=myPosition;
    if(thisColor.equals(ChessGame.TeamColor.WHITE)) {
      return whiteMoves();
    } else {
      return blackMoves();
    }
  }

  private HashSet<ChessPosition> whiteMoves() {
    HashSet<ChessPosition> endPositions=new HashSet<ChessPosition>();
    HashSet<ChessPosition> promoteEndPositions=new HashSet<ChessPosition>();
    ChessPosition coordinate=myPosition;
    Boolean emptySpace = Boolean.TRUE;
    coordinate=new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null) {
        endPositions.add(coordinate);
      } else {
        emptySpace = Boolean.FALSE;
      }
    }
    coordinate = myPosition;
    if (coordinate.getRow() == 2) {
      coordinate=new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
      if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
        if (board.getPiece(coordinate) == null && emptySpace == Boolean.TRUE) {
          endPositions.add(coordinate);
        }
      }
    }
    emptySpace = Boolean.FALSE;
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

  private HashSet<ChessPosition> blackMoves() {
    HashSet<ChessPosition> endPositions=new HashSet<ChessPosition>();
    HashSet<ChessPosition> promoteEndPositions=new HashSet<ChessPosition>();
    ChessPosition coordinate=myPosition;
    Boolean emptySpace = Boolean.TRUE;
    coordinate=new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
    if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
      if (board.getPiece(coordinate) == null) {
        endPositions.add(coordinate);
      } else {
        emptySpace = Boolean.FALSE;
      }
    }
    coordinate = myPosition;
    if (coordinate.getRow() == 7) {
      coordinate=new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
      if ((1 <= coordinate.getRow() && coordinate.getRow() <= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn() <= 8)) {
        if (board.getPiece(coordinate) == null && emptySpace == Boolean.TRUE) {
          endPositions.add(coordinate);
        }
      }
    }
    emptySpace = Boolean.FALSE;
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

  public Boolean promoteStatus() {
    if((thisColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7)
            || (thisColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2)) {
      return Boolean.TRUE;
    } else {
      return Boolean.FALSE;
    }
  }

}