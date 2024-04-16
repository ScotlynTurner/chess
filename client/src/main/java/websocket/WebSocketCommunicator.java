package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import server.ResponseException;
import server.ServerFacade;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketCommunicator extends Endpoint {
  Session session;
  ServerMessageObserver notificationHandler;
  ServerFacade server;

  public WebSocketCommunicator(String url, ServerMessageObserver notificationHandler, ServerFacade server) throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;
      this.server = server;
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

  public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
    try {
      var command = new JoinPlayer(authToken, gameID, playerColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void joinObserver(String authToken, int gameID) throws ResponseException {
    try {
      var command = new JoinObserver(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void makeMove(String authToken, int gameID, ChessMove move, String gameStatus) throws ResponseException {
    try {
      var command = new MakeMove(authToken, gameID, move, gameStatus);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
      this.session.close();
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void leave(String authToken, int gameID) throws ResponseException {
    try {
      var command = new Leave(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void resign(String authToken, int gameID) throws ResponseException {
    try {
      var command = new Resign(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
      this.session.close();
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void sendError(String authToken, int gameID, String message) throws ResponseException {
    try {
      var command=new UserError(authToken, gameID, message);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }
}