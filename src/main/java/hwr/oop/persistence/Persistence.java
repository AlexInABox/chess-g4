package hwr.oop.persistence;

import hwr.oop.match.Match;
import hwr.oop.player.Player;
import java.util.List;

public interface Persistence {
  void saveMatches(List<Match> match);

  List<Match> loadMatches();

  void savePlayers(List<Player> players);

  List<Player> loadPlayers();
}
