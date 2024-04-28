package hwr.oop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.pieces.Piece;
import hwr.oop.pieces.PieceType;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PiecesTest {

  private ChessBoard board;
  @BeforeEach
  void setup() {
    board = new ChessBoard();
  }
  //KING
  @Test
  void testKingConstructor() {
    Position position = new Position(0, 0);
    Piece whiteKing = new Piece(PieceType.KING, Color.WHITE, position, board);
    Piece blackKing = new Piece(PieceType.KING, Color.BLACK, position, board);
    assertThat(whiteKing.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteKing.getPosition()).isEqualTo(position);
    assertThat(whiteKing.getSymbol()).isEqualTo('K');

    assertThat(blackKing.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackKing.getPosition()).isEqualTo(position);
    assertThat(blackKing.getSymbol()).isEqualTo('k');
  }
  @Test
  void testKingMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(3, 2);
    Piece king = new Piece(PieceType.KING, Color.WHITE, position, board);
    board.setPieceAtPosition(king.getPosition(), king);

    king.moveTo(targetPosition);
    assertThat(king.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testKingMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 2);
    Piece king = new Piece(PieceType.KING, Color.WHITE, position, board);
    board.setPieceAtPosition(king.getPosition(), king);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> king.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(king.getPosition()).isEqualTo(position);
  }
  //BISHOP
  @Test
  void testBishopConstructor() {
    Position position = new Position(0, 0);
    Piece whiteBishop = new Piece(PieceType.BISHOP, Color.WHITE, position, board);
    Piece blackBishop = new Piece(PieceType.BISHOP, Color.BLACK, position, board);
    assertThat(whiteBishop.getType()).isEqualTo(PieceType.BISHOP);
    assertThat(whiteBishop.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteBishop.getPosition()).isEqualTo(position);
    assertThat(whiteBishop.getSymbol()).isEqualTo('B');

    assertThat(blackBishop.getType()).isEqualTo(PieceType.BISHOP);
    assertThat(blackBishop.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackBishop.getPosition()).isEqualTo(position);
    assertThat(blackBishop.getSymbol()).isEqualTo('b');
  }
  @Test
  void testBishopMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(5, 5);
    Piece bishop = new Piece(PieceType.BISHOP, Color.WHITE, position, board);
    board.setPieceAtPosition(bishop.getPosition(), bishop);

    bishop.moveTo(targetPosition);
    assertThat(bishop.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testBishopMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 1);
    Piece bishop = new Piece(PieceType.BISHOP, Color.WHITE, position, board);
    board.setPieceAtPosition(bishop.getPosition(), bishop);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> bishop.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(bishop.getPosition()).isEqualTo(position);
  }
  //KNIGHT
  @Test
  void testKnightConstructor() {
    Position position = new Position(0, 0);
    Piece whiteKnight = new Piece(PieceType.KNIGHT, Color.WHITE, position, board);
    Piece blackKnight = new Piece(PieceType.KNIGHT, Color.BLACK, position, board);
    assertThat(whiteKnight.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteKnight.getPosition()).isEqualTo(position);
    assertThat(whiteKnight.getSymbol()).isEqualTo('N');

    assertThat(blackKnight.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackKnight.getPosition()).isEqualTo(position);
    assertThat(blackKnight.getSymbol()).isEqualTo('n');
  }
  @Test
  void testKnightMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(3, 4);
    Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE, position, board);
    board.setPieceAtPosition(knight.getPosition(), knight);

    knight.moveTo(targetPosition);
    assertThat(knight.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testKnightMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 4);
    Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE, position, board);
    board.setPieceAtPosition(knight.getPosition(), knight);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> knight.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(knight.getPosition()).isEqualTo(position);
  }
  //PAWN
  @Test
  void testPawnConstructor() {
    Position position = new Position(0, 0);
    Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK, position, board);
    assertThat(whitePawn.getColor()).isEqualTo(Color.WHITE);
    assertThat(whitePawn.getPosition()).isEqualTo(position);
    assertThat(whitePawn.getSymbol()).isEqualTo('P');

    assertThat(blackPawn.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackPawn.getPosition()).isEqualTo(position);
    assertThat(blackPawn.getSymbol()).isEqualTo('p');
  }
  @Test
  void testWhitePawnMove_successful() throws IllegalMoveException {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(2, 2);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testWhitePawnMoveDoubleAdvance_successful() throws IllegalMoveException {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(3, 2);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testWhitePawnCapture_successful() throws IllegalMoveException {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(2, 3);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    Piece knight = new Piece(PieceType.KNIGHT, Color.BLACK, targetPosition, board);
    board.setPieceAtPosition(knight.getPosition(), knight);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testBlackPawnMove_successful() throws IllegalMoveException {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(5, 2);
    Piece pawn = new Piece(PieceType.PAWN, Color.BLACK, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testBlackPawnMoveDoubleAdvance_successful() throws IllegalMoveException {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(4, 2);
    Piece pawn = new Piece(PieceType.PAWN, Color.BLACK, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testBlackPawnCapture_successful() throws IllegalMoveException {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(5, 3);
    Piece pawn = new Piece(PieceType.PAWN, Color.BLACK, position, board);
    Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE, targetPosition, board);
    board.setPieceAtPosition(knight.getPosition(), knight);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    pawn.moveTo(targetPosition);
    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testWhitePawnMove_fail() {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(0, 2);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }
  @Test
  void testWhitePawnCapture_fail() {
    Position position = new Position(1, 2);
    Position targetPosition = new Position(2, 3);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }
  @Test
  void testBlackPawnCapture_fail() {
    Position position = new Position(6, 2);
    Position targetPosition = new Position(5, 3);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position, board);
    board.setPieceAtPosition(pawn.getPosition(), pawn);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }
  //QUEEN
  @Test
  void testQueenConstructor() {
    Position position = new Position(0, 0);
    Piece whiteQueen = new Piece(PieceType.QUEEN, Color.WHITE, position, board);
    Piece blackQueen = new Piece(PieceType.QUEEN, Color.BLACK, position, board);
    assertThat(whiteQueen.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteQueen.getPosition()).isEqualTo(position);
    assertThat(whiteQueen.getSymbol()).isEqualTo('Q');

    assertThat(blackQueen.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackQueen.getPosition()).isEqualTo(position);
    assertThat(blackQueen.getSymbol()).isEqualTo('q');
  }
  @Test
  void testQueenMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(5, 5);
    Piece queen = new Piece(PieceType.QUEEN, Color.WHITE, position, board);
    board.setPieceAtPosition(queen.getPosition(), queen);

    queen.moveTo(targetPosition);
    assertThat(queen.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testQueenMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 1);
    Piece queen = new Piece(PieceType.QUEEN, Color.WHITE, position, board);
    board.setPieceAtPosition(queen.getPosition(), queen);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> queen.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(queen.getPosition()).isEqualTo(position);
  }
  //ROOK
  @Test
  void testRookConstructor() {
    Position position = new Position(0, 0);
    Piece whiteRook = new Piece(PieceType.ROOK, Color.WHITE, position, board);
    Piece blackRook = new Piece(PieceType.ROOK, Color.BLACK, position, board);
    assertThat(whiteRook.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteRook.getPosition()).isEqualTo(position);
    assertThat(whiteRook.getSymbol()).isEqualTo('R');

    assertThat(blackRook.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackRook.getPosition()).isEqualTo(position);
    assertThat(blackRook.getSymbol()).isEqualTo('r');
  }
  @Test
  void testRookMove_successful() throws IllegalMoveException {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(5, 2);
    Piece rook = new Piece(PieceType.ROOK, Color.WHITE, position, board);
    board.setPieceAtPosition(rook.getPosition(), rook);

    rook.moveTo(targetPosition);
    assertThat(rook.getPosition()).isEqualTo(targetPosition);
  }
  @Test
  void testRookMove_fail() {
    Position position = new Position(2, 2);
    Position targetPosition = new Position(1, 1);
    Piece rook = new Piece(PieceType.ROOK, Color.WHITE, position, board);
    board.setPieceAtPosition(rook.getPosition(), rook);

    IllegalMoveException exception = assertThrows(IllegalMoveException.class, () -> rook.moveTo(targetPosition));
    String expectedMessage = "Illegal move";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(rook.getPosition()).isEqualTo(position);
  }

  //equals?
  @Test
  @SuppressWarnings({"EqualsWithItself", "ConstantConditions", "EqualsBetweenInconvertibleTypes"})
  void testPieceEquals() {
    Position position = new Position(3, 3);
    Piece rook1 = new Piece(PieceType.ROOK, Color.WHITE, position, board);
    Piece rook2 = new Piece(PieceType.ROOK, Color.WHITE, position, board);
    Piece queen = new Piece(PieceType.QUEEN, Color.WHITE, position, board);
    ChessBoard chessBoard = new ChessBoard();

    assertThat(rook1.equals(rook2)).isTrue();
    assertThat(rook1.equals(rook1)).isTrue();
    assertThat(rook1.equals(null)).isFalse();
    assertThat(rook1.equals(queen)).isFalse();
    assertThat(rook1.equals(chessBoard)).isFalse();
}
  @Test
  void testPieceHash() {
    Position position = new Position(3, 3);
    Piece rook1 = new Piece(PieceType.ROOK, Color.WHITE, position, board);
    Piece rook2 = new Piece(PieceType.ROOK, Color.WHITE, position, board);

    assertThat(rook1.hashCode()).isEqualTo(rook2.hashCode());
    assertThat(rook1.hashCode()).isEqualTo(Objects.hash(PieceType.ROOK, Color.WHITE, position, rook1.getSymbol()));
  }
  @Test
  void testPieceSetPosition() {
    Position oldposition = new Position(0, 0);
    Position newPosition = new Position(5, 0);
    Piece rook = new Piece(PieceType.ROOK, Color.WHITE, oldposition, board);
    rook.setPosition(newPosition);
    assertThat(board.getPieceAtPosition(oldposition)).isNull();
    assertThat(rook.getPosition()).isEqualTo(newPosition);
    assertThat(board.getPieceAtPosition(newPosition)).isEqualTo(rook);
  }
}