package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class GameDAOTests {
  GameDAO gameDAO = new SQLGameDAO();

  @AfterEach
  void setUp() throws DataAccessException {
    gameDAO.clear();
  }

  @Test
  @DisplayName("Add Game Success")
  void goodAddGame() throws DataAccessException {
    Assertions.assertDoesNotThrow(() -> gameDAO.addGame("Testing Game"));
  }

  @Test
  @DisplayName("Add Game Fail")
  void badAddGame() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameDAO.addGame(null);
    });
  }

  @Test
  @DisplayName("List Games Success")
  void goodListGames() throws DataAccessException {
    HashSet<GameData> expectedList = new HashSet<>();
    gameDAO.addGame("myGame");
    gameDAO.addGame("yay chess");
    gameDAO.addGame("queens only");
    expectedList.add(new GameData(1, null, null, "myGame", new ChessGame()));
    expectedList.add(new GameData(2, null, null, "yay chess", new ChessGame()));
    expectedList.add(new GameData(3, null, null, "queens only", new ChessGame()));
    Assertions.assertEquals(expectedList, gameDAO.listGames());
  }

  @Test
  @DisplayName("List Games Fail")
  void badListGames() throws DataAccessException {
    HashSet<GameData> expectedList = new HashSet<>();
    gameDAO.addGame("myGame");
    gameDAO.addGame("yay chess");
    gameDAO.addGame("queens only");
    expectedList.add(new GameData(1, null, null, "myGame", new ChessGame()));
    expectedList.add(new GameData(2, null, null, "yay chess", new ChessGame()));
    expectedList.add(new GameData(3, "hey", null, "queens only", new ChessGame()));
    Assertions.assertNotEquals(expectedList, gameDAO.listGames());
  }

  @Test
  @DisplayName("Get Game Success")
  void goodGetGame() throws DataAccessException {
    gameDAO.addGame("myGame");
    GameData testGame = new GameData(1, null, null, "myGame", new ChessGame());
    Assertions.assertEquals(testGame, gameDAO.getGame(1));
  }

  @Test
  @DisplayName("Get Game Fail")
  void badGetGame() throws DataAccessException {
    gameDAO.addGame("myGame");
    Assertions.assertThrows(DataAccessException.class, () ->{
      gameDAO.getGame(2);
    });
  }

  @Test
  @DisplayName("Update Game Success")
  void goodUpdate() throws DataAccessException {
    gameDAO.addGame("myGame");
    GameData game = gameDAO.getGame(1);
    gameDAO.updateGameData(game, "WHITE", "myUsername");

    GameData testGame = new GameData(1, "myUsername", null, "myGame", new ChessGame());

    Assertions.assertEquals(testGame, gameDAO.getGame(1));
  }

  @Test
  @DisplayName("Update Game Fail")
  void badUpdate() throws DataAccessException {
    gameDAO.addGame("myGame");
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameDAO.updateGameData(null, "BLACK", "myUsername");
    });
  }

  @Test
  @DisplayName("Clear Games Success")
  void clearGames() throws DataAccessException {
    Assertions.assertDoesNotThrow(() -> gameDAO.clear());
  }
}
