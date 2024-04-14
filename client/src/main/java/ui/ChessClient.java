package ui;

import chess.*;
import model.GameData;
import server.ResponseException;
import server.ServerFacade;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import websocket.ServerMessageObserver;
import websocket.WebSocketCommunicator;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
  private String serverUrl;
  private String username = null;
  private String authToken = null;
  private final ServerFacade server;
  private State state = State.SIGNEDOUT;
  private Status status = Status.LOBBY;
  private int currentGameID = 0;
  private ChessGame game;
  private ChessGame.TeamColor currentColor;
  private WebSocketCommunicator wsc;
  ServerMessageObserver notificationHandler;

  public ChessClient(String serverUrl, ServerMessageObserver notificationHandler) throws ResponseException {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.notificationHandler = notificationHandler;
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "register" -> register(params);
        case "login" -> login(params);
        case "create" -> createGame(params);
        case "list" -> listGames();
        case "join" -> joinGame(params);
        case "observe" -> observe(params);
        case "logout" -> logout();
        case "quit" -> "quit";
        case "clear" -> clear();
        case "redraw" -> redraw();
        case "move" -> move(params);
        case "leave" -> leave();
        case "resign" -> resign();
        case "show" -> showMoves(params);
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }

  private String clear() throws ResponseException {
    server.clear();
    return "Cleared database";
  }

  public String login(String... params) throws ResponseException {
    if (params.length >= 2) {
      state = State.SIGNEDIN;
      username = params[0];
      var password = params[1];
      authToken = server.login(username, password).authToken();
      return String.format("Logged in as %s", username);
    }
    throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
  }

  public String register(String... params) throws ResponseException {
    if (params.length >= 2) {
      username = params[0];
      var password = params[1];
      var email = params[2];
      server.register(username, password, email);
      return String.format("Registered user: %s", username);
    }
    throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
  }

  public String listGames() throws ResponseException {
    assertSignedIn();
    var games = server.listGames();
    var result = new StringBuilder();
    var gson = new Gson();
    for (var game : games) {
      String formattedGame = formatGame(game);
      result.append(formattedGame).append('\n');
    }
    return result.toString();
  }

  private String formatGame(GameData game) {
    DrawBoard boards = new DrawBoard(game.game().getBoard());
    String formattedGame = SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE;
    formattedGame += game.gameID() + ": " + game.gameName() + "\nWhite: " + game.whiteUsername() + " | Black: " + game.blackUsername() + "\n";
    formattedGame += SET_BG_BRIGHT_WHITE;
    formattedGame += boards.getListVersion(game.game());
    formattedGame += "\n";
    return formattedGame;
  }

  public String joinGame(String... params) throws ResponseException, DataAccessException {
    assertSignedIn();
    ChessGame.TeamColor playerColor;
    if (params.length >= 1) {
      var id = Integer.parseInt(params[0]);
      currentGameID = id;
      if (params.length >= 2 && !params[1].equalsIgnoreCase("empty")) {
        if (params[1].equalsIgnoreCase("white")) {
          playerColor = ChessGame.TeamColor.WHITE;
        } else {
          playerColor = ChessGame.TeamColor.BLACK;
        }
        wsc = new WebSocketCommunicator(serverUrl, notificationHandler);
        wsc.joinPlayer(authToken, id, playerColor);
        System.out.println(drawBoards(playerColor, getGame(id), false, null));
        status = Status.PLAYING;
        server.joinGame(playerColor, id);
        currentColor = playerColor;
        game = getGame(id);
        return String.format("%s has joined the game as %s", username, playerColor);
      }
      wsc = new WebSocketCommunicator(serverUrl, notificationHandler);
      wsc.joinObserver(authToken, id);
      server.joinGame(null, id);
      System.out.println(drawBoards(null, getGame(id), false, null));
      status = Status.OBSERVING;
      return String.format("%s has joined the game as an observer", username);
    }
    throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
  }

  public String observe(String... params) throws ResponseException, DataAccessException {
    assertSignedIn();
    if (params.length == 1) {
      var id = Integer.parseInt(params[0]);
      currentGameID = id;
      wsc = new WebSocketCommunicator(serverUrl, notificationHandler);
      wsc.joinObserver(authToken, id);
      server.joinGame(null, id);
      System.out.println(drawBoards(ChessGame.TeamColor.WHITE, getGame(id), false, null));
      status = Status.OBSERVING;
      return String.format("%s has joined the game as an observer", username);
    }
    throw new ResponseException(400, "Expected: <ID>");
  }

  public String logout() throws ResponseException {
    assertSignedIn();
    server.logout();
    state = State.SIGNEDOUT;
    return String.format("%s logged out", username);
  }

  public String createGame(String... params) throws ResponseException {
    assertSignedIn();
    if (params.length >= 1) {
      var gameName = params[0];
      int gameID = server.addGame(gameName);
      return String.format("%s created new game %s with id %d", username, gameName, gameID);
    }
    throw new ResponseException(400, "Expected: <NAME>");
  }

  private ChessGame getGame(int id) throws ResponseException {
    for (var game : server.listGames()) {
      if (game.gameID() == id) {
        return game.game();
      }
    }
    return null;
  }

  public String move(String... params) throws ResponseException, InvalidMoveException {
    assertSignedIn();
    assertPlaying();
    if (params.length >= 5) {
      var startCoordNum = Integer.parseInt(params[0]);
      var startCoordLetter = convertLetter(params[1]);
      var endCoordNum = Integer.parseInt(params[3]);
      var endCoordLetter = convertLetter(params[4]);

      ChessPosition startPosition = new ChessPosition(startCoordNum, startCoordLetter);
      ChessPosition endPosition = new ChessPosition(endCoordNum, endCoordLetter);

      ChessMove move = new ChessMove(startPosition, endPosition, null);
      game.makeMove(move);
      wsc.makeMove(authToken, currentGameID, move);
      System.out.println(drawBoards(currentColor, getGame(currentGameID), false, null));
      return String.format("%s moved (%s, %s) to (%s, %s)", username, startCoordNum, startCoordLetter, endCoordNum, endCoordLetter);
    }
    throw new ResponseException(400, "Expected: <NUMBER> <LETTER> to <NUMBER> <LETTER>");
  }

  public String redraw() throws ResponseException {
    assertSignedIn();
    System.out.println(drawBoards(currentColor, getGame(currentGameID), false, null));
    return "";
  }

  public String resign() throws ResponseException {
    assertSignedIn();
    assertPlaying();
    Scanner scanner = new Scanner(System.in);
    System.out.println("Are you sure you want to resign? <yes|no>");
    String line = scanner.nextLine();
    if (!line.equalsIgnoreCase("yes")) {
      return "Continuing game...";
    } else {
      status = Status.OBSERVING;
      wsc.resign(authToken, currentGameID);
      return String.format("%s resigned", username);
    }
  }

  public String showMoves(String... params) throws ResponseException {
    assertSignedIn();
    if (params.length >= 2) {
      var startCoordNum = Integer.parseInt(params[0]);
      var startCoordLetter = convertLetter(params[1]);
      ChessPosition position = new ChessPosition(startCoordNum, startCoordLetter);
      System.out.println(drawBoards(currentColor, getGame(currentGameID), false, position));
      return "";
    }
    throw new ResponseException(400, "Expected: <NUMBER> <LETTER>");
  }

  private int convertLetter(String letter) {
    switch (letter) {
      case "a": return 1;
      case "b": return 2;
      case "c": return 3;
      case "d": return 4;
      case "e": return 5;
      case "f": return 6;
      case "g": return 7;
      case "h": return 8;
      default: return 0;
    }
  }

  public String leave() throws ResponseException {
    wsc.leave(authToken, currentGameID);
    status = Status.LOBBY;
    wsc = null;
    return String.format("%s left the game", username);
  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      status = Status.LOBBY;
      return  SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE + """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    - help
                    """;
    } else if (status == Status.PLAYING) {
      return SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE + """
              - redraw
              - move <NUMBER> <LETTER> to <NUMBER> <LETTER>
              - resign
              - show <NUMBER> <LETTER>
              - leave
              - help
              """;
    } else if (status == status.OBSERVING) {
      return SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE + """
              - redraw
              - show <NUMBER> <LETTER>
              - leave
              - help
              """;
    }
    status = Status.LOBBY;
    return SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE + """
                - create <NAME> 
                - list 
                - join <ID> [WHITE|BLACK|<empty>]
                - observe <ID>
                - logout
                - quit 
                - help
                """;
  }

  private String drawBoards(ChessGame.TeamColor playerColor, ChessGame game, boolean highlight, ChessPosition position) {
    DrawBoard boards = new DrawBoard(game.getBoard());
    String boardLayout = "";
    if (playerColor == ChessGame.TeamColor.WHITE || playerColor == null) {
      boardLayout = boards.getWhiteDrawings(game, position);
    } else {
      boardLayout = boards.getBlackDrawings(highlight, game, position);
    }
    return boardLayout;
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.SIGNEDOUT) {
      throw new ResponseException(400, "You must sign in");
    }
  }

  private void assertPlaying() throws ResponseException {
    if (status != Status.PLAYING) {
      throw new ResponseException(400, "You are not currently playing");
    }
  }
}
