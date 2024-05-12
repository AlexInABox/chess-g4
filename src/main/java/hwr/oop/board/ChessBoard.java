package hwr.oop.board;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.pieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class ChessBoard implements Serializable {
  private final List<List<Piece>> board = new ArrayList<>();

  public ChessBoard() {
    setupEmptyBoard();
    setupPieces();
  }

  private void setupEmptyBoard() {
    IntStream.range(0, 8)
        .forEach(
            i -> {
              List<Piece> row = new ArrayList<>();
              IntStream.range(0, 8).forEach(j -> row.add(null));
              board.add(row);
            });
  }

  public ChessBoard(String fenNotation) throws ChessBoardException {
    setupEmptyBoard();
    convertFENToBoard(fenNotation);
  }

  public static Position convertInputToPosition(String input) throws ChessBoardException {
    if (input.length() != 2
        || !Character.isLetter(input.charAt(0))
        || !Character.isDigit(input.charAt(1))) {
      throw new ChessBoardException(
          "Invalid input format. Please provide a valid position (e.g., 'a1').");
    }
    int column = input.charAt(0) - 'a';
    int row = Character.getNumericValue(input.charAt(1)) - 1;

    if (column < 0 || column >= 8 || row < 0 || row >= 8) {
      throw new ChessBoardException("Invalid position. Position must be within the chessboard.");
    }

    return new Position(row, column);
  }

  private void setupPieces() {
    // Place Rooks
    board.get(0).set(0, new Piece(PieceType.ROOK, Color.WHITE, new Position(0, 0), this));
    board.get(0).set(7, new Piece(PieceType.ROOK, Color.WHITE, new Position(0, 7), this));
    board.get(7).set(0, new Piece(PieceType.ROOK, Color.BLACK, new Position(7, 0), this));
    board.get(7).set(7, new Piece(PieceType.ROOK, Color.BLACK, new Position(7, 7), this));
    // Place Knights
    board.get(0).set(1, new Piece(PieceType.KNIGHT, Color.WHITE, new Position(0, 1), this));
    board.get(0).set(6, new Piece(PieceType.KNIGHT, Color.WHITE, new Position(0, 6), this));
    board.get(7).set(1, new Piece(PieceType.KNIGHT, Color.BLACK, new Position(7, 1), this));
    board.get(7).set(6, new Piece(PieceType.KNIGHT, Color.BLACK, new Position(7, 6), this));
    // Place Bishops
    board.get(0).set(2, new Piece(PieceType.BISHOP, Color.WHITE, new Position(0, 2), this));
    board.get(0).set(5, new Piece(PieceType.BISHOP, Color.WHITE, new Position(0, 5), this));
    board.get(7).set(2, new Piece(PieceType.BISHOP, Color.BLACK, new Position(7, 2), this));
    board.get(7).set(5, new Piece(PieceType.BISHOP, Color.BLACK, new Position(7, 5), this));
    // Place Queens
    board.get(0).set(3, new Piece(PieceType.QUEEN, Color.WHITE, new Position(0, 3), this));
    board.get(7).set(3, new Piece(PieceType.QUEEN, Color.BLACK, new Position(7, 3), this));
    // Place Kings
    board.get(0).set(4, new Piece(PieceType.KING, Color.WHITE, new Position(0, 4), this));
    board.get(7).set(4, new Piece(PieceType.KING, Color.BLACK, new Position(7, 4), this));
    // Place Pawns
    for (int i = 0; i < 8; i++) {
      board.get(1).set(i, new Piece(PieceType.PAWN, Color.WHITE, new Position(1, i), this));
      board.get(6).set(i, new Piece(PieceType.PAWN, Color.BLACK, new Position(6, i), this));
    }
  }

  public Piece getPieceAtPosition(Position position) {
    return board.get(position.row()).get(position.column());
  }

  public void setPieceAtPosition(Position position, Piece piece) {
    board.get(position.row()).set(position.column(), piece);
  }

  public String convertBoardToFEN() {
    StringBuilder fen = new StringBuilder();

    for (int row = 7; row >= 0; row--) {
      int emptyCount = 0;
      for (int col = 0; col < 8; col++) {
        Piece piece = board.get(row).get(col);
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
    // TODO: Active Color
    // TODO: Castling rights
    // TODO: Possible en passant destinations
    // TODO: total number of moves
    return fen.toString();
  }

  private Piece createPieceFromFEN(char fenChar, Position position) {
    return switch (fenChar) {
      case 'P' -> new Piece(PieceType.PAWN, Color.WHITE, position, this);
      case 'N' -> new Piece(PieceType.KNIGHT, Color.WHITE, position, this);
      case 'B' -> new Piece(PieceType.BISHOP, Color.WHITE, position, this);
      case 'R' -> new Piece(PieceType.ROOK, Color.WHITE, position, this);
      case 'Q' -> new Piece(PieceType.QUEEN, Color.WHITE, position, this);
      case 'K' -> new Piece(PieceType.KING, Color.WHITE, position, this);
      case 'p' -> new Piece(PieceType.PAWN, Color.BLACK, position, this);
      case 'n' -> new Piece(PieceType.KNIGHT, Color.BLACK, position, this);
      case 'b' -> new Piece(PieceType.BISHOP, Color.BLACK, position, this);
      case 'r' -> new Piece(PieceType.ROOK, Color.BLACK, position, this);
      case 'q' -> new Piece(PieceType.QUEEN, Color.BLACK, position, this);
      case 'k' -> new Piece(PieceType.KING, Color.BLACK, position, this);
      default -> null;
    };
  }

  public void convertFENToBoard(String fenNotation) throws ChessBoardException {
    String[] rows = fenNotation.split("/");
    if (rows.length != 8) {
      throw new ChessBoardException("Invalid FEN format: 8 rows expected");
    }

    for (int i = 0; i < 8; i++) {
      int col = 0;
      for (char c : rows[7 - i].toCharArray()) {
        if (Character.isDigit(c)) {
          col += Character.getNumericValue(c);
        } else {
          Piece piece = createPieceFromFEN(c, new Position(i, col));
          if (piece != null) {
            setPieceAtPosition(new Position(i, col), piece);
          } else {
            throw new ChessBoardException("FEN notation contains invalid Piece");
          }
          col++;
        }
      }
    }
    // TODO: Active Color
    // TODO: Castling rights
    // TODO: Possible en passant destinations
    // TODO: total number of moves
  }

  public boolean isValidPosition(int row, int column) {
    return row >= 0 && row < 8 && column >= 0 && column < 8;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChessBoard that = (ChessBoard) o;
    return Objects.equals(board, that.board);
  }

  @Override
  public int hashCode() {
    return Objects.hash(board);
  }
}