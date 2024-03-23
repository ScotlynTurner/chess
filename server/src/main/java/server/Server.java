package server;

import dataAccess.DataAccessException;
import spark.*;

public class Server {
  private Handler handler = new Handler();

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
    res.status(500);
  }

  public void stop() {
    Spark.stop();
    Spark.awaitStop();
  }

  public static void main(String[] args) {
    new Server().run(8080);
  }

  private Object registration(Request req, Response res) throws DataAccessException {
    res.status(200);
    return handler.registration(req, res);
  }

  private Object login(Request req, Response res) throws DataAccessException {
    res.status(200);
    return handler.login(req, res);
  }

  private Object logout(Request req, Response res) throws DataAccessException {
    res.status(200);
    return handler.logout(req, res);
  }

  private Object listGames(Request req, Response res) throws DataAccessException {
    res.status(200);
    return handler.listGames(req, res);
  }

  private Object createGame(Request req, Response res) throws DataAccessException {
    res.status(200);
    return handler.createGame(req, res);
  }

  private Object joinGame(Request req, Response res) throws DataAccessException {
    res.status(200);
    return handler.joinGame(req, res);
  }

  private Object clear(Request req, Response res) throws DataAccessException {
    return handler.clear(req, res);
  }
}
