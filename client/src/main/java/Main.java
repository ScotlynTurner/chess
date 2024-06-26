import server.ResponseException;
import server.Server;
import ui.Repl;

public class Main {

    public static void main(String[] args) throws ResponseException {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        var port = 8080;
        System.out.println("Started test HTTP server on " + port);

        new Repl(serverUrl, port).run();
    }
}