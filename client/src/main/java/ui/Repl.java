package ui;

import server.ResponseException;
import server.Server;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
  private final ChessClient client;
  private String serverURL;

  public Repl(String serverUrl, int port) {
    client = new ChessClient(serverUrl, this);
    this.serverURL = serverUrl;
  }

  public void run() {
    System.out.println(SET_BG_CUSTOM_WHITE + "\uD83D\uDC36 Welcome to the Scotlyn's chess. Type Help to get started.");
    System.out.println("Running on port " + serverURL);
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_CUSTOM_PINK + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }

  private void printPrompt() {
    System.out.print(SET_BG_BRIGHT_WHITE + "\n" + SET_TEXT_CUSTOM_MAROON + ">>> ");
  }

  public void notify(ServerMessage notification) {
    System.out.println(SET_BG_COLOR_BLUE + notification.getServerMessageType());
    printPrompt();
  }
}