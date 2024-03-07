package dataAccess;

import model.AuthData;

public interface AuthDAO {
  String getAuth(String authToken) throws DataAccessException;
  AuthData createAuth(String username) throws DataAccessException;
  void deleteAuth(String authToken) throws DataAccessException;
  void clear() throws DataAccessException;
}
