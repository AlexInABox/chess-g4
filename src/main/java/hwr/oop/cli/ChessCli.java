package hwr.oop.cli;

import hwr.oop.MatchAlreadyExistsException;
import hwr.oop.MatchNotFoundException;
import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.pieces.Piece;
import hwr.oop.match.Match;
import hwr.oop.persistence.FileBasePersistence;
import hwr.oop.GameLogic;

import hwr.oop.player.Player;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class ChessCli {

  private static final String INVALID_COMMAND = "Oops... Invalid command.";
  private static final String MATCH_WITH_ID = "Match with ID ";
  private static final String MATCH_ALREADY_EXISTS = "The match already exists. Please use another match ID!";
  private static final String MATCH_NOT_EXIST = "The match does not exist. Please use another match ID!";
  private static final String ALL_MATCHES_PATH = "data/allMatches.txt";
  private static final String ALL_PLAYERS_PATH = "data/allPlayers.txt";

  private final PrintStream out;
  private final FileBasePersistence persistence = new FileBasePersistence();
  private final GameLogic gameLogic = new GameLogic(persistence, ALL_MATCHES_PATH, ALL_PLAYERS_PATH);
  private Match currentMatch;

  public ChessCli(OutputStream out) {
    this.out = new PrintStream(out);
  }

  public void handle(List<String> arguments) {
    if (arguments.isEmpty()) {
      out.println("I don't see anything here...");
      out.println("You haven't entered a command. Usage: chess <command> [options]");
      out.println("For help and further information: chess help");
      return;
    }

    String command = arguments.getFirst();
    switch (command) {
      case "help" -> handleHelp(arguments);
      case "print" -> handlePrint(arguments);
      case "create" -> handleCreate(arguments);
      case "save" -> handleSave(arguments);
      case "load" -> handleLoad(arguments);
      case "on" -> handleMove(arguments);
      case "resign" -> handleResign(arguments);
      case "accept" -> handleAccept(arguments);
      default -> handleUnknownCommand(command);
    }
  }

  private void handleHelp(List<String> arguments) {
    if (arguments.size() != 1) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess help");
      return;
    }

    printHelp();
  }

  private void handlePrint(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess print");
      return;
    }

    String matchToPrint = arguments.get(1);
    printChessboard(matchToPrint);
  }

  private void handleCreate(List<String> arguments) {
    if (arguments.size() != 4) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess create <ID> <Player1Name> <Player2Name>");
      return;
    }

    String matchID = arguments.get(1);
    String playerWhiteName = arguments.get(2);
    String playerBlackName = arguments.get(3);
    startMatch(playerWhiteName, playerBlackName, matchID);
  }

  private void handleSave(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess save <ID>");
      return;
    }

    String matchToSave = arguments.get(1);
    saveMatch(matchToSave);
  }

  private void handleLoad(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess load <ID>");
      return;
    }

    String matchToLoad = arguments.get(1);
    loadMatch(matchToLoad);
  }

  private void handleMove(List<String> arguments) {
    if (arguments.size() != 5) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess on <ID> move <FROM> <TO>");
      return;
    }

    String matchID = arguments.get(1);
    String from = arguments.get(3);
    String to = arguments.get(4);
    movePiece(matchID, from, to);
  }

  private void handleResign(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess resign <ID>");
      return;
    }

    String matchID = arguments.get(1);
    resign(matchID);
  }

  private void handleAccept(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess accept <ID>");
      return;
    }

    String matchID = arguments.get(1);
    acceptRemi(matchID);
  }

  private void handleUnknownCommand(String command) {
    out.println("Unknown command: " + command);
    out.println("For help and further information: chess help");
  }


  private void printHelp() {
    out.println("Supported commands:");
    out.println("  - print: Print chessboard for current match");
    out.println("  - create <ID> <PlayerWhite> <PlayerBlack>: Start a new chess match");
    out.println("  - save <ID>: Save a chess match");
    out.println("  - load <ID>: Load a saved chess match");
    out.println("  - on <ID> move <FROM> <TO>: Move a chess piece to a valid position");
    out.println("  - resign <ID>: Resign the current match");
    out.println("  - accept <ID>: Accept a remi");
    out.println("  - help: Display this help message");
  }

  private void printChessboard(String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      out.println("________________");
      for (int row = 7; row >= 0; row--) {
        for (int column = 0; column < 8; column++) {
          Piece piece = currentMatch.getBoard().getBoard().get(row).get(column);
          out.print((piece != null ? piece.getSymbol() : ".")+" ");
        }
        out.println();
      }
      out.println("________________");
    } catch (NullPointerException e) {
      out.println("You have to create a match first!");
      out.println(e.getMessage());
    }
  }

  private void startMatch (String playerWhiteName, String playerBlackName, String matchID) {
    try {
      Player playerWhite = gameLogic.loadPlayer(playerWhiteName);
      Player playerBlack = gameLogic.loadPlayer(playerBlackName);
      gameLogic.createMatch(playerWhite, playerBlack, matchID);
      loadCurrentMatchIfNecessary(matchID);
      out.println("Welcome to chess in Java!");
      out.println("Chess match created with ID: " + matchID);
      out.println("Hello " + playerWhiteName + " and " + playerBlackName + ".");
      out.println("Let's start the match.");
      out.println("Have fun!");
      printChessboard(matchID);
    } catch (MatchAlreadyExistsException e) {
      out.println(MATCH_ALREADY_EXISTS);
      out.println(e.getMessage());
    }
  }

  private void saveMatch (String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      currentMatch = gameLogic.loadMatch(matchID);
      gameLogic.saveMatch(currentMatch);
      out.println(MATCH_WITH_ID + matchID + " saved successfully.");
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void loadMatch (String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      out.println("Loading match with ID: " + matchID);
      currentMatch = gameLogic.loadMatch(matchID);
      out.println(MATCH_WITH_ID + matchID + " loaded successfully.");
      printChessboard(matchID);
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void movePiece (String matchID, String from, String to) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      gameLogic.moveTo(from, to, currentMatch);
      out.println("Moving piece in match " + matchID + " from " + from + " to " + to);
      gameLogic.saveMatch(currentMatch);
      printChessboard(matchID);
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
      out.println(e.getMessage());
    } catch (IllegalMoveException e) {
      out.println(e.getMessage());
    }
  }

  private void resign (String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      gameLogic.resign(currentMatch);
      out.println(MATCH_WITH_ID + matchID + " resigned successfully.");
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void acceptRemi (String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      gameLogic.acceptRemi(currentMatch);
      out.println(MATCH_WITH_ID + matchID + " accepted remi successfully.");
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void loadCurrentMatchIfNecessary(String matchID) throws MatchNotFoundException {
    if (currentMatch == null || !currentMatch.getId().equals(matchID)) {
      currentMatch = gameLogic.loadMatch(matchID);
    }
  }
}
