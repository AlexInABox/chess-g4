package hwr.oop.persistence;

import hwr.oop.board.ChessBoard;
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
  void testPersistenceReadWriteChessBoard() {
    // given
    final ChessBoard expectedChessBoard = new ChessBoard();
    final String filePath = "target/persistenceTest.txt";
    final File file = new File(filePath);
    final Path path = file.toPath();

    // when
    instUT.save(expectedChessBoard, path);
    final ChessBoard actualChessboard = (ChessBoard) instUT.load(path);

    // then
    assertNotNull(actualChessboard);
    assertThat(expectedChessBoard).isEqualTo(actualChessboard);
  }

  @Test
  void testPersistenceCannotWrite() {
    final ChessBoard chessBoard = new ChessBoard();
    final String filePath = "not-existing-directory/persistenceTest.txt";
    final File file = new File(filePath);
    final Path path = file.toPath();

    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.save(chessBoard, path));
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
