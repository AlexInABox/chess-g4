package hwr.oop.chess.domain;

public class GameAlreadyExistsException extends RuntimeException {
  public GameAlreadyExistsException(String gameId) {
    super("Game with ID '" + gameId + "' already exists.");
  }
}
