package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.ArrayList;

public class KingMovesCalculator {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor thisColor;

  public KingMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisColor) {
    this.board=board;
    this.myPosition=myPosition;
    this.thisColor=thisColor;
  }
  public ArrayList<ChessPosition> pieceMoves(){
    ArrayList<ChessPosition> endPositions = new ArrayList<ChessPosition>();
    ChessPosition coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() - 1);
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn());
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow(), coordinate.getColumn() + 1);
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() + 1);
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn());
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() - 1);
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow(), coordinate.getColumn() - 1);
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    return endPositions;
  }
}
