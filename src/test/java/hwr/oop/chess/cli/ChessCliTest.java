package hwr.oop.chess.cli;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import hwr.oop.chess.Color;
import hwr.oop.chess.domain.GameAlreadyExistsException;
import hwr.oop.chess.domain.GameLogic;
import hwr.oop.chess.domain.GameNotFoundException;
import hwr.oop.chess.domain.IllegalPromotionException;
import hwr.oop.chess.Position;
import hwr.oop.chess.domain.RemiWasNotOfferedException;
import hwr.oop.chess.game.Game;
import hwr.oop.chess.pieces.IllegalMoveException;
import hwr.oop.chess.pieces.King;
import hwr.oop.chess.pieces.Pawn;
import hwr.oop.chess.pieces.Piece;
import hwr.oop.chess.player.Player;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChessCliTest {

  private ByteArrayOutputStream outContent;
  @Mock GameLogic gameLogicMock;
  private ChessCli chessCli;

  @BeforeEach
  void setUp() {
    outContent = new ByteArrayOutputStream();
    OutputStream out = new PrintStream(outContent);
    chessCli = new ChessCli(out, gameLogicMock);
  }

  @Test
  void testHandle_NoCommandProvided() {
    // Arrange
    List<String> arguments = new ArrayList<>();

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output)
        .contains("I don't see anything here...")
        .contains("You haven't entered a command. Usage: chess <command> [options]")
        .contains("For help and further information: chess help");
  }

  @ParameterizedTest
  @MethodSource("unknownCommandsProvider")
  void testHandle_UnknownCommand(String command, String expectedOutput) {
    // Arrange
    List<String> arguments = Arrays.asList(command, "123");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Normalize line endings in expected output
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  private static Stream<Arguments> unknownCommandsProvider() {
    return Stream.of(
        Arguments.of("start", "Unknown command: start\nFor help and further information: chess help"),
        Arguments.of("play", "Unknown command: play\nFor help and further information: chess help"));
  }

  @ParameterizedTest
  @MethodSource("invalidArgumentsProvider")
  void testHandle_InvalidArguments(String command, String expectedOutput) {
    // Arrange
    List<String> arguments = Collections.singletonList(command);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Normalize line endings in expected output
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  // Provide invalid arguments for each command
  private static Stream<Arguments> invalidArgumentsProvider() {
    return Stream.of(
        Arguments.of("fen", "Oops... Invalid command.\nUsage: chess fen <ID>"),
        Arguments.of("create", "Oops... Invalid command.\nUsage: chess create <ID> <Player1Name> <Player2Name>"),
        Arguments.of("load", "Oops... Invalid command.\nUsage: chess load <ID>"),
        Arguments.of("move", "Oops... Invalid command.\nUsage: chess move <FROM> <TO> on <ID>"),
        Arguments.of("show-moves", "Oops... Invalid command.\nUsage: chess show-moves <FROM> on <ID>"),
        Arguments.of("promote", "Oops... Invalid command.\nUsage: chess promote <FROM> to <TYPE> on <ID>"),
        Arguments.of("resign", "Oops... Invalid command.\nUsage: chess resign <ID>"),
        Arguments.of("offer-remi", "Oops... Invalid command.\nUsage: chess offer-remi <ID>"),
        Arguments.of("accept-remi", "Oops... Invalid command.\nUsage: chess accept-remi <ID>"));
  }

  @Test
  void testHandle_InvalidArguments_HelpCommandWithExtraArgument() {
    // Arrange
    List<String> arguments = Arrays.asList("help", "extraArgument");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Normalize line endings in expected output
    String expectedOutput = "Oops... Invalid command.\nUsage: chess help";
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  void helpCommand() {
    // Arrange
    List<String> arguments = Collections.singletonList("help");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output)
        .contains("Supported commands:")
        .contains("- fen <ID>: Display the FEN notation of a chess game")
        .contains("- create <ID> <PlayerWhite> <PlayerBlack>: Start a new chess game")
        .contains("- load <ID>: Load a chess game")
        .contains("- move <FROM> <TO> on <ID>: Move a chess piece to a valid position")
        .contains("- show-moves <FROM> on <ID>: Get the possible moves for a chess piece")
        .contains("- promote <FROM> to <TYPE> on <ID>: Promote a pawn to a chess piece")
        .contains("- resign <ID>: Resign the current game")
        .contains("- offer-remi <ID>: Offer a remi")
        .contains("- accept-remi <ID>: Accept a remi")
        .contains("- help: Display this help message");
  }

  @Test
  void createGameCommand() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String gameId = "123";
    List<String> arguments = Arrays.asList("create", gameId, playerWhiteName, playerBlackName);
    Player playerWhite = new Player(playerWhiteName);
    Player playerBlack = new Player(playerBlackName);
    Game game = new Game(playerWhite, playerBlack, gameId);
    when(gameLogicMock.loadPlayer(playerWhiteName)).thenReturn(playerWhite);
    when(gameLogicMock.loadPlayer(playerBlackName)).thenReturn(playerBlack);
    when(gameLogicMock.loadGame(anyString())).thenReturn(game);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output)
        .contains("    a b c d e f g h")
        .contains("    ---------------")
        .contains("8 | r n b q k b n r | 8")
        .contains("7 | p p p p p p p p | 7")
        .contains("6 | . . . . . . . . | 6")
        .contains("5 | . . . . . . . . | 5")
        .contains("4 | . . . . . . . . | 4")
        .contains("3 | . . . . . . . . | 3")
        .contains("2 | P P P P P P P P | 2")
        .contains("1 | R N B Q K B N R | 1")
        .contains("    _______________")
        .contains("    a b c d e f g h");

    assertThat(output)
        .contains("Welcome to chess in Java!")
        .contains("Chess game created with ID: 123")
        .contains("Hello Alice and Bob.")
        .contains("Let's start the game.")
        .contains("Have fun!");
  }

  @Test
  void testThrowsGameAlreadyExistsException() throws GameAlreadyExistsException {
    // Arrange
    String gameId = "123";
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    List<String> arguments = Arrays.asList("create", gameId, playerWhiteName, playerBlackName);
    Player playerWhite = new Player(playerWhiteName);
    Player playerBlack = new Player(playerBlackName);
    when(gameLogicMock.loadPlayer(playerWhiteName)).thenReturn(playerWhite);
    when(gameLogicMock.loadPlayer(playerBlackName)).thenReturn(playerBlack);
    doThrow(new GameAlreadyExistsException("Game with ID " + gameId + " already exists"))
        .when(gameLogicMock).createGame(playerWhite, playerBlack, gameId);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Define the expected output
    String expectedOutput = "The game already exists. Please use another game ID!\nGame with ID 'Game with ID 123 already exists' already exists.";

    // Normalize line endings in expected output
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadPlayer(playerWhiteName);
    verify(gameLogicMock, times(1)).loadPlayer(playerBlackName);
    verify(gameLogicMock, times(1)).createGame(playerWhite, playerBlack, gameId);
  }

  @Test
  void testFenCommand() {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("fen", gameId);
    Game gameMock = mock(Game.class);
    String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);
    when(gameLogicMock.getFENNotation(gameMock)).thenReturn(fenNotation);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).getFENNotation(gameMock);
    assertThat(output)
        .contains("This is the FEN notation of " + gameId + ":")
        .contains(fenNotation);
  }

  @Test
  void testLoadCommand() {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("load", gameId);
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, gameId);
    when(gameLogicMock.loadGame(gameId)).thenReturn(game);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output)
        .contains("    a b c d e f g h")
        .contains("    ---------------")
        .contains("8 | r n b q k b n r | 8")
        .contains("7 | p p p p p p p p | 7")
        .contains("6 | . . . . . . . . | 6")
        .contains("5 | . . . . . . . . | 5")
        .contains("4 | . . . . . . . . | 4")
        .contains("3 | . . . . . . . . | 3")
        .contains("2 | P P P P P P P P | 2")
        .contains("1 | R N B Q K B N R | 1")
        .contains("    _______________")
        .contains("    a b c d e f g h");

    assertThat(output)
        .contains("Loading game with ID: 123")
        .contains("Game with ID 123 loaded successfully.");
  }

  @Test
  void testMovePieceCommand() throws GameNotFoundException {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String gameId = "123";
    List<String> arguments = Arrays.asList("move", "B2", "B3", "on", gameId);
    Player playerWhite = new Player(playerWhiteName);
    Player playerBlack = new Player(playerBlackName);
    Game gameUpdated = new Game(playerWhite, playerBlack,"rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR b 1", gameId);
    when(gameLogicMock.loadGame(gameId)).thenReturn(gameUpdated);
    when(gameLogicMock.moveTo("B2", "B3", gameUpdated)).thenReturn(true);
    when(gameLogicMock.endGame(gameUpdated)).thenReturn("BLACK won this game. Congrats Bob (new ELO: 1500)");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).contains("Moving piece in game 123 from B2 to B3")
        .contains("    a b c d e f g h")
        .contains("    ---------------")
        .contains("8 | r n b q k b n r | 8")
        .contains("7 | p p p p p p p p | 7")
        .contains("6 | . . . . . . . . | 6")
        .contains("5 | . . . . . . . . | 5")
        .contains("4 | . . . . . . . . | 4")
        .contains("3 | . P . . . . . . | 3")
        .contains("2 | P . P P P P P P | 2")
        .contains("1 | R N B Q K B N R | 1")
        .contains("    _______________")
        .contains("    a b c d e f g h")
        .contains("BLACK won this game. Congrats Bob (new ELO: 1500)");

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).moveTo("B2", "B3", gameUpdated);
    verify(gameLogicMock, times(1)).saveGame(gameUpdated);
    verify(gameLogicMock, times(1)).endGame(gameUpdated);
  }

  @Test
  void testMovePieceThrowsIllegalMoveException() throws GameNotFoundException, IllegalMoveException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("move", "e2", "e5", "on", gameId);

    // Mocking game and illegal move exception
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 0", gameId);
    when(gameLogicMock.loadGame(gameId)).thenReturn(game);
    doThrow(new IllegalMoveException("Illegal move")).when(gameLogicMock).moveTo("e2", "e5", game);

    // Act
    chessCli.handle(arguments);

    // Assert
    String output = outContent.toString().trim();
    assertThat(output).contains("Illegal move");

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).moveTo("e2", "e5", game);
  }

  @Test
  void testShowMovesCommand() throws GameNotFoundException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("show-moves", "e2", "on", gameId);
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, gameId);
    List<Position> possibleMoves = new ArrayList<>();
    possibleMoves.add(new Position(2,4));
    possibleMoves.add(new Position(3,4));
    when(gameLogicMock.loadGame(gameId)).thenReturn(game);
    when(gameLogicMock.getPossibleMoves("e2", game)).thenReturn(possibleMoves);
    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Green ANSI escape code
    String ANSI_GREEN = "\u001B[32m*\u001B[0m";

    // Assert
    assertThat(output).contains("Possible moves for piece at position e2: e3, e4");
    assertThat(output).contains("Capture moves for piece at position e2: ");
    assertThat(output).contains("    a b c d e f g h")
        .contains("    ---------------")
        .contains("8 | r n b q k b n r | 8")
        .contains("7 | p p p p p p p p | 7")
        .contains("6 | . . . . . . . . | 6")
        .contains("5 | . . . . . . . . | 5")
        .contains("4 | . . . . " + ANSI_GREEN + " . . . | 4")
        .contains("3 | . . . . " + ANSI_GREEN + " . . . | 3")
        .contains("2 | P P P P P P P P | 2")
        .contains("1 | R N B Q K B N R | 1")
        .contains("    _______________")
        .contains("    a b c d e f g h");
  }

  @Test
  void testShowCaptureMovesCommand() throws GameNotFoundException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("show-moves", "b2", "on", gameId);
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, gameId);
    game.getBoard().clearChessboard();
    Position whiteKingPosition = new Position(0, 0);
    Position blackKingPosition = new Position(7, 7);
    Position blackPawnPosition = new Position(2, 2);
    Position whitePawnPosition = new Position(1, 1);
    List<Position> possibleMoves = new ArrayList<>();
    possibleMoves.add(new Position(2,1));
    possibleMoves.add(new Position(3,1));
    List<Position> captureMoves = new ArrayList<>();
    captureMoves.add(new Position(2,2));

    Piece whiteKing = new King(Color.WHITE, whiteKingPosition, game.getBoard());
    Piece blackKing = new King(Color.BLACK, blackKingPosition, game.getBoard());
    Piece blackPawn = new Pawn(Color.BLACK, blackPawnPosition, game.getBoard());
    Piece whitePawn = new Pawn(Color.WHITE, whitePawnPosition, game.getBoard());

    game.getBoard().setPieceAtPosition(whiteKingPosition, whiteKing);
    game.getBoard().setPieceAtPosition(blackKingPosition, blackKing);
    game.getBoard().setPieceAtPosition(blackPawnPosition, blackPawn);
    game.getBoard().setPieceAtPosition(whitePawnPosition, whitePawn);

    when(gameLogicMock.loadGame(gameId)).thenReturn(game);
    when(gameLogicMock.getPossibleMoves("b2", game)).thenReturn(possibleMoves);
    when(gameLogicMock.getCaptureMoves("b2", possibleMoves, game)).thenReturn(captureMoves);
    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Red ANSI escape code
    String ANSI_GREEN = "\u001B[32m*\u001B[0m";
    String ANSI_RED = "\u001B[31mp\u001B[0m";

    // Assert
    assertThat(output).contains("Possible moves for piece at position b2: b3, b4");
    assertThat(output).contains("Capture moves for piece at position b2: c3");
    assertThat(output).contains("    a b c d e f g h")
        .contains("    ---------------")
        .contains("8 | . . . . . . . k | 8")
        .contains("7 | . . . . . . . . | 7")
        .contains("6 | . . . . . . . . | 6")
        .contains("5 | . . . . . . . . | 5")
        .contains("4 | . " + ANSI_GREEN + " . . . . . . | 4")
        .contains("3 | . " + ANSI_GREEN + " " + ANSI_RED + " . . . . . | 3")
        .contains("2 | . P . . . . . . | 2")
        .contains("1 | K . . . . . . . | 1")
        .contains("    _______________")
        .contains("    a b c d e f g h");
  }

  @Test
  void testPromotePawnCommand() {
    // Arrange
    String from = "e8";
    String desiredType = "Q";
    String gameID = "123";
    List<String> arguments = Arrays.asList("promote", from, "to", desiredType, "on", gameID);
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, gameID);
    when(gameLogicMock.loadGame(gameID)).thenReturn(game);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).contains("Promoting pawn in game " + gameID + " from Position " + from + " to a " + desiredType)
        .contains("    a b c d e f g h")
        .contains("    ---------------")
        .contains("8 | r n b q k b n r | 8")
        .contains("7 | p p p p p p p p | 7")
        .contains("6 | . . . . . . . . | 6")
        .contains("5 | . . . . . . . . | 5")
        .contains("4 | . . . . . . . . | 4")
        .contains("3 | . . . . . . . . | 3")
        .contains("2 | P P P P P P P P | 2")
        .contains("1 | R N B Q K B N R | 1")
        .contains("    _______________")
        .contains("    a b c d e f g h");

    verify(gameLogicMock, times(1)).loadGame(gameID);
    verify(gameLogicMock).promotePiece(game, from, desiredType);
  }

  @Test
  void testThrowsIllegalPromotionException() throws IllegalPromotionException {
    // Arrange
    String from = "e8";
    String desiredType = "Q";
    String gameID = "123";
    List<String> arguments = Arrays.asList("promote", from, "to", desiredType, "on", gameID);
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, gameID);
    when(gameLogicMock.loadGame(gameID)).thenReturn(game);
    doThrow(new IllegalPromotionException("Illegal promotion")).when(gameLogicMock).promotePiece(game, from, desiredType);

    // Act
    chessCli.handle(arguments);

    // Assert
    String output = outContent.toString().trim();
    assertThat(output).contains("Illegal promotion");

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameID);
    verify(gameLogicMock, times(1)).promotePiece(game, from, desiredType);
  }

  @Test
  void testResignCommand() {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("resign", gameId);
    Game gameMock = mock(Game.class);
    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).contains("Game with ID 123 resigned successfully.");

    // Verify that gameLogic.resign(currentGame) is called
    verify(gameLogicMock, times(1)).resign(gameMock);
  }

  @Test
  void testOfferRemiCommand() throws GameNotFoundException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("offer-remi", gameId);
    Game gameMock = mock(Game.class);
    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).contains("Offer remi on " + gameId);

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).offerRemi(gameMock);
  }

  @Test
  void testAcceptRemiCommand() throws GameNotFoundException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("accept-remi", gameId);
    Game gameMock = mock(Game.class);
    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);
    when(gameLogicMock.endGame(gameMock)).thenReturn("Game ended with a remi.");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).isEqualTo("Game ended with a remi.");

    // Verify that gameLogic.resign(currentGame) is called
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).acceptRemi(gameMock);
    verify(gameLogicMock, times(1)).endGame(gameMock);
  }

  @Test
  void testThrowsRemiWasNotOfferedException() throws RemiWasNotOfferedException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("accept-remi", gameId);
    doThrow(new RemiWasNotOfferedException()).when(gameLogicMock).acceptRemi(any());

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Define the expected output
    String expectedOutput = "You have to offer remi first before you can accept it.";

    // Normalize line endings in expected output
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).acceptRemi(any());
  }

  @ParameterizedTest
  @MethodSource("gameNotFoundCommandProvider")
  void testThrowsGameNotFoundException(List<String> arguments, String expectedOutput) throws GameNotFoundException {
    // Arrange
    when(gameLogicMock.loadGame(anyString())).thenThrow(new GameNotFoundException("Game not found"));

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Normalize line endings in expected output
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(anyString());
  }

  private static Stream<Arguments> gameNotFoundCommandProvider() {
    return Stream.of(
        Arguments.of(Arrays.asList("fen", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("load", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("move", "e2", "e4", "on", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("show-moves", "e2", "on", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("promote", "a8", "to", "Q", "on", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("resign", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("offer-remi", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("accept-remi", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found.")
    );
  }

  @Test
  void testPrintChessboardHandlesNullPointerException() throws NullPointerException {
    // Mock the behavior to throw NullPointerException
    doThrow(new NullPointerException("Game is not initialized")).when(gameLogicMock).loadGame(any());

    // Act
    chessCli.printChessboard("123");
    String output = outContent.toString().trim();

    // Define the expected output
    String expectedOutput = "You have to create a game first!\nGame is not initialized";

    // Normalize line endings in expected output
    expectedOutput =
        expectedOutput
            .replace("\n", System.lineSeparator())
            .replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  
}
