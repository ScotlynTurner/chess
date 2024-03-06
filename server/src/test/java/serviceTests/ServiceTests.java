package serviceTests;

import ResponseTypes.LoginResponse;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;
import service.Service;

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
  public void badLogin() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, () -> {
      service.login(existingUsername, existingPassword);
    });
  }

  @Test
  @DisplayName("Logout Success")
  public void goodLogout() throws DataAccessException {

  }

  @Test
  @DisplayName("Logout Fail")
  public void badLogout() throws DataAccessException {

  }

  @Test
  @DisplayName("CreateGame Success")
  public void goodCreateGame() throws DataAccessException {

  }

  @Test
  @DisplayName("CreateGame Fail")
  public void badCreateGame() throws DataAccessException {

  }

  @Test
  @DisplayName("ListGames Success")
  public void goodListGames() throws DataAccessException {

  }

  @Test
  @DisplayName("ListGames Fail")
  public void badListGames() throws DataAccessException {

  }

  @Test
  @DisplayName("JoinGame Success")
  public void goodJoinGame() throws DataAccessException {

  }

  @Test
  @DisplayName("JoinGame Fail")
  public void badJoinGame() throws DataAccessException {

  }
}
