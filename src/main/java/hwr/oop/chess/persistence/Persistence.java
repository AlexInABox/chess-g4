package hwr.oop.chess.persistence;

import hwr.oop.chess.match.Match;
import hwr.oop.chess.player.Player;
import java.util.List;

public interface Persistence {
  void saveMatches(List<Match> match);

  List<Match> loadMatches();

  void savePlayers(List<Player> players);

  List<Player> loadPlayers();
}
