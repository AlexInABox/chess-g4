package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Rook implements Piece, Serializable {
  private final Color color;
  private final ChessBoard chessBoard;
  private final char symbol;
  private Position position;

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
    List<Position> visiblePositions = visiblePositions();

    for (Position visiblePosition : visiblePositions) {
      Piece pieceAtVisiblePosition = chessBoard.getPieceAtPosition(visiblePosition);

      if (pieceAtVisiblePosition == null) {
        if (wouldKingBeNOTInCheckAfterMoveTo(visiblePosition)) {
          possibleMoves.add(visiblePosition);
        }
      } else if (pieceAtVisiblePosition.getType() != PieceType.KING
          && pieceAtVisiblePosition.getColor() != color
          && wouldKingBeNOTInCheckAfterMoveTo(visiblePosition)) {
          possibleMoves.add(visiblePosition);
        }

    }
    return possibleMoves;
  }

  public List<Position> visiblePositions() {
    List<Position> visiblePositions = new ArrayList<>();

    List<List<Integer>> directions = Arrays.asList(
            Arrays.asList(1, 0),
            Arrays.asList(-1, 0),
            Arrays.asList(0, 1),
            Arrays.asList(0, -1)
    );

    for (List<Integer> direction : directions) {
      int newRow = position.row();
      int newCol = position.column();

      boolean isPositionValid = true;
      while (isPositionValid) {
        newRow += direction.get(0);
        newCol += direction.get(1);

        if (!chessBoard.isValidPosition(newRow, newCol)) {
          isPositionValid = false;
        } else {
          Position newPosition = new Position(newRow, newCol);
          visiblePositions.add(newPosition);
          Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

          if (pieceAtNewPosition != null) {
            isPositionValid = false;
          }
        }
      }
    }

    return visiblePositions;
  }


  private boolean wouldKingBeNOTInCheckAfterMoveTo(Position target) {
    Piece pieceAtTarget = chessBoard.getPieceAtPosition(target);

    chessBoard.setPieceAtPosition(position, null);
    chessBoard.setPieceAtPosition(target, this);

    boolean isKingInCheckNow = chessBoard.getKingOfColor(color).isInCheck();

    chessBoard.setPieceAtPosition(target, pieceAtTarget);
    chessBoard.setPieceAtPosition(position, this);

    return !isKingInCheckNow;
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
    return "Rook{" + "color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }
}
