package hwr.oop;

public class MatchNotFoundException extends RuntimeException {

  public MatchNotFoundException(String matchId) {
    super("Match with ID '" + matchId + "' not found.");
  }
}
