package hwr.oop.match;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.board.ChessBoard;
import hwr.oop.pieces.*;
import hwr.oop.player.Player;
import java.io.Serializable;
import java.util.Objects;

public class Match implements Serializable {
  private final Player playerWhite;
  private final Player playerBlack;

  private ChessBoard board;
  private String fenNotation;

  private Color nextToMove = Color.WHITE;
  private short moveCount = 0;
  private boolean gameEnded = false;

  public Match(Player playerWhite, Player playerBlack) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.board = new ChessBoard();
  }

  //  public Match(Player playerWhite, Player playerBlack, ChessBoard board) {
  //    this.playerWhite = playerWhite;
  //    this.playerBlack = playerBlack;
  //    this.board = board;
  //  }

  public Match(Player playerWhite, Player playerBlack, String fenNotation) throws FENException {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.fenNotation = fenNotation;
    board = convertFENToBoard(fenNotation);
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

  public Color getNextToMove() {
    return nextToMove;
  }

  public String convertBoardToFEN() {
    StringBuilder fen = new StringBuilder();

    for (int row = 7; row >= 0; row--) {
      int emptyCount = 0;
      for (int col = 0; col < 8; col++) {
        Piece piece = board.getPieceAtPosition(new Position(row, col));
        if (piece == null) {
          emptyCount++;
        } else {
          if (emptyCount > 0) {
            fen.append(emptyCount);
            emptyCount = 0;
          }
          fen.append(piece.getSymbol());
        }
      }
      if (emptyCount > 0) {
        fen.append(emptyCount);
      }
      if (row != 0) {
        fen.append("/");
      }
    }
    fen.append(" ");
    fen.append(nextToMove == Color.WHITE ? "w" : "b");
    // TODO: Castling rights
    // TODO: Possible en passant destinations
    // TODO: total number of moves
    return fen.toString();
  }

  private Piece createPieceFromFEN(char fenChar, Position position) {
    return switch (fenChar) {
      case 'P' -> new Pawn(Color.WHITE, position, board);
      case 'N' -> new Knight(Color.WHITE, position, board);
      case 'B' -> new Bishop(Color.WHITE, position, board);
      case 'R' -> new Rook(Color.WHITE, position, board);
      case 'Q' -> new Queen(Color.WHITE, position, board);
      case 'K' -> new King(Color.WHITE, position, board);
      case 'p' -> new Pawn(Color.BLACK, position, board);
      case 'n' -> new Knight(Color.BLACK, position, board);
      case 'b' -> new Bishop(Color.BLACK, position, board);
      case 'r' -> new Rook(Color.BLACK, position, board);
      case 'q' -> new Queen(Color.BLACK, position, board);
      case 'k' -> new King(Color.BLACK, position, board);
      default -> null;
    };
  }

  public ChessBoard convertFENToBoard(String fenNotation) throws FENException {
    ChessBoard newBoard = new ChessBoard();
    newBoard.clearChessboard();
    // Split the FEN notation into board layout and other parts
    String[] parts = fenNotation.split(" ");
    if (parts.length < 2) {
      throw new FENException(
          "Invalid FEN format: expected at least 2 parts (board layout and active color)");
    }

    String[] rows = parts[0].split("/");
    if (rows.length != 8) {
      throw new FENException("Invalid FEN format: 8 rows expected");
    }
    // Convert the board layout part
    for (int i = 0; i < 8; i++) {
      int col = 0;
      for (char c : rows[7 - i].toCharArray()) {
        if (Character.isDigit(c)) {
          col += Character.getNumericValue(c);
        } else {
          Piece piece = createPieceFromFEN(c, new Position(i, col));
          if (piece != null) {
            newBoard.setPieceAtPosition(new Position(i, col), piece);
          } else {
            throw new FENException("FEN notation contains invalid Piece");
          }
          col++;
        }
      }
    }

    // Get the active color part
    String activeColor = parts[1];
    if (activeColor.equals("w")) {
      nextToMove = Color.WHITE;
    } else {
      nextToMove = Color.BLACK;
    }

    // TODO: Castling rights
    // TODO: Possible en passant destinations
    // TODO: total number of moves
    return newBoard;
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
