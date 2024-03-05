package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;
import service.Service;

public class Handler {
  private Service service;

  public Handler() {
  }

  private void exceptionHandler(DataAccessException ex, Request req, Response res) throws DataAccessException{
    switch (ex.getMessage()) {
      case "Error: bad request":
        res.status(400);
        break;
      case "Error: already taken":
        res.status(403);
        break;
      case "Error: unauthorized":
        res.status(401);
        break;
      default:
        res.status(500);
    }
    res.body(res.status() + " " + ex.getMessage());
  }

  public Object registration(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      String userName = requestBody.get("username").getAsString();
      String password = requestBody.get("password").getAsString();
      String email = requestBody.get("email").getAsString();
      String authToken = service.register(userName, password, email);
      return authToken;
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return "";
  }

  public Object login(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      String userName = requestBody.get("username").getAsString();
      String password = requestBody.get("password").getAsString();
      return new Gson().toJson(service.login(userName, password));
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return "";
  }

  public Object logout(Request req, Response res) throws DataAccessException {
    try {
      String authToken = req.headers("authorization: ");
      service.logout(authToken);
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return "";
  }

  public Object listGames(Request req, Response res) throws DataAccessException {
    try {
      String authToken = req.headers("authorization: ");
      return new Gson().toJson(service.listGames(authToken));
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return null;
  }

  public Object createGame(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      String gameName = requestBody.get("gameName").getAsString();
      String authToken = req.headers("authorization: ");
      int gameID = (int) service.addGame(gameName, authToken);
      return new Gson().toJson(gameID);
    } catch(DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return null;
  }

  public Object joinGame(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      String authToken = req.headers("authorization ");
      String clientColor = requestBody.get("playerColor").getAsString();
      int gameID = requestBody.get("gameID").getAsInt();
      service.joinGame(clientColor, gameID, authToken);
    } catch(DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return null;
  }

  public Object clear(Request req, Response res) throws DataAccessException {
    try {
      if (service != null) {
        service.clear();
        res.status(200);
        return "Database cleared successfully.";
      }
    } catch(DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return null;
  }
}
