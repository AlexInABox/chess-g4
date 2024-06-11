package hwr.oop.chess.persistence;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.player.Player;
import java.util.List;

/** Interface defining the persistence operations for the chess game. */
public interface Persistence {

  /**
   * Saves the list of games.
   *
   * @param games The list of games to save.
   */
  void saveGames(List<Game> games);

  /**
   * Loads the list of games.
   *
   * @return The list of loaded games.
   */
  List<Game> loadGames();

  /**
   * Saves the list of players.
   *
   * @param players The list of players to save.
   */
  void savePlayers(List<Player> players);

  /**
   * Loads the list of players.
   *
   * @return The list of loaded players.
   */
  List<Player> loadPlayers();
}
