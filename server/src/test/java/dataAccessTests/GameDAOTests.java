package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.HashSet;

public class GameDAOTests {
  GameDAO gameDAO = new SQLGameDAO();
  ChessGame game = new ChessGame();

  @BeforeEach
  void setUp() throws DataAccessException {
    gameDAO.clear();
    game.getBoard().resetBoard();
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
    expectedList.add(new GameData(1, null, null, "myGame", game));
    expectedList.add(new GameData(2, null, null, "yay chess", game));
    expectedList.add(new GameData(3, null, null, "queens only", game));
    Assertions.assertEquals(expectedList, gameDAO.listGames());
  }

  @Test
  @DisplayName("List Games Fail")
  void badListGames() throws DataAccessException {
    HashSet<GameData> expectedList = new HashSet<>();
    gameDAO.addGame("myGame");
    gameDAO.addGame("yay chess");
    gameDAO.addGame("queens only");
    expectedList.add(new GameData(1, null, null, "myGame", game));
    expectedList.add(new GameData(2, null, null, "yay chess", game));
    expectedList.add(new GameData(3, "hey", null, "queens only", game));
    Assertions.assertNotEquals(expectedList, gameDAO.listGames());
  }

  @Test
  @DisplayName("Get Game Success")
  void goodGetGame() throws DataAccessException {
    gameDAO.addGame("myGame");
    GameData testGame = new GameData(1, null, null, "myGame", game);
    Assertions.assertEquals(testGame, gameDAO.getGame(1));
  }

  @Test
  @DisplayName("Get Game Fail")
  void badGetGame() throws DataAccessException {
    gameDAO.addGame("myGame");
    Assertions.assertThrows(DataAccessException.class, () ->{
      gameDAO.getGame(-2);
    });
  }

  @Test
  @DisplayName("Update Game Success")
  void goodUpdate() throws DataAccessException {
    gameDAO.addGame("myGame");
    GameData game = gameDAO.getGame(1);
    gameDAO.updateGameData(game, "WHITE", "myUsername");

    GameData testGame = new GameData(1, "myUsername", null, "myGame", game.game());

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
