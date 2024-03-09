package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {
  AuthDAO authDAO = new SQLAuthDAO();
  private String existingUsername = "existingUser";

  @AfterEach
  void setUp() throws DataAccessException {
    authDAO.clear();
  }

  @Test
  @DisplayName("Get Auth Success")
  void goodGetAuth() throws DataAccessException {
    AuthData existingAuthData = authDAO.createAuth(existingUsername);
    Assertions.assertEquals(existingAuthData.username(), authDAO.getAuth(existingAuthData.authToken()));
  }

  @Test
  @DisplayName("Get Auth Fail")
  void badGetAuth() throws DataAccessException {
    authDAO.createAuth(existingUsername);
    Assertions.assertThrows(DataAccessException.class, () -> {
      authDAO.getAuth(null);
    });
  }

  @Test
  @DisplayName("Create Auth Success")
  void goodCreateAuth() {
    Assertions.assertDoesNotThrow(() -> authDAO.createAuth(existingUsername));
  }

  @Test
  @DisplayName("Create Auth Fail")
  void badCreateAuth() {
    Assertions.assertThrows(DataAccessException.class, () -> {
      authDAO.createAuth(null);
    });
  }

  @Test
  @DisplayName("Delete Auth Success")
  void goodDeleteAuth() throws DataAccessException {
    AuthData existingAuthData = authDAO.createAuth(existingUsername);
    Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(existingAuthData.authToken()));
  }

  @Test
  @DisplayName("Delete Auth Fail")
  void badDeleteAuth() throws DataAccessException {
    authDAO.createAuth(existingUsername);
    Assertions.assertThrows(DataAccessException.class, () -> {
      authDAO.deleteAuth(null);
    });
  }

  @Test
  @DisplayName("Clear Auth")
  void clearAuth() {
    Assertions.assertDoesNotThrow(() -> authDAO.clear());
  }

}
