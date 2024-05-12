package hwr.oop.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ChessCliTest {

  @Test
  void testHandle_NoCommandProvided() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = new ArrayList<>();

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo("Invalid command. Usage: chess <command> [options]\n" +
        "For help and further information: chess help");
  }

  @ParameterizedTest
  @MethodSource("unknownCommandsProvider")
  void testHandle_UnknownCommand(String command, String expectedOutput) {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList(command, "123");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  private static Stream<Arguments> unknownCommandsProvider() {
    return Stream.of(
        Arguments.of("start", "Unknown command: start"),
        Arguments.of("play", "Unknown command: play")
    );
  }

  @ParameterizedTest
  @MethodSource("invalidArgumentsProvider")
  void testHandle_InvalidArguments(String command, String expectedOutput) {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Collections.singletonList(command);

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  // Provide invalid arguments for each command
  private static Stream<Object[]> invalidArgumentsProvider() {
    return Stream.of(
        new Object[]{"create", "Invalid command. Usage: chess create <ID> <Player1Name> <Player2Name>"},
        new Object[]{"save", "Invalid command. Usage: chess save <ID>"},
        new Object[]{"load", "Invalid command. Usage: chess load <ID>"},
        new Object[]{"on", "Invalid command. Usage: chess on <ID> move <FROM> <TO>"}
    );
  }

  @Test
  void testHandle_InvalidArguments_HelpCommandWithExtraArgument() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("help", "extraArgument");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo("Invalid command. Usage: chess help");
  }

  @Test
  void helpCommand() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Collections.singletonList("help");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    String expectedOutput = """
        Supported commands:
          - create <ID> <Player1Name> <Player2Name>: Start a new chess game
          - save <ID>: Save a chess game
          - load <ID>: Load a saved chess game
          - on <ID> move <FROM> <TO>: Move a chess piece to a valid position
          - help: Display this help message""";
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  void createGameCommand() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("create", "123", "Alice", "Bob");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    String expectedOutput = """
        Welcome to chess in Java!
        Chess game created with ID: 123
        Hello Alice and Bob.
        Let's start the game.
        Have fun!""";
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  void saveGameCommand() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("save", "123");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo("Game with ID 123 saved successfully.");
  }

  @Test
  void loadGameCommand() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("load", "456");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo("Loading game with ID: 456\n" +
        "Game 456 loaded successfully.");
  }

  @Test
  void testMovePiece() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("on", "123", "move", "A1", "B2");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    String expectedOutput = "Moving piece in game 123 from A1 to B2\nPiece was successfully moved.";
    assertThat(output).contains(expectedOutput);
    assertThat(output.lines().count()).isEqualTo(2);
  }

  @Test
  void testMain_HandleMethodCall() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String gameId = "123";
    String playerOneName = "Alice";
    String playerTwoName = "Bob";

    // Act
    Main.main(new String[]{"create", gameId, playerOneName, playerTwoName});

    // Assert
    String output = outputStream.toString().trim();
    assertThat(output)
        .contains("Welcome to chess in Java!")
        .contains("Chess game created with ID: " + gameId)
        .contains("Hello " + playerOneName + " and " + playerTwoName + ".")
        .contains("Let's start the game.")
        .contains("Have fun!");
  }
}
