package hwr.oop;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import hwr.oop.match.Match;
import hwr.oop.persistence.FileBasePersistence;
import hwr.oop.persistence.Persistence;
import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.pieces.Pawn;
import hwr.oop.pieces.Piece;
import hwr.oop.pieces.Rook;
import hwr.oop.player.Player;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameLogicTest {

  private GameLogic gameLogic;
  private Persistence persistence;
  private static final String TEST_FILE_PATH_MATCHES = "target/GameLogicTestMatches.txt";
  private static final String TEST_FILE_PATH_PLAYERS = "target/GameLogicTestPlayers.txt";

  private Path pathMatches;
  private Path pathPlayers;

  @BeforeEach
  void setUp() {
    File fileMatches = new File(TEST_FILE_PATH_MATCHES);
    pathMatches = fileMatches.toPath();
    File filePlayers = new File(TEST_FILE_PATH_PLAYERS);
    pathPlayers = filePlayers.toPath();
    persistence = new FileBasePersistence();
    gameLogic = new GameLogic(persistence, TEST_FILE_PATH_MATCHES, TEST_FILE_PATH_PLAYERS);
  }

  @AfterEach
  void tearDown() {
    File fileMatches = new File(TEST_FILE_PATH_MATCHES);
    if (fileMatches.exists()) {
      if (!fileMatches.delete()) {
        throw new RuntimeException("Deleting the file was unsuccessful.");
      }
    }
    File filePlayers = new File(TEST_FILE_PATH_PLAYERS);
    if (filePlayers.exists()) {
      if (!filePlayers.delete()) {
        throw new RuntimeException("Deleting the file was unsuccessful.");
      }
    }
  }

  @Test
  void testLoadMatch_MatchFound() {
    // Arrange
    Match expectedMatch = new Match(new Player("Player1"), new Player("Player2"), "123");
    List<Match> matches = new ArrayList<>();
    matches.add(expectedMatch);
    persistence.saveMatches(matches, pathMatches);

    // Act
    Match loadedMatch = gameLogic.loadMatch("123");

    // Assert
    assertNotNull(loadedMatch);
    assertEquals(expectedMatch, loadedMatch);
  }

  @Test
  void testLoadMatch_MatchNotFound() {
    // Act & Assert
    assertThrows(MatchNotFoundException.class, () -> gameLogic.loadMatch("123"));
    MatchNotFoundException exception =
        assertThrows(MatchNotFoundException.class, () -> gameLogic.loadMatch("123"));
    String expectedMessage = "Match with ID '123' not found.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testCreateMatch_Successful() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "456";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);

    // Act
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);

    // Assert
    List<Match> loadedMatches = persistence.loadMatches(pathMatches);
    assertFalse(loadedMatches.isEmpty());
  }

  @Test
  void testCreateMatch_MatchAlreadyExists() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "456";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);

    // Arrange: Save a match with the same ID
    List<Match> matches = new ArrayList<>();
    matches.add(new Match(new Player("Player1"), new Player("Player2"), matchId));
    persistence.saveMatches(matches, pathMatches);

    // Act & Assert
    assertThrows(
        MatchAlreadyExistsException.class,
        () -> gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId));
  }

  @Test
  void testSaveMatch_NewMatch() {
    // Arrange
    Match oldMatch = new Match(new Player("Player1"), new Player("Player2"), "123");
    List<Match> matches = new ArrayList<>();
    matches.add(oldMatch);
    persistence.saveMatches(matches, pathMatches);
    Match newMatch = new Match(new Player("Alice"), new Player("Bob"), "67");

    // Act
    gameLogic.saveMatch(newMatch);

    // Assert
    List<Match> loadedMatches = persistence.loadMatches(pathMatches);
    assertTrue(loadedMatches.contains(newMatch));
  }

  @Test
  void testSaveMatch_OverwriteExistingMatch() {
    // Arrange
    Match existingMatch = new Match(new Player("Player1"), new Player("Player2"), "123");
    Match updatedMatch = new Match(new Player("Player1"), new Player("Player3"), "123");
    List<Match> matches = new ArrayList<>();
    matches.add(existingMatch);
    persistence.saveMatches(matches, pathMatches);

    // Act
    gameLogic.saveMatch(updatedMatch);

    // Assert
    List<Match> loadedMatches = persistence.loadMatches(pathMatches);
    assertTrue(loadedMatches.contains(updatedMatch));
  }

  @Test
  void testLoadPlayer_PlayerFound() {
    // Arrange
    Player expectedPlayer = new Player("Player1");
    List<Player> players = new ArrayList<>();
    players.add(expectedPlayer);
    persistence.savePlayers(players, pathPlayers);

    // Act
    Player loadedPlayer = gameLogic.loadPlayer("Player1");

    // Assert
    assertNotNull(loadedPlayer);
    assertEquals(expectedPlayer, loadedPlayer);
  }

  @Test
  void testLoadPlayer_PlayerNotFound() {
    // Act & Assert
    assertThrows(PlayerNotFoundException.class, () -> gameLogic.loadPlayer("Player1"));
    PlayerNotFoundException exception =
        assertThrows(PlayerNotFoundException.class, () -> gameLogic.loadPlayer("Player1"));
    String expectedMessage = "Player with the name 'Player1' was not found.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testCreatePlayer_Successful() {
    // Arrange
    String playerName = "Alice";

    // Act
    gameLogic.createPlayer(playerName);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers(pathPlayers);
    assertTrue(loadedPlayers.stream().anyMatch(p -> p.getName().equals(playerName)));
  }

  @Test
  void testCreatePlayer_PlayerAlreadyExists() {
    // Arrange
    String playerName = "Alice";

    // Arrange: Save a player with the same name
    List<Player> players = new ArrayList<>();
    players.add(new Player(playerName));
    persistence.savePlayers(players, pathPlayers);

    // Act & Assert
    assertThrows(PlayerAlreadyExistsException.class, () -> gameLogic.createPlayer(playerName));
  }

  @Test
  void testSavePlayer_NewPlayer() {
    // Arrange
    Player newPlayer = new Player("Alice");

    // Act
    gameLogic.savePlayer(newPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers(pathPlayers);
    assertTrue(loadedPlayers.contains(newPlayer));
  }

  @Test
  void testSavePlayer_OverwriteExistingPlayer() {
    // Arrange
    Player existingPlayer = new Player("Alice");
    Player updatedPlayer = new Player("Alice");
    updatedPlayer.setElo((short) 110);
    List<Player> players = new ArrayList<>();
    players.add(existingPlayer);
    persistence.savePlayers(players, pathPlayers);

    // Act
    gameLogic.savePlayer(updatedPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers(pathPlayers);
    assertTrue(loadedPlayers.contains(updatedPlayer));
  }

  @Test
  void testLoadPlayer_PlayerWithSameName() {
    // Arrange
    Player existingPlayer = new Player("Alice");
    List<Player> players = new ArrayList<>();
    players.add(existingPlayer);
    persistence.savePlayers(players, pathPlayers);

    // Act
    Player loadedPlayer = gameLogic.loadPlayer("Alice");

    // Assert
    assertNotNull(loadedPlayer);
    assertEquals(existingPlayer, loadedPlayer);
  }

  @Test
  void testSavePlayer_ExistingPlayerOverwritten() {
    // Arrange
    Player existingPlayer = new Player("Alice");
    Player updatedPlayer = new Player("Alice");
    updatedPlayer.setElo((short) 110);
    List<Player> players = new ArrayList<>();
    players.add(existingPlayer);
    persistence.savePlayers(players, pathPlayers);

    // Act
    gameLogic.savePlayer(updatedPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers(pathPlayers);
    assertTrue(loadedPlayers.contains(updatedPlayer));
  }

  @Test
  void testMoveTo_ValidMove() throws IllegalMoveException {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);
    Position initialPosition = new Position(1, 4);
    Position targetPosition = new Position(3, 4);

    // Act

    gameLogic.moveTo(initialPosition, targetPosition, match);
    gameLogic.saveMatch(match);

    // Assert
    Piece movedPiece = match.getBoard().getPieceAtPosition(targetPosition);
    assertNotNull(movedPiece);
    assertEquals(targetPosition, movedPiece.getPosition());
  }

  @Test
  void testMoveTo_InvalidMove_NoPieceAtStartPosition() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);
    Position initialPosition = new Position(2, 3);

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, new Position(3, 3), match));
    assertEquals(
        "No piece at the specified position: Position[row=2, column=3]", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_WrongPlayerTurn() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);
    Position initialPosition = new Position(6, 4);
    Position targetPosition = new Position(5, 4);

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals("It's not your turn. Expected: WHITE, but got: BLACK", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_TargetPositionOutOfBounds() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);
    Position initialPosition = new Position(1, 4);
    Position targetPosition = new Position(8, 4);

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals(
        "Illegal move to position: Position[row=8, column=4]. Possible possible moves are for example: Position[row=2, column=4], Position[row=3, column=4]",
        exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_InvalidDirectionForPiece() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);
    Position initialPosition = new Position(0, 1);
    Position targetPosition = new Position(4, 1);

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals(
        "Illegal move to position: Position[row=4, column=1]. Possible possible moves are for example: Position[row=2, column=2], Position[row=2, column=0]",
        exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_BlockingPieceInPath() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);
    match.getBoard().clearChessboard();
    match
        .getBoard()
        .setPieceAtPosition(
            new Position(2, 4),
            new Rook(Color.WHITE, new Position(2, 4), match.getBoard())); // Rook blocking the path
    match
        .getBoard()
        .setPieceAtPosition(
            new Position(3, 4),
            new Pawn(
                Color.BLACK, new Position(3, 4), match.getBoard())); // Pawn trying to move here
    Position initialPosition = new Position(2, 4);
    Position targetPosition = new Position(4, 4);

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals(
        "Illegal move to position: Position[row=4, column=4]. Possible possible moves are for example: Position[row=3, column=4], Position[row=1, column=4]",
        exception.getMessage());
  }

  @Test
  void testSaveMatch_AddNewMatchWhenNotExists() {
    // Arrange
    Match newMatch = new Match(new Player("Alice"), new Player("Bob"), "67");

    // Act
    gameLogic.saveMatch(newMatch);

    // Assert
    List<Match> loadedMatches = persistence.loadMatches(pathMatches);
    assertTrue(loadedMatches.contains(newMatch));
  }

  @Test
  void testSavePlayer_AddNewPlayerWhenNotExists() {
    // Arrange
    Player newPlayer = new Player("Alice");

    // Act
    gameLogic.savePlayer(newPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers(pathPlayers);
    assertTrue(loadedPlayers.contains(newPlayer));
  }

  @Test
  void testMoveTo_InvalidMove_SameStartAndEndPosition() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);

    Position startPosition = new Position(1, 4);
    Position endPosition = new Position(1, 4); // Same as start position

    // Set a piece at the start position
    Piece piece = new Pawn(Color.WHITE, startPosition, match.getBoard());
    match.getBoard().setPieceAtPosition(startPosition, piece);

    // Act & Assert
    IllegalMoveException exception = assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(startPosition, endPosition, match)
    );

    // Check that the appropriate exception is thrown
    assertEquals("Illegal move to position: " + endPosition + ". The start and end positions are the same.", exception.getMessage());
  }
  @Test
  void testMoveTo_NullMatch() {
    // Arrange
    Position startPosition = new Position(1, 4);
    Position endPosition = new Position(3, 4);

    // Act & Assert
    IllegalMoveException exception = assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(startPosition, endPosition, null)
    );

    // Check that the appropriate exception is thrown
    assertEquals("Match is not initialized", exception.getMessage());
  }
  @Test
  void testAcceptRemi() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Act
    gameLogic.acceptRemi(match);

    // Assert
    assertEquals("Remi", match.getWinner());
    assertTrue(match.isGameEnded());

  }
  @Test
  void testResign_WhitePlayerResigns() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Act
    gameLogic.resign(match);

    // Assert
    assertEquals("Black", match.getWinner());
    assertTrue(match.isGameEnded());

  }

  @Test
  void testResign_BlackPlayerResigns() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "match1";
    gameLogic.createPlayer(playerWhiteName);
    gameLogic.createPlayer(playerBlackName);
    gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Swap next to move to black to simulate black player's turn
    match.toggleNextToMove();

    // Act
    gameLogic.resign(match);

    // Assert
    assertEquals("White", match.getWinner());
    assertTrue(match.isGameEnded());

  }
  @Test
  void testCreateMatch_PlayerNotFound() {
    // Arrange
    String playerWhiteName = "Alice";
    String playerBlackName = "Bob";
    String matchId = "456";

    gameLogic.createPlayer(playerWhiteName);

    PlayerNotFoundException exception = assertThrows(
            PlayerNotFoundException.class,
            () -> gameLogic.createMatch(gameLogic.loadPlayer(playerWhiteName), gameLogic.loadPlayer(playerBlackName), matchId));

    String expectedMessage = "Player with the name 'Bob' was not found.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

}
