package hwr.oop.chess.domain;

public class PlayerNotFoundException extends RuntimeException {

  public PlayerNotFoundException(String name) {
    super("Player with the name '" + name + "' was not found.");
  }
}
