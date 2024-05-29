/*package hwr.oop.cli;

import static org.assertj.core.api.Assertions.assertThat;

import hwr.oop.GameLogic;
import hwr.oop.match.Match;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ChessCliTest {

  private ByteArrayOutputStream outContent;
  private ChessCli chessCli;

  @BeforeEach
  void setUp() {
    outContent = new ByteArrayOutputStream();
    OutputStream out = new PrintStream(outContent);
    chessCli = new ChessCli(out);
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
    expectedOutput = expectedOutput.replace("\n", System.lineSeparator()).replace("\r\n", System.lineSeparator());

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
    expectedOutput = expectedOutput.replace("\n", System.lineSeparator()).replace("\r\n", System.lineSeparator());

    // Assert
    assertThat(output).isEqualTo(expectedOutput);
  }

  // Provide invalid arguments for each command
  private static Stream<Arguments> invalidArgumentsProvider() {
    return Stream.of(
        Arguments.of("print", "Oops... Invalid command.\nUsage: chess print"),
        Arguments.of("create", "Oops... Invalid command.\nUsage: chess create <ID> <Player1Name> <Player2Name>"),
        Arguments.of("save", "Oops... Invalid command.\nUsage: chess save <ID>"),
        Arguments.of("load", "Oops... Invalid command.\nUsage: chess load <ID>"),
        Arguments.of("on", "Oops... Invalid command.\nUsage: chess on <ID> move <FROM> <TO>"),
        Arguments.of("resign", "Oops... Invalid command.\nUsage: chess resign <ID>"),
        Arguments.of("accept", "Oops... Invalid command.\nUsage: chess accept <ID>")
    );
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
    expectedOutput = expectedOutput.replace("\n", System.lineSeparator()).replace("\r\n", System.lineSeparator());

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
        .contains("- print: Print chessboard for current match")
        .contains("- create <ID> <PlayerWhite> <PlayerBlack>: Start a new chess match")
        .contains("- save <ID>: Save a chess match")
        .contains("- load <ID>: Load a saved chess match")
        .contains("- on <ID> move <FROM> <TO>: Move a chess piece to a valid position")
        .contains("- resign <ID>: Resign the current match")
        .contains("- accept <ID>: Accept a remi")
        .contains("- help: Display this help message");
  }

  @Test
  void printMatchCommand() {
    // Arrange
    List<String> arguments = Collections.singletonList("print");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
  }

  @Test
  void createMatchCommand() {
    // Arrange
    List<String> arguments = Arrays.asList("create", "123", "Alice", "Bob");

    // Act
    chessCli.handle(arguments);
    String output = outContent.toString().trim();

    // Assert
    assertThat(output)
        .contains("Welcome to chess in Java!")
        .contains("Chess match created with ID: 123")
        .contains("Hello Alice and Bob.")
        .contains("Let's start the match.")
        .contains("Have fun!");
  }

  @Test
  void saveMatchCommand() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("save", "123");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output).isEqualTo("Match with ID 123 saved successfully.");
  }

  @Test
  void loadMatchCommand_MatchExists() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("load", "123");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output)
        .contains("Loading match with ID: 123")
        .contains("Match with ID 123 loaded successfully.");
  }

  @Test
  void loadMatchCommand_MatchDoesNotExist() {
    // Arrange
    OutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ChessCli chessCli = new ChessCli(printStream);
    List<String> arguments = Arrays.asList("load", "456");

    // Act
    chessCli.handle(arguments);
    String output = outputStream.toString().trim();

    // Assert
    assertThat(output)
        .contains("Match with ID 456 does not exist. Please choose a different ID.");
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
    assertThat(output)
        .contains("Moving piece in match 123 from A1 to B2")
        .contains("Piece was successfully moved.");
    assertThat(output.lines().count()).isEqualTo(2);
  }

  @Test
  void testMain_HandleMethodCall() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String matchID = "123";
    String playerOneName = "Alice";
    String playerTwoName = "Bob";

    // Act
    Main.main(new String[] {"create", matchID, playerOneName, playerTwoName});

    // Assert
    String output = outputStream.toString().trim();
    assertThat(output)
        .contains("Welcome to chess in Java!")
        .contains("Chess match created with ID: " + matchID)
        .contains("Hello " + playerOneName + " and " + playerTwoName + ".")
        .contains("Let's start the match.")
        .contains("Have fun!");
  }
}
*/