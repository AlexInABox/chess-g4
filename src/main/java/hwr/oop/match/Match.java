package hwr.oop.match;

import hwr.oop.Color;
import hwr.oop.board.ChessBoard;
import hwr.oop.board.ChessBoardException;
import hwr.oop.player.Player;
import java.io.Serializable;
import java.util.Objects;

public class Match implements Serializable {
  private final Player playerWhite;
  private final Player playerBlack;

  private final ChessBoard board;
  private String fenNotation;

  private Color nextToMove = Color.WHITE;
  private short moveCount = 0;
  private boolean gameEnded = false;

  public Match(Player playerWhite, Player playerBlack) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.board = new ChessBoard();
  }

  public Match(Player playerWhite, Player playerBlack, ChessBoard board) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.board = board;
  }

  public Match(Player playerWhite, Player playerBlack, String fenNotation)
      throws ChessBoardException {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.fenNotation = fenNotation;
    board = new ChessBoard(fenNotation);
  }

  public Player getPlayerWhite() {
    return playerWhite;
  }

  public Player getPlayerBlack() {
    return playerBlack;
  }

  public String getFEN() {
    return fenNotation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Match match = (Match) o;
    return moveCount == match.moveCount
        && gameEnded == match.gameEnded
        && Objects.equals(playerWhite, match.playerWhite)
        && Objects.equals(playerBlack, match.playerBlack)
        && Objects.equals(board, match.board)
        && Objects.equals(fenNotation, match.fenNotation)
        && nextToMove == match.nextToMove;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        playerWhite, playerBlack, board, fenNotation, nextToMove, moveCount, gameEnded);
  }

  @Override
  public String toString() {
    return "Match{"
        + "playerWhite="
        + playerWhite
        + ", playerBlack="
        + playerBlack
        + ", board="
        + board
        + ", fenNotation='"
        + fenNotation
        + '\''
        + ", nextToMove="
        + nextToMove
        + ", moveCount="
        + moveCount
        + ", gameEnded="
        + gameEnded
        + '}';
  }
}
