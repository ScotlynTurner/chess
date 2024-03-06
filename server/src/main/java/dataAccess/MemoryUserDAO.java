package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
  final private HashMap<String, UserData> user = new HashMap<>();

  public UserData getUser(String username) throws DataAccessException {
    if (user.get(username) != null) {
      return user.get(username);
    } else {
      return null;
    }
  }

  public UserData verifyPassword(String userName, String password) {
    if (user.get(userName).password().equals(password)) {
      return user.get(userName);
    }
    return null;
  }

  public void createUser(String username, String password, String email) {
    user.put(username, new UserData(username, password, email));
  }

  public void clear() {
    user.clear();
  }
}
