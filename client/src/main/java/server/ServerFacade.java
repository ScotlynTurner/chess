package server;

import ResponseTypes.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.AuthData;
import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;

public class ServerFacade {

  private final String serverUrl;
  private String authToken;

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
      AuthData authData = this.makeRequest("POST", path, requestBody, AuthData.class);
      authToken = authData.authToken();
      return authData;
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
      LoginResponse loginResponse = this.makeRequest("POST", path, requestBody, LoginResponse.class);
      authToken = loginResponse.authToken();
      return loginResponse;
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void logout() throws ResponseException {
    try {
      var path = "/session";
      this.makeRequest("DELETE", path, null, Void.class);
    } catch (Exception e) {
      System.err.println("Logout failed with 500 response: " + e.getMessage());
      throw new ResponseException(500, e.getMessage());
    }
  }

  public int addGame(String gameName) throws ResponseException {
    try {
      var path = "/game";
      var requestBody = Map.of(
              "gameName", gameName
      );
      return this.makeRequest("POST", path, requestBody, Integer.class);
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

  public HashSet<GameData> listGames() throws ResponseException {
    try {
      var path = "/game";
      record listGamesResponse(HashSet<GameData> games) {
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

      if (authToken != null) {
        http.setRequestProperty("Authorization", authToken);
      }

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      if (responseClass == Void.class) {
        return null;
      } else {
        return readBody(http, responseClass);
      }
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
    try (InputStream respBody = http.getInputStream()) {
      if (responseClass != null && respBody.available() > 0) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != Integer.class) {
          response = new Gson().fromJson(reader, responseClass);
        } else {
          JsonObject object = new Gson().fromJson(reader, JsonObject.class);
          JsonElement gameID = object.get("gameID");
          if (gameID != null && !gameID.isJsonNull()) {
            response = (T) Integer.valueOf(gameID.getAsJsonPrimitive().getAsInt());
          }
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }

  // Only for testing purposes
  public void invalidateAuthToken() {
    authToken = null;
  }
}
