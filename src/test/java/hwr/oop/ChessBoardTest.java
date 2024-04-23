package hwr.oop;

import hwr.oop.exceptions.ChessBoardException;
import hwr.oop.exceptions.MovePieceException;
import hwr.oop.pieces.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static hwr.oop.ChessBoard.convertInputToPosition;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ChessBoardTest {
  private ChessBoard board;
  List<List<Piece>> actualBoard;

  @BeforeEach
  void setup(){
    board = new ChessBoard();
    actualBoard = board.getBoard();
  }



  @Test
  void testInitialBoardSetup() {
    SoftAssertions.assertSoftly(softly -> {
      // Test for Rooks
      softly.assertThat(actualBoard.get(0).get(0)).isEqualTo(new Piece(PieceType.ROOK, Color.WHITE, new Position(0, 0), board));
      softly.assertThat(actualBoard.get(0).get(7)).isEqualTo(new Piece(PieceType.ROOK, Color.WHITE, new Position(0, 7), board));
      softly.assertThat(actualBoard.get(7).get(0)).isEqualTo(new Piece(PieceType.ROOK, Color.BLACK, new Position(7, 0), board));
      softly.assertThat(actualBoard.get(7).get(7)).isEqualTo(new Piece(PieceType.ROOK, Color.BLACK, new Position(7, 7), board));

      // Test for Knights
      softly.assertThat(actualBoard.get(0).get(1)).isEqualTo(new Piece(PieceType.KNIGHT, Color.WHITE, new Position(0, 1), board));
      softly.assertThat(actualBoard.get(0).get(6)).isEqualTo(new Piece(PieceType.KNIGHT, Color.WHITE, new Position(0, 6), board));
      softly.assertThat(actualBoard.get(7).get(1)).isEqualTo(new Piece(PieceType.KNIGHT, Color.BLACK, new Position(7, 1), board));
      softly.assertThat(actualBoard.get(7).get(6)).isEqualTo(new Piece(PieceType.KNIGHT, Color.BLACK, new Position(7, 6), board));

      // Test for Bishops
      softly.assertThat(actualBoard.get(0).get(2)).isEqualTo(new Piece(PieceType.BISHOP, Color.WHITE, new Position(0, 2), board));
      softly.assertThat(actualBoard.get(0).get(5)).isEqualTo(new Piece(PieceType.BISHOP, Color.WHITE, new Position(0, 5), board));
      softly.assertThat(actualBoard.get(7).get(2)).isEqualTo(new Piece(PieceType.BISHOP, Color.BLACK, new Position(7, 2), board));
      softly.assertThat(actualBoard.get(7).get(5)).isEqualTo(new Piece(PieceType.BISHOP, Color.BLACK, new Position(7, 5), board));

      // Test for Queens
      softly.assertThat(actualBoard.get(0).get(3)).isEqualTo(new Piece(PieceType.QUEEN, Color.WHITE, new Position(0, 3), board));
      softly.assertThat(actualBoard.get(7).get(3)).isEqualTo(new Piece(PieceType.QUEEN, Color.BLACK, new Position(7, 3), board));

      // Test for Kings
      softly.assertThat(actualBoard.get(0).get(4)).isEqualTo(new Piece(PieceType.KING, Color.WHITE, new Position(0, 4), board));
      softly.assertThat(actualBoard.get(7).get(4)).isEqualTo(new Piece(PieceType.KING, Color.BLACK, new Position(7, 4), board));

      // Test for Pawns
      for (int i = 0; i < 8; i++) {
        softly.assertThat(actualBoard.get(1).get(i)).isEqualTo(new Piece(PieceType.PAWN, Color.WHITE, new Position(1, i), board));
        softly.assertThat(actualBoard.get(6).get(i)).isEqualTo(new Piece(PieceType.PAWN, Color.BLACK, new Position(6, i), board));
      }
    });

  }


//  @Test
//  void testPrintChessBoard() {
//    board = new ChessBoard();
//    printChessBoard(board.getBoard());
//    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outputStreamCaptor));
//    printChessBoard(board.getBoard());
//    String expectedOutput =
//            """
//                  a b c d e f g h\r
//                +-----------------+\r
//               8| r n b q k b n r |\r
//               7| p p p p p p p p |\r
//               6| . . . . . . . . |\r
//               5| . . . . . . . . |\r
//               4| . . . . . . . . |\r
//               3| . . . . . . . . |\r
//               2| P P P P P P P P |\r
//               1| R N B Q K B N R |\r
//                +-----------------+\r
//                """;
//    assertThat(outputStreamCaptor.toString()).hasToString(expectedOutput);
//  }


  @Test
  void convertInputToPosition_Valid() {
    SoftAssertions.assertSoftly(softly -> {
      try {
        softly.assertThat(convertInputToPosition("a8")).isEqualTo(new Position(7, 0));
        softly.assertThat(convertInputToPosition("h1")).isEqualTo(new Position(0, 7));
        softly.assertThat(convertInputToPosition("e5")).isEqualTo(new Position(4, 4));
      } catch (ChessBoardException e) {
        throw new RuntimeException(e);
      }

    });
  }

  @Test
  void convertInputToPosition_InvalidFormat() {
    assertThrows(ChessBoardException.class, () -> convertInputToPosition(""));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("a"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("abc"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("a12"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("12"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("abc12"));
  }

  @Test
  void testInvalidPosition() {
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("i1"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("a0"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("a9"));
    assertThrows(ChessBoardException.class, () -> convertInputToPosition("h9"));
  }

  @Test
  void movePiece_Successful() throws MovePieceException {
    Position from = new Position(1,0);
    Position to = new Position(2,0);
    assertThat(board.movePiece(from, to)).isTrue();
    assertThat(actualBoard.get(1).getFirst()).isNull();
    assertThat(actualBoard.get(2).getFirst()).isEqualTo(new Piece(PieceType.PAWN, Color.WHITE, new Position(2,0), board));
  }

  @Test
  void movePiece_Fail_OutsideBoardNegativeColumn(){
    Position from = new Position(1,0); //a2
    Position to = new Position(2,-1); //Outside Board
    MovePieceException exception = assertThrows(MovePieceException.class, () -> board.movePiece(from, to));
    String expectedMessage = "Invalid destination position!";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(actualBoard.get(1).getFirst()).isEqualTo(new Piece(PieceType.PAWN, Color.WHITE, new Position(1,0), board));
  }

  @Test
  void movePiece_Fail_OutsideBoardNegativeRow(){
    Position from = new Position(0,0); //a1
    Position to = new Position(-1,0); //Outside Board
    MovePieceException exception = assertThrows(MovePieceException.class, () -> board.movePiece(from, to));
    String expectedMessage = "Invalid destination position!";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(actualBoard.getFirst().getFirst()).isEqualTo(new Piece(PieceType.ROOK, Color.WHITE, new Position(0,0), board));
  }

  @Test
  void movePiece_Fail_ColumnOutOfBounds(){
    Position from = new Position(1,7); //h2
    Position to = new Position(2,8); //Outside Board (i3)
    MovePieceException exception = assertThrows(MovePieceException.class, () -> board.movePiece(from, to));
    String expectedMessage = "Invalid destination position!";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(actualBoard.get(1).get(7)).isEqualTo(new Piece(PieceType.PAWN, Color.WHITE, new Position(1,7), board));
  }

  @Test
  void movePiece_Fail_RowOutOfBounds(){
    Position from = new Position(7,0); //a8
    Position to = new Position(8,0); //Outside Board (a9)
    MovePieceException exception = assertThrows(MovePieceException.class, () -> board.movePiece(from, to));
    String expectedMessage = "Invalid destination position!";
    assertThat(exception.getMessage()).contains(expectedMessage);
    assertThat(actualBoard.get(7).getFirst()).isEqualTo(new Piece(PieceType.ROOK, Color.BLACK, new Position(7,0), board));
  }

  @Test
  void movePiece_Fail_OccupiedPositionByOwnPiece(){
    Position from = new Position(0,0); //a1
    Position to = new Position(1,0); //a2
    MovePieceException exception = assertThrows(MovePieceException.class, () -> board.movePiece(from, to));
    String expectedMessage = "Destination position occupied by own piece!";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void movePiece_Fail_NoPieceOnFromPosition(){
    Position from = new Position(3,0); //a3
    Position to = new Position(4,0); //a4
    MovePieceException exception = assertThrows(MovePieceException.class, () -> board.movePiece(from, to));
    String expectedMessage = "No piece at the specified position!";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
  @Test
  void testIsPositionValid() {
    ChessBoard board = new ChessBoard();

    Position position_inBounds = new Position(2, 2);
    Position position_tooLowRow = new Position(-1, 2);
    Position position_tooHighRow = new Position(9, 2);
    Position position_tooLowColumn = new Position(2, -1);
    Position position_tooHighColumn = new Position(2, 9);
    Position position_tooLowAll = new Position(-1, -1);
    Position position_tooHighAll = new Position(9, 9);

    assertThat(board.isValidPosition(position_inBounds.row(), position_inBounds.column())).isTrue();
    assertThat(board.isValidPosition(position_tooLowRow.row(), position_tooLowRow.column())).isFalse();
    assertThat(board.isValidPosition(position_tooHighRow.row(), position_tooHighRow.column())).isFalse();
    assertThat(board.isValidPosition(position_tooLowColumn.row(), position_tooLowColumn.column())).isFalse();
    assertThat(board.isValidPosition(position_tooHighColumn.row(), position_tooHighColumn.column())).isFalse();
    assertThat(board.isValidPosition(position_tooLowAll.row(), position_tooLowAll.column())).isFalse();
    assertThat(board.isValidPosition(position_tooHighAll.row(), position_tooHighAll.column())).isFalse();
  }

}