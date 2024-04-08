package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
  private CommandType commandType;
  private Integer gameID;
  private ChessGame.TeamColor playerColor;

  public JoinPlayer(String authToken, CommandType commandType, Integer gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.commandType = commandType;
    this.gameID = gameID;
    this.playerColor = playerColor;
  }
}
