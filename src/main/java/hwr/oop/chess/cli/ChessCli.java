package hwr.oop.chess.cli;

import hwr.oop.chess.GameAlreadyExistsException;
import hwr.oop.chess.GameNotFoundException;
import hwr.oop.chess.IllegalMoveBecauseKingIsInCheckException;
import hwr.oop.chess.IllegalPromotionException;
import hwr.oop.chess.Position;
import hwr.oop.chess.RemiWasNotOfferedException;
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
  private static final String GAME_NOT_EXIST = "The game does not exist. Please create this game first!";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_RESET = "\u001B[0m ";

  private final PrintStream out;
  private final GameLogic gameLogic;
  private Game currentGame;

  public ChessCli(OutputStream out, GameLogic gameLogic) {
    this.out = new PrintStream(out);
    this.gameLogic = gameLogic;
  }

  public void handle(List<String> arguments) {
    if (arguments.isEmpty()) {
      printNoCommandMessage();
      return;
    }

    String command = arguments.getFirst();
    switch (command) {
      case "help" -> handleHelp(arguments);
      case "fen" -> handleFEN(arguments);
      case "create" -> handleCreate(arguments);
      case "load" -> handleLoad(arguments);
      case "move" -> handleMove(arguments);
      case "show-moves" -> handleShowMoves(arguments);
      case "promote" -> handlePromotePawn(arguments);
      case "resign" -> handleResign(arguments);
      case "offer-remi" -> handleOfferRemi(arguments);
      case "accept-remi" -> handleAccept(arguments);
      default -> handleUnknownCommand(command);
    }
  }

  private void printNoCommandMessage() {
    out.println("I don't see anything here...");
    out.println("You haven't entered a command. Usage: chess <command> [options]");
    out.println("For help and further information: chess help");
  }

  private void handleHelp(List<String> arguments) {
    if (arguments.size() != 1) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess help");
      return;
    }

    printHelp();
  }

  private void handleFEN(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess fen <ID>");
      return;
    }

    String gameID = arguments.get(1);
    printFENNotation(gameID);
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

  private void handleLoad(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess load <ID>");
      return;
    }

    String gameID = arguments.get(1);
    loadGame(gameID);
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

  private void handlePromotePawn(List<String> arguments) {
    if (arguments.size() != 6) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess promote <FROM> to <TYPE> on <ID>");
      return;
    }

    String from = arguments.get(1);
    String desiredType = arguments.get(3);
    String gameID = arguments.get(5);
    promotePawn(from, desiredType, gameID);
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

  private void handleOfferRemi(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess offer-remi <ID>");
      return;
    }

    String gameID = arguments.get(1);
    offerRemi(gameID);
  }

  private void handleAccept(List<String> arguments) {
    if (arguments.size() != 2) {
      out.println(INVALID_COMMAND);
      out.println("Usage: chess accept-remi <ID>");
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
    out.println("  - fen <ID>: Display the FEN notation of a chess game");
    out.println("  - load <ID>: Load a chess game");
    out.println("  - move <FROM> <TO> on <ID>: Move a chess piece to a valid position");
    out.println("  - show-moves <FROM> on <ID>: Get the possible moves for a chess piece");
    out.println("  - promote <FROM> to <TYPE> on <ID>: Promote a pawn to a chess piece");
    out.println("  - resign <ID>: Resign the current game");
    out.println("  - offer-remi <ID>: Offer a remi");
    out.println("  - accept-remi <ID>: Accept a remi");
    out.println("  - help: Display this help message");
  }

  private void printFENNotation(String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      String fenNotation = gameLogic.getFENNotation(currentGame);
      out.println("This is the FEN notation of " + gameID + ":");
      out.println(fenNotation);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void startGame (String playerWhiteName, String playerBlackName, String gameID) {
    try {
      Player playerWhite = gameLogic.loadPlayer(playerWhiteName);
      Player playerBlack = gameLogic.loadPlayer(playerBlackName);
      gameLogic.createGame(playerWhite, playerBlack, gameID);
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

  private void loadGame (String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      out.println("Loading game with ID: " + gameID);
      currentGame = gameLogic.loadGame(gameID);
      out.println(GAME_WITH_ID + gameID + " loaded successfully.");
      printChessboard(gameID);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void movePiece (String from, String to, String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      boolean isCheckMate = gameLogic.moveTo(from, to, currentGame);
      out.println("Moving piece in game " + gameID + " from " + from + " to " + to);
      gameLogic.saveGame(currentGame);
      printChessboard(gameID);
      if (isCheckMate) {
        String victoryMessage = gameLogic.endGame(currentGame);
        out.println(victoryMessage);
      }
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    } catch (IllegalMoveException | IllegalMoveBecauseKingIsInCheckException e) {
      out.println(e.getMessage());
    }
  }

  private void showMoves(String from, String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      Position position = parsePosition(from);
      List<Position> possibleMoves = getPossibleMoves(position, currentGame);
      List<Position> captureMoves = new ArrayList<>();
      Piece piece = currentGame.getBoard().getPieceAtPosition(position);
      for (Position pos : possibleMoves) {
        Piece targetPiece = currentGame.getBoard().getPieceAtPosition(pos);
        if (isEnemyPiece(piece, targetPiece)) {
          captureMoves.add(pos);}}

      possibleMoves.removeAll(captureMoves);

      out.println("Possible moves for piece at position " + from + ": " + possibleMovesToString(possibleMoves));
      out.println("Capture moves for piece at position " + from + ": " + possibleMovesToString(captureMoves));
      printChessboardHighlighted(gameID, possibleMoves, captureMoves);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void promotePawn(String from, String desiredType, String gameID) {
     try {
       loadCurrentGameIfNecessary(gameID);
       gameLogic.promotePiece(currentGame, from, desiredType);
       out.println("Promoting pawn in game " + gameID + " from Position " + from + " to a " + desiredType);
       printChessboard(gameID);
     } catch (GameNotFoundException e) {
       out.println(GAME_NOT_EXIST);
       out.println(e.getMessage());
     } catch (IllegalPromotionException e) {
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

  private void offerRemi (String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      gameLogic.offerRemi(currentGame);
      out.println("Offer remi on " + gameID);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    }
  }

  private void acceptRemi (String gameID) {
    try {
      loadCurrentGameIfNecessary(gameID);
      gameLogic.acceptRemi(currentGame);
      String victoryMessage = gameLogic.endGame(currentGame);
      out.println(victoryMessage);
    } catch (GameNotFoundException e) {
      out.println(GAME_NOT_EXIST);
      out.println(e.getMessage());
    } catch (RemiWasNotOfferedException e) {
      out.println(e.getMessage());
    }
  }

  public void loadCurrentGameIfNecessary(String gameID) throws GameNotFoundException {
    if (currentGame == null || !currentGame.getId().equals(gameID)) {
      currentGame = gameLogic.loadGame(gameID);
    }
  }

  public void printChessboard(String gameID) {
    printChessboardHighlighted(gameID, new ArrayList<>(), new ArrayList<>());
  }

  public void printChessboardHighlighted(String gameID, List<Position> highlightPositions, List<Position> capturePositions) {
    try {
      loadCurrentGameIfNecessary(gameID);
      out.println("    a b c d e f g h\n    ---------------");
      for (int row = 7; row >= 0; row--) {
        out.print((row + 1) + " | ");
        printChessboardRowWithHighlights(row, highlightPositions, capturePositions);
        out.println("| " + (row + 1));
      }
      out.println("    a b c d e f g h\n    _______________");
    } catch (NullPointerException e) {
      out.println("You have to create a game first!");
      out.println(e.getMessage());
    }
  }

  public void printChessboardRowWithHighlights(int row, List<Position> highlightPositions, List<Position> capturePositions) {
    for (int column = 0; column < 8; column++) {
      Position pos = new Position(row, column);
      Piece piece = currentGame.getBoard().getBoard().get(row).get(column);
      if (capturePositions.contains(pos)) {
        out.print(ANSI_RED + (piece != null ? piece.getSymbol() : "*") + ANSI_RESET);
      } else if (highlightPositions.contains(pos)) {
        out.print(ANSI_GREEN + (piece != null ? piece.getSymbol() : "*") + ANSI_RESET);
      } else {
        out.print((piece != null ? piece.getSymbol() : ".") + " ");
      }
    }
  }

  public boolean isEnemyPiece(Piece piece, Piece targetPiece) {
    return piece != null && targetPiece != null && !piece.getColor().equals(targetPiece.getColor());
  }

  public Position parsePosition(String positionStr) {
    int column = positionStr.charAt(0) - 'a';
    int row = Character.getNumericValue(positionStr.charAt(1)) - 1;
    return new Position(row, column);
  }

  public List<Position> getPossibleMoves(Position position, Game game) {
    List<Position> possibleMoves = new ArrayList<>();
    Piece piece = game.getBoard().getPieceAtPosition(position);
    if (piece != null) {
      possibleMoves = piece.possibleMoves();
    }
    return possibleMoves;
  }

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
