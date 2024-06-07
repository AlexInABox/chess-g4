package hwr.oop.chess;

public class IllegalPromotionException extends RuntimeException{
  public IllegalPromotionException() {
    super("Promotion is not allowed: The specified type is invalid. Valid promotion types are 'Queen', 'Rook', 'Bishop', or 'Knight'.");
  }
}
