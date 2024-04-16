package webSocketMessages.userCommands;

public class UserError extends UserGameCommand{
  private String message;
  public UserError(String authToken, int gameID, String message) {
    super(authToken, gameID);
    this.commandType = CommandType.USER_ERROR;
    this.message = message;
  }
}
