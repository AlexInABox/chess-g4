package hwr.oop.chess.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hwr.oop.chess.match.Match;
import hwr.oop.chess.player.Player;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileBasedPersistenceTest {
  private static final String TEST_FILE_PATH = "target/persistenceTest.txt";

  private FileBasePersistence instUT;
  File file;

  @BeforeEach
  void setUp() {
    file = new File(TEST_FILE_PATH);
    Path path = file.toPath();
    instUT = new FileBasePersistence(path,path);
  }

  @AfterEach
  void tearDown() {
    File file = new File(TEST_FILE_PATH);
    if (file.exists()) {
      if (!file.delete()) {
        throw new RuntimeException("Deleting the file was unsuccessful.");
      }
    }
  }

  @Test
  void testPersistenceReadWriteMatch() {
    // given
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match match = new Match(playerWhite, playerBlack, "1");
    List<Match> expectedMatches = new ArrayList<>();
    expectedMatches.add(match);

    // when
    instUT.saveMatches(expectedMatches);
    final List<Match> actualMatches = instUT.loadMatches();

    // then
    assertNotNull(actualMatches);
    assertThat(expectedMatches).isEqualTo(actualMatches);
  }

  @Test
  void testPersistenceReadWriteMatchWithFenNotation() {
    // given
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final String fenNotation = "8/8/8/8/8/8/8/8 w";
    final Match match = new Match(playerWhite, playerBlack, fenNotation);
    List<Match> expectedMatches = new ArrayList<>();
    expectedMatches.add(match);

    // when
    instUT.saveMatches(expectedMatches);
    final List<Match> actualMatches = instUT.loadMatches();

    // then
    assertNotNull(actualMatches);
    assertThat(expectedMatches).isEqualTo(actualMatches);
  }

  @Test
  void testPersistenceCannotWriteMatch_NotExistingDirectory() {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match match = new Match(playerWhite, playerBlack, "1");
    List<Match> expectedMatches = new ArrayList<>();
    expectedMatches.add(match);
    final String filePath = "not-existing-directory/persistenceTest.txt";
    File fileNew = new File(filePath);
    Path pathNew = fileNew.toPath();
    instUT = new FileBasePersistence(pathNew, pathNew);

    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.saveMatches(expectedMatches));
    String expectedMessage = "Cannot write.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testPersistenceReadEmptyFileMatch() throws IOException {
    // given
    final Path path = Path.of(TEST_FILE_PATH);
    Files.createFile(path);

    // when
    List<Match> loadedMatches = instUT.loadMatches();

    // then
    assertNotNull(loadedMatches);
    assertTrue(loadedMatches.isEmpty());
  }

  @Test
  void testLoadEmptyFileReturnsEmptyListMatch() throws IOException {
    final Path path = Path.of(TEST_FILE_PATH);
    Files.createFile(path);

    List<Match> loadedMatches = instUT.loadMatches();

    // Assert that the loaded matches list is empty
    assertEquals(0, loadedMatches.size(), "Loaded matches list should be empty for an empty file");
  }

  @Test
  void testSavePlayers() {
    // given
    final Player player = new Player("player1");
    List<Player> expectedPlayers = new ArrayList<>();
    expectedPlayers.add(player);

    // when
    instUT.savePlayers(expectedPlayers);
    final List<Player> actualPlayers = instUT.loadPlayers();

    // then
    assertNotNull(actualPlayers);
    assertThat(expectedPlayers).isEqualTo(actualPlayers);
  }

  @Test
  void testLoadPlayers_EmptyFile() throws IOException {
    // given
    final Path path = Path.of(TEST_FILE_PATH);
    Files.createFile(path);

    // when
    List<Player> loadedPlayers = instUT.loadPlayers();

    // then
    assertNotNull(loadedPlayers);
    assertTrue(loadedPlayers.isEmpty());
  }

  @Test
  void testSavePlayers_CannotWrite_NotExistingDirectory() {
    final Player player = new Player("player1");
    List<Player> expectedPlayers = new ArrayList<>();
    expectedPlayers.add(player);
    final String filePath = "not-existing-directory/persistenceTest.txt";
    File fileNew = new File(filePath);
    Path pathNew = fileNew.toPath();
    instUT = new FileBasePersistence(pathNew, pathNew);
    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.savePlayers(expectedPlayers));
    String expectedMessage = "Cannot write.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testLoadPlayers_FileWithData() {
    // given
    final Player player1 = new Player("player1");
    final Player player2 = new Player("player2");
    List<Player> expectedPlayers = new ArrayList<>();
    expectedPlayers.add(player1);
    expectedPlayers.add(player2);

    // when
    instUT.savePlayers(expectedPlayers);
    final List<Player> actualPlayers = instUT.loadPlayers();

    // then
    assertNotNull(actualPlayers);
    assertThat(expectedPlayers).isEqualTo(actualPlayers);
  }

  @Test
  void testLoadMatches_EmptyFileReturnsEmptyList() throws IOException {
    // given
    final Path path = Path.of(TEST_FILE_PATH);
    Files.createFile(path);

    // when
    List<Match> loadedMatches = instUT.loadMatches();

    // then
    assertNotNull(loadedMatches, "Loaded matches list should not be null");
    assertTrue(loadedMatches.isEmpty(), "Loaded matches list should be empty for an empty file");
  }

  @Test
  void testLoadPlayers_EmptyFileReturnsEmptyList() throws IOException {
    // given
    final Path path = Path.of(TEST_FILE_PATH);
    Files.createFile(path);

    // when
    List<Player> loadedPlayers = instUT.loadPlayers();

    // then
    assertNotNull(loadedPlayers, "Loaded players list should not be null");
    assertTrue(loadedPlayers.isEmpty(), "Loaded players list should be empty for an empty file");
  }

  @Test
  void testLoadMatches_WithInvalidFileFormat_ShouldThrowPersistenceException() throws IOException {
    // given

    // create an invalid file format
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write("invalid data".getBytes());
    }

    // then
    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.loadMatches());
    String expectedMessage = "Cannot read.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testLoadPlayers_WithInvalidFileFormat_ShouldThrowPersistenceException() throws IOException {
    // given
    final File file = new File(TEST_FILE_PATH);
    // create an invalid file format
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write("invalid data".getBytes());
    }

    // then
    PersistenceException exception =
        assertThrows(PersistenceException.class, () -> instUT.loadPlayers());
    String expectedMessage = "Cannot read.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
