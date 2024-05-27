package hwr.oop;

import hwr.oop.match.Match;
import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.player.Player;

/** Interface defining the basic functionalities for the chess game. */
public interface Domain {

  /**
   * Loads a match based on its ID.
   *
   * @param matchId The ID of the match to load.
   * @return The loaded match.
   * @throws MatchNotFoundException If no match is found with the specified ID.
   */
  Match loadMatch(String matchId) throws MatchNotFoundException;

  /**
   * Saves a new match or updates an existing one.
   *
   * @param newMatch The match to save.
   */
  void saveMatch(Match newMatch);

  /**
   * Creates a new match with the given players and a unique ID.
   *
   * @param playerWhite The name of the white player.
   * @param playerBlack The name of the black player.
   * @param id The unique ID of the match.
   * @throws MatchAlreadyExistsException If a match with the same ID already exists.
   */
  void createMatch(Player playerWhite, Player playerBlack, String id)
      throws MatchAlreadyExistsException;

  /**
   * Loads a player based on their name.
   *
   * @param name The name of the player to load.
   * @return The loaded player.
   * @throws PlayerNotFoundException If no player is found with the specified name.
   */
  Player loadPlayer(String name) throws PlayerNotFoundException;

  /**
   * Saves a new player or updates an existing one.
   *
   * @param player The player to save.
   */
  void savePlayer(Player player);

  /**
   * Creates a new player with the given name.
   *
   * @param name The name of the new player.
   * @throws PlayerAlreadyExistsException If a player with the same name already exists.
   */
  void createPlayer(String name) throws PlayerAlreadyExistsException;

  /**
   * Moves a piece from an old position to a new position.
   *
   * @param oldPosition The old position of the piece.
   * @param newPosition The new position of the piece.
   * @param match The match in which the move is performed.
   * @throws IllegalMoveException If the move is illegal.
   */
  void moveTo(Position oldPosition, Position newPosition, Match match) throws IllegalMoveException;

  /**
   * Accepts a draw offer, ending the match in a draw.
   *
   * @param match The match in which the draw offer is accepted.
   */
  void acceptRemi(Match match);

  /**
   * Resigns from the match, declaring the opponent as the winner.
   *
   * @param match The match in which the resignation occurs.
   */
  void resign(Match match);
}
