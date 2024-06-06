package hwr.oop.chess.persistence;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.player.Player;
import java.util.List;

public interface Persistence {
  void saveGames(List<Game> game);

  List<Game> loadGames();

  void savePlayers(List<Player> players);

  List<Player> loadPlayers();
}
