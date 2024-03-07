package dataAccess;

import model.GameData;

import java.util.HashSet;

public class SQLGameDAO implements GameDAO{
  public Integer addGame(String gameName) throws DataAccessException {
    return null;
  }

  public HashSet<GameData> listGames() throws DataAccessException {
    return null;
  }

  public GameData getGame(int id) throws DataAccessException {
    return null;
  }

  public void clear() throws DataAccessException {

  }

  public void updateGameData(GameData game, String clientColorChange, String username) throws DataAccessException {

  }
}
