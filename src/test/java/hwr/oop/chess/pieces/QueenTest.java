package hwr.oop.chess.pieces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class QueenTest {

  private ChessBoard board;

  @BeforeEach
  void setup() {
    board = new ChessBoard();
  }

  @Test
  void testWhiteQueenConstructor() {
    Position position = new Position(0, 0);
    Piece whiteQueen = new Queen(Color.WHITE, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(whiteQueen.getColor()).isEqualTo(Color.WHITE);
          softly.assertThat(whiteQueen.getPosition()).isEqualTo(position);
          softly.assertThat(whiteQueen.getSymbol()).isEqualTo('Q');
        });
  }

  @Test
  void testBlackQueenConstructor() {
    Position position = new Position(0, 0);
    Piece blackQueen = new Queen(Color.BLACK, position, board);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(blackQueen.getColor()).isEqualTo(Color.BLACK);
          softly.assertThat(blackQueen.getPosition()).isEqualTo(position);
          softly.assertThat(blackQueen.getSymbol()).isEqualTo('q');
        });
  }

  @Test
  void testQueenMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(5, 5);
    Piece queen = new Queen(Color.WHITE, position, board);
    board.setPieceAtPosition(queen.getPosition(), queen);

    queen.moveTo(targetPosition);
    assertThat(queen.getPosition()).isEqualTo(targetPosition);
  }

  @Test
  void testQueenCapture_successful() throws IllegalMoveException {
    board.clearChessboard();
    Position kingPosition = new Position(2, 2);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(0, 0);
    Position queenTargetPosition = new Position(1, 0);

    Position enemyPawnPosition = new Position(1, 0);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    Piece firstPawn = new Pawn(Color.BLACK, enemyPawnPosition, board);
    board.setPieceAtPosition(queen.getPosition(), queen);
    board.setPieceAtPosition(firstPawn.getPosition(), firstPawn);

    queen.moveTo(queenTargetPosition);
    assertThat(queen.getPosition()).isEqualTo(queenTargetPosition);
  }

  @Test
  void testQueenMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(4, 5);
    Piece queen = new Queen(Color.WHITE, position, board);
    board.setPieceAtPosition(queen.getPosition(), queen);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> queen.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(queen.getPosition()).isEqualTo(position);
  }

  @Test
  void testQueenMoveRestricted_successful() throws IllegalMoveException {
    board.clearChessboard();
    Position kingPosition = new Position(2, 2);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(0, 0);
    Position queenTargetPosition = new Position(0, 5);

    Position firstPawnBlockPosition = new Position(1, 0);
    Position secondPawnBlockPosition = new Position(1, 1);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    Piece firstPawn = new Pawn(Color.WHITE, firstPawnBlockPosition, board);
    Piece secondPawn = new Pawn(Color.WHITE, secondPawnBlockPosition, board);
    board.setPieceAtPosition(queen.getPosition(), queen);
    board.setPieceAtPosition(firstPawn.getPosition(), firstPawn);
    board.setPieceAtPosition(secondPawn.getPosition(), secondPawn);

    queen.moveTo(queenTargetPosition);
    assertThat(queen.getPosition()).isEqualTo(queenTargetPosition);
  }

  @Test
  void testQueenMoveRestricted_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(2, 2);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(0, 0);
    Position queenTargetPosition = new Position(5, 0);

    Position firstPawnBlockPosition = new Position(1, 0);
    Position secondPawnBlockPosition = new Position(1, 1);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    Piece firstPawn = new Pawn(Color.WHITE, firstPawnBlockPosition, board);
    Piece secondPawn = new Pawn(Color.WHITE, secondPawnBlockPosition, board);
    board.setPieceAtPosition(queen.getPosition(), queen);
    board.setPieceAtPosition(firstPawn.getPosition(), firstPawn);
    board.setPieceAtPosition(secondPawn.getPosition(), secondPawn);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> queen.moveTo(queenTargetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(queen.getPosition()).isEqualTo(queenPosition);
  }

  @Test
  void equals_IdenticalPieces() {
    Piece piece1 = new Queen(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Queen(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isTrue();
  }

  @Test
  @SuppressWarnings("EqualsWithItself")
  void equals_sameInstance() {
    Piece piece = new Queen(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(piece)).isTrue();
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void equals_InstanceNull() {
    Piece piece = new Queen(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals(null)).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentClass() {
    Piece piece = new Queen(Color.WHITE, new Position(0, 0), board);
    assertThat(piece.equals("String")).isFalse();
  }

  @Test
  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  void equals_DifferentPieces() {
    Piece piece1 = new Queen(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.equals(piece2)).isFalse();
  }

  @Test
  void hashCode_IdenticalPieces() {
    Piece piece1 = new Queen(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new Queen(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isEqualTo(piece2.hashCode());
  }

  @Test
  void hashCode_DifferentPieces() {
    Piece piece1 = new Queen(Color.WHITE, new Position(0, 0), board);
    Piece piece2 = new King(Color.WHITE, new Position(0, 0), board);
    assertThat(piece1.hashCode()).isNotEqualTo(piece2.hashCode());
  }

  @Test
  void testPieceSetPosition() {
    Position oldPosition = new Position(0, 0);
    Position newPosition = new Position(5, 0);
    Piece queen = new Queen(Color.WHITE, oldPosition, board);
    queen.setPosition(newPosition);
    assertThat(board.getPieceAtPosition(oldPosition)).isNull();
    assertThat(queen.getPosition()).isEqualTo(newPosition);
    assertThat(board.getPieceAtPosition(newPosition)).isEqualTo(queen);
  }

  @Test
  void testToString() {
    Position position = new Position(0, 0);
    Piece piece = new Queen(Color.WHITE, position, board);
    String expectedString = "Queen{color=WHITE, position=Position[row=0, column=0], symbol=Q}";
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
    Queen queen = new Queen(Color.WHITE, position, board);
    assertThat(queen.getType()).isEqualTo(PieceType.QUEEN);
  }

  @Test
  void hashCode_IdenticalHashCode() {
    ChessBoard board2 = new ChessBoard();
    Queen queen1 = new Queen(Color.WHITE, new Position(4, 4), board);
    Queen queen2 = new Queen(Color.WHITE, new Position(4, 4), board2);
    assertThat(queen1.hashCode()).isEqualTo(queen2.hashCode());
  }

  @Test
  void hashCode_DifferentHashCode() {
    Queen queen1 = new Queen(Color.BLACK, new Position(4, 4), board);
    Queen queen2 = new Queen(Color.WHITE, new Position(4, 4), board);
    assertThat(queen1.hashCode()).isNotEqualTo(queen2.hashCode());
  }

  @Test
  void equals_DifferentQueens() {
    Position position1 = new Position(4, 4);
    Position position2 = new Position(5, 5);
    Queen queen1 = new Queen(Color.WHITE, position1, board);
    Queen queen2 = new Queen(Color.BLACK, position2, board);
    assertThat(queen1.equals(queen2)).isFalse();
  }

  @Test
  void testQueenPossibleMovesMutationInList_successful() {
    board.clearChessboard();
    Position kingPosition = new Position(7, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(4, 4);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    board.setPieceAtPosition(queen.getPosition(), queen);

    List<Position> possibleMoves = queen.possibleMoves();

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
            new Position(0, 0),
            new Position(5, 4),
            new Position(6, 4),
            new Position(7, 4),
            new Position(3, 4),
            new Position(2, 4),
            new Position(1, 4),
            new Position(0, 4),
            new Position(4, 5),
            new Position(4, 6),
            new Position(4, 7),
            new Position(4, 3),
            new Position(4, 2),
            new Position(4, 1),
            new Position(4, 0));

    assertEquals(expectedMoves, possibleMoves);
  }

  @Test
  void testQueenBlockCheck_successful() throws IllegalMoveException {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(1, 1);
    Position queenTarget = new Position(1, 0);
    Position enemyRookPosition = new Position(2, 0);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(queen.getPosition(), queen);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    queen.moveTo(queenTarget);
    assertThat(queen.getPosition()).isEqualTo(queenTarget);
  }

  @Test
  void testQueenBlockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(1, 1);
    Position queenTarget = new Position(1, 3);
    Position enemyRookPosition = new Position(2, 0);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(queen.getPosition(), queen);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> queen.moveTo(queenTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(queen.getPosition()).isEqualTo(queenPosition);
  }

  @Test
  void testQueenUnblockCheck_fail() {
    board.clearChessboard();
    Position kingPosition = new Position(0, 0);
    Piece king = new King(Color.WHITE, kingPosition, board);
    board.setPieceAtPosition(king.getPosition(), king);

    Position queenPosition = new Position(1, 0);
    Position queenTarget = new Position(1, 1);
    Position enemyRookPosition = new Position(2, 0);

    Piece queen = new Queen(Color.WHITE, queenPosition, board);
    Piece enemyRook = new Rook(Color.BLACK, enemyRookPosition, board);

    board.setPieceAtPosition(queen.getPosition(), queen);
    board.setPieceAtPosition(enemyRook.getPosition(), enemyRook);

    IllegalMoveException exception =
        assertThrows(IllegalMoveException.class, () -> queen.moveTo(queenTarget));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(queen.getPosition()).isEqualTo(queenPosition);
  }
}
