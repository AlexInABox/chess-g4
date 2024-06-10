package hwr.oop.chess.domain;

public class GameHasNotEndedException extends RuntimeException {
  public GameHasNotEndedException(String message) {
    super(message);
  }
}
