//package hwr.oop;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import hwr.oop.board.ChessBoard;
//import hwr.oop.pieces.*;
//
//import org.assertj.core.api.SoftAssertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class PiecesTest {
//
//  private ChessBoard board;
//
//  @BeforeEach
//  void setup() {
//    board = new ChessBoard();
//  }

  // KING
//  @Test
//  void testWhiteKingConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface whiteKing = new King(Color.WHITE, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(whiteKing.getColor()).isEqualTo(Color.WHITE);
//          softly.assertThat(whiteKing.getPosition()).isEqualTo(position);
//          softly.assertThat(whiteKing.getSymbol()).isEqualTo('K');
//        });
//  }

//  @Test
//  void testBlackKingConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface blackKing = new King(Color.BLACK, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(blackKing.getColor()).isEqualTo(Color.BLACK);
//          softly.assertThat(blackKing.getPosition()).isEqualTo(position);
//          softly.assertThat(blackKing.getSymbol()).isEqualTo('k');
//        });
//  }

//  @Test
//  void testKingMove_successful() throws IllegalMoveException {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(3, 2);
//    PieceInterface king = new King(Color.WHITE, position, board);
//    board.setPieceAtPosition(king.getPosition(), king);
//
//    king.moveTo(targetPosition);
//    assertThat(king.getPosition()).isEqualTo(targetPosition);
//  }

//  @Test
//  void testKingMove_fail() {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(1, 2);
//    PieceInterface king = new King(Color.WHITE, position, board);
//    board.setPieceAtPosition(king.getPosition(), king);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> king.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(king.getPosition()).isEqualTo(position);
//  }

  // BISHOP
//  @Test
//  void testWhiteBishopConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface whiteBishop = new Bishop(Color.WHITE, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(whiteBishop.getType()).isEqualTo(PieceType.BISHOP);
//          softly.assertThat(whiteBishop.getColor()).isEqualTo(Color.WHITE);
//          softly.assertThat(whiteBishop.getPosition()).isEqualTo(position);
//          softly.assertThat(whiteBishop.getSymbol()).isEqualTo('B');
//        });
//  }
//
//  @Test
//  void testBlackBishopConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface blackBishop = new Bishop(Color.BLACK, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(blackBishop.getType()).isEqualTo(PieceType.BISHOP);
//          softly.assertThat(blackBishop.getColor()).isEqualTo(Color.BLACK);
//          softly.assertThat(blackBishop.getPosition()).isEqualTo(position);
//          softly.assertThat(blackBishop.getSymbol()).isEqualTo('b');
//        });
//  }
//
//  @Test
//  void testBishopMove_successful() throws IllegalMoveException {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(5, 5);
//    PieceInterface bishop = new Bishop(Color.WHITE, position, board);
//    board.setPieceAtPosition(bishop.getPosition(), bishop);
//
//    bishop.moveTo(targetPosition);
//    assertThat(bishop.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testBishopMove_fail() {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(1, 1);
//    PieceInterface bishop = new Bishop(Color.WHITE, position, board);
//    board.setPieceAtPosition(bishop.getPosition(), bishop);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> bishop.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(bishop.getPosition()).isEqualTo(position);
//  }

  // KNIGHT
//  @Test
//  void testWhiteKnightConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface whiteKnight = new Knight(Color.WHITE, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(whiteKnight.getColor()).isEqualTo(Color.WHITE);
//          softly.assertThat(whiteKnight.getPosition()).isEqualTo(position);
//          softly.assertThat(whiteKnight.getSymbol()).isEqualTo('N');
//        });
//  }
//
//  @Test
//  void testBlackKnightConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface blackKnight = new Knight(Color.BLACK, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(blackKnight.getColor()).isEqualTo(Color.BLACK);
//          softly.assertThat(blackKnight.getPosition()).isEqualTo(position);
//          softly.assertThat(blackKnight.getSymbol()).isEqualTo('n');
//        });
//  }
//
//  @Test
//  void testKnightMove_successful() throws IllegalMoveException {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(3, 4);
//    PieceInterface knight = new Knight(Color.WHITE, position, board);
//    board.setPieceAtPosition(knight.getPosition(), knight);
//
//    knight.moveTo(targetPosition);
//    assertThat(knight.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testKnightMove_fail() {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(1, 4);
//    PieceInterface knight = new Knight(Color.WHITE, position, board);
//    board.setPieceAtPosition(knight.getPosition(), knight);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> knight.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(knight.getPosition()).isEqualTo(position);
//  }

  // PAWN
//  @Test
//  void testWhitePawnConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface whitePawn = new Pawn(Color.WHITE, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(whitePawn.getColor()).isEqualTo(Color.WHITE);
//          softly.assertThat(whitePawn.getPosition()).isEqualTo(position);
//          softly.assertThat(whitePawn.getSymbol()).isEqualTo('P');
//        });
//  }
//
//  @Test
//  void testBlackPawnConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface blackPawn = new Pawn(Color.BLACK, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(blackPawn.getColor()).isEqualTo(Color.BLACK);
//          softly.assertThat(blackPawn.getPosition()).isEqualTo(position);
//          softly.assertThat(blackPawn.getSymbol()).isEqualTo('p');
//        });
//  }

//  @Test
//  void testWhitePawnMove_successful() throws IllegalMoveException {
//    Position position = new Position(1, 2);
//    Position targetPosition = new Position(2, 2);
//    PieceInterface pawn = new Pawn(Color.WHITE, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    pawn.moveTo(targetPosition);
//    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testWhitePawnMoveDoubleAdvance_successful() throws IllegalMoveException {
//    Position position = new Position(1, 2);
//    Position targetPosition = new Position(3, 2);
//    PieceInterface pawn = new Pawn(Color.WHITE, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    pawn.moveTo(targetPosition);
//    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testWhitePawnCapture_successful() throws IllegalMoveException {
//    Position position = new Position(1, 2);
//    Position targetPosition = new Position(2, 3);
//    PieceInterface pawn = new Pawn(Color.WHITE, position, board);
//    PieceInterface knight = new Knight(Color.BLACK, targetPosition, board);
//    board.setPieceAtPosition(knight.getPosition(), knight);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    pawn.moveTo(targetPosition);
//    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testBlackPawnMove_successful() throws IllegalMoveException {
//    Position position = new Position(6, 2);
//    Position targetPosition = new Position(5, 2);
//    PieceInterface pawn = new Pawn(Color.BLACK, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    pawn.moveTo(targetPosition);
//    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testBlackPawnMoveDoubleAdvance_successful() throws IllegalMoveException {
//    Position position = new Position(6, 2);
//    Position targetPosition = new Position(4, 2);
//    PieceInterface pawn = new Pawn(Color.BLACK, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    pawn.moveTo(targetPosition);
//    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testBlackPawnCapture_successful() throws IllegalMoveException {
//    Position position = new Position(4, 3);
//    Position targetPosition = new Position(3, 2);
//    PieceInterface pawn = new Pawn(Color.BLACK, position, board);
//    PieceInterface enemyPiece = new Knight(Color.WHITE, targetPosition, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//    board.setPieceAtPosition(enemyPiece.getPosition(), enemyPiece);
//
//    pawn.moveTo(targetPosition);
//    assertThat(pawn.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testWhitePawnMove_fail() {
//    Position position = new Position(1, 2);
//    Position targetPosition = new Position(0, 2);
//    PieceInterface pawn = new Pawn(Color.WHITE, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(pawn.getPosition()).isEqualTo(position);
//  }
//
//  @Test
//  void testWhitePawnCapture_fail() {
//    Position position = new Position(1, 2);
//    Position targetPosition = new Position(2, 3);
//    PieceInterface pawn = new Pawn(Color.WHITE, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(pawn.getPosition()).isEqualTo(position);
//  }
//
//  @Test
//  void testBlackPawnCapture_fail() {
//    Position position = new Position(6, 2);
//    Position targetPosition = new Position(5, 3);
//    PieceInterface pawn = new Pawn(Color.WHITE, position, board);
//    board.setPieceAtPosition(pawn.getPosition(), pawn);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> pawn.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(pawn.getPosition()).isEqualTo(position);
//  }

  // QUEEN
//  @Test
//  void testWhiteQueenConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface whiteQueen = new Queen(Color.WHITE, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(whiteQueen.getColor()).isEqualTo(Color.WHITE);
//          softly.assertThat(whiteQueen.getPosition()).isEqualTo(position);
//          softly.assertThat(whiteQueen.getSymbol()).isEqualTo('Q');
//        });
//  }
//
//  @Test
//  void testBlackQueenConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface blackQueen = new Queen(Color.BLACK, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(blackQueen.getColor()).isEqualTo(Color.BLACK);
//          softly.assertThat(blackQueen.getPosition()).isEqualTo(position);
//          softly.assertThat(blackQueen.getSymbol()).isEqualTo('q');
//        });
//  }
//
//  @Test
//  void testQueenMove_successful() throws IllegalMoveException {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(5, 5);
//    PieceInterface queen = new Queen(Color.WHITE, position, board);
//    board.setPieceAtPosition(queen.getPosition(), queen);
//
//    queen.moveTo(targetPosition);
//    assertThat(queen.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testQueenMove_fail() {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(1, 1);
//    PieceInterface queen = new Queen(Color.WHITE, position, board);
//    board.setPieceAtPosition(queen.getPosition(), queen);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> queen.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(queen.getPosition()).isEqualTo(position);
//  }

  // ROOK
//  @Test
//  void testWhiteRookConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface whiteRook = new Rook(Color.WHITE, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(whiteRook.getColor()).isEqualTo(Color.WHITE);
//          softly.assertThat(whiteRook.getPosition()).isEqualTo(position);
//          softly.assertThat(whiteRook.getSymbol()).isEqualTo('R');
//        });
//  }
//
//  @Test
//  void testBlackRookConstructor() {
//    Position position = new Position(0, 0);
//    PieceInterface blackRook = new Rook(Color.BLACK, position, board);
//    SoftAssertions.assertSoftly(
//        softly -> {
//          softly.assertThat(blackRook.getColor()).isEqualTo(Color.BLACK);
//          softly.assertThat(blackRook.getPosition()).isEqualTo(position);
//          softly.assertThat(blackRook.getSymbol()).isEqualTo('r');
//        });
//  }
//
//  @Test
//  void testRookMove_successful() throws IllegalMoveException {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(5, 2);
//    PieceInterface rook = new Rook(Color.WHITE, position, board);
//    board.setPieceAtPosition(rook.getPosition(), rook);
//
//    rook.moveTo(targetPosition);
//    assertThat(rook.getPosition()).isEqualTo(targetPosition);
//  }
//
//  @Test
//  void testRookMove_fail() {
//    Position position = new Position(2, 2);
//    Position targetPosition = new Position(1, 1);
//    PieceInterface rook = new Rook(Color.WHITE, position, board);
//    board.setPieceAtPosition(rook.getPosition(), rook);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> rook.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(rook.getPosition()).isEqualTo(position);
//  }
//
//  @Test
//  void testRookMove_failObstacle() {
//    Position position = new Position(2, 2);
//    Position obstaclePosition = new Position(3, 2); // Position occupied by another piece
//    Position targetPosition = new Position(5, 2);
//    PieceInterface rook = new Rook(Color.WHITE, position, board);
//    PieceInterface obstacle = new Pawn(Color.WHITE, obstaclePosition, board);
//    board.setPieceAtPosition(rook.getPosition(), rook);
//    board.setPieceAtPosition(obstacle.getPosition(), obstacle);
//
//    IllegalMoveException exception =
//        assertThrows(IllegalMoveException.class, () -> rook.moveTo(targetPosition));
//    String expectedMessage = "Illegal move";
//    assertThat(exception.getMessage()).contains(expectedMessage);
//    assertThat(rook.getPosition()).isEqualTo(position);
//  }

//  @Test
//  void equals_IdenticalPieces() {
//    PieceInterface piece1 = new King(Color.WHITE, new Position(0, 0), board);
//    PieceInterface piece2 = new King(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece1.equals(piece2)).isTrue();
//  }
//
//  @Test
//  @SuppressWarnings("EqualsWithItself")
//  void equals_sameInstance() {
//    PieceInterface piece = new King(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece.equals(piece)).isTrue();
//  }
//
//  @Test
//  @SuppressWarnings("ConstantConditions")
//  void equals_InstanceNull() {
//    PieceInterface piece = new King(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece.equals(null)).isFalse();
//  }
//
//  @Test
//  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
//  void equals_DifferentClass() {
//    PieceInterface piece = new King(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece.equals("String")).isFalse();
//  }
//
//  @Test
//  void equals_DifferentPieces() {
//    PieceInterface piece1 = new King(Color.WHITE, new Position(0, 0), board);
//    PieceInterface piece2 = new Queen(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece1.equals(piece2)).isFalse();
//  }
//
//  @Test
//  void hashCode_IdenticalPieces() {
//    PieceInterface piece1 = new King(Color.WHITE, new Position(0, 0), board);
//    PieceInterface piece2 = new King(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece1.hashCode()).isEqualTo(piece2.hashCode());
//  }
//
//  @Test
//  void hashCode_DifferentPieces() {
//    PieceInterface piece1 = new King(Color.WHITE, new Position(0, 0), board);
//    PieceInterface piece2 = new Queen(Color.WHITE, new Position(0, 0), board);
//    assertThat(piece1.hashCode()).isNotEqualTo(piece2.hashCode());
//  }
//
//  @Test
//  void testPieceSetPosition() {
//    Position oldposition = new Position(0, 0);
//    Position newPosition = new Position(5, 0);
//    PieceInterface rook = new Rook(Color.WHITE, oldposition, board);
//    rook.setPosition(newPosition);
//    assertThat(board.getPieceAtPosition(oldposition)).isNull();
//    assertThat(rook.getPosition()).isEqualTo(newPosition);
//    assertThat(board.getPieceAtPosition(newPosition)).isEqualTo(rook);
//  }
//
//  @Test
//  void testToString() {
//    Position position = new Position(0, 0);
//    PieceInterface piece = new Rook(Color.WHITE, position, board);
//    String expectedString =
//        "Rook{color=WHITE, position=Position[row=0, column=0], symbol=R}";
//    assertThat(piece.toString()).isEqualTo(expectedString);
//  }
//
//  @Test
//  void testToStringWithDifferentPieceType() {
//    ChessBoard chessBoard = new ChessBoard();
//    PieceInterface piece = new Pawn(Color.BLACK, new Position(6, 1), chessBoard);
//    String expectedString =
//        "Queen{color=BLACK, position=Position[row=6, column=1], symbol=p}";
//    assertThat(piece.toString()).isNotEqualTo(expectedString);
//  }
//
//  @Test
//  void testToStringWithDifferentPosition() {
//    ChessBoard chessBoard = new ChessBoard();
//    PieceInterface piece = new Pawn(Color.BLACK, new Position(0, 0), chessBoard);
//    String expectedString =
//        "Pawn{color=BLACK, position=Position[row=4, column=3], symbol=p}";
//    assertThat(piece.toString()).isNotEqualTo(expectedString);
//  }
//}
