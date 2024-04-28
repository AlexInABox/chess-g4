package hwr.oop.persistence;

import hwr.oop.ChessBoard;
import hwr.oop.exceptions.ChessBoardException;
import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.pieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static hwr.oop.ChessBoard.convertInputToPosition;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceImplTest {

  private PersistenceImpl instUT;

  @BeforeEach
  void setUp(){
    instUT = new PersistenceImpl();
  }

  @Test
  void testPersistenceReadWriteChessBoard(){
    //given
    final ChessBoard expectedChessBoard = new ChessBoard();
    final String filePath = "target/persistenceTest.txt";

    //when
    instUT.write(expectedChessBoard, filePath);
    final ChessBoard actualChessboard = (ChessBoard) instUT.read(filePath);

    //then
    assertNotNull(actualChessboard);
    assertThat(expectedChessBoard).isEqualTo(actualChessboard);
  }

  @Test
  void testPersistenceCannotWrite(){
    final ChessBoard chessBoard = new ChessBoard();
    final String filePath = "not-existing-directory/persistenceTest.txt";

    PersistenceException exception = assertThrows(PersistenceException.class, () -> instUT.write(chessBoard, filePath));
    String expectedMessage = "Cannot write.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }

  @Test
  void testPersistenceCannotRead(){
    final String filePath = "target/notExistingFile.txt";

    PersistenceException exception = assertThrows(PersistenceException.class, () -> instUT.read(filePath));
    String expectedMessage = "Cannot read.";
    assertThat(exception.getMessage()).contains(expectedMessage);
  }
}
