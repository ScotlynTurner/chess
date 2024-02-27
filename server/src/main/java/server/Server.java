package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import handlers.ClearService;
import handlers.CreateGameService;
import service.Service;
import spark.*;

import java.util.Map;

public class Server {
  private Service service;
  public Server() {
  }

  public int run(int desiredPort) {
    Spark.port(desiredPort);

    Spark.staticFiles.location("web");

    Spark.post("/user", this::registration);
    Spark.post("/session", this::login);
    Spark.delete("/session", this::logout);
    Spark.get("/game", this::listGames);
    Spark.post("/game", this::createGame);
    Spark.put("/game", this::joinGame);
    Spark.delete("/db", this::clear);
    Spark.exception(DataAccessException.class, this::exceptionHandler);

    Spark.awaitInitialization();
    return Spark.port();
  }

  private void exceptionHandler(DataAccessException ex, Request req, Response res) {

  }

  public void stop() {
    Spark.stop();
    Spark.awaitStop();
  }

  private Object registration(Request req, Response res) throws DataAccessException {
    return "";
  }

  private Object login(Request req, Response res) throws DataAccessException {
    return "";
  }

  private Object logout(Request req, Response res) throws DataAccessException {
    return "";
  }

  private Object listGames(Request req, Response res) throws DataAccessException {
    res.type("application/json");
    var list = service.listGames().toArray();
    return new Gson().toJson(Map.of("pet", list));
  }

  private Object createGame(Request req, Response res) throws DataAccessException {
    var game = new Gson().fromJson(req.body(), ChessGame.class);
    game = service.addGame(game);
    return new Gson().toJson(game);
  }

  private Object joinGame(Request req, Response res) throws DataAccessException {
    return "";
  }

  private Object clear(Request req, Response res) throws DataAccessException {
    service.clear();
    res.status(200);
    return "";
  }

}
