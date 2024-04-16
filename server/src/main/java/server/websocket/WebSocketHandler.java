package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  private AuthDAO authAccess = new SQLAuthDAO();
  private GameDAO gameAccess = new SQLGameDAO();
  private UserDAO userAccess = new SQLUserDAO();
  private String username;
  private ChessGame game;
  private String authToken;
  private ChessGame.TeamColor playerColor;
  private ChessMove move;
  private Service service;

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
        case JOIN_PLAYER -> {
          JoinPlayer joinPlayerCommand = new Gson().fromJson(message, JoinPlayer.class);
          joinPlayer(command.getAuthString(), session, joinPlayerCommand);}
        case JOIN_OBSERVER -> joinObserver(command.getAuthString(), session);
        case LEAVE -> leave(command.getAuthString(), session);
        case RESIGN -> resign(command.getAuthString(), session);
        case MAKE_MOVE -> {
          MakeMove makeMoveCommand = new Gson().fromJson(message, MakeMove.class);
          makeMove(command.getAuthString(), session, makeMoveCommand.getMove(), makeMoveCommand.getStatus());}
        case USER_ERROR -> sendError(command.getAuthString(), "ERROR", session);
      }
    } catch (Exception e) {
      sendError(authToken, e.getMessage(), session);
    }
  }

  private void joinPlayer(String authToken, Session session, JoinPlayer joinPlayerCommand) throws IOException, DataAccessException {
    try {
      GameData gameData = gameAccess.getGame(joinPlayerCommand.getGameID());
      String clientColor = joinPlayerCommand.getPlayerColor().toString();
      if (clientColor == null || clientColor.equals("empty")) {
        throw new DataAccessException("Error: color is null");
      } else {
        if (clientColor.equals("BLACK")) {
          if (gameData.blackUsername() != null) {
            throw new DataAccessException("Error: already taken");
          }
        } else if (clientColor.equals("WHITE")) {
          if (gameData.whiteUsername() != null) {
            throw new DataAccessException("Error: already taken");
          }
        }
        connections.add(authToken, session);
        var load=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String message=String.format("%s joined the game as %s", username, playerColor);
        var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastExcludeUser(authToken, notification);
        connections.broadcastRoot(authToken, load);
      }
    } catch (Exception e) {
      sendError(authToken, e.getMessage(), session);
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
      sendError(authToken, e.getMessage(), session);
    }
  }

  private void leave(String authToken, Session session) throws IOException, DataAccessException {
    try {
      String message=String.format("%s left the game", username);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastExcludeUser(authToken, notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage(), session);
    }
  }

  private void resign(String authToken, Session session) throws IOException, DataAccessException {
    try {
      connections.remove(authToken);
      String message=String.format("%s resigned from the game", username);
      var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcastAll(notification);
    } catch (Exception e) {
      sendError(authToken, e.getMessage(), session);
    }
  }

  private void makeMove(String authToken, Session session, ChessMove move, String gameStatus) throws IOException, DataAccessException {
    try {
      var load=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
      if (gameStatus != null && gameStatus.equals("checkmate")) {
        String message=String.format("%s moved %s to %s. Checkmate!", username, move.getStartPosition(), move.getEndPosition());
        var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastAll(load);
        connections.broadcastAll(notification);
      } else if (gameStatus != null && gameStatus.equals("check")){
        String message=String.format("%s moved %s to %s. Check!", username, move.getStartPosition(), move.getEndPosition());
        var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastAll(load);
        connections.broadcastAll(notification);
      } else {
        String message=String.format("%s moved %s to %s", username, move.getStartPosition(), move.getEndPosition());
        var notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcastAll(load);
        connections.broadcastExcludeUser(authToken, notification);
      }
    } catch (Exception e) {
      sendError(authToken, e.getMessage(), session);
    }
  }

  public void sendError(String authToken, String message, Session session) throws DataAccessException, IOException {
    connections.add(authToken, session);
    var error = new Error(ServerMessage.ServerMessageType.ERROR, message);
    connections.broadcastRoot(authToken, error);
  }
}
