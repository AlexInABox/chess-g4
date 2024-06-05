package hwr.oop.chess;

public class GameHasNotEndedException extends RuntimeException {
  public GameHasNotEndedException(String message) {
    super(message);
  }
}
