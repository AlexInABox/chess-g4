package hwr.oop.match;

import hwr.oop.board.ChessBoard;
import hwr.oop.board.ChessBoardException;
import hwr.oop.player.Player;
import java.io.Serializable;

public class Match implements Serializable {
  private final Player playerWhite;
  private final Player playerBlack;

  private final ChessBoard board;
  private String fen_notation;

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

  public Match(Player playerWhite, Player playerBlack, String fen_notation)
      throws ChessBoardException {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.fen_notation = fen_notation;
    board = new ChessBoard(fen_notation);
  }

  public Player getPlayerWhite() {
    return playerWhite;
  }

  public Player getPlayerBlack() {
    return playerBlack;
  }

  public String getFEN() {
    return fen_notation;
  }
}
