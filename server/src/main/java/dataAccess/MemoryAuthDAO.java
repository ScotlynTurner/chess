package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
  final private HashMap<String, AuthData> auth = new HashMap<>();

  public String getAuth(String authToken) {
    AuthData authData = auth.get(authToken);
    if (authData == null) {
      return null;
    }
    return authData.username();
  }

  public String createAuth(String username) {
    String authToken = UUID.randomUUID().toString();
    auth.put(username, new AuthData(authToken, username));
    return authToken;
  }

  public void deleteAuth(String authToken) {
    auth.remove(authToken);
  }

  public void clear() {
    auth.clear();
  }
}
