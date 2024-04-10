package server.websocket;

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

  public void broadcastExcludeUser(String excludeUsername, ServerMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.username.equals(excludeUsername)) {
          c.send(notification.toString());
        }
      } else {
        removeList.add(c);
      }
    }


    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.username);
    }
  }

  public void broadcastRoot(String username, ServerMessage notification) throws IOException {
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (c.username.equals(username)) {
          c.send(notification.toString());
          break;
        }
      }
      connections.remove(c.username);
    }
  }

  public void broadcastAll(ServerMessage notification) throws IOException {
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        c.send(notification.toString());
      }
    }

    // Clean up any connections that were left open.
    for (var c : connections.values()) {
      connections.remove(c.username);
    }
  }
}
