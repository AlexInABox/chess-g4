package hwr.oop.chess.domain;

public class GameNotFoundException extends RuntimeException {

  public GameNotFoundException(String gameId) {
    super("Game with ID '" + gameId + "' not found.");
  }
}
