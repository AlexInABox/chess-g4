package hwr.oop;

import static hwr.oop.GameLogic.convertInputToPosition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
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
  void testLoadMatch_MatchFound(){
    // Arrange
    String matchId = "shortestGame";
    Player playerWhite = gameLogic.loadPlayer("Player1");
    Player playerBlack = gameLogic.loadPlayer("Player2");
    List<Match> matches = new ArrayList<>();
    Match expectedMatch = new Match(playerWhite, playerBlack, matchId);

    matches.add(expectedMatch);
    persistence.saveMatches(matches, pathMatches);

    // Act
    Match loadedMatch = gameLogic.loadMatch("shortestGame");

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
  void testSaveMatch_NewMatch() {
    // Arrange
    List<Match> matches = new ArrayList<>();
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
    Player playerWhite = gameLogic.loadPlayer("Player1");
    Player playerBlack = gameLogic.loadPlayer("Player2");
    Player playerWhite2 = gameLogic.loadPlayer("Player3");

    // Arrange
    Match existingMatch = new Match(playerWhite, playerBlack, "123");
    Match updatedMatch = new Match(playerWhite2, playerBlack, "123");
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
  void testCreateMatch_Successful() {
    // Arrange
    String matchId = "456";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");

    // Act
    gameLogic.createMatch(playerWhite, playerBlack, matchId);

    // Assert
    List<Match> loadedMatches = persistence.loadMatches(pathMatches);
    assertTrue(
            loadedMatches.contains(
                    new Match(playerWhite, playerBlack, matchId)));
  }

  @Test
  void testCreateMatch_MatchAlreadyExists() {
    // Arrange
    String matchId = "456";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");

    // Arrange: Save a match with the same ID
    List<Match> matches = new ArrayList<>();
    matches.add(
            new Match(playerWhite, playerBlack, matchId));
    persistence.saveMatches(matches, pathMatches);

    // Act & Assert
    assertThrows(
            MatchAlreadyExistsException.class,
            () -> gameLogic.createMatch(playerWhite, playerBlack, matchId));
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
  void testMoveTo_ValidMove1()
          throws ConvertInputToPositionException, IllegalMoveException {
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    String initialPosition = "e2";
    String targetPosition = "e4";

    // Act

    gameLogic.moveTo(initialPosition, targetPosition, match);
    gameLogic.saveMatch(match);

    // Assert
    Piece movedPiece = match.getBoard().getPieceAtPosition(convertInputToPosition(targetPosition));
    assertNotNull(movedPiece);
    assertEquals(convertInputToPosition(targetPosition), movedPiece.getPosition());
  }

  @Test
  void testMoveTo_InvalidMove_NoPieceAtStartPosition(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    String initialPosition = "d3";
    String targetPosition = "d4";

    // Act & Assert
    IllegalMoveException exception =
            assertThrows(
                    IllegalMoveException.class,
                    () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals(
            "No piece at the specified position: Position[row=2, column=3]", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_WrongPlayerTurn(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    String initialPosition = "e7";
    String targetPosition = "e6";

    // Act & Assert
    IllegalMoveException exception =
            assertThrows(
                    IllegalMoveException.class,
                    () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals("It's not your turn. Expected: WHITE, but got: BLACK", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_TargetPositionOutOfBounds(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    String initialPosition = "e2";
    String targetPosition = "e9";

    // Act & Assert
    ConvertInputToPositionException exception =
            assertThrows(
                    ConvertInputToPositionException.class,
                    () -> gameLogic.moveTo(initialPosition, targetPosition, match));
    assertEquals(
            "Invalid position. Position must be within the chessboard.", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_InvalidDirectionForPiece(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    String initialPosition = "b1";
    String targetPosition = "b5";

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
  void testMoveTo_InvalidMove_BlockingPieceInPath(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
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
    String initialPosition = "e3";
    String targetPosition = "e5";

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
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    Match newMatch = new Match(playerWhite, playerBlack, matchId);

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
  void testMoveTo_InvalidMove_SameStartAndEndPosition()
          throws ConvertInputToPositionException{
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    String startPosition = "b5";
    String endPosition = "b5"; // Same as start position

    // Set a piece at the start position
    Piece piece = new Pawn(Color.WHITE, convertInputToPosition(startPosition), match.getBoard());
    match.getBoard().setPieceAtPosition(convertInputToPosition(startPosition), piece);

    // Act & Assert
    IllegalMoveException exception =
            assertThrows(
                    IllegalMoveException.class, () -> gameLogic.moveTo(startPosition, endPosition, match));

    assertEquals(
            "Illegal move to position: "
                    + convertInputToPosition(endPosition)
                    + ". The start and end positions are the same.",
            exception.getMessage());
  }


  @Test
  void testAcceptRemi(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Act
    gameLogic.acceptRemi(match);

    // Assert
    assertEquals(MatchOutcome.REMI, match.getWinner());
    assertTrue(match.isGameEnded());
  }

  @Test
  void testResign_WhitePlayerResigns(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Act
    gameLogic.resign(match);

    // Assert
    assertEquals(MatchOutcome.BLACK, match.getWinner());
    assertTrue(match.isGameEnded());
  }

  @Test
  void testResign_BlackPlayerResigns(){
    // Arrange
    String matchId = "match1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    match.toggleNextToMove();

    // Act
    gameLogic.resign(match);

    // Assert
    assertEquals(MatchOutcome.WHITE, match.getWinner());
    assertTrue(match.isGameEnded());
  }

  @Test
  void testShortestGame(){
    // Arrange
    String matchId = "shortestGame";

    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    try {
      gameLogic.moveTo("f2", "f3", match);
      gameLogic.moveTo("e7", "e5", match);

      gameLogic.moveTo("g2", "g4", match);
      gameLogic.moveTo("d8", "h4", match);

      // TODO: Expand with check and checkmate
      match.declareWinner(MatchOutcome.BLACK);
      System.out.println(gameLogic.endGame(match));
      assertTrue(match.isGameEnded());
      assertEquals(MatchOutcome.BLACK, match.getWinner());
      assertEquals(4, match.getMoveCount());
    } catch (IllegalMoveException e) {
      fail(e.getMessage());
    } catch (ConvertInputToPositionException e) {
      throw new RuntimeException(e);
    }
  }
  @Test
  void testLoadMatch_UpdatePlayers() {
    // Arrange
    String matchId = "testMatch";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");


    List<Match> matches = new ArrayList<>();
    Match match = new Match(playerWhite, playerBlack, matchId);
    matches.add(match);
    persistence.saveMatches(matches, pathMatches);

    // Act
    Match loadedMatch = gameLogic.loadMatch(matchId);

    // Assert
    assertNotNull(loadedMatch);
    assertSoftly(softly -> {
      softly.assertThat(loadedMatch.getPlayerWhite().getName()).isEqualTo("Alice");
      softly.assertThat(loadedMatch.getPlayerBlack().getName()).isEqualTo("Bob");
      softly.assertThat(loadedMatch.getPlayerWhite()).isEqualTo(playerWhite);
      softly.assertThat(loadedMatch.getPlayerBlack()).isEqualTo(playerBlack);
    });
  }


  @Test
  void testEndGame_DeclareWinnerRemi() {
    // Arrange
    String matchId = "matchToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Act
    gameLogic.acceptRemi(match);
    String result = gameLogic.endGame(match);

    // Assert
    assertSoftly(softly -> {
      softly.assertThat(match.isGameEnded()).isTrue();
      softly.assertThat(match.getWinner()).isEqualTo(MatchOutcome.REMI);
      softly.assertThat(result).contains("The game ended in Remi.");
    });
  }

  @Test
  void testEndGame_DeclareWinnerWhite() {
    // Arrange
    String matchId = "matchToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    match.declareWinner(MatchOutcome.WHITE);

    // Act
    String result = gameLogic.endGame(match);

    // Assert
    assertSoftly(softly -> {
      softly.assertThat(match.isGameEnded()).isTrue();
      softly.assertThat(match.getWinner()).isEqualTo(MatchOutcome.WHITE);
      softly.assertThat(result).contains("WHITE won this game. Congrats Alice (new ELO: " + playerWhite.getElo() + ")");
    });
  }

  @Test
  void testEndGame_DeclareWinnerBlack() {
    // Arrange
    String matchId = "matchToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    match.declareWinner(MatchOutcome.BLACK);

    // Act
    String result = gameLogic.endGame(match);

    // Assert
    assertSoftly(softly -> {
      softly.assertThat(match.isGameEnded()).isTrue();
      softly.assertThat(match.getWinner()).isEqualTo(MatchOutcome.BLACK);
      softly.assertThat(result).contains("BLACK won this game. Congrats Bob (new ELO: " + playerBlack.getElo() + ")");
    });
  }

  @Test
  void testDeleteMatch_MatchExists() {
    // Arrange
    String matchId = "matchToDelete";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);

    // Act & Assert
    TheMatchHasNotEndedException exception =
            assertThrows(
                    TheMatchHasNotEndedException.class, () -> gameLogic.endGame(match));

    assertEquals(
            "The game has not ended yet",
            exception.getMessage());
  }

  @Test
  void testDeleteMatch_MatchDoesNotExist() {
    // Arrange
    String matchId = "nonExistentMatch";

    // Act
    List<Match> loadedMatches = persistence.loadMatches(pathMatches);
    int initialSize = loadedMatches.size();

    // Assert
    assertSoftly(softly -> {
      softly.assertThat(initialSize).isEqualTo(loadedMatches.size());
      softly.assertThat(loadedMatches.stream().noneMatch(m -> m.getId().equals(matchId))).isTrue();
    });
  }
  @Test
  void testEndGame_DeleteMatchAfterEnding() {
    // Arrange
    String matchId = "matchToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createMatch(playerWhite, playerBlack, matchId);
    Match match = gameLogic.loadMatch(matchId);
    match.declareWinner(MatchOutcome.BLACK);

    // Act
    String result = gameLogic.endGame(match);

    // Assert
    assertSoftly(softly -> {
      softly.assertThat(match.isGameEnded()).isTrue();
      softly.assertThat(match.getWinner()).isEqualTo(MatchOutcome.BLACK);
      softly.assertThat(result).contains("BLACK won this game. Congrats Bob (new ELO: " + playerBlack.getElo() + ")");
      softly.assertThat(persistence.loadMatches(pathMatches).stream().anyMatch(m -> m.getId().equals(matchId))).isFalse();

    });
  }

  @Test
  void convertInputToPosition_Valid() {
    assertSoftly(
            softly -> {
              try {
                softly.assertThat(convertInputToPosition("a8")).isEqualTo(new Position(7, 0));
                softly.assertThat(convertInputToPosition("h1")).isEqualTo(new Position(0, 7));
                softly.assertThat(convertInputToPosition("e5")).isEqualTo(new Position(4, 4));
              } catch (ConvertInputToPositionException e) {
                throw new RuntimeException(e);
              }
            });
  }

  @Test
  void convertInputToPosition_InvalidFormat() {
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition(""));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("a"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("abc"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("a12"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("12"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("abc12"));
  }

  @Test
  void testInvalidPosition() {
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("i1"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("a0"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("a9"));
    assertThrows(ConvertInputToPositionException.class, () -> convertInputToPosition("h9"));
  }
}