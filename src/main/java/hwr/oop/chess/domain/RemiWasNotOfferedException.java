package hwr.oop.chess.domain;

public class RemiWasNotOfferedException extends RuntimeException {
  public RemiWasNotOfferedException() {
    super("You have to offer remi first before you can accept it.");
  }
}
