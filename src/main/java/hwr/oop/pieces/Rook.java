package hwr.oop.pieces;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rook implements Piece, Serializable {
  private final Color color;
  private Position position;
  private final ChessBoard chessBoard;
  private final char symbol;

  public Rook(Color color, Position position, ChessBoard chessBoard) {
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = color == Color.WHITE ? 'R' : 'r';
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public void setPosition(Position target) {
    chessBoard.setPieceAtPosition(position, null);
    this.position = target;
    chessBoard.setPieceAtPosition(target, this);
  }

  @Override
  public char getSymbol() {
    return symbol;
  }

  @Override
  public PieceType getType() {
    return PieceType.ROOK;
  }

  @Override
  public void moveTo(Position target) throws IllegalMoveException {
    List<Position> possibleMoves = possibleMoves();
    if (possibleMoves.contains(target)) {
      setPosition(target);
    } else {
      throw new IllegalMoveException("Illegal move");
    }
  }

  @Override
  public List<Position> possibleMoves() {
    List<Position> possibleMoves = new ArrayList<>();
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    for (int[] direction : directions) {
      int newRow = position.row();
      int newCol = position.column();

      while (true) {
        newRow += direction[0];
        newCol += direction[1];

        if (!chessBoard.isValidPosition(newRow, newCol)) {
          break;
        }

        Position newPosition = new Position(newRow, newCol);
        Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

        if (pieceAtNewPosition == null || pieceAtNewPosition.getColor() != color) {
          possibleMoves.add(newPosition);
        } else {
          break;
        }
      }
    }
    return possibleMoves;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rook rook = (Rook) o;
    return symbol == rook.symbol && color == rook.color && Objects.equals(position, rook.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }

  @Override
  public String toString() {
    return "Rook{" +
            "color=" + color +
            ", position=" + position +
            ", symbol=" + symbol +
            '}';
  }
}
