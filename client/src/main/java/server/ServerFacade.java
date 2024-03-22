package server;

import ResponseTypes.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.GameDAO;
import model.AuthData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  public AuthData register(String username, String password, String email) throws ResponseException {
    try {
      var path="/user";
      var requestBody=Map.of(
              "username", username,
              "password", password,
              "email", email
      );

      return this.makeRequest("POST", path, requestBody, AuthData.class);
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public LoginResponse login(String username, String password) throws ResponseException {
    try {
      var path = "/session";
      var requestBody = Map.of(
              "username", username,
              "password", password
      );
      return this.makeRequest("POST", path, requestBody, LoginResponse.class);
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void logout() throws ResponseException {
    try {
      var path = "/session";
      this.makeRequest("DELETE", path, null, null);
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public Object addGame(String gameName) throws ResponseException {
    try {
      var path = "/game";
      return this.makeRequest("POST", path, gameName, JsonObject.class);
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void clear() throws ResponseException {
    try {
      var path = "/db";
      this.makeRequest("DELETE", path, null, null);
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public GameDAO[] listGames() throws ResponseException {
    try {
      var path = "/game";
      record listGamesResponse(GameDAO[] games) {
      }
      var response = this.makeRequest("GET", path, null, listGamesResponse.class);
      return response.games();
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void joinGame(String playerColor, int gameID) throws ResponseException {
    try {
      var path = "/game";
      var requestBody = Map.of(
              "playerColor", playerColor,
              "gameID", gameID
      );
      this.makeRequest("PUT", path, requestBody, null);
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}
