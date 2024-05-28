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

class PawnTest {

  private ChessBoard board;

  @BeforeEach
  void setup() {
    board = new ChessBoard();
  }

  @Test
  void testWhitePawnConstructor() {
    Position position = new Position(0, 0);
    Piece whitePawn = new Pawn(Color.WHITE, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(whitePawn.getColor()).isEqualTo(Color.WHITE);
          softly.assertThat(whitePawn.getPosition()).isEqualTo(position);
          softly.assertThat(whitePawn.getSymbol()).isEqualTo('P');
        });
  }

  @Test
  void testBlackPawnConstructor() {
    Position position = new Position(0, 0);
    Piece blackPawn = new Pawn(Color.BLACK, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(blackPawn.getColor()).isEqualTo(Color.BLACK);
          softly.assertThat(blackPawn.getPosition()).isEqualTo(position);
          softly.assertThat(blackPawn.getSymbol()).isEqualTo('p');
        });
  }

  @Test
  void testWhitePawnMove_successful() throws IllegalMoveException {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(2, 2);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testWhitePawnMoveDoubleAdvance_successful() throws IllegalMoveException {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(3, 2);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testWhitePawnCapture_successful() throws IllegalMoveException {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(2, 3);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    Piece knight = new Knight(Color.BLACK, targetPosition, board);
    board.setPieceAtPosition(knight.getPosition(), knight);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testPawnMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(3, 3);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }

  @Test
  void testBlackPawnMove_successful() throws IllegalMoveException {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(5, 2);
    Piece pawn = new Pawn(Color.BLACK, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testBlackPawnMoveDoubleAdvance_successful() throws IllegalMoveException {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(4, 2);
    Piece pawn = new Pawn(Color.BLACK, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testBlackPawnCapture_successful() throws IllegalMoveException {
    Position position = new Position(4, 3);
    Position targetPosition = new Position(3, 2);
    Piece pawn = new Pawn(Color.BLACK, position, board);
    Piece enemyPiece = new Knight(Color.WHITE, targetPosition, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);
    board.setPieceAtPosition(enemyPiece.getPosition(), enemyPiece);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testWhitePawnMove_fail() {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(0, 2);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }

  @Test
  void testWhitePawnCapture_fail() {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(2, 3);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }

  @Test
  void testBlackPawnCapture_fail() {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(5, 3);
    Piece pawn = new Pawn(Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }

  @Test
  void equals_IdenticalPieces() {
    Piece piece1 = new Pawn(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Pawn(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isTrue();
  }

  @Test
  @SuppressWarnings("EqualsWithItself")
  void equals_sameInstance() {
    Piece piece = new Pawn(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(piece)).isTrue();
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void equals_InstanceNull() {
    Piece piece = new Pawn(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(null)).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentClass() {
    Piece piece = new Pawn(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals("String")).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentPieces() {
    Piece piece1 = new Pawn(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isFalse();
  }

  @Test
  void hashCode_IdenticalPieces() {
    Piece piece1 = new Pawn(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Pawn(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isEqualTo(piece2.hashCode());
  }

  @Test
  void hashCode_DifferentPieces() {
    Piece piece1 = new Pawn(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isNotEqualTo(piece2.hashCode());
  }

  @Test
  void testPieceSetPosition() {
    Position oldPosition = new Position(0, 0);
    Position newPosition = new Position(5, 0);
    Piece pawn = new Pawn(Color.WHITE, oldPosition, board);
    pawn.setPosition(newPosition);
    assertThat(board.getPieceAtPosition(oldPosition)).isNull();
    assertThat(pawn.getPosition()).isEqualTo(newPosition);
    assertThat(board.getPieceAtPosition(newPosition)).isEqualTo(pawn);
  }

  @Test
  void testToString() {
    Position position = new Position(0, 0);
    Piece piece = new Pawn(Color.WHITE, position, board);
    String expectedString = "Pawn{color=WHITE, position=Position[row=0, column=0], symbol=P}";
    assertThat(piece.toString()).isEqualTo(expectedString);
  }

  @Test
  void testToStringWithDifferentPieceType() {
    ChessBoard chessBoard = new ChessBoard();
    Piece piece = new Pawn(Color.BLACK, new Position(6, 1), chessBoard);
    String expectedString = "Queen{color=BLACK, position=Position[row=6, column=1], symbol=p}";
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
    Pawn pawn = new Pawn(Color.WHITE, position, board);
    assertThat(pawn.getType()).isEqualTo(PieceType.PAWN);
  }

  @Test
  void hashCode_IdenticalHashCode() {
    ChessBoard board2 = new ChessBoard();
    Pawn pawn1 = new Pawn(Color.WHITE, new Position(4, 4), board);
    Pawn pawn2 = new Pawn(Color.WHITE, new Position(4, 4), board2);
    assertThat(pawn1.hashCode()).isEqualTo(pawn2.hashCode());
  }

  @Test
  void hashCode_DifferentHashCode() {
    Pawn pawn1 = new Pawn(Color.BLACK, new Position(4, 4), board);
    Pawn pawn2 = new Pawn(Color.WHITE, new Position(4, 4), board);
    assertThat(pawn1.hashCode()).isNotEqualTo(pawn2.hashCode());
  }

  @Test
  void equals_DifferentPawns() {
    Position position1 = new Position(4, 4);
    Position position2 = new Position(5, 5);
    Pawn pawn1 = new Pawn(Color.WHITE, position1, board);
    Pawn pawn2 = new Pawn(Color.BLACK, position2, board);
    assertThat(pawn1.equals(pawn2)).isFalse();
  }

  @Test
  void testPawnPossibleMovesMutationInList_successful() {
    board.clearChessboard();
    Position kingPosition = new Position(7, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position friendlyPawnPosition = new Position(1, 1);
    Position leftCapturePosition = new Position(2, 0);
    Position rightCapturePosition = new Position(2, 2);

    Piece friendlyPawn = new Pawn(Color.WHITE, friendlyPawnPosition, board);
    Piece leftCapture = new Pawn(Color.BLACK, leftCapturePosition, board);
    Piece rightCapture = new Pawn(Color.BLACK, rightCapturePosition, board);

    board.setPieceAtPosition(friendlyPawn.getPosition(), friendlyPawn);
    board.setPieceAtPosition(leftCapture.getPosition(), leftCapture);
    board.setPieceAtPosition(rightCapture.getPosition(), rightCapture);

    List<Position> possibleMoves = friendlyPawn.possibleMoves();

    List<Position> expectedMoves =
        Arrays.asList(
            new Position(2, 1), new Position(3, 1), new Position(2, 2), new Position(2, 0));

    assertEquals(expectedMoves, possibleMoves);
  }

  @Test
  void testPawnBlockCheck_successful() throws IllegalMoveException {
    board.clearChessboard();
    Position kingPosition = new Position(2, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position pawnPosition = new Position(1, 1);
    Position pawnTarget = new Position(2, 1);
    Position enemyRookPosition = new Position(2, 2);

    Piece pawn = new Pawn(Color.WHITE, pawnPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(pawn.getPosition(), pawn);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    pawn.moveTo(pawnTarget);
    assertThat(pawn.getPosition()).isEqualTo(pawnTarget);
  }

  @Test
  void testPawnBlockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(2, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position pawnPosition = new Position(1, 1);
    Position pawnTarget = new Position(3, 1);
    Position enemyRookPosition = new Position(2, 2);

    Piece pawn = new Pawn(Color.WHITE, pawnPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(pawn.getPosition(), pawn);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(pawnTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(pawnPosition);
  }

  @Test
  void testPawnUnblockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(1, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position pawnPosition = new Position(1, 1);
    Position pawnTarget = new Position(2, 1);
    Position enemyRookPosition = new Position(1, 2);

    Piece pawn = new Pawn(Color.WHITE, pawnPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(pawn.getPosition(), pawn);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(pawnTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(pawnPosition);
  }
}
