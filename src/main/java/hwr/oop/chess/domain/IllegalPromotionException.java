package hwr.oop.chess.domain;

public class IllegalPromotionException extends RuntimeException {
  public IllegalPromotionException(String message) {
    super(message);
  }
}
