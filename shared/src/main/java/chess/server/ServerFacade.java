package chess.server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.GameDAO;
import org.eclipse.jetty.http.MetaData;

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

  public Object register(String username, String password, String email) throws ResponseException {
    var path = "/user";
    var requestBody = Map.of(
            "username", username,
            "password", password,
            "email", email
    );

    return this.makeRequest("POST", path, requestBody, JsonObject.class);
  }

  public Object login(String username, String password) throws ResponseException {
    var path = "/session";
    var requestBody = Map.of(
            "username", username,
            "password", password
    );
    return this.makeRequest("POST", path, requestBody, JsonObject.class);
  }

  public void logout() throws ResponseException {
    var path = "/session";
    this.makeRequest("DELETE", path, null, null);
  }

  public Object addGame(String gameName) throws ResponseException {
    var path = "/game";
    return this.makeRequest("POST", path, gameName, JsonObject.class);
  }

  public void clear() throws ResponseException {
    var path = "/db";
    this.makeRequest("DELETE", path, null, null);
  }

  public Object listGames() throws ResponseException {
    var path = "/game";
    record listGamesResponse(GameDAO[] games) {
    }
    var response = this.makeRequest("GET", path, null, listGamesResponse.class);
    return response.games();
  }

  public void joinGame(String playerColor, int gameID) throws ResponseException {
    var path = "/game";
    var requestBody = Map.of(
            "playerColor", playerColor,
            "gameID", gameID
    );
    this.makeRequest("PUT", path, requestBody, null);
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
