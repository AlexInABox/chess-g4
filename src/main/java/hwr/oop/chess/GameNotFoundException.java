package hwr.oop.chess;

public class GameNotFoundException extends RuntimeException {

  public GameNotFoundException(String gameId) {
    super("Game with ID '" + gameId + "' not found.");
  }
}
