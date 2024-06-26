package service;

import ResponseTypes.LoginResponse;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class Service {
  SQLAuthDAO authDAO = new SQLAuthDAO();
  SQLUserDAO userDAO = new SQLUserDAO();
  SQLGameDAO gameDAO = new SQLGameDAO();

  public Service() {
  }

  public void clear() throws DataAccessException {
    if (authDAO == null || userDAO == null || gameDAO == null) {
      throw new DataAccessException("Error: DAO is null");
    }
    authDAO.clear();
    userDAO.clear();
    gameDAO.clear();
  }

  public Integer addGame(String gameName, String authToken) throws DataAccessException {
    authorize(authToken);
    int gameID = gameDAO.addGame(gameName);
    return gameID;
  }

  public HashSet<GameData> listGames(String authToken) throws DataAccessException {
    authorize(authToken);
    return gameDAO.listGames();
  }

  public void joinGame(String clientColor, int gameID, String authToken) throws DataAccessException {
    String username = authDAO.getAuth(authToken);
    GameData gameData = gameDAO.getGame(gameID);
    authorize(authToken);
    if (username == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    if (gameData == null) {
      throw new DataAccessException("Error: bad request");
    }

    // If clientColor is null or is the string "empty", the user is joined as an observer.
    // Otherwise, they are joined as the color chosen.
    if (clientColor == null || clientColor.equals("empty")) {
      return;
    }
    if (clientColor.equals("WHITE")) {
      if (gameData.whiteUsername() != null) {
        throw new DataAccessException("Error: already taken");
      }
    } else if (clientColor.equals("BLACK")) {
      if (gameData.blackUsername() != null) {
        throw new DataAccessException("Error: already taken");
      }
    }

    // Creates a new game with updated users based on clientColor
    gameDAO.updateGameData(gameData, clientColor, username);
  }

  public AuthData register(String userName, String password, String email) throws DataAccessException {
    if (userDAO.getUser(userName) != null) {
      throw new DataAccessException("Error: already taken");
    }
    userDAO.createUser(userName, password, email);
    return authDAO.createAuth(userName);
  }

  public LoginResponse login(String userName, String password) throws DataAccessException {
    if (userDAO.getUser(userName) == null || userDAO.verifyPassword(userName, password) == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    return new LoginResponse(userName, authDAO.createAuth(userName).authToken());
  }

  public void logout(String authToken) throws DataAccessException {
    authorize(authToken);
    authDAO.deleteAuth(authToken);
  }

  private void authorize(String authToken) throws DataAccessException{
    if (authDAO.getAuth(authToken) == null) {
      throw new DataAccessException("Error: unauthorized");
    }
  }
}
