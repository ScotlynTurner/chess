package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
  private Integer gameID;
  private CommandType commandType = CommandType.LEAVE;

  public Leave(String authToken, Integer gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
