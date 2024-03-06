package serviceTests;

import ResponseTypes.LoginResponse;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.Service;

import java.util.HashSet;

public class ServiceTests {

  private Service service = new Service();
  private String existingUsername = "existingUser";
  private String existingPassword = "existingPass";
  private String existingEmail = "myEmail@gmail.com";

  @Test
  @DisplayName("Clear")
  public void goodClear() throws DataAccessException {
    Assertions.assertDoesNotThrow(() -> service.clear());
  }
  // I was told that I don't need a negative test for clear

  @Test
  @DisplayName("Register Success")
  public void goodRegister() throws DataAccessException {
    AuthData authToken = service.register(existingUsername, existingPassword, existingEmail);
    Assertions.assertNotNull(authToken);
  }

  @Test
  @DisplayName("Register Fail")
  public void badRegister() throws DataAccessException {
    existingUsername = null;
    service.register(existingUsername, existingPassword, existingEmail);
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.register(existingUsername, existingPassword, existingEmail);
    });
  }

  @Test
  @DisplayName("Login Success")
  public void goodLogin() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    LoginResponse newLogin = service.login(existingUsername, existingPassword);
    Assertions.assertEquals(existingUsername, newLogin.username());
    Assertions.assertNotNull(newLogin.authToken());
  }

  @Test
  @DisplayName("Login Fail")
  public void badLogin() {
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.login(existingUsername, existingPassword);
    });
  }

  @Test
  @DisplayName("Logout Success")
  public void goodLogout() throws DataAccessException {
    AuthData authToken = service.register(existingUsername, existingPassword, existingEmail);
    service.login(existingUsername, existingPassword);
    service.logout(authToken.authToken());
  }

  @Test
  @DisplayName("Logout Fail")
  public void badLogout() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    service.login(existingUsername, existingPassword);
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.logout(null);
    });
  }

  @Test
  @DisplayName("CreateGame Success")
  public void goodCreateGame() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    LoginResponse login = service.login(existingUsername, existingPassword);
    int gameID = service.addGame("myGame", login.authToken());
    Assertions.assertEquals(1, gameID);
    Assertions.assertEquals(1, service.listGames(login.authToken()).size());
  }

  @Test
  @DisplayName("CreateGame Fail")
  public void badCreateGame() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    LoginResponse login = service.login(existingUsername, existingPassword);
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.addGame("myGame", null);
    });
  }

  @Test
  @DisplayName("ListGames Success")
  public void goodListGames() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    LoginResponse login = service.login(existingUsername, existingPassword);
    HashSet<GameData> expectedList = new HashSet<>();
    service.addGame("myGame", login.authToken());
    service.addGame("yay chess", login.authToken());
    service.addGame("queens only", login.authToken());
    expectedList.add(new GameData(1, null, null, "myGame", new ChessGame()));
    expectedList.add(new GameData(2, null, null, "yay chess", new ChessGame()));
    expectedList.add(new GameData(3, null, null, "queens only", new ChessGame()));
    Assertions.assertEquals(expectedList, service.listGames(login.authToken()));
  }

  @Test
  @DisplayName("ListGames Fail")
  public void badListGames() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    LoginResponse login = service.login(existingUsername, existingPassword);
    HashSet<GameData> expectedList = new HashSet<>();
    service.addGame("myGame", login.authToken());
    service.addGame("yay chess", login.authToken());
    service.addGame("queens only", login.authToken());
    expectedList.add(new GameData(1, null, null, "myGame", new ChessGame()));
    expectedList.add(new GameData(2, null, null, "yay chess", new ChessGame()));
    expectedList.add(new GameData(3, null, null, "queens only", new ChessGame()));
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.listGames(null);
    });
  }

  @Test
  @DisplayName("JoinGame Success")
  public void goodJoinGame() throws DataAccessException {
    HashSet<GameData> expectedList = new HashSet<>();

    service.register(existingUsername, existingPassword, existingEmail);
    service.register("newUser",  "newPassword", "new@gmail.com");
    service.register("watchUser", "watchPass", "watch@gmail.com");

    LoginResponse existingUserLogin = service.login(existingUsername, existingPassword);
    LoginResponse newUserLogin = service.login("newUser", "newPassword");
    LoginResponse watchUserLogin = service.login("watchUser", "watchPass");

    service.addGame("myGame", existingUserLogin.authToken());
    service.joinGame("WHITE", 1, existingUserLogin.authToken());
    service.joinGame("BLACK", 1, newUserLogin.authToken());

    expectedList.add(new GameData(1, existingUsername, "newUser", "myGame", new ChessGame()));
    Assertions.assertEquals(expectedList, service.listGames(existingUserLogin.authToken()));

    // join to watch
    Assertions.assertDoesNotThrow(() -> {
      service.joinGame("empty", 1, watchUserLogin.authToken());
    });

  }

  @Test
  @DisplayName("JoinGame Fail")
  public void badJoinGame() throws DataAccessException {
    service.register(existingUsername, existingPassword, existingEmail);
    LoginResponse login = service.login(existingUsername, existingPassword);
    service.addGame("myGame", login.authToken());
    service.joinGame("WHITE", 1, login.authToken());

    // try and join when taken
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.joinGame("WHITE", 1, login.authToken());
    });

    Assertions.assertThrows(DataAccessException.class, () -> {
      service.joinGame("BLACK", 1, null);
    });
  }
}
