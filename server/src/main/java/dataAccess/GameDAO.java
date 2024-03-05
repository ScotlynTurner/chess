package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
  Integer addGame(String gameName) throws DataAccessException;
  Collection<GameData> listGames() throws DataAccessException;
  GameData getGame(int id) throws DataAccessException;
  void deleteGame(Integer id) throws DataAccessException;
  void clear() throws DataAccessException;
}
