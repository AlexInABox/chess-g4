package hwr.oop.player;

import java.io.Serializable;

public class Player implements Serializable {
  private final String name;

  private short elo = 100;

  public Player(String name) {
    this.name = name;
  }

  public Player(String name, short elo) {
    this.name = name;
    this.elo = elo;
  }

  public String getName() {
    return name;
  }

  public short getElo() {
    return elo;
  }
}
