package ui;

import chess.server.ResponseException;
import chess.server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
  private String username = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  public ChessClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
  }

//  public String eval(String input) {
//    try {
//      var tokens = input.toLowerCase().split(" ");
//      var cmd = (tokens.length > 0) ? tokens[0] : "help";
//      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
//      return switch (cmd) {
//        case "register" -> register(params);
//        case "login" -> login(params);
//        case "create" -> createGame(params);
//        case "list" -> listGames();
//        case "join" -> joinGame(params);
//        case "observe" -> observe(params);
//        case "logout" -> logout();
//        case "quit" -> "quit";
//        default -> help();
//      };
//    } catch (ResponseException ex) {
//      return ex.getMessage();
//    }
//  }

//  public String login(String... params) throws ResponseException {
//    if (params.length >= 1) {
//      state = State.SIGNEDIN;
//      username = String.join("-", params);
//      server.register(username, password, email);
//      return String.format("Logged in as %s", username);
//    }
//    throw new ResponseException(400, "Expected: <yourname>");
//  }
//
//  public String rescuePet(String... params) throws ResponseException {
//    assertSignedIn();
//    if (params.length >= 2) {
//      var name = params[0];
//      var type = PetType.valueOf(params[1].toUpperCase());
//      var pet = new Pet(0, name, type);
//      pet = server.addPet(pet);
//      return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
//      return "";
//    }
//    throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG>");
//  }
//
//  public String listGames() throws ResponseException {
//    assertSignedIn();
//    var games = server.listGames();
//    var result = new StringBuilder();
//    var gson = new Gson();
//    for (var pet : pets) {
//      result.append(gson.toJson(pet)).append('\n');
//    }
//    return result.toString();
//  }
//
//  public String adoptPet(String... params) throws ResponseException {
//    assertSignedIn();
//    if (params.length == 1) {
//      try {
//        var id = Integer.parseInt(params[0]);
//        var pet = getPet(id);
//        if (pet != null) {
//          server.deletePet(id);
//          return String.format("%s says %s", pet.name(), pet.sound());
//        }
//      } catch (NumberFormatException ignored) {
//      }
//    }
//    throw new ResponseException(400, "Expected: <pet id>");
//  }
//
//  public String adoptAllPets() throws ResponseException {
//    assertSignedIn();
//    var buffer = new StringBuilder();
//    for (var pet : server.listPets()) {
//      buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//    }
//
//    server.deleteAllPets();
//    return buffer.toString();
//  }
//
//  public String signOut() throws ResponseException {
//    assertSignedIn();
//    ws.leavePetShop(visitorName);
//    ws = null;
//    state = State.SIGNEDOUT;
//    return String.format("%s left the shop", visitorName);
//  }
//
//  private Pet getPet(int id) throws ResponseException {
//    for (var pet : server.listPets()) {
//      if (pet.id() == id) {
//        return pet;
//      }
//    }
//    return null;
//  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    """;
    }
    return """
                - create <NAME>
                - list
                - join <ID> [WHITE|BLACK|<empty>]
                - observe <ID>
                - logout
                - quit
                """;
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.SIGNEDOUT) {
      throw new ResponseException(400, "You must sign in");
    }
  }
}
