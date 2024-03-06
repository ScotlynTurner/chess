package dataAccess;

import model.UserData;

public interface UserDAO {
  UserData getUser(String username) throws DataAccessException;
  UserData verifyPassword(String userName, String password);
  void createUser(String username, String password, String email);
  void clear();
}
