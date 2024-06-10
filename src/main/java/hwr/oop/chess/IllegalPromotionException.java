package hwr.oop.chess;

public class IllegalPromotionException extends RuntimeException {
  public IllegalPromotionException(String message) {
    super(message);
  }
}
