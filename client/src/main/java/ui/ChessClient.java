package ui;

import chess.*;
import model.AuthData;
import model.GameData;
import server.ResponseException;
import server.ServerFacade;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
  private String serverUrl;
  private String username = null;
  private final ServerFacade server;
  private State state = State.SIGNEDOUT;
  private Status status = Status.LOBBY;
  private int currentGameID = 0;
  private String currentColor;
  private ChessGame game;
  private NotificationHandler notificationHandler;
  private WebSocketFacade ws;

  public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
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
        case "show" -> showMoves();
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
      String authToken = server.login(username, password).authToken();
      ws = new WebSocketFacade(serverUrl, notificationHandler);
      ws.login(authToken);
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
    String formattedGame = SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE;
    formattedGame += game.gameID() + ": " + game.gameName() + "\nWhite: " + game.whiteUsername() + " | Black: " + game.blackUsername() + "\n";
    formattedGame += SET_BG_BRIGHT_WHITE;
    formattedGame += drawBoards("WHITE", game.game().getBoard(), true, false, null);
    formattedGame += "\n";
    return formattedGame;
  }

  public String joinGame(String... params) throws ResponseException, DataAccessException {
    assertSignedIn();
    if (params.length >= 1) {
      var id = Integer.parseInt(params[0]);
      if (params.length >= 2 && !params[1].equals("empty")) {
        var playerColor = params[1];
        server.joinGame(playerColor, id);
        System.out.println(drawBoards(playerColor, getGame(id).getBoard(), false, false, null));
        status = Status.PLAYING;
        currentGameID = id;
        game = getGame(id);
        return String.format("%s has joined the game as %s", username, playerColor);
      }
      server.joinGame("empty", id);
      System.out.println(drawBoards(null, getGame(id).getBoard(), false, false, null));
      status = Status.OBSERVING;
      currentGameID = id;
      return String.format("%s has joined the game as an observer", username);
    }
    throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
  }

  public String observe(String... params) throws ResponseException, DataAccessException {
    assertSignedIn();
    if (params.length == 1) {
      var id = Integer.parseInt(params[0]);
      server.joinGame("empty", id);
      System.out.println(drawBoards("WHITE", getGame(id).getBoard(), false, false, null));
      status = Status.OBSERVING;
      currentGameID = id;
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

  private ChessGame getGame(int id) throws ResponseException, DataAccessException {
    for (var game : server.listGames()) {
      if (game.gameID() == id) {
        return game.game();
      }
    }
    return null;
  }

  public String move(String... params) throws ResponseException, DataAccessException, InvalidMoveException {
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
      System.out.println(drawBoards(currentColor, getGame(currentGameID).getBoard(), false, false, null));
      return String.format("%s moved (%s, %s) to (%s, %s)", username, startCoordNum, startCoordLetter, endCoordNum, endCoordLetter);
    }
    throw new ResponseException(400, "Expected: <NUMBER> <LETTER> to <NUMBER> <LETTER>");
  }

  private int convertLetter(String letter) {
    char c = letter.charAt(0);
    if (c >= 'a' && c <= 'h') {
      return c - 'a' + 1;
    }
    return 0;
  }

  public String redraw() throws ResponseException, DataAccessException {
    assertSignedIn();
    System.out.println(drawBoards(currentColor, getGame(currentGameID).getBoard(), false, false, null));
    return "";
  }

  public String resign() throws ResponseException {
    assertSignedIn();
    assertPlaying();
    Scanner scanner = new Scanner(System.in);
    System.out.println("Are you sure you want to resign? <yes|no>");
    String line = scanner.nextLine();
    if (line.equalsIgnoreCase("no")) {
      return "Continuing game...";
    } else {
      status = Status.OBSERVING;
      return String.format("%s resigned", username);
    }
  }

  public String showMoves() {
    return null;
  }

  public String leave() {
    return null;
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
              - show moves <NUMBER> <LETTER>
              - leave
              - help
              """;
    } else if (status == status.OBSERVING) {
      return SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE + """
              - redraw
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

  private String drawBoards(String playerColor, ChessBoard board, boolean list, boolean highlight, ChessPosition position) {
    DrawBoard boards = new DrawBoard(board);
    String boardLayout = "";
    if (!list) {
      if (playerColor == "WHITE" || playerColor == "empty") {
        boardLayout = boards.getWhiteDrawings(game, position);
        currentColor = "WHITE";
      } else {
        boardLayout = boards.getBlackDrawings(highlight, game, position);
        currentColor = "BLACK";
      }
    } else {
      boardLayout = boards.drawNormal();
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
