package hwr.oop.chess;

public class GameAlreadyExistsException extends RuntimeException {
  public GameAlreadyExistsException(String gameId) {
    super("Game with ID '" + gameId + "' already exists.");
  }
}
