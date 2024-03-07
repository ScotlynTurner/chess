package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

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
}
