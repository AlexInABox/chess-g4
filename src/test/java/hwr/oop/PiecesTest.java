package hwr.oop;

import hwr.oop.pieces.Piece;
import hwr.oop.pieces.PieceType;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class PiecesTest {
  //BISHOP
  @Test
  void testBishopConstructor() {
    Position position = new Position(0, 0);
    Piece whiteBishop = new Piece(PieceType.BISHOP, Color.WHITE, position);
    Piece blackBishop = new Piece(PieceType.BISHOP, Color.BLACK, position);
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
  void testGetBishopPosition() {
    Position position = new Position(3, 4);
    Piece bishop = new Piece(PieceType.BISHOP, Color.WHITE, position);
    assertThat(bishop.getPosition()).isEqualTo(position);
  }

  @Test
  void testSetBishopPosition() {
    Position position = new Position(7, 8);
    Piece bishop = new Piece(PieceType.BISHOP, Color.WHITE, position);
    assertThat(bishop.getPosition()).isEqualTo(position);

    Position newPosition = new Position(1, 1);
    bishop.setPosition(newPosition);
    assertThat(bishop.getPosition()).isEqualTo(newPosition);
  }

  @Test
  void testBishopToString() {
    Position position = new Position(3, 3);
    Piece bishop = new Piece(PieceType.BISHOP, Color.WHITE, position);
    assertThat(bishop.toString())
            .hasToString("Piece{type=BISHOP, color=WHITE, position=Position[row=3, column=3], symbol=B}");
  }
  //KING
  @Test
  void testKingConstructor() {
    Position position = new Position(0, 0);
    Piece whiteKing = new Piece(PieceType.KING, Color.WHITE, position);
    Piece blackKing = new Piece(PieceType.KING, Color.BLACK, position);
    assertThat(whiteKing.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteKing.getPosition()).isEqualTo(position);
    assertThat(whiteKing.getSymbol()).isEqualTo('K');

    assertThat(blackKing.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackKing.getPosition()).isEqualTo(position);
    assertThat(blackKing.getSymbol()).isEqualTo('k');
  }

  @Test
  void testGetKingPosition() {
    Position position = new Position(3, 4);
    Piece king = new Piece(PieceType.KING, Color.WHITE, position);
    assertThat(king.getPosition()).isEqualTo(position);
  }

  @Test
  void testSetKingPosition() {
    Position position = new Position(7, 8);
    Piece king = new Piece(PieceType.KING, Color.WHITE, position);
    assertThat(king.getPosition()).isEqualTo(position);

    Position newPosition = new Position(1, 1);
    king.setPosition(newPosition);
    assertThat(king.getPosition()).isEqualTo(newPosition);
  }

  @Test
  void testKingToString() {
    Position position = new Position(3, 3);
    Piece king = new Piece(PieceType.KING, Color.WHITE, position);
    assertThat(king.toString())
            .hasToString("Piece{type=KING, color=WHITE, position=Position[row=3, column=3], symbol=K}");
  }
  //KNIGHT
  @Test
  void testKnightConstructor() {
    Position position = new Position(0, 0);
    Piece whiteKnight = new Piece(PieceType.KNIGHT, Color.WHITE, position);
    Piece blackKnight = new Piece(PieceType.KNIGHT, Color.BLACK, position);
    assertThat(whiteKnight.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteKnight.getPosition()).isEqualTo(position);
    assertThat(whiteKnight.getSymbol()).isEqualTo('N');

    assertThat(blackKnight.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackKnight.getPosition()).isEqualTo(position);
    assertThat(blackKnight.getSymbol()).isEqualTo('n');
  }

  @Test
  void testGetKnightPosition() {
    Position position = new Position(3, 4);
    Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE, position);
    assertThat(knight.getPosition()).isEqualTo(position);
  }

  @Test
  void testSetKnightPosition() {
    Position position = new Position(7, 8);
    Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE, position);
    assertThat(knight.getPosition()).isEqualTo(position);

    Position newPosition = new Position(1, 1);
    knight.setPosition(newPosition);
    assertThat(knight.getPosition()).isEqualTo(newPosition);
  }

  @Test
  void testKnightToString() {
    Position position = new Position(3, 3);
    Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE, position);
    assertThat(knight.toString())
            .hasToString("Piece{type=KNIGHT, color=WHITE, position=Position[row=3, column=3], symbol=N}");
  }
  //PAWN
  @Test
  void testPawnConstructor() {
    Position position = new Position(0, 0);
    Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE, position);
    Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK, position);
    assertThat(whitePawn.getColor()).isEqualTo(Color.WHITE);
    assertThat(whitePawn.getPosition()).isEqualTo(position);
    assertThat(whitePawn.getSymbol()).isEqualTo('P');

    assertThat(blackPawn.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackPawn.getPosition()).isEqualTo(position);
    assertThat(blackPawn.getSymbol()).isEqualTo('p');
  }

  @Test
  void testGetPawnPosition() {
    Position position = new Position(3, 4);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position);
    assertThat(pawn.getPosition()).isEqualTo(position);
  }

  @Test
  void testSetPawnPosition() {
    Position position = new Position(7, 8);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position);
    assertThat(pawn.getPosition()).isEqualTo(position);

    Position newPosition = new Position(1, 1);
    pawn.setPosition(newPosition);
    assertThat(pawn.getPosition()).isEqualTo(newPosition);
  }

  @Test
  void testPawnToString() {
    Position position = new Position(3, 3);
    Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, position);
    assertThat(pawn.toString())
            .hasToString("Piece{type=PAWN, color=WHITE, position=Position[row=3, column=3], symbol=P}");
  }
  //QUEEN
  @Test
  void testQueenConstructor() {
    Position position = new Position(0, 0);
    Piece whiteQueen = new Piece(PieceType.QUEEN, Color.WHITE, position);
    Piece blackQueen = new Piece(PieceType.QUEEN, Color.BLACK, position);
    assertThat(whiteQueen.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteQueen.getPosition()).isEqualTo(position);
    assertThat(whiteQueen.getSymbol()).isEqualTo('Q');

    assertThat(blackQueen.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackQueen.getPosition()).isEqualTo(position);
    assertThat(blackQueen.getSymbol()).isEqualTo('q');
  }

  @Test
  void testGetQueenPosition() {
    Position position = new Position(3, 4);
    Piece queen = new Piece(PieceType.QUEEN, Color.WHITE, position);
    assertThat(queen.getPosition()).isEqualTo(position);
  }

  @Test
  void testSetQueenPosition() {
    Position position = new Position(7, 8);
    Piece queen = new Piece(PieceType.QUEEN, Color.WHITE, position);
    assertThat(queen.getPosition()).isEqualTo(position);

    Position newPosition = new Position(1, 1);
    queen.setPosition(newPosition);
    assertThat(queen.getPosition()).isEqualTo(newPosition);
  }

  @Test
  void testQueenToString() {
    Position position = new Position(3, 3);
    Piece queen = new Piece(PieceType.QUEEN, Color.WHITE, position);
    assertThat(queen.toString())
            .hasToString("Piece{type=QUEEN, color=WHITE, position=Position[row=3, column=3], symbol=Q}");
  }
  //ROOK
  @Test
  void testRookConstructor() {
    Position position = new Position(0, 0);
    Piece whiteRook = new Piece(PieceType.ROOK, Color.WHITE, position);
    Piece blackRook = new Piece(PieceType.ROOK, Color.BLACK, position);
    assertThat(whiteRook.getColor()).isEqualTo(Color.WHITE);
    assertThat(whiteRook.getPosition()).isEqualTo(position);
    assertThat(whiteRook.getSymbol()).isEqualTo('R');

    assertThat(blackRook.getColor()).isEqualTo(Color.BLACK);
    assertThat(blackRook.getPosition()).isEqualTo(position);
    assertThat(blackRook.getSymbol()).isEqualTo('r');
  }

  @Test
  void testGetRookPosition() {
    Position position = new Position(3, 4);
    Piece rook = new Piece(PieceType.ROOK, Color.WHITE, position);
    assertThat(rook.getPosition()).isEqualTo(position);
  }

  @Test
  void testSetRookPosition() {
    Position position = new Position(7, 8);
    Piece rook = new Piece(PieceType.ROOK, Color.WHITE, position);
    assertThat(rook.getPosition()).isEqualTo(position);

    Position newPosition = new Position(1, 1);
    rook.setPosition(newPosition);
    assertThat(rook.getPosition()).isEqualTo(newPosition);
  }

  @Test
  void testRookToString() {
    Position position = new Position(3, 3);
    Piece rook = new Piece(PieceType.ROOK, Color.WHITE, position);
    assertThat(rook.toString())
            .hasToString("Piece{type=ROOK, color=WHITE, position=Position[row=3, column=3], symbol=R}");
  }
  //equals?
  @Test
  void testRookEquals() {
    Position position = new Position(3, 3);
    Piece rook1 = new Piece(PieceType.ROOK, Color.WHITE, position);
    Piece rook2 = new Piece(PieceType.ROOK, Color.WHITE, position);
    ChessBoard chessBoard = new ChessBoard();

    assertThat(rook1.equals(rook2)).isTrue();
    assertThat(rook1.equals(rook1)).isTrue();
    assertThat(rook1.equals(null)).isFalse();
    assertThat(rook1.equals(chessBoard)).isFalse();
}
  @Test
  void testRookHash() {
    Position position = new Position(3, 3);
    Piece rook1 = new Piece(PieceType.ROOK, Color.WHITE, position);
    Piece rook2 = new Piece(PieceType.ROOK, Color.WHITE, position);

    //assertThat(rook1.hashCode()).isEqualTo(rook2.hashCode()); //ändert nichts an Line/Mutation Coverage
    assertThat(rook1.hashCode()).isEqualTo(Objects.hash(PieceType.ROOK, Color.WHITE, position, rook1.getSymbol()));
  }
}
