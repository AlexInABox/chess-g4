package hwr.oop.chess;

import static hwr.oop.chess.GameLogic.convertInputToPosition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.persistence.FileBasedPersistence;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.pieces.*;
import hwr.oop.chess.player.Player;
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
  private static final String TEST_FILE_PATH_GAMES = "target/GameLogicTestGames.txt";
  private static final String TEST_FILE_PATH_PLAYERS = "target/GameLogicTestPlayers.txt";

  @BeforeEach
  void setUp() {
    File fileGames = new File(TEST_FILE_PATH_GAMES);
    Path pathGames = fileGames.toPath();
    File filePlayers = new File(TEST_FILE_PATH_PLAYERS);
    Path pathPlayers = filePlayers.toPath();
    persistence = new FileBasedPersistence(pathGames, pathPlayers);
    gameLogic = new GameLogic(persistence);
  }

  @AfterEach
  void tearDown() {
    File fileGames = new File(TEST_FILE_PATH_GAMES);
    if (fileGames.exists()) {
      if (!fileGames.delete()) {
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
  void testLoadGame_GameFound() {
    // Arrange
    String gameId = "shortestGame";
    Player playerWhite = gameLogic.loadPlayer("Player1");
    Player playerBlack = gameLogic.loadPlayer("Player2");
    List<Game> games = new ArrayList<>();
    Game expectedGame = new Game(playerWhite, playerBlack, gameId);

    games.add(expectedGame);
    persistence.saveGames(games);

    // Act
    Game loadedGame = gameLogic.loadGame("shortestGame");

    // Assert
    assertNotNull(loadedGame);
    assertEquals(expectedGame, loadedGame);
    assertEquals(expectedGame.getPlayerWhite().getElo(), gameLogic.loadPlayer("Player1").getElo());
  }

  @Test
  void testLoadGame_GameNotFound() {
    // Act & Assert
    assertThrows(GameNotFoundException.class, () -> gameLogic.loadGame("123"));
    GameNotFoundException exception =
        assertThrows(GameNotFoundException.class, () -> gameLogic.loadGame("123"));
    String expectedMessage = "Game with ID '123' not found.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testSaveGame_NewGame() {
    // Arrange
    List<Game> games = new ArrayList<>();
    persistence.saveGames(games);
    Game newGame = new Game(new Player("Alice"), new Player("Bob"), "67");
    // Act
    gameLogic.saveGame(newGame);

    // Assert
    List<Game> loadedGames = persistence.loadGames();
    assertTrue(loadedGames.contains(newGame));
  }

  @Test
  void testSaveGame_OverwriteExistingGame() {
    Player playerWhite = gameLogic.loadPlayer("Player1");
    Player playerBlack = gameLogic.loadPlayer("Player2");
    Player playerWhite2 = gameLogic.loadPlayer("Player3");

    // Arrange
    Game existingGame = new Game(playerWhite, playerBlack, "123");
    Game updatedGame = new Game(playerWhite2, playerBlack, "123");
    List<Game> games = new ArrayList<>();
    games.add(existingGame);
    persistence.saveGames(games);

    // Act
    gameLogic.saveGame(updatedGame);

    // Assert
    List<Game> loadedGames = persistence.loadGames();
    assertTrue(loadedGames.contains(updatedGame));
  }

  @Test
  void testCreateGame_Successful() {
    // Arrange
    String gameId = "456";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");

    // Act
    gameLogic.createGame(playerWhite, playerBlack, gameId);

    // Assert
    List<Game> loadedGames = persistence.loadGames();
    assertTrue(loadedGames.contains(new Game(playerWhite, playerBlack, gameId)));
  }

  @Test
  void testCreateGame_GameAlreadyExists() {
    // Arrange
    String gameId = "456";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");

    // Arrange: Save a game with the same ID
    List<Game> games = new ArrayList<>();
    games.add(new Game(playerWhite, playerBlack, gameId));
    persistence.saveGames(games);

    // Act & Assert
    assertThrows(
        GameAlreadyExistsException.class,
        () -> gameLogic.createGame(playerWhite, playerBlack, gameId));
  }

  @Test
  void testLoadPlayer_PlayerFound() {
    // Arrange
    Player expectedPlayer = new Player("Player1");
    List<Player> players = new ArrayList<>();
    players.add(expectedPlayer);
    persistence.savePlayers(players);

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
    List<Player> loadedPlayers = persistence.loadPlayers();
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
    persistence.savePlayers(players);

    // Act
    gameLogic.savePlayer(updatedPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers();
    assertTrue(loadedPlayers.contains(updatedPlayer));
  }

  @Test
  void testLoadPlayer_PlayerWithSameName() {
    // Arrange
    Player existingPlayer = new Player("Alice");
    List<Player> players = new ArrayList<>();
    players.add(existingPlayer);
    persistence.savePlayers(players);

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
    persistence.savePlayers(players);

    // Act
    gameLogic.savePlayer(updatedPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers();
    assertTrue(loadedPlayers.contains(updatedPlayer));
  }

  @Test
  void testMoveTo_ValidMove1() throws ConvertInputToPositionException, IllegalMoveException {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    String initialPosition = "e2";
    String targetPosition = "e4";

    // Act

    gameLogic.moveTo(initialPosition, targetPosition, game);
    gameLogic.saveGame(game);

    // Assert
    Piece movedPiece = game.getBoard().getPieceAtPosition(convertInputToPosition(targetPosition));
    assertNotNull(movedPiece);
    assertEquals(convertInputToPosition(targetPosition), movedPiece.getPosition());
  }

  @Test
  void testMoveTo_InvalidMove_NoPieceAtStartPosition() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    String initialPosition = "d3";
    String targetPosition = "d4";

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, game));
    assertEquals(
        "No piece at the specified position: Position[row=2, column=3]", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_WrongPlayerTurn() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    String initialPosition = "e7";
    String targetPosition = "e6";

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, game));
    assertEquals("It's not your turn. Expected: WHITE, but got: BLACK", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_TargetPositionOutOfBounds() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    String initialPosition = "e2";
    String targetPosition = "e9";

    // Act & Assert
    ConvertInputToPositionException exception =
        assertThrows(
            ConvertInputToPositionException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, game));
    assertEquals(
        "Invalid position. Position must be within the chessboard.", exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_InvalidDirectionForPiece() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    String initialPosition = "b1";
    String targetPosition = "b5";

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, game));
    assertEquals(
        "Illegal move to position: Position[row=4, column=1]. Possible possible moves are for example: Position[row=2, column=2], Position[row=2, column=0]",
        exception.getMessage());
  }

  @Test
  void testMoveTo_InvalidMove_BlockingPieceInPath() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    game.getBoard().clearChessboard();
    game
        .getBoard()
        .setPieceAtPosition(
            new Position(0, 0), new King(Color.WHITE, new Position(0, 0), game.getBoard()));
    game
        .getBoard()
        .setPieceAtPosition(
            new Position(2, 4),
            new Rook(Color.WHITE, new Position(2, 4), game.getBoard())); // Rook blocking the path
    game
        .getBoard()
        .setPieceAtPosition(
            new Position(3, 4),
            new Pawn(
                Color.BLACK, new Position(3, 4), game.getBoard())); // Pawn trying to move here
    String initialPosition = "e3";
    String targetPosition = "e5";

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class,
            () -> gameLogic.moveTo(initialPosition, targetPosition, game));
    assertEquals(
        "Illegal move to position: Position[row=4, column=4]. Possible possible moves are for example: Position[row=3, column=4], Position[row=1, column=4]",
        exception.getMessage());
  }

  @Test
  void testSaveGame_AddNewGameWhenNotExists() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    Game newGame = new Game(playerWhite, playerBlack, gameId);

    // Act
    gameLogic.saveGame(newGame);

    // Assert
    List<Game> loadedGames = persistence.loadGames();
    assertTrue(loadedGames.contains(newGame));
  }

  @Test
  void testSaveGame_onlyOverwritesOneGame() {
    // Arrange
    String gameId2 = "game2";
    Player playerWhite2 = gameLogic.loadPlayer("Luisa");
    Player playerBlack2 = gameLogic.loadPlayer("Jake");
    gameLogic.createGame(playerWhite2, playerBlack2, gameId2);
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    Game newGame = new Game(playerWhite, playerBlack, gameId);

    // Act
    gameLogic.saveGame(newGame);

    // Assert
    List<Game> loadedGames = persistence.loadGames();
    assertTrue(loadedGames.contains(newGame));
    assertThat(loadedGames.getFirst().getPlayerWhite().getName()).isEqualTo("Luisa");
    assertThat(loadedGames.get(1).getPlayerWhite().getName()).isEqualTo("Alice");
  }

  @Test
  void testSavePlayer_AddNewPlayerWhenNotExists() {
    // Arrange
    Player newPlayer = new Player("Alice");

    // Act
    gameLogic.savePlayer(newPlayer);

    // Assert
    List<Player> loadedPlayers = persistence.loadPlayers();
    assertTrue(loadedPlayers.contains(newPlayer));
  }

  @Test
  void testMoveTo_InvalidMove_SameStartAndEndPosition() throws ConvertInputToPositionException {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    String startPosition = "b5";
    String endPosition = "b5"; // Same as start position

    // Set a piece at the start position
    Piece piece = new Pawn(Color.WHITE, convertInputToPosition(startPosition), game.getBoard());
    game.getBoard().setPieceAtPosition(convertInputToPosition(startPosition), piece);

    // Act & Assert
    IllegalMoveException exception =
        assertThrows(
            IllegalMoveException.class, () -> gameLogic.moveTo(startPosition, endPosition, game));

    assertEquals(
        "Illegal move to position: "
            + convertInputToPosition(endPosition)
            + ". The start and end positions are the same.",
        exception.getMessage());
  }

  @Test
  void testAcceptRemi() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    playerWhite.setElo((short) 2950);
    playerBlack.setElo((short) 2630);
    gameLogic.savePlayer(playerWhite);
    gameLogic.savePlayer(playerBlack);
    playerWhite = gameLogic.loadPlayer("Alice");
    playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    // Act
    gameLogic.acceptRemi(game);

    // Assert
    assertEquals(GameOutcome.REMI, game.getWinner());
    assertTrue(game.isGameEnded());
    assertThat(gameLogic.loadPlayer("Alice").getElo()).isEqualTo((short) 2943);
    assertThat(gameLogic.loadPlayer("Bob").getElo()).isEqualTo((short) 2637);
  }

  @Test
  void testResign_WhitePlayerResigns() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    // Act
    gameLogic.resign(game);

    // Assert
    assertEquals(GameOutcome.BLACK, game.getWinner());
    assertTrue(game.isGameEnded());
  }

  @Test
  void testResign_BlackPlayerResigns() {
    // Arrange
    String gameId = "game1";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    game.toggleNextToMove();

    // Act
    gameLogic.resign(game);

    // Assert
    assertEquals(GameOutcome.WHITE, game.getWinner());
    assertTrue(game.isGameEnded());
  }

  @Test
  void testShortestGame() {
    // Arrange
    String gameId = "shortestGame";

    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    try {
      gameLogic.moveTo("f2", "f3", game);
      gameLogic.moveTo("e7", "e5", game);

      gameLogic.moveTo("g2", "g4", game);
      gameLogic.moveTo("d8", "h4", game);

      // TODO: Expand with check and checkmate
      game.declareWinner(GameOutcome.BLACK);
      System.out.println(gameLogic.endGame(game));
      assertTrue(game.isGameEnded());
      assertEquals(GameOutcome.BLACK, game.getWinner());
      assertEquals(4, game.getMoveCount());
      assertEquals((short) 1190, gameLogic.loadPlayer("Alice").getElo());
      assertEquals((short) 1210, gameLogic.loadPlayer("Bob").getElo());
    } catch (IllegalMoveException e) {
      fail(e.getMessage());
    } catch (ConvertInputToPositionException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testLoadGame_UpdatePlayers() {
    // Arrange
    String gameId = "testGame";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");

    List<Game> games = new ArrayList<>();
    Game game = new Game(playerWhite, playerBlack, gameId);
    games.add(game);
    persistence.saveGames(games);

    playerWhite.setElo((short) 1299);
    playerBlack.setElo((short) 1266);
    gameLogic.savePlayer(playerWhite);
    gameLogic.savePlayer(playerBlack);
    // Act
    Game loadedGame = gameLogic.loadGame(gameId);

    // Assert
    assertNotNull(loadedGame);
    assertSoftly(
        softly -> {
          softly.assertThat(loadedGame.getPlayerWhite().getName()).isEqualTo("Alice");
          softly.assertThat(loadedGame.getPlayerBlack().getName()).isEqualTo("Bob");
          softly.assertThat(loadedGame.getPlayerWhite()).isEqualTo(playerWhite);
          softly.assertThat(loadedGame.getPlayerBlack()).isEqualTo(playerBlack);
        });
  }

  @Test
  void testEndGame_DeclareWinnerRemi() {
    // Arrange
    String gameId = "gameToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    // Act
    gameLogic.acceptRemi(game);
    String result = gameLogic.endGame(game);

    // Assert
    assertSoftly(
        softly -> {
          softly.assertThat(game.isGameEnded()).isTrue();
          softly.assertThat(game.getWinner()).isEqualTo(GameOutcome.REMI);
          softly.assertThat(result).contains("The game ended in Remi.");
        });
  }

  @Test
  void testEndGame_DeclareWinnerWhite() {
    // Arrange
    String gameId = "gameToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    game.declareWinner(GameOutcome.WHITE);

    // Act
    String result = gameLogic.endGame(game);

    // Assert
    assertSoftly(
        softly -> {
          softly.assertThat(game.isGameEnded()).isTrue();
          softly.assertThat(game.getWinner()).isEqualTo(GameOutcome.WHITE);
          softly.assertThat(result).contains("WHITE won this game. Congrats Alice (new ELO: 1210)");
          softly.assertThat(gameLogic.loadPlayer("Alice").getElo()).isEqualTo((short) 1210);
          softly.assertThat(gameLogic.loadPlayer("Bob").getElo()).isEqualTo((short) 1190);
        });
  }

  @Test
  void testEndGame_DeclareWinnerBlack() {
    // Arrange
    String gameId = "gameToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    game.declareWinner(GameOutcome.BLACK);

    // Act
    String result = gameLogic.endGame(game);

    // Assert
    assertSoftly(
        softly -> {
          softly.assertThat(game.isGameEnded()).isTrue();
          softly.assertThat(game.getWinner()).isEqualTo(GameOutcome.BLACK);
          softly.assertThat(result).contains("BLACK won this game. Congrats Bob (new ELO: 1210)");
        });
  }

  @Test
  void testDeleteGame_GameExists() {
    // Arrange
    String gameId = "gameToDelete";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    // Act & Assert
    GameHasNotEndedException exception =
        assertThrows(GameHasNotEndedException.class, () -> gameLogic.endGame(game));

    assertEquals("The game has not ended yet", exception.getMessage());
  }

  @Test
  void testDeleteGame_GameDoesNotExist() {
    // Arrange
    String gameId = "nonExistentGame";

    // Act
    List<Game> loadedGames = persistence.loadGames();
    int initialSize = loadedGames.size();

    // Assert
    assertSoftly(
        softly -> {
          softly.assertThat(initialSize).isEqualTo(loadedGames.size());
          softly
              .assertThat(loadedGames.stream().noneMatch(m -> m.getId().equals(gameId)))
              .isTrue();
        });
  }

  @Test
  void testEndGame_DeleteGameAfterEnding() {
    // Arrange
    String gameId = "gameToEnd";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    game.declareWinner(GameOutcome.BLACK);

    // Act
    String result = gameLogic.endGame(game);

    // Assert
    assertSoftly(
        softly -> {
          softly.assertThat(game.isGameEnded()).isTrue();
          softly.assertThat(game.getWinner()).isEqualTo(GameOutcome.BLACK);
          softly.assertThat(result).contains("BLACK won this game. Congrats Bob (new ELO: 1210)");
          softly
              .assertThat(
                  persistence.loadGames().stream().anyMatch(m -> m.getId().equals(gameId)))
              .isFalse();
        });
  }

  @Test
  void testDeleteGame_OnlyDeletesOneGame() {
    // Arrange
    String gameId = "gameToDelete";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    gameLogic.createGame(playerWhite, playerBlack, "gameNotToDelete");
    Game game = gameLogic.loadGame(gameId);
    game.declareWinner(GameOutcome.BLACK);

    // Act
    gameLogic.endGame(game);
    List<Game> loadedGames = persistence.loadGames();


    // Act & Assert

    assertThat(loadedGames.getFirst().getId()).isEqualTo("gameNotToDelete");
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

  @Test
  void testGetPossibleMoves_PieceAtPosition() {
    // Arrange
    String gameId = "testGame";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);
    Position initialPosition = convertInputToPosition("e2");
    Piece piece = new Pawn(Color.WHITE, initialPosition, game.getBoard());
    game.getBoard().setPieceAtPosition(initialPosition, piece);

    // Act
    List<Position> possibleMoves = gameLogic.getPossibleMoves("e2", game);

    // Assert
    assertNotNull(possibleMoves);
    assertFalse(possibleMoves.isEmpty());
  }

  @Test
  void testGetPossibleMoves_NoPieceAtPosition() {
    // Arrange
    String gameId = "testGame";
    Player playerWhite = gameLogic.loadPlayer("Alice");
    Player playerBlack = gameLogic.loadPlayer("Bob");
    gameLogic.createGame(playerWhite, playerBlack, gameId);
    Game game = gameLogic.loadGame(gameId);

    // Act
    List<Position> possibleMoves = gameLogic.getPossibleMoves("e3", game);
    // Assert
    assertNotNull(possibleMoves);
    assertTrue(possibleMoves.isEmpty());
    //Test, return value can not be changed to Collections.emptyList
    possibleMoves.add(new Position(2,3));
  }
}
