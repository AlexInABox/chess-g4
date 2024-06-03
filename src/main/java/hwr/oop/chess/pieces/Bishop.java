package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bishop implements Piece, Serializable {
  private final Color color;
  private final ChessBoard chessBoard;
  private final char symbol;
  private Position position;

  public Bishop(Color color, Position position, ChessBoard chessBoard) {
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = color == Color.WHITE ? 'B' : 'b';
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
    return PieceType.BISHOP;
  }

  @Override
  public void moveTo(Position target) throws IllegalMoveException {
    List<Position> possibleMoves = possibleMoves();
    if (possibleMoves.contains(target) && !wouldKingBeInCheckAfterMoveTo(target)) {
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

    int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

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

        visiblePositions.add(newPosition);
        if (pieceAtNewPosition != null) {
          break;
        }
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
    Bishop bishop = (Bishop) o;
    return symbol == bishop.symbol
        && color == bishop.color
        && Objects.equals(position, bishop.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }

  @Override
  public String toString() {
    return "Bishop{" + "color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }
}
