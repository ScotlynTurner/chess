package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDAOTests {
  UserDAO userDAO = new SQLUserDAO();
  private String existingUsername = "existingUser";
  private String existingPassword = "existingPass";
  private String existingEmail = "myEmail@gmail.com";

  @AfterEach
  void setUp() throws DataAccessException {
    userDAO.clear();
  }

  @Test
  @DisplayName("Get User Success")
  void goodGetUser() throws DataAccessException {
    Assertions.assertDoesNotThrow(() -> userDAO.createUser(existingUsername, existingPassword, existingEmail));
    UserData userData = userDAO.getUser(existingUsername);

    // Check that the retrieved user data is not null
    Assertions.assertNotNull(userData);

    // Verify that the hashed passwords match
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    Assertions.assertTrue(encoder.matches(existingPassword, userData.password()));

    // Check that the other fields match
    Assertions.assertEquals(existingEmail, userData.email());
    Assertions.assertEquals(existingUsername, userData.username());
  }

  @Test
  @DisplayName("Get User Fail")
  void badGetUser() throws DataAccessException {
    userDAO.createUser(existingUsername, existingPassword, existingEmail);
    Assertions.assertThrows(DataAccessException.class, () -> {
      userDAO.getUser(null);
    });
  }

  @Test
  @DisplayName("Verify Password Success")
  void goodVerifyPassword() throws DataAccessException {
    userDAO.createUser(existingUsername, existingPassword, existingEmail);
    UserData existingUserData = userDAO.getUser(existingUsername);
    Assertions.assertEquals(existingUserData, userDAO.verifyPassword(existingUsername, existingPassword));
  }

  @Test
  @DisplayName("Verify Password Fail")
  void badVerifyPassword() throws DataAccessException {
    userDAO.createUser(existingUsername, existingPassword, existingEmail);
    Assertions.assertThrows(DataAccessException.class, () -> {
      userDAO.verifyPassword(existingUsername, "incorrectPassword");
    });
  }

  @Test
  @DisplayName("Create User Success")
  void goodCreateUser() {
    Assertions.assertDoesNotThrow(() -> userDAO.createUser(existingUsername, existingPassword, existingEmail));
  }

  @Test
  @DisplayName("Create User Fail")
  void badCreateUser() {
    Assertions.assertThrows(DataAccessException.class, () -> {
      userDAO.createUser(null, null, null);
    });
  }

  @Test
  @DisplayName("Clear User")
  void clearUser() {
    Assertions.assertDoesNotThrow(() -> userDAO.clear());
  }
}
