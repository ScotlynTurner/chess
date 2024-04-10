package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
  private Integer gameID;
  private CommandType commandType = CommandType.RESIGN;

  public Resign(String authToken, Integer gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
