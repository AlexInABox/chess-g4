package hwr.oop.chess.player;

import java.io.Serializable;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return elo == player.elo && Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, elo);
  }

  @Override
  public String toString() {
    return "Player{" + "name='" + name + '\'' + ", elo=" + elo + '}';
  }
}
