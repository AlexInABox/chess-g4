package hwr.oop;

import hwr.oop.match.Match;
import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.player.Player;

public interface Domain {
  Match loadMatch(String matchId);

  void saveMatch(Match newMatch);

  void createMatch(String playerWhiteName, String playerBlackName, String id);

  Player loadPlayer(String name);

  void savePlayer(Player player);

  void createPlayer(String name);

  void moveTo(Position oldPosition, Position newPosition, Match match) throws IllegalMoveException;

  void acceptRemi(Match match);

  void resign(Match match);
}
