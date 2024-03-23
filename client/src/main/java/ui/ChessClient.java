package ui;

import chess.ChessBoard;
import model.GameData;
import server.ResponseException;
import server.ServerFacade;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ChessClient {
  private String username = null;
  private final ServerFacade server;
  private State state = State.SIGNEDOUT;

  public ChessClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
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
      server.login(username, password);
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
    formattedGame += drawBoards("WHITE", game.game().getBoard(), true);
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
        System.out.println(drawBoards(playerColor, getBoard(id), false));
        return String.format("%s has joined the game as %s", username, playerColor);
      }
      server.joinGame("empty", id);
      System.out.println(drawBoards(null, getBoard(id), false));
      return String.format("%s has joined the game as an observer", username);
    }
    throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
  }

  public String observe(String... params) throws ResponseException, DataAccessException {
    assertSignedIn();
    if (params.length == 1) {
      var id = Integer.parseInt(params[0]);
      server.joinGame("empty", id);
      System.out.println(drawBoards(null, getBoard(id), false));
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

  private ChessBoard getBoard(int id) throws ResponseException, DataAccessException {
    for (var game : server.listGames()) {
      if (game.gameID() == id) {
        return game.game().getBoard();
      }
    }
    return null;
  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      return  SET_TEXT_CUSTOM_MAROON + SET_BG_CUSTOM_WHITE + """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    - help
                    """;
    }
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

  private String drawBoards(String playerColor, ChessBoard board, boolean list) {
    DrawBoard boards = new DrawBoard(board);
    String boardLayout = "";
    if (!list) {
      if (playerColor == "WHITE" || playerColor == "empty") {
        boardLayout = boards.getWhiteDrawings();
      } else {
        boardLayout = boards.getBlackDrawings();
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
}
