package hwr.oop.chess.pieces;

public class IllegalMoveException extends Exception {
  public IllegalMoveException(String message) {
    super(message);
  }
}
