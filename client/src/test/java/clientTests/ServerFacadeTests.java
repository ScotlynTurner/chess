package clientTests;

import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

  private static Server server;
  static ServerFacade facade;
  static String baseUrl = "http://localhost:";
  private String username = "player1";
  private String password = "testPassword";
  private String email = "p1@gmail.com";

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
  @DisplayName("Login")
  public void login() throws ResponseException {
    var authData = facade.register(username, password, email);

    assertTrue(authData.authToken().length() > 10);
  }

}