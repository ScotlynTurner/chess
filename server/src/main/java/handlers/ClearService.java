package handlers;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class ClearService {
  private DataAccess dataAccess;

  public void clear() throws DataAccessException {
    dataAccess.clear();
  }
}
