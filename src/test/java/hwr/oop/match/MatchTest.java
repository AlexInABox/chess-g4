package hwr.oop.match;

import hwr.oop.board.ChessBoard;
import hwr.oop.board.ChessBoardException;
import hwr.oop.player.Player;
import hwr.oop.Position;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

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
}
