package hwr.oop.chess.domain;

public class IllegalMoveBecauseKingIsInCheckException extends RuntimeException {
  public IllegalMoveBecauseKingIsInCheckException() {
    super("You can not move your piece to this position because your king is in check.");
  }
}
