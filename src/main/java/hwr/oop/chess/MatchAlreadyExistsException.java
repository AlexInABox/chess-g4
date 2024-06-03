package hwr.oop.chess;

public class MatchAlreadyExistsException extends RuntimeException {
  public MatchAlreadyExistsException(String matchId) {
    super("Match with ID '" + matchId + "' already exists.");
  }
}
