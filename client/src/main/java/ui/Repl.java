package ui;

import server.Server;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
  private final ChessClient client;
  private String serverURL;
  private static Server server = new Server();

  public Repl(String serverUrl, int port) {
    client = new ChessClient(serverUrl);
    this.serverURL = serverUrl;
    server.run(port);
  }

  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to the Scotlyn's chess. Type Help to get started.");
    System.out.println("Running on port " + serverURL);
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
    server.stop();
  }

  private void printPrompt() {
    System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_GREEN);
  }
}