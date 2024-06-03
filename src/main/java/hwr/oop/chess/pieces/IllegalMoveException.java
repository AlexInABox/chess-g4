package hwr.oop.chess.pieces;

public class IllegalMoveException extends RuntimeException {
  public IllegalMoveException(String message) {
    super(message);
  }
}
