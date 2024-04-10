package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    switch (command.getCommandType()) {
      case JOIN_PLAYER, JOIN_OBSERVER -> join(command.getAuthString(), session);
      case LEAVE -> leave(command.getAuthString());
      case RESIGN -> resign(command.getAuthString(), session);
      case MAKE_MOVE -> makeMove(command.getAuthString(), session);
    }
  }

  private void join(String authToken, Session session) throws IOException {
    connections.add(authToken, session);
    var load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
    connections.broadcastRoot(authToken, load);
    var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
    connections.broadcastExcludeUser(authToken, notification);
  }

  private void leave(String authToken) throws IOException {
    var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
    connections.broadcastExcludeUser(authToken, notification);
  }

  private void resign(String authToken, Session session) throws IOException {
    connections.remove(authToken);
    var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
    connections.broadcastAll(notification);
  }

  private void makeMove(String authToken, Session session) throws IOException {
    var load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
    connections.broadcastAll(load);
    var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
    connections.broadcastExcludeUser(authToken, notification);
  }
}
