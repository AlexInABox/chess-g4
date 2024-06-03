package hwr.oop.chess.match;

import hwr.oop.chess.Color;
import hwr.oop.chess.board.ChessBoard;
import hwr.oop.chess.player.Player;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
  private Match match;

  @Test
  void testOnlyPlayerConstructors() {
    Player whitePlayer = new Player("White");
    Player blackPlayer = new Player("Black");
    match = new Match(whitePlayer, blackPlayer, "1");

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(match.getPlayerWhite()).isEqualTo(whitePlayer);
          softly.assertThat(match.getPlayerBlack()).isEqualTo(blackPlayer);
        });
  }

  @Test
  void testPlayerAndBoardConstructors() {
    Player whitePlayer = new Player("White");
    Player blackPlayer = new Player("Black");
    match = new Match(whitePlayer, blackPlayer, "1");

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(match.getPlayerWhite()).isEqualTo(whitePlayer);
          softly.assertThat(match.getPlayerBlack()).isEqualTo(blackPlayer);
        });
  }

  @Test
  void testPlayerAndFENConstructors() throws FENException {
    Player whitePlayer = new Player("White");
    Player blackPlayer = new Player("Black");
    String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b";
    match = new Match(whitePlayer, blackPlayer, fenNotation, "1");

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(match.getPlayerWhite()).isEqualTo(whitePlayer);
          softly.assertThat(match.getPlayerBlack()).isEqualTo(blackPlayer);
          softly.assertThat(match.getFEN()).isEqualTo(fenNotation);
          softly.assertThat(match.getNextToMove()).isEqualTo(Color.BLACK);
        });
  }

  @Test
  void testEquals_IdenticalInstances() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match1 = new Match(playerWhite, playerBlack, "1");
    Match match2 = new Match(playerWhite, playerBlack, "1");
    assertThat(match1.equals(match2)).isTrue();
  }

  @SuppressWarnings("EqualsWithItself")
  @Test
  void testEquals_SameInstance() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack, "1");
    assertThat(match.equals(match)).isTrue();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void testEquals_InstanceNull() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack, "1");
    assertThat(match.equals(null)).isFalse();
  }

  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  @Test
  void testEquals_DifferentClass() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack, "1");
    assertThat(match.equals(board)).isFalse();
  }

  @Test
  void testHashCode_IdenticalHashCode() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match1 = new Match(playerWhite, playerBlack, "1");
    Match match2 = new Match(playerWhite, playerBlack, "1");
    assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
  }

  @Test
  void testHashCode_DifferentHashCode() {
    Player playerWhite1 = new Player("White");
    Player playerBlack1 = new Player("Black");
    Match match1 = new Match(playerWhite1, playerBlack1, "1");

    Player playerWhite2 = new Player("Alice");
    Player playerBlack2 = new Player("James");
    Match match2 = new Match(playerWhite2, playerBlack2, "1");

    assertThat(match1.hashCode()).isNotEqualTo(match2.hashCode());
  }

  @Test
  void testToString() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    String id = "1";
    Match match = new Match(playerWhite, playerBlack, id);
    String expectedString =
        "Match{"
            + "id='1', playerWhite="
            + playerWhite
            + ", playerBlack="
            + playerBlack
            + ", board="
            + board
            + ", fenNotation='"
            + null
            + '\''
            + ", nextToMove=WHITE"
            + ", moveCount=0"
            + ", gameEnded=false"
            + '}';
    assertThat(match.toString()).isEqualTo(expectedString);
  }

  @Test
  void testToFEN_InitialSetUp() {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match match = new Match(playerWhite, playerBlack, "1");
    assertThat(match.convertBoardToFEN())
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 0");
  }

  @Test
  void testToFEN_CustomPosition() throws FENException {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    String fen = "5b1r/8/8/3Q4/8/8/8/8 w 0";
    final Match match = new Match(playerWhite, playerBlack, fen, "1");
    assertThat(match.convertBoardToFEN()).isEqualTo(fen);
  }

  @Test
  void testBuildFromFEN_ValidFEN() throws FENException {
    final String fenNotation = "rnbqkbnr/pppppppp/8/4p1p1/2P5/5P2/PPPPPPPP/RNBQKBNR b 0";
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match match = new Match(playerWhite, playerBlack, fenNotation, "1");

    assertThat(match.convertBoardToFEN()).isEqualTo(fenNotation);
  }

  @Test
  void testBuildFromFEN_InvalidFEN() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP w 0"; // Missing last part of FEN
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen, "1"));
    String expectedMessage = "Invalid FEN format: 8 rows expected";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testBuildFromFEN_InvalidRowCount() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/8 w 0"; // 9 rows instead of 8
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen, "1"));
    String expectedMessage = "Invalid FEN format: 8 rows expected";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testBuildFromFEN_InvalidPiece() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPXPPPP/RNBQKBNR w 0"; // Invalid piece 'X'
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen, "1"));
    String expectedMessage = "FEN notation contains invalid Piece";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testBuildFromFEN_InvalidNumberOfParts() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPXPPPP/RNBQKBNR"; // Invalid piece 'X'
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen, "1"));
    String expectedMessage =
        "Invalid FEN format: expected at least 2 parts (board layout and active color)";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testEquals_SameAttributes() {
    Player white1 = new Player("White");
    Player black1 = new Player("Black");
    Match match1 = new Match(white1, black1, "8/8/8/8/8/8/8/8 w 0");

    Player white2 = new Player("White");
    Player black2 = new Player("Black");
    Match match2 = new Match(white2, black2, "8/8/8/8/8/8/8/8 w 0");

    assertThat(match1).isEqualTo(match2);
  }

  @Test
  void testEquals_DifferentPlayers() {
    Player white1 = new Player("White1");
    Player black1 = new Player("Black1");
    Match match1 = new Match(white1, black1, "8/8/8/8/8/8/8/8 w 0");

    Player white2 = new Player("White2");
    Player black2 = new Player("Black2");
    Match match2 = new Match(white2, black2, "8/8/8/8/8/8/8/8 w 0");
    assertThat(match1).isNotEqualTo(match2);
  }

  @Test
  void testEquals_DifferentBoard() {
    Player white = new Player("White");
    Player black = new Player("Black");
    Match match1 = new Match(white, black, "8/8/8/8/8/8/8/8 w 0");
    Match match2 = new Match(white, black, "8/8/8/8/8/8/8/7k w 0");

    assertThat(match1).isNotEqualTo(match2);
  }

  @Test
  void testEquals_DifferentNextToMove() {
    Player white = new Player("White");
    Player black = new Player("Black");
    Match match1 = new Match(white, black, "8/8/8/8/8/8/8/8 w 0");
    Match match2 = new Match(white, black, "8/8/8/8/8/8/8/8 b 0");

    assertThat(match1).isNotEqualTo(match2);
  }

  @Test
  void testGetBoard() {
    Player whitePlayer = new Player("White");
    Player blackPlayer = new Player("Black");
    Match match = new Match(whitePlayer, blackPlayer, "1");

    ChessBoard boardFromMatch = match.getBoard();
    ChessBoard expectedBoard = new ChessBoard();

    assertThat(boardFromMatch).isNotNull().isEqualTo(expectedBoard);
  }

  @Test
  void testToggleNextToMove() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack, "1");

    assertThat(match.getNextToMove()).isEqualTo(Color.WHITE);
    assertThat(match.getMoveCount()).isEqualTo((short) 0);

    match.toggleNextToMove();
    assertThat(match.getNextToMove()).isEqualTo(Color.BLACK);
    assertThat(match.getMoveCount()).isEqualTo((short) 1);

    match.toggleNextToMove();
    assertThat(match.getNextToMove()).isEqualTo(Color.WHITE);
    assertThat(match.getMoveCount()).isEqualTo((short) 2);
  }

  @Test
  void testGetMoveCount_initiallyZero() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack, "1");

    assertThat(match.getMoveCount()).isEqualTo((short) 0);
  }

  @Test
  void testGetMoveCount_afterTogglingNextToMove() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack, "1");

    match.toggleNextToMove();
    assertThat(match.getMoveCount()).isEqualTo((short) 1);

    match.toggleNextToMove();
    assertThat(match.getMoveCount()).isEqualTo((short) 2);
  }

  @Test
  void testIsGameEnded_initiallyFalse() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack, "1");

    assertThat(match.isGameEnded()).isFalse();
  }

  @Test
  void testMoveCountInitialization() throws FENException {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 0";
    final Match match = new Match(playerWhite, playerBlack, fen, "1");

    assertThat(match.getMoveCount()).isEqualTo((short) 0);
  }

  @Test
  void testMoveCountIncrementation() throws FENException {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 5";
    final Match match = new Match(playerWhite, playerBlack, fen, "1");

    assertThat(match.getMoveCount()).isEqualTo((short) 5);

    match.toggleNextToMove();
    assertThat(match.getMoveCount()).isEqualTo((short) 6);
  }

  @Test
  void testMoveCountInFEN() throws FENException {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b 10";
    final Match match = new Match(playerWhite, playerBlack, fen, "1");

    assertThat(match.convertBoardToFEN()).isEqualTo(fen);

    match.toggleNextToMove();
    assertThat(match.convertBoardToFEN())
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 11");
  }

  @Test
  void testInvalidMoveCountInFEN() {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w abc";
    assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen, "1"));
  }
}
