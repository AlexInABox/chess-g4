package hwr.oop.chess.cli;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import hwr.oop.chess.GameAlreadyExistsException;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;
import hwr.oop.chess.game.Game;
import hwr.oop.chess.GameLogic;
import hwr.oop.chess.GameNotFoundException;
import hwr.oop.chess.pieces.IllegalMoveException;
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
        Arguments.of("create", "Oops... Invalid command.\nUsage: chess create <ID> <Player1Name> <Player2Name>"),
        Arguments.of("load", "Oops... Invalid command.\nUsage: chess load <ID>"),
        Arguments.of("move", "Oops... Invalid command.\nUsage: chess move <FROM> <TO> on <ID>"),
        Arguments.of("show-moves", "Oops... Invalid command.\nUsage: chess show-moves <FROM> on <ID>"),
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
        .contains("- create <ID> <PlayerWhite> <PlayerBlack>: Start a new chess game")
        .contains("- load <ID>: Load a chess game")
        .contains("- move <FROM> <TO> on <ID>: Move a chess piece to a valid position")
        .contains("- show-moves <FROM> on <ID>: Get the possible moves for a chess piece")
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

    // Mock the behavior to throw GameAlreadyExistsException
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
        .contains("    a b c d e f g h");

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).moveTo("B2", "B3", gameUpdated);
    verify(gameLogicMock, times(1)).saveGame(gameUpdated);
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
    Game gameMock = mock(Game.class);
    ChessBoard boardMock = mock(ChessBoard.class);
    Piece pieceMock = mock(Piece.class);
    Position fromPosition = new Position(1, 4); // e2
    Position toPosition1 = new Position(2, 4); // e3
    Position toPosition2 = new Position(3, 4); // e4
    List<Position> possibleMoves = Arrays.asList(toPosition1, toPosition2);

    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);
    when(gameMock.getBoard()).thenReturn(boardMock);
    when(boardMock.getPieceAtPosition(fromPosition)).thenReturn(pieceMock);
    when(pieceMock.possibleMoves()).thenReturn(possibleMoves);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).contains("Possible moves for piece at position e2: e3, e4");
  }

  @Test
  void testShowCaptureMovesCommand() throws GameNotFoundException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("show-moves", "e2", "on", gameId);
    Game gameMock = mock(Game.class);
    ChessBoard boardMock = mock(ChessBoard.class);
    Piece pieceMock = mock(Piece.class);
    Position fromPosition = new Position(1, 4); // e2
    Position toPosition1 = new Position(2, 3); // d3
    Position toPosition2 = new Position(2, 5); // f3
    List<Position> possibleMoves = Arrays.asList(toPosition1, toPosition2);

    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);
    when(gameMock.getBoard()).thenReturn(boardMock);
    when(boardMock.getPieceAtPosition(fromPosition)).thenReturn(pieceMock);
    when(pieceMock.possibleMoves()).thenReturn(possibleMoves);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output).contains("Capture moves for piece at position e2: ");
    assertThat(output).doesNotContain("Capture moves for piece at position e2: d3, f3");
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

  /*@Test
  void testAcceptCommand() {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("accept-remi", gameId);
    Game gameMock = mock(Game.class);
    when(gameLogicMock.loadGame(gameId)).thenReturn(gameMock);

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert

    // Verify that gameLogic.resign(currentGame) is called
    verify(gameLogicMock, times(1)).acceptRemi(gameMock);
  }*/

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
        Arguments.of(Arrays.asList("load", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("move", "e2", "e4", "on", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("show-moves", "e2", "on", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("resign", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("offer-remi", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found."),
        Arguments.of(Arrays.asList("accept-remi", "123"), "The game does not exist. Please create this game first!\nGame with ID 'Game not found' not found.")
    );
  }
}
