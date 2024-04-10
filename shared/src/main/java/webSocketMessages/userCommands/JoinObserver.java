package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
  private Integer gameID;
  private CommandType commandType = CommandType.JOIN_OBSERVER;

  public JoinObserver(String authToken, Integer gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
