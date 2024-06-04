package hwr.oop.chess.cli;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.GameLogic;
import hwr.oop.chess.GameNotFoundException;
import hwr.oop.chess.Position;
import hwr.oop.chess.pieces.IllegalMoveException;
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
        .contains("Welcome to chess in Java!")
        .contains("Chess game created with ID: 123")
        .contains("Hello Alice and Bob.")
        .contains("Let's start the game.")
        .contains("Have fun!");
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
        Arguments.of(
            "start", "Unknown command: start\nFor help and further information: chess help"),
        Arguments.of(
            "play", "Unknown command: play\nFor help and further information: chess help"));
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
        Arguments.of(
            "print", "Unknown command: print\nFor help and further information: chess help"),
        Arguments.of(
            "create",
            "Oops... Invalid command.\nUsage: chess create <ID> <Player1Name> <Player2Name>"),
        Arguments.of("save", "Unknown command: save\nFor help and further information: chess help"),
        Arguments.of("on", "Unknown command: on\nFor help and further information: chess help"),
        Arguments.of("resign", "Oops... Invalid command.\nUsage: chess resign <ID>"),
        Arguments.of("accept", "Oops... Invalid command.\nUsage: chess accept <ID>"));
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
        .contains("- move <FROM> <TO> on <ID>: Move a chess piece to a valid position")
        .contains("- resign <ID>: Resign the current game")
        .contains("- accept <ID>: Accept a remi")
        .contains("- help: Display this help message");
  }


  @Test
  void testMovePiece() throws GameNotFoundException {
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
            .contains("________________")
            .contains("r n b q k b n r")
            .contains("p p p p p p p p")
            .contains(". . . . . . . .")
            .contains(". . . . . . . .")
            .contains(". . . . . . . .")
            .contains(". P . . . . . .")
            .contains("P . P P P P P P")
            .contains("R N B Q K B N R")
            .contains("________________");

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameId);
    verify(gameLogicMock, times(1)).moveTo("B2", "B3", gameUpdated);
    verify(gameLogicMock, times(1)).saveGame(gameUpdated);
  }

  
  @Test
  void testMovePieceThrowsGameNotFoundException2() throws GameNotFoundException {
    // Arrange
    List<String> arguments = Arrays.asList("move", "e2", "e4", "on", "nonexistentID");

    // We need to mock the behavior of gameLogic to throw the exception
    doThrow(new GameNotFoundException("Game not found")).when(gameLogicMock).loadGame("nonexistentID");

    // Act & Assert
    chessCli.handle(arguments);

    // Verify that the appropriate message was printed
    String output = outContent.toString();
    assertTrue(output.contains("The game does not exist. Please use another game ID!"));
  }
  @Test
  void testMovePieceThrowsGameNotFoundException() throws GameNotFoundException {
    // Arrange
    List<String> arguments = Arrays.asList("move", "e2", "e4", "on", "nonexistentID");

    // We need to mock the behavior of gameLogic to throw the exception
    doThrow(new GameNotFoundException("Game not found")).when(gameLogicMock).loadGame("nonexistentID");

    // Act & Assert
    chessCli.handle(arguments);

    // Verify that the appropriate message was printed
    String output = outContent.toString();
    assertTrue(output.contains("The game does not exist. Please use another game ID!"));
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
  void testShowMoves() throws GameNotFoundException {
    // Arrange
    String gameId = "123";
    List<String> arguments = Arrays.asList("show-moves", "e2", "on", gameId);

    // Mocking game and possible moves
    Player playerWhite = new Player("Alice");
    Player playerBlack = new Player("Bob");
    Game game = new Game(playerWhite, playerBlack, gameId);
    when(gameLogicMock.loadGame(gameId)).thenReturn(game);

    List<Position> possibleMoves = Arrays.asList(new Position(2, 4), new Position(3, 4));
    when(gameLogicMock.getPossibleMoves("e2", game)).thenReturn(possibleMoves);

    // Act
    chessCli.handle(arguments);

    // Assert
    String output = outContent.toString().trim();
    assertThat(output).contains("Possible moves for piece at position e2: e3, e4");

    // Verify interactions with mocks
    verify(gameLogicMock, times(1)).loadGame(gameId);
  }

  @Test
  void testShowMovesThrowsGameNotFoundException() throws GameNotFoundException {
    // Arrange
    List<String> arguments = Arrays.asList("show-moves", "e2", "on", "nonexistentID");

    // We need to mock the behavior of gameLogic to throw the exception
    doThrow(new GameNotFoundException("Game not found")).when(gameLogicMock).loadGame("nonexistentID");

    // Act
    chessCli.handle(arguments);

    // Verify that the appropriate message was printed
    String output = outContent.toString();
    assertTrue(output.contains("The game does not exist. Please use another game ID!"));
  }


}
