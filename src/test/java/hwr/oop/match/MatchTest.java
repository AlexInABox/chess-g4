package hwr.oop.match;

import hwr.oop.Color;
import hwr.oop.board.ChessBoard;
import hwr.oop.player.Player;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatchTest {
  private Match match;

  @Test
  void testOnlyPlayerConstructors() {
    Player whitePlayer = new Player("White");
    Player blackPlayer = new Player("Black");
    match = new Match(whitePlayer, blackPlayer);

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
    match = new Match(whitePlayer, blackPlayer);

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
    match = new Match(whitePlayer, blackPlayer, fenNotation);

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
    Match match1 = new Match(playerWhite, playerBlack);
    Match match2 = new Match(playerWhite, playerBlack);
    assertThat(match1.equals(match2)).isTrue();
  }

  @SuppressWarnings("EqualsWithItself")
  @Test
  void testEquals_SameInstance() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack);
    assertThat(match.equals(match)).isTrue();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void testEquals_InstanceNull() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match = new Match(playerWhite, playerBlack);
    assertThat(match.equals(null)).isFalse();
  }

  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  @Test
  void testEquals_DifferentClass() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack);
    assertThat(match.equals(board)).isFalse();
  }

  @Test
  void testHashCode_IdenticalHashCode() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    Match match1 = new Match(playerWhite, playerBlack);
    Match match2 = new Match(playerWhite, playerBlack);
    assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
  }

  @Test
  void testHashCode_DifferentHashCode() {
    Player playerWhite1 = new Player("White");
    Player playerBlack1 = new Player("Black");
    Match match1 = new Match(playerWhite1, playerBlack1);

    Player playerWhite2 = new Player("Alice");
    Player playerBlack2 = new Player("James");
    Match match2 = new Match(playerWhite2, playerBlack2);

    assertThat(match1.hashCode()).isNotEqualTo(match2.hashCode());
  }

  @Test
  void testToString() {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack);
    String expectedString =
        "Match{"
            + "playerWhite="
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
    final Match match = new Match(playerWhite, playerBlack);
    assertThat(match.convertBoardToFEN())
        .isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");
  }

  @Test
  void testToFEN_CustomPosition() throws FENException {
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    String fen = "5b1r/8/8/3Q4/8/8/8/8 w";
    final Match match = new Match(playerWhite, playerBlack, fen);
    System.out.println("Match: " + match.convertBoardToFEN());
    assertThat(match.convertBoardToFEN()).isEqualTo(fen);
  }

  @Test
  void testBuildFromFEN_ValidFEN() throws FENException {
    final String fenNotation = "rnbqkbnr/pppppppp/8/4p1p1/2P5/5P2/PPPPPPPP/RNBQKBNR b";
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    final Match match = new Match(playerWhite, playerBlack, fenNotation);

    assertThat(match.convertBoardToFEN()).isEqualTo(fenNotation);
  }

  @Test
  void testBuildFromFEN_InvalidFEN() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP w"; // Missing last part of FEN
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen));
    String expectedMessage = "Invalid FEN format: 8 rows expected";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testBuildFromFEN_InvalidRowCount() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/8 w"; // 9 rows instead of 8
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen));
    String expectedMessage = "Invalid FEN format: 8 rows expected";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testBuildFromFEN_InvalidPiece() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPXPPPP/RNBQKBNR w"; // Invalid piece 'X'
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen));
    String expectedMessage = "FEN notation contains invalid Piece";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testBuildFromFEN_InvalidNumberOfParts() {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPXPPPP/RNBQKBNR"; // Invalid piece 'X'
    final Player playerWhite = new Player("player1");
    final Player playerBlack = new Player("player2");
    FENException exception =
        assertThrows(FENException.class, () -> new Match(playerWhite, playerBlack, fen));
    String expectedMessage =
        "Invalid FEN format: expected at least 2 parts (board layout and active color)";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
