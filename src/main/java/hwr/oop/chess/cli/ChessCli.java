package hwr.oop.chess.cli;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class ChessCli {

  private final PrintStream out;

  public ChessCli(OutputStream out) {
    this.out = new PrintStream(out);
  }

  public void handle(List<String> arguments) {
    if (arguments.isEmpty()) {
      out.println("Invalid command. Usage: chess <command> [options]");
      out.println("For help and further information: chess help");
      return;
    }

    String command = arguments.get(0);
    switch (command) {
      case "help" -> {
        if (arguments.size() != 1) {
          out.println("Invalid command. Usage: chess help");
          return;
        }

        printHelp();
      }
      case "create" -> {
        if (arguments.size() != 4) {
          out.println("Invalid command. Usage: chess create <ID> <Player1Name> <Player2Name>");
          return;
        }

        String gameId = arguments.get(1);
        String playerOneName = arguments.get(2);
        String playerTwoName = arguments.get(3);
        startGame(gameId, playerOneName, playerTwoName);
      }
      case "save" -> {
        if (arguments.size() != 2) {
          out.println("Invalid command. Usage: chess save <ID>");
          return;
        }

        String gameToSave = arguments.get(1);
        saveGame(gameToSave);
      }
      case "load" -> {
        if (arguments.size() != 2) {
          out.println("Invalid command. Usage: chess load <ID>");
          return;
        }

        String gameToLoad = arguments.get(1);
        loadGame(gameToLoad);
      }
      case "on" -> {
        if (arguments.size() != 5) {
          out.println("Invalid command. Usage: chess on <ID> move <FROM> <TO>");
          return;
        }

        String gameId = arguments.get(1);
        String from = arguments.get(3);
        String to = arguments.get(4);
        movePiece(gameId, from, to);
      }
      default -> out.println("Unknown command: " + command);
    }
  }

  private void printHelp() {
    out.println("Supported commands:");
    out.println("  - create <ID> <Player1Name> <Player2Name>: Start a new chess game");
    out.println("  - save <ID>: Save a chess game");
    out.println("  - load <ID>: Load a saved chess game");
    out.println("  - on <ID> move <FROM> <TO>: Move a chess piece to a valid position");
    out.println("  - help: Display this help message");
  }

  private void startGame(String gameId, String playerOneName, String playerTwoName) {
    out.println("Welcome to chess in Java!");
    out.println("Chess game created with ID: " + gameId);
    out.println("Hello " + playerOneName + " and " + playerTwoName + ".");
    out.println("Let's start the game.\nHave fun!");
  }

  private void saveGame(String gameId) {
    out.println("Game with ID " + gameId + " saved successfully.");
  }

  private void loadGame(String gameId) {
    out.println("Loading game with ID: " + gameId);
    out.println("Game " + gameId + " loaded successfully.");
  }

  private void movePiece(String gameId, String from, String to) {
    out.println("Moving piece in game " + gameId + " from " + from + " to " + to);
    out.println("Piece was successfully moved.");
  }
}
