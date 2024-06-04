package hwr.oop.chess.cli;

import hwr.oop.chess.GameAlreadyExistsException;
import hwr.oop.chess.GameNotFoundException;
import hwr.oop.chess.Position;
import hwr.oop.chess.pieces.IllegalMoveException;
import hwr.oop.chess.pieces.Piece;
import hwr.oop.chess.game.Game;
import hwr.oop.chess.GameLogic;

import hwr.oop.chess.player.Player;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ChessCli {

  private static final String INVALID_COMMAND = "Oops... Invalid command.";
  private static final String GAME_WITH_ID = "Game with ID ";
  private static final String GAME_ALREADY_EXISTS = "The game already exists. Please use another game ID!";
  private static final String GAME_NOT_EXIST = "The game does not exist. Please use another game ID!";


  private final PrintStream out;
  private final GameLogic gameLogic;
  private Game currentGame;

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

    String gameID = arguments.get(1);
    String playerWhiteName = arguments.get(2);
    String playerBlackName = arguments.get(3);
    startGame(playerWhiteName, playerBlackName, gameID);
  }


  private void handleMove(List<String> arguments) {
    if (arguments.size() != 5) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess move <FROM> <TO> on <ID>");
      return;
    }

    String from = arguments.get(1);
    String to = arguments.get(2);
    String gameID = arguments.get(4);
    movePiece(from, to, gameID);
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

    String gameID = arguments.get(1);
    resign(gameID);
  }

  private void handleAccept(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess accept <ID>");
      return;
    }

    String gameID = arguments.get(1);
    acceptRemi(gameID);
  }

  private void handleUnknownCommand(String command) {
    out.println("Unknown command: " + command);
    out.println("For help and further information: chess help");
  }


  private void printHelp() {
    out.println("Supported commands:");
    out.println("  - create <ID> <PlayerWhite> <PlayerBlack>: Start a new chess game");
    out.println("  - save <ID>: Save a chess game");
    out.println("  - move <FROM> <TO> on <ID>: Move a chess piece to a valid position");
    out.println("  - resign <ID>: Resign the current game");
    out.println("  - accept <ID>: Accept a remi");
    out.println("  - help: Display this help message");
  }

  private void printChessboard(String gameID) {
    printChessboardHighlighted(gameID, new ArrayList<>());
  }

  private void printChessboardHighlighted(String gameID, List<Position> highlightPositions) {
    try {
      loadCurrentGameIfNecessary(gameID);
      out.println("________________");
      for (int row = 7; row >= 0; row--) {
        for (int column = 0; column < 8; column++) {
          Position pos = new Position(row, column);
          Piece piece = currentGame.getBoard().getBoard().get(row).get(column);
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
      out.println("You have to create a game first!");
      out.println(e.getMessage());
    }
  }

  /*private void printChessboard(String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      out.println("________________");
      for (int row = 7; row >= 0; row--) {
        for (int column = 0; column < 8; column++) {
          Piece piece = currentGame.getBoard().getBoard().get(row).get(column);
          out.print((piece != null ? piece.getSymbol() : ".")+" ");
        }
        out.println();
      }
      out.println("________________");
    } catch (NullPointerException e) {
      out.println("You have to create a game first!");
      out.println(e.getMessage());
    }
  }*/

  private void startGame (String playerWhiteName, String playerBlackName, String gameID) {
    try {
      Player playerWhite = gameLogic.loadPlayer(playerWhiteName);
      Player playerBlack = gameLogic.loadPlayer(playerBlackName);
      gameLogic.createGame(playerWhite, playerBlack, gameID);
      loadCurrentGameIfNecessary(gameID);
      out.println("Welcome to chess in Java!");
      out.println("Chess game created with ID: " + gameID);
      out.println("Hello " + playerWhiteName + " and " + playerBlackName + ".");
      out.println("Let's start the game.");
      out.println("Have fun!");
      printChessboard(gameID);
    } catch (GameAlreadyExistsException e) {
      out.println(GAME_ALREADY_EXISTS);
      out.println(e.getMessage());
    }
  }

//  private void loadGame (String gameID) {
//    try {
//      loadCurrentGameIfNecessary(gameID);
//      out.println("Loading game with ID: " + gameID);
//      currentGame = gameLogic.loadGame(gameID);
//      out.println(GAME_WITH_ID + gameID + " loaded successfully.");
//      printChessboard(gameID);
//    } catch (GameNotFoundException e) {
//      out.println(GAME_NOT_EXIST);
//      out.println(e.getMessage());
//    }
//  }

  private void movePiece (String from, String to, String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      gameLogic.moveTo(from, to, currentGame);
      out.println("Moving piece in game " + gameID + " from " + from + " to " + to);
      gameLogic.saveGame(currentGame);
      loadCurrentGameIfNecessary(gameID); //TODO: es könnte sein, dass wir diese Zeile nicht brauchen @Gero mal testen
      printChessboard(gameID);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    } catch (IllegalMoveException e) {
      out.println(e.getMessage());
    }
  }

  private void showMoves(String from, String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      List<Position> possibleMoves = gameLogic.getPossibleMoves(from, currentGame);
      out.println("Possible moves for piece at position " + from + ": " + possibleMovesToString(possibleMoves));
      printChessboardHighlighted(gameID, possibleMoves);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void resign (String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      gameLogic.resign(currentGame);
      out.println(GAME_WITH_ID + gameID + " resigned successfully.");
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void acceptRemi (String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      gameLogic.acceptRemi(currentGame);
      out.println(GAME_WITH_ID + gameID + " accepted remi successfully.");
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void loadCurrentGameIfNecessary(String gameID) throws GameNotFoundException {
    if (currentGame == null || !currentGame.getId().equals(gameID)) {
      currentGame = gameLogic.loadGame(gameID);
    }
  }

//  private Position parsePosition(String positionStr) {
//    int column = positionStr.charAt(0) - 'a';
//    int row = Character.getNumericValue(positionStr.charAt(1)) - 1;
//    return new Position(row, column);
//  }

//  public List<Position> getPossibleMoves(Position position, Game game) {
//    List<Position> possibleMoves = new ArrayList<>();
//    Piece piece = game.getBoard().getPieceAtPosition(position);
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
