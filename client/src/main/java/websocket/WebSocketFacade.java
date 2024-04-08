package websocket;

import com.google.gson.Gson;
import server.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
          notificationHandler.notify(notification);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void login(String authToken) throws ResponseException {
    try {
      var command = new UserGameCommand(authToken);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void logout(String authToken) throws ResponseException {
    try {
      var command = new UserGameCommand(authToken);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
      this.session.close();
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

}