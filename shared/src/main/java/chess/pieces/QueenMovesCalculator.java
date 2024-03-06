package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.HashSet;

public class QueenMovesCalculator {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor thisColor;

  public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisColor) {
    this.board=board;
    this.myPosition=myPosition;
    this.thisColor=thisColor;
  }
  public HashSet<ChessPosition> pieceMoves(){
    HashSet<ChessPosition> endPositions = new HashSet<ChessPosition>();
    ChessPosition coordinate = myPosition;
    RookMovesCalculator rookMoves = new RookMovesCalculator(board, myPosition, thisColor);
    BishopMovesCalculator bishopMoves = new BishopMovesCalculator(board, myPosition, thisColor);

    endPositions.addAll(rookMoves.pieceMoves());
    endPositions.addAll(bishopMoves.pieceMoves());
    return endPositions;
  }
}