package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

  public SQLAuthDAO() {
    try {
      configureDatabase();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public String getAuth(String authToken) throws DataAccessException {
    if (authToken == null) {
      throw new DataAccessException("Error: AuthToken cannot be null.");
    }
    AuthData authData = null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE authToken = ?")) {
        preparedStatement.setString(1, authToken);
        try (var resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            authData = new AuthData(
                    resultSet.getString("authToken"),
                    resultSet.getString("username")
            );
          }
        }
      }
      return authData.username();
    } catch (Exception e) {
      throw new DataAccessException("Error: unauthorized");
    }
  }

  public AuthData createAuth(String username) throws DataAccessException{
    if (username == null) {
      throw new DataAccessException("Error: username cannot be null");
    }
    String authToken = UUID.randomUUID().toString();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES (?, ?)")) {
        preparedStatement.setString(1, authToken);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error creating auth: " + e.getMessage());
    }
    return new AuthData(authToken, username);
  }

  public void deleteAuth(String authToken) throws DataAccessException {
    if (authToken == null) {
      throw new DataAccessException("Error: username cannot be null");
    }
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
        preparedStatement.setString(1, authToken);
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error deleting auth: " + e.getMessage());
    }
  }

  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM auth")) {
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error clearing table: " + e.getMessage());
    }
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              INDEX(authToken),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
  };


  private void configureDatabase() throws DataAccessException {
    SQLGameDAO.createDatabaseIfNone(createStatements);
  }
}
