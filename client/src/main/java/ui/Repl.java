package ui;

import server.ResponseException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.ServerMessageObserver;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements ServerMessageObserver {
  private final ChessClient client;
  private String serverURL;

  public Repl(String serverUrl, int port) throws ResponseException {
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
        notify(new Error(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
      }
    }
    System.out.println();
  }

  private void printPrompt() {
    System.out.print(SET_BG_BRIGHT_WHITE + "\n" + SET_TEXT_CUSTOM_MAROON + ">>> ");
  }

  public void notify(ServerMessage notification) {
    if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
      System.out.println(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLACK + "game loading...");
    } else if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
      System.out.println(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLACK + notification.getServerMessageType() + SET_BG_BRIGHT_WHITE);
    } else {
      System.out.println(SET_BG_CUSTOM_MAROON + SET_TEXT_CUSTOM_WHITE + notification.getServerMessageType() + SET_BG_BRIGHT_WHITE);
    }
  }
}