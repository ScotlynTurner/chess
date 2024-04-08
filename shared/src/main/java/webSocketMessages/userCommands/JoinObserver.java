package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
  private Integer gameID;
  public JoinObserver(String authToken, Integer gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
