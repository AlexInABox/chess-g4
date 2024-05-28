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

  public void clearChessboard() {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        setPieceAtPosition(new Position(row, col), null);
      }
    }
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


  private void setupPieces() {
    // Place Rooks
    board.get(0).set(0, new Rook(Color.WHITE, new Position(0, 0), this));
    board.get(0).set(7, new Rook(Color.WHITE, new Position(0, 7), this));
    board.get(7).set(0, new Rook(Color.BLACK, new Position(7, 0), this));
    board.get(7).set(7, new Rook(Color.BLACK, new Position(7, 7), this));
    // Place Knights
    board.get(0).set(1, new Knight(Color.WHITE, new Position(0, 1), this));
    board.get(0).set(6, new Knight(Color.WHITE, new Position(0, 6), this));
    board.get(7).set(1, new Knight(Color.BLACK, new Position(7, 1), this));
    board.get(7).set(6, new Knight(Color.BLACK, new Position(7, 6), this));
    // Place Bishops
    board.get(0).set(2, new Bishop(Color.WHITE, new Position(0, 2), this));
    board.get(0).set(5, new Bishop(Color.WHITE, new Position(0, 5), this));
    board.get(7).set(2, new Bishop(Color.BLACK, new Position(7, 2), this));
    board.get(7).set(5, new Bishop(Color.BLACK, new Position(7, 5), this));
    // Place Queens
    board.get(0).set(3, new Queen(Color.WHITE, new Position(0, 3), this));
    board.get(7).set(3, new Queen(Color.BLACK, new Position(7, 3), this));
    // Place Kings
    board.get(0).set(4, new King(Color.WHITE, new Position(0, 4), this));
    board.get(7).set(4, new King(Color.BLACK, new Position(7, 4), this));
    // Place Pawns
    for (int i = 0; i < 8; i++) {
      board.get(1).set(i, new Pawn(Color.WHITE, new Position(1, i), this));
      board.get(6).set(i, new Pawn(Color.BLACK, new Position(6, i), this));
    }
  }

  public Piece getPieceAtPosition(Position position) {
    if (isValidPosition(position.row(), position.column())) {
      return board.get(position.row()).get(position.column());
    }
    return null;
  }

  public void setPieceAtPosition(Position position, Piece piece) {
    board.get(position.row()).set(position.column(), piece);
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
