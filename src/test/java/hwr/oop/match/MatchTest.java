package hwr.oop.match;

import hwr.oop.board.ChessBoard;
import hwr.oop.board.ChessBoardException;
import hwr.oop.player.Player;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


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
    ChessBoard board = new ChessBoard();
    match = new Match(whitePlayer, blackPlayer, board);

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(match.getPlayerWhite()).isEqualTo(whitePlayer);
          softly.assertThat(match.getPlayerBlack()).isEqualTo(blackPlayer);
        });
  }

  @Test
  void testPlayerAndFENConstructors() throws ChessBoardException {
    Player whitePlayer = new Player("White");
    Player blackPlayer = new Player("Black");
    String fenNotation = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    match = new Match(whitePlayer, blackPlayer, fenNotation);

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(match.getPlayerWhite()).isEqualTo(whitePlayer);
          softly.assertThat(match.getPlayerBlack()).isEqualTo(blackPlayer);
          softly.assertThat(match.getFEN()).isEqualTo(fenNotation);
        });
  }
  @Test
  void testEquals_IdenticalInstances() throws ChessBoardException {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match1 = new Match(playerWhite, playerBlack, board);
    Match match2 = new Match(playerWhite, playerBlack, board);
    assertThat(match1.equals(match2)).isTrue();
  }

  @SuppressWarnings("EqualsWithItself")
  @Test
  void testEquals_SameInstance() throws ChessBoardException {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack, board);
    assertThat(match.equals(match)).isTrue();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void testEquals_InstanceNull() throws ChessBoardException {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack, board);
    assertThat(match.equals(null)).isFalse();
  }

  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  @Test
  void testEquals_DifferentClass() throws ChessBoardException {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack, board);
    assertThat(match.equals(board)).isFalse();
  }

  @Test
  void testHashCode_IdenticalHashCode() throws ChessBoardException {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match1 = new Match(playerWhite, playerBlack, board);
    Match match2 = new Match(playerWhite, playerBlack, board);
    assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
  }

  @Test
  void testHashCode_DifferentHashCode() throws ChessBoardException {
    Player playerWhite1 = new Player("White");
    Player playerBlack1 = new Player("Black");
    ChessBoard board1 = new ChessBoard();
    Match match1 = new Match(playerWhite1, playerBlack1, board1);

    Player playerWhite2 = new Player("Alice");
    Player playerBlack2 = new Player("James");
    ChessBoard board2 = new ChessBoard();
    Match match2 = new Match(playerWhite2, playerBlack2, board2);

    assertThat(match1.hashCode()).isNotEqualTo(match2.hashCode());
  }

  @Test
  void testToString() throws ChessBoardException {
    Player playerWhite = new Player("White");
    Player playerBlack = new Player("Black");
    ChessBoard board = new ChessBoard();
    Match match = new Match(playerWhite, playerBlack, board);
    String expectedString = "Match{" +
            "playerWhite=" + playerWhite +
            ", playerBlack=" + playerBlack +
            ", board=" + board +
            ", fenNotation='" + null + '\'' +
            ", nextToMove=WHITE" +
            ", moveCount=0" +
            ", gameEnded=false" +
            '}';
    assertThat(match.toString()).isEqualTo(expectedString);
  }
}
