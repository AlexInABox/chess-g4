package hwr.oop.chess;

public class RemiWasNotOfferedException extends RuntimeException {
  public RemiWasNotOfferedException() {
    super("You have to offer remi first before you can accept it.");
  }
}
