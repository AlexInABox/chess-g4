package hwr.oop.persistence;

import hwr.oop.match.Match;
import hwr.oop.player.Player;
import java.nio.file.Path;
import java.util.List;

public interface Persistence {
  void saveMatches(List<Match> match, Path filePath);

  List<Match> loadMatches(Path filePath);

  void savePlayers(List<Player> players, Path filePath);

  List<Player> loadPlayers(Path filePath);
}
