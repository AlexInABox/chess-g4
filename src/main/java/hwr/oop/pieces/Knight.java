package hwr.oop.pieces;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.board.ChessBoard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Knight implements Piece, Serializable {
  private final Color color;
  private final ChessBoard chessBoard;
  private final char symbol;
  private Position position;

  public Knight(Color color, Position position, ChessBoard chessBoard) {
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = color == Color.WHITE ? 'N' : 'n';
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
    return PieceType.KNIGHT;
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
    int[][] moveOffsets = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

    int currentRow = position.row();
    int currentCol = position.column();

    for (int[] offset : moveOffsets) {
      int newRow = currentRow + offset[0];
      int newCol = currentCol + offset[1];

      if (!chessBoard.isValidPosition(newRow, newCol)) {
        continue;
      }
      Position newPosition = new Position(newRow, newCol);
      Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

      if ((pieceAtNewPosition == null)
          || ((pieceAtNewPosition.getColor() != color)
              && (pieceAtNewPosition.getType() != PieceType.KING))) {
        possibleMoves.add(newPosition);
      }
    }
    return possibleMoves;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Knight knight = (Knight) o;
    return symbol == knight.symbol
        && color == knight.color
        && Objects.equals(position, knight.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }

  @Override
  public String toString() {
    return "Knight{" + "color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }
}
