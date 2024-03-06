package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
  private int id = 1;
  final private HashMap<Integer, GameData> games = new HashMap<>();

  public Integer addGame(String gameName) throws DataAccessException {
    games.put(id, new GameData(id, null, null, gameName, new ChessGame()));
    id++;
    return id - 1;
  }

  public HashSet<GameData> listGames() throws DataAccessException {
    return new HashSet<>(games.values());
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

  public void updateGameData(GameData game, String clientColorChange, String username) throws DataAccessException {
    if (clientColorChange.equals("WHITE")) {
      games.put(game.gameID(), new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
    } else {
      games.put(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
    }
  }
}
