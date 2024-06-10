package hwr.oop.chess.cli;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void testMain() {
    // Arrange
    OutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    // Act & Assert
    assertDoesNotThrow(() -> Main.main(new String[0]));

    // Reset System.out
    System.setOut(originalOut);
  }

  @Test
  void main_CanBeCalledWithHelpCommand() {
    Main mainTest = new Main();
    System.out.println(mainTest);
    Main.main(new String[] {"help"});
    assertSoftly(
        softly -> softly.assertThat(outContent.toString()).contains("Supported commands:"));
  }
}
