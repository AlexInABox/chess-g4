package hwr.oop.chess;

import hwr.oop.chess.match.Match;
import hwr.oop.chess.pieces.IllegalMoveException;
import hwr.oop.chess.player.Player;

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
   * Loads a player based on their name or creates a new player, if there is no player with this
   * name yet.
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
   * Moves a piece from an old position to a new position.
   *
   * @param oldPositionString The old position of the piece.
   * @param newPositionString The new position of the piece.
   * @param match The match in which the move is performed.
   * @throws IllegalMoveException If the move is illegal.
   */
  void moveTo(String oldPositionString, String newPositionString, Match match)
      throws IllegalMoveException, ConvertInputToPositionException;

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
