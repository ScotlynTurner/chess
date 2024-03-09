package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

  public SQLUserDAO() {
    try {
      configureDatabase();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public UserData getUser(String username) throws DataAccessException {
    UserData userData = null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
        preparedStatement.setString(1, username);
        try (var resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            if (resultSet.getString("username") == null) {
              return null;
            }
            userData = new UserData(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email")
            );
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error getting user: " + e.getMessage());
    }
    return userData;
  }

  public UserData verifyPassword(String userName, String password) throws DataAccessException {
    String hashedPassword = getUser(userName).password();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    boolean passwordMatch = encoder.matches(password, hashedPassword);
    if (passwordMatch) {
      return getUser(userName);
    } else {
      return null; // Password does not match
    }
  }

  public void createUser(String username, String password, String email) throws DataAccessException {
    String hashedPassword = hashPassword(password);
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, hashedPassword);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error creating user: " + e.getMessage());
    }
  }

  private String hashPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(password);

    return hashedPassword;
  }

  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM user")) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error clearing table: " + e.getMessage());
    }
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              INDEX(username),
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
  };


  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Error: " + ex.getMessage());
    }
  }
}