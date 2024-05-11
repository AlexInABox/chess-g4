package hwr.oop.player;

import java.io.Serializable;

public class Player implements Serializable {
  private final String name;

  private short elo = 100;

  public Player(String name) {
    this.name = name;
  }

  public Player(String name, short elo) throws PlayerException {
    this.name = name;
    setElo(elo);
  }

  public String getName() {
    return name;
  }

  public short getElo() {
    return elo;
  }

  public void setElo(short elo) throws PlayerException {
    if (elo < 100) throw new PlayerException("Elo cannot be lower than 100");
    this.elo = elo;
  }
}
