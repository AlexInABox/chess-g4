package hwr.oop.pieces;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pawn implements Piece, Serializable {
  private final Color color;
  private Position position;
  private final ChessBoard chessBoard;
  private final char symbol;

  public Pawn(Color color, Position position, ChessBoard chessBoard) {
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = color == Color.WHITE ? 'P' : 'p';
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
    return PieceType.PAWN;
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
    int direction = color == Color.WHITE ? 1 : -1;
    int startRow = color == Color.WHITE ? 1 : 6;

    Position oneStepAhead = new Position(position.row() + direction, position.column());
    if (chessBoard.isValidPosition(oneStepAhead.row(), oneStepAhead.column()) &&
            chessBoard.getPieceAtPosition(oneStepAhead) == null) {
      possibleMoves.add(oneStepAhead);

      Position twoStepsAhead = new Position(position.row() + 2 * direction, position.column());
      if (position.row() == startRow &&
              chessBoard.getPieceAtPosition(twoStepsAhead) == null) {
        possibleMoves.add(twoStepsAhead);
      }
    }

    int[][] captureOffsets = {{direction, 1}, {direction, -1}};
    for (int[] offset : captureOffsets) {
      int newRow = position.row() + offset[0];
      int newCol = position.column() + offset[1];
      Position capturePosition = new Position(newRow, newCol);
      if (chessBoard.isValidPosition(newRow, newCol)) {
        Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(capturePosition);
        if (pieceAtNewPosition != null && pieceAtNewPosition.getColor() != color) {
          possibleMoves.add(capturePosition);
        }
      }
    }
    return possibleMoves;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pawn pawn = (Pawn) o;
    return symbol == pawn.symbol && color == pawn.color && Objects.equals(position, pawn.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }

  @Override
  public String toString() {
    return "Pawn{" +
            "color=" + color +
            ", position=" + position +
            ", symbol=" + symbol +
            '}';
  }
}
