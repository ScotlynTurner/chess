package dataAccess;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitialization {
  public static void initializeDatabase() throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement()) {

//      // Drop existing tables
//      stmt.executeUpdate("DROP TABLE IF EXISTS games;");
//      stmt.executeUpdate("DROP TABLE IF EXISTS user;");
//      stmt.executeUpdate("DROP TABLE IF EXISTS auth;");

      stmt.executeUpdate("CREATE TABLE games (gameID int NOT NULL, whiteUsername varchar(256), blackUsername varchar(256), gameName varchar(256) NOT NULL, game TEXT(65535) NOT NULL, PRIMARY KEY (gameID), INDEX(whiteUsername), INDEX(blackUsername), INDEX(gameName)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
      stmt.executeUpdate("CREATE TABLE user (username varchar(256) NOT NULL, password varchar(256) NOT NULL, email varchar(256) NOT NULL, PRIMARY KEY (username)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
      stmt.executeUpdate("CREATE TABLE auth (username varchar(256) NOT NULL, authToken varchar(256) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");

    } catch (Exception e) {
      throw new DataAccessException("Error: " + e.getMessage());
    }
  }
}
