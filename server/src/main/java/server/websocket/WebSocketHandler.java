package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    switch (command.getCommandType()) {
      case JOIN_PLAYER -> enter(command.getAuthString(), session);
      case LEAVE, RESIGN -> exit(command.getAuthString());
//    case MAKE_MOVE -> ;
    }
  }

  private void enter(String username, Session session) throws IOException {
    connections.add(username, session);
    var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
    connections.broadcast(username, notification);
  }

  private void exit(String visitorName) throws IOException {
    connections.remove(visitorName);
    var message = String.format("%s left the shop", visitorName);
    var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
    connections.broadcast(visitorName, notification);
  }
}
