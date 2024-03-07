package dataAccess;

import model.UserData;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

  public UserData getUser(String username) throws DataAccessException {
    UserData userData = null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
        preparedStatement.setString(1, username);
        try (var resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
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
    UserData userData = null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE password = ?")) {
        preparedStatement.setString(1, password);
        try (var resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            userData = new UserData(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email")
            );
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error verifying password: " + e.getMessage());
    }
    return userData;
  }

  public void createUser(String username, String password, String email) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error creating user: " + e.getMessage());
    }
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
}