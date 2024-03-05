package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
  final private HashMap<String, UserData> user = new HashMap<>();

  public String getUser(String username) throws DataAccessException {
    if (user.get(username) != null) {
      return user.get(username).username();
    } else {
      throw new DataAccessException("Error: bad request");
    }
  }

  public String verifyPassword(String userName, String password) {
    if (user.get(userName).password().equals(password)) {
      return password;
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
