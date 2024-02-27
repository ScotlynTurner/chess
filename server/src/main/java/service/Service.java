package service;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;

import java.util.Collection;

public class Service {
  private DataAccess dataAccess;
  public Service(DataAccess dataAccess) {
  }

  public void clear() throws DataAccessException {
    dataAccess.clear();
  }

  public ChessGame addGame(ChessGame game) throws DataAccessException {
    return dataAccess.addGame(game);
  }
  public Collection<ChessGame> listGames() throws DataAccessException {
    return dataAccess.listGames();
  }
}
