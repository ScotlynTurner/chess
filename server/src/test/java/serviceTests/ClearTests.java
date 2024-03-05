package serviceTests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;
import server.Server;

public class ClearTests {
  private static TestModels.TestUser existingUser;

  private static TestModels.TestUser newUser;

  private static TestModels.TestCreateRequest createRequest;

  private static TestServerFacade serverFacade;
  private static Server server;

  private String existingAuth;

  @BeforeEach
  public void setUp() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);

    serverFacade = new TestServerFacade("localhost", Integer.toString(port));

    existingUser = new TestModels.TestUser();
    existingUser.username = "ExistingUser";
    existingUser.password = "existingUserPassword";
    existingUser.email = "eu@mail.com";

    newUser = new TestModels.TestUser();
    newUser.username = "NewUser";
    newUser.password = "newUserPassword";
    newUser.email = "nu@mail.com";

    createRequest = new TestModels.TestCreateRequest();
    createRequest.gameName = "testGame";
  }
  @AfterAll
  public static void stop() {
    server.stop();
  }

  @Test
  public void clearTestTest() {
    serverFacade.clear();
  }

}
