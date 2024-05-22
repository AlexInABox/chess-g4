package hwr.oop.pieces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.board.ChessBoard;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BishopTest {

  private ChessBoard board;

  @BeforeEach
  void setup() {
    board = new ChessBoard();
  }

  @Test
  void testWhiteBishopConstructor() {
    Position position = new Position(0, 0);
    Piece whiteBishop = new Bishop(Color.WHITE, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(whiteBishop.getType()).isEqualTo(PieceType.BISHOP);
          softly.assertThat(whiteBishop.getColor()).isEqualTo(Color.WHITE);
          softly.assertThat(whiteBishop.getPosition()).isEqualTo(position);
          softly.assertThat(whiteBishop.getSymbol()).isEqualTo('B');
        });
  }

  @Test
  void testBlackBishopConstructor() {
    Position position = new Position(0, 0);
    Piece blackBishop = new Bishop(Color.BLACK, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(blackBishop.getType()).isEqualTo(PieceType.BISHOP);
          softly.assertThat(blackBishop.getColor()).isEqualTo(Color.BLACK);
          softly.assertThat(blackBishop.getPosition()).isEqualTo(position);
          softly.assertThat(blackBishop.getSymbol()).isEqualTo('b');
        });
  }

  @Test
  void testBishopMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(5, 5);
    Piece bishop = new Bishop(Color.WHITE, position, board);
    board.setPieceAtPosition(bishop.getPosition(), bishop);

    bishop.moveTo(targetPosition);
    assertThat(bishop.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testBishopMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 1);
    Piece bishop = new Bishop(Color.WHITE, position, board);
    board.setPieceAtPosition(bishop.getPosition(), bishop);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> bishop.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(bishop.getPosition()).isEqualTo(position);
  }

  @Test
  void equals_IdenticalPieces() {
    Piece piece1 = new Bishop(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Bishop(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isTrue();
  }

  @Test
  @SuppressWarnings("EqualsWithItself")
  void equals_sameInstance() {
    Piece piece = new Bishop(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(piece)).isTrue();
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void equals_InstanceNull() {
    Piece piece = new Bishop(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(null)).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentClass() {
    Piece piece = new Bishop(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals("String")).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentPieces() {
    Piece piece1 = new Bishop(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isFalse();
  }

  @Test
  void hashCode_IdenticalPieces() {
    Piece piece1 = new Bishop(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Bishop(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isEqualTo(piece2.hashCode());
  }

  @Test
  void hashCode_DifferentPieces() {
    Piece piece1 = new Bishop(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isNotEqualTo(piece2.hashCode());
  }

  @Test
  void testPieceSetPosition() {
    Position oldPosition = new Position(0, 0);
    Position newPosition = new Position(5, 0);
    Piece bishop = new Bishop(Color.WHITE, oldPosition, board);
    bishop.setPosition(newPosition);
    assertThat(board.getPieceAtPosition(oldPosition)).isNull();
    assertThat(bishop.getPosition()).isEqualTo(newPosition);
    assertThat(board.getPieceAtPosition(newPosition)).isEqualTo(bishop);
  }

  @Test
  void testToString() {
    Position position = new Position(0, 0);
    Piece piece = new Bishop(Color.WHITE, position, board);
    String expectedString = "Bishop{color=WHITE, position=Position[row=0, column=0], symbol=B}";
    assertThat(piece.toString()).isEqualTo(expectedString);
  }

  @Test
  void testToStringWithDifferentPieceType() {
    ChessBoard chessBoard = new ChessBoard();
    Piece piece = new Pawn(Color.BLACK, new Position(6, 1), chessBoard);
    String expectedString = "Bishop{color=BLACK, position=Position[row=6, column=1], symbol=p}";
    assertThat(piece.toString()).isNotEqualTo(expectedString);
  }

  @Test
  void testToStringWithDifferentPosition() {
    ChessBoard chessBoard = new ChessBoard();
    Piece piece = new Pawn(Color.BLACK, new Position(0, 0), chessBoard);
    String expectedString = "Pawn{color=BLACK, position=Position[row=4, column=3], symbol=p}";
    assertThat(piece.toString()).isNotEqualTo(expectedString);
  }

  @Test
  void getType() {
    Position position = new Position(0, 0);
    Bishop bishop = new Bishop(Color.WHITE, position, board);
    assertThat(bishop.getType()).isEqualTo(PieceType.BISHOP);
  }

  @Test
  void hashCode_IdenticalHashCode() {
    ChessBoard board2 = new ChessBoard();
    Bishop bishop1 = new Bishop(Color.WHITE, new Position(4, 4), board);
    Bishop bishop2 = new Bishop(Color.WHITE, new Position(4, 4), board2);
    assertThat(bishop1.hashCode()).isEqualTo(bishop2.hashCode());
  }

  @Test
  void hashCode_DifferentHashCode() {
    Bishop bishop1 = new Bishop(Color.BLACK, new Position(4, 4), board);
    Bishop bishop2 = new Bishop(Color.WHITE, new Position(4, 4), board);
    assertThat(bishop1.hashCode()).isNotEqualTo(bishop2.hashCode());
  }

  @Test
  void equals_DifferentBishops() {
    Position position1 = new Position(4, 4);
    Position position2 = new Position(5, 5);
    Bishop bishop1 = new Bishop(Color.WHITE, position1, board);
    Bishop bishop2 = new Bishop(Color.BLACK, position2, board);
    assertThat(bishop1.equals(bishop2)).isFalse();
  }
}
