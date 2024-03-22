package clientTests;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

  private static Server server;
  static ServerFacade facade;
  static String baseUrl = "http://localhost:";
  private String username = "player1";
  private String password = "testPassword";
  private String email = "p1@gmail.com";

  private ChessGame game = new ChessGame();

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    var serverURL = baseUrl + port;
    facade = new ServerFacade(serverURL);
  }

  @BeforeEach
  public void clear() throws ResponseException {
    facade.clear();
    game.getBoard().resetBoard();
  }

  @AfterAll
  static void stopServer() {
    server.stop();
  }


  @Test
  @DisplayName("Register Success")
  public void goodRegister() throws ResponseException {
    var authData = facade.register(username, password, email);
    assertTrue(authData.authToken().length() > 10);
  }

  @Test
  @DisplayName("Register Fail")
  public void badRegister() throws ResponseException {
    assertThrows(ResponseException.class, () -> {
      facade.register(null, null, null);
    });
  }


  @Test
  @DisplayName("Login Success")
  public void goodLogin() throws ResponseException {
    facade.register(username, password, email);
    var loginResponse = facade.login(username, password);
    assertEquals(username, loginResponse.username());
    assertTrue(loginResponse.authToken().length() > 10);
  }

  @Test
  @DisplayName("Login Fail")
  public void badLogin() throws ResponseException {
    facade.register(username, password, email);
    assertThrows(ResponseException.class, () -> {
      facade.login(null, null);
    });
  }

  @Test
  @DisplayName("Logout Success")
  public void goodLogout() throws ResponseException {
    facade.register(username, password, email);
    facade.login(username, password);
    assertDoesNotThrow(() -> facade.logout());
  }

  @Test
  @DisplayName("Logout Fail")
  public void badLogout() throws ResponseException {
    facade.register(username, password, email);
    // Logout without logging in
    assertThrows(ResponseException.class, () -> {
      facade.logout();
    });
  }

  @Test
  @DisplayName("Create Game Success")
  public void goodCreate() throws ResponseException {
    facade.register(username, password, email);
    facade.login(username, password);
    int gameID = facade.addGame("Test Game");
    Assertions.assertEquals(1, gameID);
  }

  @Test
  @DisplayName("Create Game Fail")
  public void badCreate() throws ResponseException {
    facade.register(username, password, email);
    facade.login(username, password);
    assertThrows(ResponseException.class, () -> {
      facade.addGame(null);
    });
  }

  @Test
  @DisplayName("List Games Success")
  public void goodList() throws ResponseException {
    HashSet<GameData> expectedList = new HashSet<>();
    facade.register(username, password, email);
    facade.login(username, password);

    int game1 = facade.addGame("Test Game");
    int game2 = facade.addGame("Chessy chess time");
    int game3 = facade.addGame("it's britney");
    expectedList.add(new GameData(game1, null, null, "Test Game", game));
    expectedList.add(new GameData(game2, null, null, "Chessy chess time", game));
    expectedList.add(new GameData(game3, null, null, "it's britney", game));

    Assertions.assertEquals(expectedList, facade.listGames());
  }

  @Test
  @DisplayName("List Games Fail")
  public void badList() throws ResponseException {
    HashSet<GameData> expectedList = new HashSet<>();
    facade.register(username, password, email);
    facade.login(username, password);

    facade.addGame("Test Game");
    facade.addGame("Chessy chess time");
    facade.addGame("it's britney");

    facade.invalidateAuthToken();

    assertThrows(ResponseException.class, () -> {
      facade.listGames();
    });
  }

  @Test
  @DisplayName("Join Game Success")
  public void goodJoin() throws ResponseException {
    HashSet<GameData> expectedList = new HashSet<>();
    facade.register(username, password, email);
    facade.login(username, password);

    int gameID = facade.addGame("Test Game");
    facade.joinGame("WHITE", gameID);

    expectedList.add(new GameData(gameID, username, null, "Test Game", game));

    Assertions.assertEquals(expectedList, facade.listGames());
  }

  @Test
  @DisplayName("Join Game Fail")
  public void badJoin() throws ResponseException {
    HashSet<GameData> expectedList = new HashSet<>();
    facade.register(username, password, email);
    facade.login(username, password);

    int gameID = facade.addGame("Test Game");
    facade.joinGame("WHITE", gameID);

    assertThrows(ResponseException.class, () -> {
      facade.joinGame("WHITE", gameID);
    });
  }

  @Test
  @DisplayName("Clear")
  public void clearTest() {
    assertDoesNotThrow(() -> {
      facade.clear();
    });
  }

}