package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();
  private AuthDAO authAccess = new SQLAuthDAO();
  private GameDAO gameAccess = new SQLGameDAO();
  private String username;
  private ChessGame game;
  private String authToken;
  private ChessGame.TeamColor playerColor;
  private ChessMove move;

  private String getUsername(String authToken) throws DataAccessException {
    return authAccess.getAuth(authToken);
  }

  private ChessGame getGame(int id) throws DataAccessException {
    return gameAccess.getGame(id).game();
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException, DataAccessException {
    try {
      UserGameCommand command=new Gson().fromJson(message, UserGameCommand.class);
      authToken = command.getAuthString();
      username=getUsername(command.getAuthString());
      game = getGame(command.getGameID());
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> joinPlayer(command.getAuthString(), session);
        case JOIN_OBSERVER -> joinObserver(command.getAuthString(), session);
        case LEAVE -> leave(command.getAuthString());
        case RESIGN -> resign(command.getAuthString(), session);
        case MAKE_MOVE -> makeMove(command.getAuthString(), session, move);
      }
    } catch (Exception e) {
      sendError(authToken, e.getMessage());
    }
  }

  private void joinPlayer(String authToken, Session session) throws IOException, DataAccessException {
    try {
      connections.add(authToken, session);
      var load=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
      connections.broadcastRoot(authToken, load);
      String message=String.format("%s joined the game as %s", username, playerColor);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastExcludeUser(authToken, notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage());
    }
  }

  private void joinObserver(String authToken, Session session) throws IOException, DataAccessException {
    try {
      connections.add(authToken, session);
      var load=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
      connections.broadcastRoot(authToken, load);
      String message=String.format("%s joined the game as an observer", username);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastExcludeUser(authToken, notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage());
    }
  }

  private void leave(String authToken) throws IOException, DataAccessException {
    try {
      String message=String.format("%s left the game", username);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastExcludeUser(authToken, notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage());
    }
  }

  private void resign(String authToken, Session session) throws IOException, DataAccessException {
    try {
      connections.remove(authToken);
      String message=String.format("%s resigned from the game", username);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastAll(notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage());
    }
  }

  private void makeMove(String authToken, Session session, ChessMove move) throws IOException, DataAccessException {
    try {
      var load=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
      connections.broadcastAll(load);
      String message=String.format("%s moved %s to %s", username, move.getStartPosition(), move.getEndPosition());
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastExcludeUser(authToken, notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage());
    }
  }

  private void sendError(String authToken, String message) throws DataAccessException, IOException {
    var error = new Error(ServerMessage.ServerMessageType.ERROR, message);
    connections.broadcastRoot(authToken, error);
  }
}
