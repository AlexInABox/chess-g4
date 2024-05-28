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

class KnightTest {

  private ChessBoard board;

  @BeforeEach
  void setup() {
    board = new ChessBoard();
  }

  @Test
  void testWhiteKnightConstructor() {
    Position position = new Position(0, 0);
    Piece whiteKnight = new Knight(Color.WHITE, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(whiteKnight.getColor()).isEqualTo(Color.WHITE);
          softly.assertThat(whiteKnight.getPosition()).isEqualTo(position);
          softly.assertThat(whiteKnight.getSymbol()).isEqualTo('N');
        });
  }

  @Test
  void testBlackKnightConstructor() {
    Position position = new Position(0, 0);
    Piece blackKnight = new Knight(Color.BLACK, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(blackKnight.getColor()).isEqualTo(Color.BLACK);
          softly.assertThat(blackKnight.getPosition()).isEqualTo(position);
          softly.assertThat(blackKnight.getSymbol()).isEqualTo('n');
        });
  }

  @Test
  void testKnightMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(3, 4);
    Piece knight = new Knight(Color.WHITE, position, board);
    board.setPieceAtPosition(knight.getPosition(), knight);

    knight.moveTo(targetPosition);
    assertThat(knight.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testKnightMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 4);
    Piece knight = new Knight(Color.WHITE, position, board);
    board.setPieceAtPosition(knight.getPosition(), knight);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> knight.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(knight.getPosition()).isEqualTo(position);
  }

  @Test
  void equals_IdenticalPieces() {
    Piece piece1 = new Knight(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Knight(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isTrue();
  }

  @Test
  @SuppressWarnings("EqualsWithItself")
  void equals_sameInstance() {
    Piece piece = new Knight(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(piece)).isTrue();
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void equals_InstanceNull() {
    Piece piece = new Knight(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(null)).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentClass() {
    Piece piece = new Knight(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals("String")).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentPieces() {
    Piece piece1 = new Knight(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isFalse();
  }

  @Test
  void hashCode_IdenticalPieces() {
    Piece piece1 = new Knight(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Knight(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isEqualTo(piece2.hashCode());
  }

  @Test
  void hashCode_DifferentPieces() {
    Piece piece1 = new Knight(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isNotEqualTo(piece2.hashCode());
  }

  @Test
  void testPieceSetPosition() {
    Position oldPosition = new Position(0, 0);
    Position newPosition = new Position(5, 0);
    Piece knight = new Knight(Color.WHITE, oldPosition, board);
    knight.setPosition(newPosition);
    assertThat(board.getPieceAtPosition(oldPosition)).isNull();
    assertThat(knight.getPosition()).isEqualTo(newPosition);
    assertThat(board.getPieceAtPosition(newPosition)).isEqualTo(knight);
  }

  @Test
  void testToString() {
    Position position = new Position(0, 0);
    Piece piece = new Knight(Color.WHITE, position, board);
    String expectedString = "Knight{color=WHITE, position=Position[row=0, column=0], symbol=N}";
    assertThat(piece.toString()).isEqualTo(expectedString);
  }

  @Test
  void testToStringWithDifferentPieceType() {
    ChessBoard chessBoard = new ChessBoard();
    Piece piece = new Pawn(Color.BLACK, new Position(6, 1), chessBoard);
    String expectedString = "Knight{color=BLACK, position=Position[row=6, column=1], symbol=p}";
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
    Knight knight = new Knight(Color.WHITE, position, board);
    assertThat(knight.getType()).isEqualTo(PieceType.KNIGHT);
  }

  @Test
  void hashCode_IdenticalHashCode() {
    ChessBoard board2 = new ChessBoard();
    Knight knight1 = new Knight(Color.WHITE, new Position(4, 4), board);
    Knight knight2 = new Knight(Color.WHITE, new Position(4, 4), board2);
    assertThat(knight1.hashCode()).isEqualTo(knight2.hashCode());
  }

  @Test
  void hashCode_DifferentHashCode() {
    Knight knight1 = new Knight(Color.BLACK, new Position(4, 4), board);
    Knight knight2 = new Knight(Color.WHITE, new Position(4, 4), board);
    assertThat(knight1.hashCode()).isNotEqualTo(knight2.hashCode());
  }

  @Test
  void equals_DifferentKnights() {
    Position position1 = new Position(4, 4);
    Position position2 = new Position(5, 5);
    Knight knight1 = new Knight(Color.WHITE, position1, board);
    Knight knight2 = new Knight(Color.BLACK, position2, board);
    assertThat(knight1.equals(knight2)).isFalse();
  }

  @Test
  void testKnightPossibleMovesMutationInList_successful() {
    board.clearChessboard();
    Position kingPosition = new Position(7, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position knightPosition = new Position(4, 4);

    Piece knight = new Knight(Color.WHITE, knightPosition, board);
    board.setPieceAtPosition(knight.getPosition(), knight);

    List<Position> possibleMoves = knight.possibleMoves();

    List<Position> expectedMoves =
        Arrays.asList(
            new Position(6, 5),
            new Position(6, 3),
            new Position(2, 5),
            new Position(2, 3),
            new Position(5, 6),
            new Position(5, 2),
            new Position(3, 6),
            new Position(3, 2));

    assertEquals(expectedMoves, possibleMoves);
  }

  @Test
  void testKnightBlockCheck_successful() throws IllegalMoveException {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position knightPosition = new Position(1, 3);
    Position knightTarget = new Position(0, 1);
    Position enemyRookPosition = new Position(0, 2);

    Piece knight = new Knight(Color.WHITE, knightPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(knight.getPosition(), knight);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    knight.moveTo(knightTarget);
    assertThat(knight.getPosition()).isEqualTo(knightTarget);
  }

  @Test
  void testKnightBlockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position knightPosition = new Position(1, 3);
    Position knightTarget = new Position(2, 1);
    Position enemyRookPosition = new Position(0, 2);

    Piece knight = new Knight(Color.WHITE, knightPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(knight.getPosition(), knight);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
            assertThrows(IllegalMoveException.class, () -> knight.moveTo(knightTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(knight.getPosition()).isEqualTo(knightPosition);
  }

  @Test
  void testKnightUnblockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position knightPosition = new Position(0, 1);
    Position knightTarget = new Position(1, 3);
    Position enemyRookPosition = new Position(0, 2);

    Piece knight = new Knight(Color.WHITE, knightPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(knight.getPosition(), knight);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
            assertThrows(IllegalMoveException.class, () -> knight.moveTo(knightTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(knight.getPosition()).isEqualTo(knightPosition);
  }
}
