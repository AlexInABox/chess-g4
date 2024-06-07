package hwr.oop.chess;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.pieces.IllegalMoveException;
import hwr.oop.chess.player.Player;

import java.util.List;

/** Interface defining the basic functionalities for the chess game. */
public interface Domain {

  /**
   * Loads a game based on its ID.
   *
   * @param gameId The ID of the game to load.
   * @return The loaded game.
   * @throws GameNotFoundException If no game is found with the specified ID.
   */
  Game loadGame(String gameId);

  /**
   * Saves a new game or updates an existing one.
   *
   * @param newGame The game to save.
   */
  void saveGame(Game newGame);

  /**
   * Creates a new game with the given players and a unique ID.
   *
   * @param playerWhite The name of the white player.
   * @param playerBlack The name of the black player.
   * @param id The unique ID of the game.
   * @throws GameAlreadyExistsException If a game with the same ID already exists.
   */
  void createGame(Player playerWhite, Player playerBlack, String id);

  /**
   * Loads a player based on their name or creates a new player, if there is no player with this
   * name yet.
   *
   * @param name The name of the player to load.
   * @return The loaded player.
   * @throws PlayerNotFoundException If no player is found with the specified name.
   */
  Player loadPlayer(String name);

  /**
   * Saves a new player or updates an existing one.
   *
   * @param player The player to save.
   */
  void savePlayer(Player player);

  void promotePiece(Game game, String position, String type);

  /**
   * Moves a piece from an old position to a new position.
   *
   * @param oldPositionString The old position of the piece. (e.g. a1)
   * @param newPositionString The new position of the piece. (e.g. c2)
   * @param game              The game in which the move is performed.
   * @return true, if this move led to check mate.
   * @throws ConvertInputToPositionException If the oldPositionString or newPositionString does not have the right format.
   * @throws IllegalMoveException If the move is illegal.
   * @throws IllegalMoveBecauseKingIsInCheckException If king is in check and this move does not save them.
   */
  boolean moveTo(String oldPositionString, String newPositionString, Game game);

  /**
   * Accepts a draw offer, ending the game in a draw.
   *
   * @param game The game in which the draw offer is accepted.
   */
  void endGameWithRemi(Game game);

  /**
   * Resigns from the game, declaring the opponent as the winner.
   *
   * @param game The game in which the resignation occurs.
   */
  void resign(Game game);

  /**
   * Get fen notation of the current game status.
   *
   * @param game The game from which to get the fen notation.
   */
  String getFENNotation(Game game);

  /**
   * Ends the game, declaring the winner and updating players' ELO ratings.
   *
   * @param game The game to end.
   * @return A message indicating the outcome of the game, including the winner and their new ELO rating.
   */
  String endGame(Game game);
  /**
   * Gets a list of possible moves for the piece at the given position in the specified match.
   *
   * @param currentPositionString The current position of the piece as a string (e.g., "e2").
   * @param game The game in which to check possible moves.
   * @return A list of possible positions to which the piece can move. Returns an empty list if there is no piece at the specified position.
   */
  List<Position> getPossibleMoves (String currentPositionString, Game game);
}
