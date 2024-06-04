package hwr.oop.chess.cli;

import hwr.oop.chess.MatchAlreadyExistsException;
import hwr.oop.chess.MatchNotFoundException;
import hwr.oop.chess.Position;
import hwr.oop.chess.pieces.IllegalMoveException;
import hwr.oop.chess.pieces.Piece;
import hwr.oop.chess.match.Match;
import hwr.oop.chess.GameLogic;

import hwr.oop.chess.player.Player;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ChessCli {

  private static final String INVALID_COMMAND = "Oops... Invalid command.";
  private static final String MATCH_WITH_ID = "Match with ID ";
  private static final String MATCH_ALREADY_EXISTS = "The match already exists. Please use another match ID!";
  private static final String MATCH_NOT_EXIST = "The match does not exist. Please use another match ID!";


  private final PrintStream out;
  private final GameLogic gameLogic;
  private Match currentMatch;

  public ChessCli(OutputStream out, GameLogic gameLogic) {
    this.out = new PrintStream(out);
    this.gameLogic = gameLogic;
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
      case "create" -> handleCreate(arguments);
      case "move" -> handleMove(arguments);
      case "show-moves" -> handleShowMoves(arguments);
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


  private void handleMove(List<String> arguments) {
    if (arguments.size() != 5) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess move <FROM> <TO> on <ID>");
      return;
    }

    String from = arguments.get(1);
    String to = arguments.get(2);
    String matchID = arguments.get(4);
    movePiece(from, to, matchID);
  }

  private void handleShowMoves(List<String> arguments) {
    if (arguments.size() != 4) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess show-moves <FROM> on <ID>");
      return;
    }

    String from = arguments.get(1);
    String gameID = arguments.get(3);
    showMoves(from, gameID);
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
    out.println("  - create <ID> <PlayerWhite> <PlayerBlack>: Start a new chess match");
    out.println("  - save <ID>: Save a chess match");
    out.println("  - move <FROM> <TO> on <ID>: Move a chess piece to a valid position");
    out.println("  - resign <ID>: Resign the current match");
    out.println("  - accept <ID>: Accept a remi");
    out.println("  - help: Display this help message");
  }

  private void printChessboard(String matchID) {
    printChessboardHighlighted(matchID, new ArrayList<>());
  }

  private void printChessboardHighlighted(String matchID, List<Position> highlightPositions) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      out.println("________________");
      for (int row = 7; row >= 0; row--) {
        for (int column = 0; column < 8; column++) {
          Position pos = new Position(row, column);
          Piece piece = currentMatch.getBoard().getBoard().get(row).get(column);
          if (highlightPositions.contains(pos)) {
            out.print((piece != null ? piece.getSymbol() : "*") + " ");
          } else {
            out.print((piece != null ? piece.getSymbol() : ".") + " ");
          }
        }
        out.println();
      }
      out.println("________________");
    } catch (NullPointerException e) {
      out.println("You have to create a match first!");
      out.println(e.getMessage());
    }
  }

  /*private void printChessboard(String matchID) {
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
  }*/

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

//  private void loadMatch (String matchID) {
//    try {
//      loadCurrentMatchIfNecessary(matchID);
//      out.println("Loading match with ID: " + matchID);
//      currentMatch = gameLogic.loadMatch(matchID);
//      out.println(MATCH_WITH_ID + matchID + " loaded successfully.");
//      printChessboard(matchID);
//    } catch (MatchNotFoundException e) {
//      out.println(MATCH_NOT_EXIST);
//      out.println(e.getMessage());
//    }
//  }

  private void movePiece (String from, String to, String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      gameLogic.moveTo(from, to, currentMatch);
      out.println("Moving piece in match " + matchID + " from " + from + " to " + to);
      gameLogic.saveMatch(currentMatch);
      loadCurrentMatchIfNecessary(matchID); //TODO: es könnte sein, dass wir diese Zeile nicht brauchen @Gero mal testen
      printChessboard(matchID);
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
      out.println(e.getMessage());
    } catch (IllegalMoveException e) {
      out.println(e.getMessage());
    }
  }

  private void showMoves(String from, String matchID) {
    try {
      loadCurrentMatchIfNecessary(matchID);
      List<Position> possibleMoves = gameLogic.getPossibleMoves(from, currentMatch);
      out.println("Possible moves for piece at position " + from + ": " + possibleMovesToString(possibleMoves));
      printChessboardHighlighted(matchID, possibleMoves);
    } catch (MatchNotFoundException e) {
      out.println(MATCH_NOT_EXIST);
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

//  private Position parsePosition(String positionStr) {
//    int column = positionStr.charAt(0) - 'a';
//    int row = Character.getNumericValue(positionStr.charAt(1)) - 1;
//    return new Position(row, column);
//  }

//  public List<Position> getPossibleMoves(Position position, Match match) {
//    List<Position> possibleMoves = new ArrayList<>();
//    Piece piece = match.getBoard().getPieceAtPosition(position);
//    if (piece != null) {
//      possibleMoves = piece.possibleMoves();
//    }
//    return possibleMoves;
//  }

  private String possibleMovesToString(List<Position> positions) {
    StringBuilder sb = new StringBuilder();
    for (Position pos : positions) {
      sb.append(positionToString(pos)).append(", ");
    }
    return !sb.isEmpty() ? sb.substring(0, sb.length() - 2) : "";
  }

  private String positionToString(Position position) {
    char column = (char) ('a' + position.column());
    int row = position.row() + 1;
    return "" + column + row;
  }
}
