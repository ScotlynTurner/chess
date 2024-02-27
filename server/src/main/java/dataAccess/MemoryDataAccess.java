package dataAccess;

import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
  private int nextId = 1;
  final private HashMap<Integer, ChessGame> games = new HashMap<>();

  public ChessGame addGame(ChessGame game) throws DataAccessException {
    return null;
  }

  public Collection<ChessGame> listGames() throws DataAccessException {
    return null;
  }

  public ChessGame getGame(int id) throws DataAccessException {
    return null;
  }

  public void deleteGame(Integer id) throws DataAccessException {

  }

  public void clear() throws DataAccessException {
    games.clear();
  }
}
