package dataAccess;

import chess.ChessGame;

import java.util.Collection;

public interface DataAccess {

  ChessGame addGame(ChessGame game) throws DataAccessException;

  Collection<ChessGame> listGames() throws DataAccessException;

  ChessGame getGame(int id) throws DataAccessException;

  void deleteGame(Integer id) throws DataAccessException;

  void clear() throws DataAccessException;
}
