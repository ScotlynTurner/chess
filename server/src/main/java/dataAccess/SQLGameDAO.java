package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO{
  final private HashMap<Integer, GameData> games = new HashMap<>();
  private int id = 1;

  public SQLGameDAO() {
    try {
      //DatabaseInitialization.initializeDatabase();
      configureDatabase();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public Integer addGame(String gameName) throws DataAccessException {
    if (gameName == null) {
      throw new DataAccessException("Error: gameName cannot be null");
    }
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")) {
        preparedStatement.setInt(1, id);
        preparedStatement.setNull(2, Types.VARCHAR); // whiteUsername initialized later
        preparedStatement.setNull(3, Types.VARCHAR); // blackUsername initialized later
        preparedStatement.setString(4, gameName);

        // Serialize ChessGame object to JSON string
        String game = new Gson().toJson(new ChessGame());
        preparedStatement.setString(5, game);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error creating game: " + e.getMessage());
    }
    id++;
    return id - 1;
  }

  public HashSet<GameData> listGames() throws DataAccessException {
    HashSet<GameData> gameList = new HashSet<>();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
        try (var resultSet = preparedStatement.executeQuery()) {
          while (resultSet.next()) {
            String serializedGame = resultSet.getString("game");
            ChessGame game = new Gson().fromJson(serializedGame, ChessGame.class);

            GameData gameData = new GameData(
                    resultSet.getInt("gameID"),
                    resultSet.getString("whiteUsername"),
                    resultSet.getString("blackUsername"),
                    resultSet.getString("gameName"),
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
    if (id >= this.id || id < 0) {
      throw new DataAccessException("Error: Invalid ID.");
    }
    GameData gameData = null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID = ?")) {
        preparedStatement.setInt(1, id);
        try (var resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            String serializedGame = resultSet.getString("game");
            ChessGame game = new Gson().fromJson(serializedGame, ChessGame.class);
            gameData = new GameData(
                    resultSet.getInt("gameID"),
                    resultSet.getString("whiteUsername"),
                    resultSet.getString("blackUsername"),
                    resultSet.getString("gameName"),
                    game
            );
          } else {
            return null;
          }
        }
      }
    } catch (Exception e) {
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
    if (game == null) {
      throw new DataAccessException("Error: Game cannot be null.");
    }
    try (var conn = DatabaseManager.getConnection()) {
      if (clientColorChange.equals("WHITE")) {
        try (var preparedStatement=conn.prepareStatement("UPDATE games SET whiteUsername = ? WHERE gameID = ?")) {
          preparedStatement.setString(1, username);
          preparedStatement.setInt(2, game.gameID());
          preparedStatement.executeUpdate();
        }
      } else {
        try (var preparedStatement=conn.prepareStatement("UPDATE games SET blackUsername = ? WHERE gameID = ?")) {
          preparedStatement.setString(1, username);
          preparedStatement.setInt(2, game.gameID());
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error updating game: " + e.getMessage());
    }
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS games (
        gameID int NOT NULL,
        whiteUsername varchar(256),
        blackUsername varchar(256),
        gameName varchar(256) NOT NULL,
        game TEXT(65535) NOT NULL,
        PRIMARY KEY (gameID),
        INDEX(whiteUsername),
        INDEX(blackUsername),
        INDEX(gameName)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """,
          """
    ALTER TABLE games MODIFY whiteUsername VARCHAR(256) DEFAULT NULL;
    """,
          """
    ALTER TABLE games MODIFY blackUsername VARCHAR(256) DEFAULT NULL;
    """
  };


  private void configureDatabase() throws DataAccessException {
    createDatabaseIfNone(createStatements);
  }

  static void createDatabaseIfNone(String[] createStatements) throws DataAccessException {
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
