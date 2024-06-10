package hwr.oop.chess;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.persistence.Persistence;
import hwr.oop.chess.pieces.*;
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
  public Game loadGame(String gameId) {

    List<Game> games = persistence.loadGames();

    for (Game game : games) {
      if (game.getId().equals(gameId)) {
        Player playerWhite = loadPlayer(game.getPlayerWhite().getName());
        Player playerBlack = loadPlayer(game.getPlayerBlack().getName());
        game.updatePlayers(playerWhite, playerBlack);

        return game;}}
    throw new GameNotFoundException(gameId);
  }

  @Override
  public void saveGame(Game newGame) {
    List<Game> games = persistence.loadGames();
    boolean gameExists = false;
    for (int i = 0; i < games.size(); i++) {
      if (games.get(i).getId().equals(newGame.getId())) {
        games.set(i, newGame);
        gameExists = true;
        break;
      }
    }
    if (!gameExists) {
      games.add(newGame);
    }
    persistence.saveGames(games);
  }

  @Override
  public void createGame(Player playerWhite, Player playerBlack, String id) {
    Player loadedPlayerWhite = loadPlayer(playerWhite.getName());
    Player loadedPlayerBlack = loadPlayer(playerBlack.getName());

    if (gameExists(id)) {
      throw new GameAlreadyExistsException(id);
    }

    Game newGame = new Game(loadedPlayerWhite, loadedPlayerBlack, id);
    saveGame(newGame);
  }

  @Override
  public Player loadPlayer(String playerName) {
    return persistence.loadPlayers().stream()
        .filter(player -> player.getName().equals(playerName))
        .findFirst()
        .orElseGet(() -> new Player(playerName));
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
  public void promotePiece(Game game, String positionString, String type){
    Position position = convertInputToPosition(positionString);
    Piece currentPiece = game.getBoard().getPieceAtPosition(position);
    if(currentPiece == null){
      throw new IllegalPromotionException("Promotion is not allowed. No piece at given position.");
    }
    if(currentPiece.getType() != PieceType.PAWN){
      throw new IllegalPromotionException("Promotion is not allowed. You can only promote pawns");
    }

    type = type.toUpperCase();
    Piece newPiece;
    switch (type) {
      case "N" -> newPiece = new Knight(currentPiece.getColor(), position, game.getBoard());
      case "B" -> newPiece = new Bishop(currentPiece.getColor(), position, game.getBoard());
      case "R" -> newPiece = new Rook(currentPiece.getColor(), position, game.getBoard());
      case "Q" -> newPiece = new Queen(currentPiece.getColor(), position, game.getBoard());
      default -> throw new IllegalPromotionException("Promotion is not allowed: The specified type is invalid. Valid promotion types are 'Queen', 'Rook', 'Bishop', or 'Knight'.");
    }
    game.getBoard().promoteTo(position, newPiece);
  }

  @Override
  public boolean moveTo(String oldPositionString, String newPositionString, Game game) {

    Position oldPosition = convertInputToPosition(oldPositionString);
    Position newPosition = convertInputToPosition(newPositionString);

    if (oldPosition.equals(newPosition)) {
      throw new IllegalMoveException(
          "Illegal move to position: "
              + newPosition
              + ". The start and end positions are the same.");
    }

    Piece currentPiece = game.getBoard().getPieceAtPosition(oldPosition);

    if (currentPiece == null) {
      throw new IllegalMoveException("No piece at the specified position: " + oldPosition);
    }

    if (currentPiece.getColor() != game.getNextToMove()) {
      throw new IllegalMoveException(
          "It's not your turn. Expected: "
              + game.getNextToMove()
              + ", but got: "
              + currentPiece.getColor());
    }

    List<Position> possibleMoves = currentPiece.possibleMoves();
    if (!possibleMoves.contains(newPosition)) {

      if (game.getBoard().getKingOfColor(game.getNextToMove()).isInCheck()){
        throw new IllegalMoveBecauseKingIsInCheckException();
      }
      String firstTwoPossibleMoves =
          possibleMoves.stream().limit(2).map(Position::toString).collect(Collectors.joining(", "));
      throw new IllegalMoveException(
          "Illegal move to position: "
              + newPosition
              + ". Possible possible moves are for example: "
              + firstTwoPossibleMoves);
    }

    currentPiece.moveTo(newPosition);
    game.offerRemi(false);
    game.toggleNextToMove();
    saveGame(game);
    if(game.getBoard().isCheckMate()){
      if(game.getNextToMove() == Color.WHITE){
        game.declareWinner(GameOutcome.BLACK);

      }else{
        game.declareWinner(GameOutcome.WHITE);
      }
      return true;
    }
    return false;
  }

  @Override
  public List<Position> getPossibleMoves(String currentPositionString, Game game) {
    Position currentPosition = convertInputToPosition(currentPositionString);
    Piece currentPiece = game.getBoard().getPieceAtPosition(currentPosition);
    if (currentPiece == null) {
      return new ArrayList<>();
    }
    return currentPiece.possibleMoves();
  }

  @Override
  public void offerRemi(Game game) {
    game.offerRemi(true);
    saveGame(game);
  }

  @Override
  public void acceptRemi(Game game) {
    if (game.isRemiOffered()) {
      game.declareWinner(GameOutcome.REMI);
    } else {
      throw new RemiWasNotOfferedException();
    }
  }

  @Override
  public void resign(Game game) {
    Color currentPlayer = game.getNextToMove();
    if (currentPlayer == Color.WHITE) {
      game.declareWinner(GameOutcome.BLACK);
    } else {
      game.declareWinner(GameOutcome.WHITE);
    }
    endGame(game);
  }

  @Override
  public String getFENNotation(Game game) {
    return game.convertBoardToFEN();
  }

  private void calculateAndSetEloForBothPlayers(Game game) {
    Player playerWhite = loadPlayer(game.getPlayerWhite().getName());
    Player playerBlack = loadPlayer(game.getPlayerBlack().getName());
    double chanceToWinPlayerWhite = calculateChanceToWinPlayerWhite(playerWhite, playerBlack);
    double chanceToWinPlayerBlack = 1 - chanceToWinPlayerWhite;
    switch (game.getWinner()) {
      case GameOutcome.REMI -> {
        playerWhite.setElo(calculateNewEloRemi(playerWhite, chanceToWinPlayerWhite));
        playerBlack.setElo(calculateNewEloRemi(playerBlack, chanceToWinPlayerBlack));
      }

      case GameOutcome.WHITE -> {
        playerWhite.setElo(calculateNewEloWinner(playerWhite, chanceToWinPlayerWhite));
        playerBlack.setElo(calculateNewEloLooser(playerBlack, chanceToWinPlayerBlack));
      }
      case GameOutcome.BLACK -> {
        playerWhite.setElo(calculateNewEloLooser(playerWhite, chanceToWinPlayerWhite));
        playerBlack.setElo(calculateNewEloWinner(playerBlack, chanceToWinPlayerBlack));
      }
      case GameOutcome.NOT_FINISHED_YET ->
          throw new GameHasNotEndedException("The game has not ended yet");
    }
    savePlayer(playerWhite);
    savePlayer(playerBlack);
  }

  @Override
  public String endGame(Game game) {
    calculateAndSetEloForBothPlayers(game);
    Player playerWhite = loadPlayer(game.getPlayerWhite().getName());
    Player playerBlack = loadPlayer(game.getPlayerBlack().getName());

    String victoryMessage = "";
    switch (game.getWinner()) {
      case GameOutcome.REMI -> victoryMessage = "The game ended in Remi.";

      case GameOutcome.WHITE ->
          victoryMessage =
              "WHITE won this game. Congrats "
                  + playerWhite.getName()
                  + " (new ELO: "
                  + playerWhite.getElo()
                  + ")";
      case GameOutcome.BLACK ->
          victoryMessage =
              "BLACK won this game. Congrats "
                  + playerBlack.getName()
                  + " (new ELO: "
                  + playerBlack.getElo()
                  + ")";
      case NOT_FINISHED_YET -> {
        // this case is already checked (throws GameHasNotEndedException) within calculateAndSetEloForBothPlayers()
      }
    }
    deleteGame(game.getId());
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

  private void deleteGame(String gameId) {
    List<Game> games = persistence.loadGames();
    games.removeIf(game -> game.getId().equals(gameId));
    persistence.saveGames(games);
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

  private boolean gameExists(String gameId) {
    List<Game> games = persistence.loadGames();
    for (Game game : games) {
      if (game.getId().equals(gameId)) {
        return true;
      }
    }
    return false;
  }
}
