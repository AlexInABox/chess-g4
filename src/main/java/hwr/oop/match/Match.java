package hwr.oop.match;

import hwr.oop.Color;
import hwr.oop.board.ChessBoard;
import hwr.oop.board.ChessBoardException;
import hwr.oop.player.Player;
import java.io.Serializable;

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
}
