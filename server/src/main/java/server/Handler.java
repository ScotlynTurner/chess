package server;

import ResponseTypes.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import spark.Request;
import spark.Response;
import service.Service;

import java.util.HashSet;
import java.util.Map;

public class Handler {
  private Service service = new Service();

  public Handler() {
  }

  public Object registration(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      JsonElement username = requestBody.get("username");
      JsonElement password = requestBody.get("password");
      JsonElement email = requestBody.get("email");
      if (username == null || password == null || email == null) {
        throw new DataAccessException("Error: bad request");
      }

      AuthData authToken = service.register(username.getAsString(), password.getAsString(), email.getAsString());
      return new Gson().toJson(authToken);

    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
  }

  public Object login(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      String userName = requestBody.get("username").getAsString();
      String password = requestBody.get("password").getAsString();
      LoginResponse loginResponse = service.login(userName, password);
      return new Gson().toJson(loginResponse);
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
  }

  public Object logout(Request req, Response res) throws DataAccessException {
    try {
      String authToken = req.headers("authorization");
      service.logout(authToken);
      return "{}";
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
  }

  public Object listGames(Request req, Response res) throws DataAccessException {
    try {
      String authToken = req.headers("authorization");
      HashSet<GameData> games = service.listGames(authToken);
      return new Gson().toJson(new ListGamesResponse(games));
    } catch (DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
  }

  public Object createGame(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      JsonElement gameName = requestBody.get("gameName");
      if (gameName == null || gameName.isJsonNull()) {
        throw new DataAccessException("Error: bad request");
      }

      String authToken = req.headers("authorization");
      int gameID = service.addGame(gameName.getAsString(), authToken);
      return new Gson().toJson(new CreateGameResponse(gameID));

    } catch(DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
  }

  public Object joinGame(Request req, Response res) throws DataAccessException {
    try {
      JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
      String authToken = req.headers("authorization");
      JsonElement clientColor = requestBody.get("playerColor");
      JsonElement gameID = requestBody.get("gameID");
      if (gameID == null) {
        throw new DataAccessException("Error: bad request");
      }

      if (clientColor == null) {
        service.joinGame(null, gameID.getAsInt(), authToken);
      } else if (clientColor.equals("WHITE")){
        service.joinGame("WHITE", gameID.getAsInt(), authToken);
      } else {
        service.joinGame("BLACK", gameID.getAsInt(), authToken);
      }
      return "{}";

    } catch(DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
  }

  public Object clear(Request req, Response res) throws DataAccessException {
    try {
      if (service != null) {
        service.clear();
        res.status(200);
        return "{}";
      }
    } catch(DataAccessException e) {
      exceptionHandler(e, req, res);
    }
    return res.body();
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
    res.body(new Gson().toJson(Map.of("message",ex.getMessage())));
  }
}
