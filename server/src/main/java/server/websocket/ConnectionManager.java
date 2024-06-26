package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String username, Session session) {
    var connection = new Connection(username, session);
    connections.put(username, connection);
  }

  public void remove(String username) {
    connections.remove(username);
  }

  public void broadcastExcludeUser(String excludeAuthToken, ServerMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.authToken.equals(excludeAuthToken)) {
          c.send(notification.toString());
        }
      } else {
        removeList.add(c);
      }
    }

    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authToken);
    }
  }

  public void broadcastRoot(String authToken, ServerMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (c.authToken.equals(authToken)) {
          c.send(notification.toString());
          return;  // Exit the loop after sending the notification to the specified user
        }
      } else {
        removeList.add(c);
      }
    }

    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authToken);
    }
  }

  public void broadcastAll(ServerMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        c.send(new Gson().toJson(notification));
      } else {
        removeList.add(c);
      }
    }

    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authToken);
    }
  }
}
