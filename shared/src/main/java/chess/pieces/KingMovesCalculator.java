package chess.pieces;

import chess.*;

import java.util.HashSet;

public class KingMovesCalculator {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor thisColor;

  public KingMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisColor) {
    this.board=board;
    this.myPosition=myPosition;
    this.thisColor=thisColor;
  }
  public HashSet<ChessPosition> pieceMoves(){
    HashSet<ChessPosition> endPositions = new HashSet<ChessPosition>();
    ChessPosition coordinate = myPosition;
    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() - 1);
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn());
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow(), coordinate.getColumn() + 1);
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() + 1);
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn());
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() - 1);
    coordinate=getChessPosition(endPositions, coordinate);
    coordinate = new ChessPosition(coordinate.getRow(), coordinate.getColumn() - 1);
    getChessPosition(endPositions, coordinate);

    return endPositions;
  }

  private ChessPosition getChessPosition(HashSet<ChessPosition> endPositions, ChessPosition coordinate) {
    if((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
      if (board.getPiece(coordinate) == null || board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
        coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
      } else if(board.getPiece(coordinate).pieceColor != thisColor) {
        endPositions.add(coordinate);
      }
    }
    coordinate = myPosition;
    return coordinate;
  }

  public boolean isInCheck(ChessGame.TeamColor teamColor) {
    ChessPosition position;
    ChessPosition teamKing = new ChessPosition(0,0);
    HashSet<ChessMove> oppositionMoves = new HashSet<>();
    for (int i = 1; i < 9; i++) {
      for (int j = 1; j < 9; j++) {
        position = new ChessPosition(i, j);
        if (board.getPiece(position) != null && board.getPiece(position).pieceColor != teamColor){
          for (ChessMove chessMove : board.getPiece(position).pieceMoves(board, position)) {
            oppositionMoves.add(chessMove);
          }

        } else if (board.getPiece(position) != null && board.getPiece(position).pieceColor == teamColor){
          if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
            teamKing = position;
          }
        }
      }
    }
    for (ChessMove moves : oppositionMoves) {
      if (moves.getEndPosition().getRow() == teamKing.getRow() && moves.getEndPosition().getColumn() == teamKing.getColumn()) {
        return true;
      }
    }

    return false;
  }
}
