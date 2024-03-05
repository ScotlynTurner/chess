package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
  String getAuth(String authToken);
  String createAuth(String username);
  void deleteAuth(String authToken);
  void clear();
}
