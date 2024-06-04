package hwr.oop.chess;

import hwr.oop.chess.match.Match;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.pieces.IllegalMoveException;
import hwr.oop.chess.pieces.Piece;
import hwr.oop.chess.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameLogic implements Domain {

  Persistence persistence;

  public GameLogic(Persistence persistence) {
    this.persistence = persistence;
  }

  @Override
  public Match loadMatch(String matchId) {

    List<Match> matches = persistence.loadMatches();

    for (Match match : matches) {
      if (match.getId().equals(matchId)) {
        Player playerWhite = loadPlayer(match.getPlayerWhite().getName());
        Player playerBlack = loadPlayer(match.getPlayerBlack().getName());
        match.updatePlayers(playerWhite, playerBlack);

        return match;}}
    throw new MatchNotFoundException(matchId);
  }

  @Override
  public void saveMatch(Match newMatch) {
    List<Match> matches = persistence.loadMatches();
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
    persistence.saveMatches(matches);
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
  public Player loadPlayer(String playerName) {
    return persistence.loadPlayers().stream()
        .filter(player -> player.getName().equals(playerName))
        .findFirst()
        .orElseGet(
            () -> new Player(playerName));
  }

  @Override
  public void savePlayer(Player newPlayer) {
    List<Player> players = persistence.loadPlayers();
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getName().equals(newPlayer.getName())) {
        players.set(i, newPlayer);
        persistence.savePlayers(players);
        return;
      }
    }
    players.add(newPlayer);
    persistence.savePlayers(players);
  }

  @Override
  public void moveTo(String oldPositionString, String newPositionString, Match match)
      throws IllegalMoveException, ConvertInputToPositionException {

    Position oldPosition = convertInputToPosition(oldPositionString);
    Position newPosition = convertInputToPosition(newPositionString);

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
  public List<Position> getPossibleMoves(String currentPositionString, Match match) {
    Position currentPosition = convertInputToPosition(currentPositionString);
    Piece currentPiece = match.getBoard().getPieceAtPosition(currentPosition);
    if (currentPiece == null) {
      return new ArrayList<>();
    }
    return currentPiece.possibleMoves();
  }

  @Override
  public void acceptRemi(Match match) {

    match.declareWinner(MatchOutcome.REMI);
    endGame(match);
  }

  @Override
  public void resign(Match match) {
    Color currentPlayer = match.getNextToMove();
    if (currentPlayer == Color.WHITE) {
      match.declareWinner(MatchOutcome.BLACK);
    } else {
      match.declareWinner(MatchOutcome.WHITE);
    }
  }

  @Override
  public String endGame(Match match) {
    Player playerWhite = loadPlayer(match.getPlayerWhite().getName());
    Player playerBlack = loadPlayer(match.getPlayerBlack().getName());
    double chanceToWinPlayerWhite = calculateChanceToWinPlayerWhite(playerWhite, playerBlack);
    double chanceToWinPlayerBlack = 1 - chanceToWinPlayerWhite;
    String victoryMessage = "";
    switch (match.getWinner()) {
      case MatchOutcome.REMI -> {
        playerWhite.setElo(calculateNewEloRemi(playerWhite, chanceToWinPlayerWhite));
        playerBlack.setElo(calculateNewEloRemi(playerBlack, chanceToWinPlayerBlack));
        victoryMessage = "The game ended in Remi.";
      }

      case MatchOutcome.WHITE -> {
        playerWhite.setElo(calculateNewEloWinner(playerWhite, chanceToWinPlayerWhite));
        playerBlack.setElo(calculateNewEloLooser(playerBlack, chanceToWinPlayerBlack));
        victoryMessage =
            "WHITE won this game. Congrats "
                + playerWhite.getName()
                + " (new ELO: "
                + playerWhite.getElo()
                + ")";
      }
      case MatchOutcome.BLACK -> {
        playerWhite.setElo(calculateNewEloLooser(playerWhite, chanceToWinPlayerWhite));
        playerBlack.setElo(calculateNewEloWinner(playerBlack, chanceToWinPlayerBlack));
        victoryMessage =
            "BLACK won this game. Congrats "
                + playerBlack.getName()
                + " (new ELO: "
                + playerBlack.getElo()
                + ")";
      }
      case MatchOutcome.NOT_FINISHED_YET ->
          throw new TheMatchHasNotEndedException("The game has not ended yet");
    }
    savePlayer(playerWhite);
    savePlayer(playerBlack);
    deleteMatch(match.getId());
    return victoryMessage;
  }

  private double calculateChanceToWinPlayerWhite(Player playerWhite, Player playerBlack) {
    return (double)
            Math.round(
                1
                    / (1
                        + Math.pow(
                            10, ((double) (playerBlack.getElo() - playerWhite.getElo()) / 400)))
                    * 100)
        / 100;
  }

  private short calculateNewEloWinner(Player player, double chanceToWin) {

    return (short) Math.round((player.getElo() + 20 * (1 - chanceToWin)));
  }

  private short calculateNewEloLooser(Player player, double chanceToWin) {

    return (short) Math.round((player.getElo() + 20 * (0 - chanceToWin)));
  }

  private short calculateNewEloRemi(Player player, double chanceToWin) {
    return (short) Math.round((player.getElo() + 20 * (0.5 - chanceToWin)));
  }

  private void deleteMatch(String matchId) {
    List<Match> matches = persistence.loadMatches();
    matches.removeIf(match -> match.getId().equals(matchId));
    persistence.saveMatches(matches);
  }

  public static Position convertInputToPosition(String input)
      throws ConvertInputToPositionException {
    if (input.length() != 2
        || !Character.isLetter(input.charAt(0))
        || !Character.isDigit(input.charAt(1))) {
      throw new ConvertInputToPositionException(
          "Invalid input format. Please provide a valid position (e.g., 'a1').");
    }

    int column = input.charAt(0) - 'a';
    int row = Character.getNumericValue(input.charAt(1)) - 1;

    if (column < 0 || column >= 8 || row < 0 || row >= 8) {
      throw new ConvertInputToPositionException(
          "Invalid position. Position must be within the chessboard.");
    }

    return new Position(row, column);
  }

  private boolean matchExists(String matchId) {
    List<Match> matches = persistence.loadMatches();
    for (Match match : matches) {
      if (match.getId().equals(matchId)) {
        return true;
      }
    }
    return false;
  }
}
