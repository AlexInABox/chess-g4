package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    List<Position> visiblePositions = visiblePositions();

    for (Position visiblePosition : visiblePositions) {
      Piece pieceAtVisiblePosition = chessBoard.getPieceAtPosition(visiblePosition);
      if (pieceAtVisiblePosition == null && !wouldKingBeInCheckAfterMoveTo(visiblePosition)) {
        possibleMoves.add(visiblePosition);
        continue;
      }

      if (pieceAtVisiblePosition != null) {
        if (pieceAtVisiblePosition.getType() == PieceType.KING) continue;
        if (pieceAtVisiblePosition.getColor() == color) continue;

        if (!wouldKingBeInCheckAfterMoveTo(visiblePosition)) {
          possibleMoves.add(visiblePosition);
        }
      }
    }
    return possibleMoves;
  }

  public List<Position> visiblePositions() {
    List<Position> visiblePositions = new ArrayList<>();

    List<List<Integer>> moveOffsets =
        Arrays.asList(
            Arrays.asList(2, 1),
            Arrays.asList(2, -1),
            Arrays.asList(-2, 1),
            Arrays.asList(-2, -1),
            Arrays.asList(1, 2),
            Arrays.asList(1, -2),
            Arrays.asList(-1, 2),
            Arrays.asList(-1, -2));
    int currentRow = position.row();
    int currentCol = position.column();

    for (List<Integer> offset : moveOffsets) {
      int newRow = currentRow + offset.get(0);
      int newCol = currentCol + offset.get(1);

      if (chessBoard.isValidPosition(newRow, newCol)) {
        visiblePositions.add(new Position(newRow, newCol));
      }
    }
    return visiblePositions;
  }

  private boolean wouldKingBeInCheckAfterMoveTo(Position target) {
    Piece pieceAtTarget = chessBoard.getPieceAtPosition(target);

    chessBoard.setPieceAtPosition(position, null);
    chessBoard.setPieceAtPosition(target, this);

    boolean isKingInCheckNow = chessBoard.getKingOfColor(color).isInCheck();

    chessBoard.setPieceAtPosition(target, pieceAtTarget);
    chessBoard.setPieceAtPosition(position, this);

    return isKingInCheckNow;
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
