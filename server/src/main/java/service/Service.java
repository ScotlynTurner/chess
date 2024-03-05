package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;

import java.util.Collection;

public class Service {
  AuthDAO authDAO;
  UserDAO userDAO;
  GameDAO gameDAO;

  public Service() {
  }

  public void clear() throws DataAccessException {
    authDAO.clear();
    userDAO.clear();
    gameDAO.clear();
  }

  public Object addGame(String gameName, String authToken) throws DataAccessException {
    if (authDAO.getAuth(authToken) == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    int gameID = gameDAO.addGame(gameName);
    return gameID;
  }

  public Collection<GameData> listGames(String authToken) throws DataAccessException {
    if (authDAO.getAuth(authToken) == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    return gameDAO.listGames();
  }

  public void joinGame(String clientColor, int gameID, String authToken) throws DataAccessException {
    String username = authDAO.getAuth(authToken);
    GameData gameData = gameDAO.getGame(gameID);
    if (username == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    if (gameData == null) {
      throw new DataAccessException("Error: bad request");
    }
    if (clientColor == "BLACK") {
      if (gameData.blackUser() != null) {
        throw new DataAccessException("Error: already taken");
      }
      gameData = new GameData(gameData.gameID(), gameData.whiteUser(), username, gameData.gameName(), gameData
              .game());
    }
    if (clientColor == "WHITE") {
      if (gameData.whiteUser() != null) {
        throw new DataAccessException("Error: already taken");
      }
      gameData = new GameData(gameData.gameID(), username, gameData.blackUser(), gameData.gameName(), gameData
              .game());
    }
  }

  public String register(String userName, String password, String email) throws DataAccessException {
    if (userDAO.getUser(userName) != null) {
      throw new DataAccessException("Error: already taken");
    }
    userDAO.createUser(userName, password, email);
    return authDAO.createAuth(userName);
  }

  public String login(String userName, String password) throws DataAccessException {
    if (userDAO.getUser(userName) == null || userDAO.verifyPassword(userName, password) == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    return authDAO.createAuth(userName);
  }

  public void logout(String authToken) throws DataAccessException {
    if (authDAO.getAuth(authToken) == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    authDAO.deleteAuth(authToken);
  }
}
