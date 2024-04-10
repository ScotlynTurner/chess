package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
  private Integer gameID;
  private ChessMove move;
  private CommandType commandType = CommandType.MAKE_MOVE;

  public MakeMove(String authToken, Integer gameID, ChessMove move) {
    super(authToken);
    this.gameID = gameID;
    this.move = move;
  }
}
