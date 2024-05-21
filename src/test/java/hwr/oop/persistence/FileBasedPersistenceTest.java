package hwr.oop.persistence;

import hwr.oop.board.ChessBoardException;
import hwr.oop.match.Match;
import hwr.oop.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileBasedPersistenceTest {

  private FileBasePersistence instUT;

  @BeforeEach
  void setUp() {
    instUT = new FileBasePersistence();
  }

  @Test
  void testPersistenceReadWriteMatch() {
    // given
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match expectedMatch = new Match(playerWhite, playerBlack);
    final String filePath = "target/persistenceTest.txt";
    final File file = new File(filePath);
    final Path path = file.toPath();

    // when
    instUT.save(expectedMatch, path);
    final Match actualMatch = instUT.load(path);

    // then
    assertNotNull(actualMatch);
    assertThat(expectedMatch).isEqualTo(actualMatch);
  }

  @Test
  void testPersistenceReadWriteMatchWithFenNotation() throws ChessBoardException {
    // given
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final String fenNotation = "8/8/8/8/8/8/8/8";
    final Match expectedMatch = new Match(playerWhite, playerBlack, fenNotation);
    final String filePath = "target/persistenceTest.txt";
    final File file = new File(filePath);
    final Path path = file.toPath();

    // when
    instUT.save(expectedMatch, path);
    final Match actualMatch = instUT.load(path);

    // then
    assertNotNull(actualMatch);
    assertThat(expectedMatch).isEqualTo(actualMatch);
  }

  @Test
  void testPersistenceCannotWrite() {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match match = new Match(playerWhite, playerBlack);
    final String filePath = "not-existing-directory/persistenceTest.txt";
    final File file = new File(filePath);
    final Path path = file.toPath();

    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.save(match, path));
    String expectedMessage = "Cannot write.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testPersistenceCannotRead() {
    final String filePath = "target/notExistingFile.txt";
    final File file = new File(filePath);
    final Path path = file.toPath();

    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.load(path));
    String expectedMessage = "Cannot read.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
