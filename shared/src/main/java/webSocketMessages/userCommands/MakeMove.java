package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
  private ChessMove move;
  private String gameStatus;

  public MakeMove(String authToken, int gameID, ChessMove move, String gameStatus) {
    super(authToken, gameID);
    this.move = move;
    this.commandType = CommandType.MAKE_MOVE;
    this.gameStatus = gameStatus;
  }

  public String getStatus() {
    return gameStatus;
  }
  public ChessMove getMove() {
    return move;
  }
}
