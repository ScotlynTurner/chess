package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
  Integer addGame(String gameName) throws DataAccessException;
  HashSet<GameData> listGames() throws DataAccessException;
  GameData getGame(int id) throws DataAccessException;
  void clear() throws DataAccessException;
  void updateGameData(GameData game, String clientColorChange, String username) throws DataAccessException;
}
