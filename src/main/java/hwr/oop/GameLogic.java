package hwr.oop;

import hwr.oop.match.Match;
import hwr.oop.persistence.Persistence;
import hwr.oop.pieces.IllegalMoveException;
import hwr.oop.pieces.Piece;
import hwr.oop.player.Player;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class GameLogic implements Domain {

  Persistence persistence;
  private final Path pathMatches;
  private final Path pathPlayers;

  public GameLogic(Persistence persistence, String allMatchesPath, String allPlayersPath) {
    this.persistence = persistence;
    final File fileMatches = new File(allMatchesPath);
    pathMatches = fileMatches.toPath();
    final File filePlayers = new File(allPlayersPath);
    pathPlayers = filePlayers.toPath();
  }

  @Override
  public Match loadMatch(String matchId) {

    List<Match> matches = persistence.loadMatches(pathMatches);

    for (Match match : matches) {
      if (match.getId().equals(matchId)) {
        return match;
      }
    }
    throw new MatchNotFoundException(matchId);
  }

  @Override
  public void saveMatch(Match newMatch) {
    List<Match> matches = persistence.loadMatches(pathMatches);
    boolean matchExists = false;
    for (int i = 0; i < matches.size(); i++) {
      if (matches.get(i).getId().equals(newMatch.getId())) {
        matches.set(i, newMatch);
        matchExists = true;
        break;
      }
    }
    if (!matchExists) {
      matches.add(newMatch);
    }
    persistence.saveMatches(matches, pathMatches);
  }

  @Override
  public void createMatch(Player playerWhite, Player playerBlack, String id) {
    Player loadedPlayerWhite = loadPlayer(playerWhite.getName());
    Player loadedPlayerBlack = loadPlayer(playerBlack.getName());

    if (matchExists(id)) {
      throw new MatchAlreadyExistsException(id);
    }

    Match newMatch = new Match(loadedPlayerWhite, loadedPlayerBlack, id);
    saveMatch(newMatch);
  }

  @Override
  public Player loadPlayer(String name) {
    List<Player> players = persistence.loadPlayers(pathPlayers);

    for (Player player : players) {
      if (player.getName().equals(name)) {
        return player;
      }
    }
    throw new PlayerNotFoundException(name);
  }

  @Override
  public void savePlayer(Player newPlayer) {
    List<Player> players = persistence.loadPlayers(pathPlayers);
    boolean playerExists = false;
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getName().equals(newPlayer.getName())) {
        players.set(i, newPlayer);
        playerExists = true;
        break;
      }
    }
    if (!playerExists) {
      players.add(newPlayer);
    }
    persistence.savePlayers(players, pathPlayers);
  }

  @Override
  public void createPlayer(String name) {
    if (playerExists(name)) {
      throw new PlayerAlreadyExistsException(name);
    }

    Player newPlayer = new Player(name);
    savePlayer(newPlayer);
  }

  public void moveTo(Position oldPosition, Position newPosition, Match match)
      throws IllegalMoveException {
    if (match == null) {
      throw new IllegalMoveException("Match is not initialized");
    }

    if (oldPosition.equals(newPosition)) {
      throw new IllegalMoveException(
          "Illegal move to position: "
              + newPosition
              + ". The start and end positions are the same.");
    }

    Piece currentPiece = match.getBoard().getPieceAtPosition(oldPosition);

    if (currentPiece == null) {
      throw new IllegalMoveException("No piece at the specified position: " + oldPosition);
    }

    if (currentPiece.getColor() != match.getNextToMove()) {
      throw new IllegalMoveException(
          "It's not your turn. Expected: "
              + match.getNextToMove()
              + ", but got: "
              + currentPiece.getColor());
    }

    List<Position> possibleMoves = currentPiece.possibleMoves();
    if (!possibleMoves.contains(newPosition)) {
      String firstTwoPossibleMoves =
          possibleMoves.stream().limit(2).map(Position::toString).collect(Collectors.joining(", "));
      throw new IllegalMoveException(
          "Illegal move to position: "
              + newPosition
              + ". Possible possible moves are for example: "
              + firstTwoPossibleMoves);
    }

    currentPiece.moveTo(newPosition);
    match.toggleNextToMove();
  }

  @Override
  public void acceptRemi(Match match) {
    match.declareWinner("Remi");
  }

  @Override
  public void resign(Match match) {
    Color currentPlayer = match.getNextToMove();
    if (currentPlayer == Color.WHITE) {
      match.declareWinner("Black");
    } else {
      match.declareWinner("White");
    }
  }

  private boolean matchExists(String matchId) {
    List<Match> matches = persistence.loadMatches(pathMatches);
    for (Match match : matches) {
      if (match.getId().equals(matchId)) {
        return true;
      }
    }
    return false;
  }

  private boolean playerExists(String name) {
    List<Player> players = persistence.loadPlayers(pathPlayers);
    for (Player player : players) {
      if (player.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }
}
