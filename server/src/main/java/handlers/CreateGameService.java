package handlers;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;

public class CreateGameService {
  private DataAccess dataAccess;

  void CreateGameService() {

  }

  public ChessGame addGame(ChessGame game) throws DataAccessException {

    return dataAccess.addGame(game);
  }

}
