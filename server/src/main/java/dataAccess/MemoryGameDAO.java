package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
  private int id = 1;
  final private HashMap<Integer, GameData> games = new HashMap<>();

  public Integer addGame(String gameName) throws DataAccessException {
    games.put(id, new GameData(id, null, null, gameName, new ChessGame()));
    id++;
    return id - 1;
  }

  public Collection<GameData> listGames() throws DataAccessException {
    return games.values();
  }

  public GameData getGame(int id) throws DataAccessException {
    return games.get(id);
  }

  public void deleteGame(Integer id) throws DataAccessException {
    games.remove(id);
  }

  public void clear() throws DataAccessException {
    games.clear();
  }
}
