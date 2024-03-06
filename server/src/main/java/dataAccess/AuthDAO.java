package dataAccess;

import model.AuthData;

public interface AuthDAO {
  String getAuth(String authToken);
  AuthData createAuth(String username);
  void deleteAuth(String authToken);
  void clear();
}
