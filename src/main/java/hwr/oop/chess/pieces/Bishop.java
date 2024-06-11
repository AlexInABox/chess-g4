package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    if (possibleMoves.contains(target) && wouldKingBeNOTInCheckAfterMoveTo(target)) {
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
      } else {
        if (pieceAtVisiblePosition.getType() != PieceType.KING
                && pieceAtVisiblePosition.getColor() != color
                && wouldKingBeNOTInCheckAfterMoveTo(visiblePosition)) {
          possibleMoves.add(visiblePosition);
        }
      }
    }
    return possibleMoves;
  }


  public List<Position> visiblePositions() {
    List<Position> visiblePositions = new ArrayList<>();

    List<List<Integer>> directions = Arrays.asList(
            Arrays.asList(1, 1),
            Arrays.asList(1, -1),
            Arrays.asList(-1, 1),
            Arrays.asList(-1, -1)
    );

    for (List<Integer> direction : directions) {
      int newRow = position.row();
      int newCol = position.column();

      boolean valid = true;
      while (valid) {
        newRow += direction.get(0);
        newCol += direction.get(1);

        valid = chessBoard.isValidPosition(newRow, newCol);
        if (valid) {
          Position newPosition = new Position(newRow, newCol);
          visiblePositions.add(newPosition);
          Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

          if (pieceAtNewPosition != null) {
            valid = false; // Exit the loop as there's a piece in the way
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
