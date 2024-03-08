package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

  public SQLAuthDAO() throws DataAccessException {
    configureDatabase();
  }

  public String getAuth(String authToken) throws DataAccessException {
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
    } catch (SQLException e) {
      throw new DataAccessException("Error getting auth: " + e.getMessage());
    }
    return authData.username();
  }

  public AuthData createAuth(String username) throws DataAccessException{
    String authToken = UUID.randomUUID().toString();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES (?, ?)")) {
        preparedStatement.setString(1, authToken);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error creating auth: " + e.getMessage());
    }
    return new AuthData(authToken, username);
  }

  public void deleteAuth(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
        preparedStatement.setString(1, authToken);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting auth: " + e.getMessage());
    }
  }

  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM auth")) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
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
    DatabaseManager.createDatabase();
    try (var conn=DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement=conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Error: " + ex.getMessage());
    }
  }
}
