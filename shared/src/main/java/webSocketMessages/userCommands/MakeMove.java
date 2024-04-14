package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
  private ChessMove move;

  public MakeMove(String authToken, int gameID, ChessMove move) {
    super(authToken, gameID);
    this.move = move;
    this.commandType = CommandType.MAKE_MOVE;
  }
}
