package dataAccess;

public interface UserDAO {
  String getUser(String username) throws DataAccessException;
  String verifyPassword(String userName, String password);
  void createUser(String username, String password, String email);
  void clear();
}
