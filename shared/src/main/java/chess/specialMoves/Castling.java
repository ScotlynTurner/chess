package chess.specialMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

public class Castling {
  private Boolean kingHasMoved;
  private Boolean rookHasMoved;
  private ChessBoard board;
  private Boolean checkStatus;
  private ChessGame.TeamColor team;

  public Castling(Boolean kingHasMoved, Boolean rookHasMoved, ChessBoard board, Boolean checkStatus, ChessGame.TeamColor team) {
    this.kingHasMoved = kingHasMoved;
    this.rookHasMoved = rookHasMoved;
    this.board = board;
    this.checkStatus = checkStatus;
    this.team = team;
  }

  public Boolean canCastle() {
    if (!kingHasMoved && !rookHasMoved && !checkStatus) {
      if (team == ChessGame.TeamColor.WHITE && board.getPiece(new ChessPosition(1,6)) == null &&
              board.getPiece(new ChessPosition(1,7)) == null) {
        return true;
      } else if (team == ChessGame.TeamColor.WHITE && board.getPiece(new ChessPosition(8,6)) == null &&
              board.getPiece(new ChessPosition(8,7)) == null) {
        return true;
      }
    }
    return false;
  }
}
