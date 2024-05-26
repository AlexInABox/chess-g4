package hwr.oop;

public class PlayerAlreadyExistsException extends RuntimeException {
  public PlayerAlreadyExistsException(String name) {
    super("Player with the name '" + name + "' already exists.");
  }
}
