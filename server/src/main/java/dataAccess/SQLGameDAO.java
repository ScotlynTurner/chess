package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO{
  final private HashMap<Integer, GameData> games = new HashMap<>();
  private int id = 1;

  public SQLGameDAO() throws DataAccessException {
    configureDatabase();
  }

  public Integer addGame(String gameName) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameName) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, gameName);
        preparedStatement.executeUpdate();
        try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            id = generatedKeys.getInt(1);
          } else {
            throw new SQLException("Creating game failed, no ID obtained.");
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error creating game: " + e.getMessage());
    }
    games.put(id, new GameData(id, null, null, gameName, new ChessGame()));
    id++;
    return id - 1;
  }

  public HashSet<GameData> listGames() throws DataAccessException {
    HashSet<GameData> gameList = new HashSet<>();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
        try (var resultSet = preparedStatement.executeQuery()) {
          while (resultSet.next()) {
            ChessGame game = new ChessGame();
            GameData gameData = new GameData(
                    resultSet.getInt("gameID"),
                    resultSet.getString("username"),
                    resultSet.getString("whiteUsername"),
                    resultSet.getString("blackUsername"),
                    game
            );
            gameList.add(gameData);
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error getting games: " + e.getMessage());
    }
    return gameList;
  }

  public GameData getGame(int id) throws DataAccessException {
    GameData gameData = null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID = ?")) {
        preparedStatement.setInt(1, id);
        try (var resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            ChessGame game = new ChessGame();
            gameData = new GameData(
                    resultSet.getInt("gameID"),
                    resultSet.getString("username"),
                    resultSet.getString("whiteUsername"),
                    resultSet.getString("blackUsername"),
                    game
            );
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error getting game: " + e.getMessage());
    }
    return gameData;
  }

  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM games")) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error clearing table: " + e.getMessage());
    }
  }

  public void updateGameData(GameData game, String clientColorChange, String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("UPDATE games SET gameName = ? WHERE id = ?")) {
        preparedStatement.setString(1, game.gameName());
        preparedStatement.setInt(2, game.gameID());
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error updating game: " + e.getMessage());
    }
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(game)
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
