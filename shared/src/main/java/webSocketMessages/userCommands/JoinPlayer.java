package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
  private CommandType commandType = CommandType.JOIN_PLAYER;
  private Integer gameID;
  private ChessGame.TeamColor playerColor;

  public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.playerColor = playerColor;
  }
}
