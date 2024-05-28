package hwr.oop.pieces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.board.ChessBoard;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
    assertThat(board.getPieceAtPosition(new Position(3, 3))).isNull();
  }

  @Test
  void testBishopTake_successful() throws IllegalMoveException {
    Position bishopPosition = new Position(2, 2);
    Position bishopTargetPosition = new Position(6, 6);
    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);
    board.setPieceAtPosition(bishop.getPosition(), bishop);

    bishop.moveTo(bishopTargetPosition);
    assertThat(bishop.getPosition()).isEqualTo(bishopTargetPosition);
    assertThat(board.getPieceAtPosition(new Position(3, 3))).isNull();
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
    assertThat(board.getPieceAtPosition(bishop.getPosition())).isEqualTo(bishop);
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

  @Test
  void testBishopPossibleMovesMutationInList_successful() {
    board.clearChessboard();
    Position kingPosition = new Position(7, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position bishopPosition = new Position(4, 4);

    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);
    board.setPieceAtPosition(bishop.getPosition(), bishop);

    List<Position> possibleMoves = bishop.possibleMoves();

    List<Position> expectedMoves =
        Arrays.asList(
            new Position(5, 5),
            new Position(6, 6),
            new Position(7, 7),
            new Position(5, 3),
            new Position(6, 2),
            new Position(7, 1),
            new Position(3, 5),
            new Position(2, 6),
            new Position(1, 7),
            new Position(3, 3),
            new Position(2, 2),
            new Position(1, 1),
            new Position(0, 0));

    assertEquals(expectedMoves, possibleMoves);
  }

  @Test
  void testBishopBlockCheck_successful() throws IllegalMoveException {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position bishopPosition = new Position(1, 0);
    Position bishopTarget = new Position(0, 1);
    Position enemyRookPosition = new Position(0, 2);

    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(bishop.getPosition(), bishop);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    bishop.moveTo(bishopTarget);
    assertThat(bishop.getPosition()).isEqualTo(bishopTarget);
  }

  @Test
  void testBishopBlockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position bishopPosition = new Position(1, 0);
    Position bishopTarget = new Position(2, 1);
    Position enemyRookPosition = new Position(0, 2);

    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(bishop.getPosition(), bishop);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
            assertThrows(IllegalMoveException.class, () -> bishop.moveTo(bishopTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(bishop.getPosition()).isEqualTo(bishopPosition);
  }

  @Test
  void testBishopUnblockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position bishopPosition = new Position(0, 1);
    Position bishopTarget = new Position(1, 0);
    Position enemyRookPosition = new Position(0, 2);

    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(bishop.getPosition(), bishop);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
            assertThrows(IllegalMoveException.class, () -> bishop.moveTo(bishopTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(bishop.getPosition()).isEqualTo(bishopPosition);
  }

  @Test
  void testBishopTakeFriendlyKing_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position bishopPosition = new Position(1, 1);
    Position bishopTarget = new Position(0, 0);

    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);

    board.setPieceAtPosition(bishop.getPosition(), bishop);

    IllegalMoveException exception =
            assertThrows(IllegalMoveException.class, () -> bishop.moveTo(bishopTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(bishop.getPosition()).isEqualTo(bishopPosition);
  }

  @Test
  void testBishopTakeEnemyKing_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(7, 7);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position enemyKingPosition = new Position(0, 0);
    Piece enemyKing = new King(Color.BLACK, enemyKingPosition, board);
    board.setPieceAtPosition(enemyKing.getPosition(), enemyKing);

    Position bishopPosition = new Position(1, 1);
    Position bishopTarget = new Position(0, 0);

    Piece bishop = new Bishop(Color.WHITE, bishopPosition, board);

    board.setPieceAtPosition(bishop.getPosition(), bishop);

    IllegalMoveException exception =
            assertThrows(IllegalMoveException.class, () -> bishop.moveTo(bishopTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(bishop.getPosition()).isEqualTo(bishopPosition);
  }
}
