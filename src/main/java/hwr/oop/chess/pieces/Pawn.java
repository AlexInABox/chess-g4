package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Pawn implements Piece, Serializable {
  private final Color color;
  private final ChessBoard chessBoard;
  private final char symbol;
  private Position position;

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
    if (chessBoard.isValidPosition(oneStepAhead.row(), oneStepAhead.column())
        && chessBoard.getPieceAtPosition(oneStepAhead) == null
        && !wouldKingBeInCheckAfterMoveTo(oneStepAhead)) {
      possibleMoves.add(oneStepAhead);

      Position twoStepsAhead;
      if (color == Color.WHITE) {
        twoStepsAhead = new Position(position.row() + 2, position.column());
      } else twoStepsAhead = new Position(position.row() - 2, position.column());

      if (position.row() == startRow
          && chessBoard.getPieceAtPosition(twoStepsAhead) == null
          && !wouldKingBeInCheckAfterMoveTo(twoStepsAhead)) {
        possibleMoves.add(twoStepsAhead);
      }
    }

    List<List<Integer>> captureOffsets = Arrays.asList(
            Arrays.asList(direction, 1),
            Arrays.asList(direction, -1)
    );    for (List<Integer> offset : captureOffsets) {
      int newRow = position.row() + offset.get(0);
      int newCol = position.column() + offset.get(1);
      Position capturePosition = new Position(newRow, newCol);
      if (chessBoard.isValidPosition(newRow, newCol)) {
        Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(capturePosition);
        if ((pieceAtNewPosition != null)
            && (pieceAtNewPosition.getColor() != color)
            && (pieceAtNewPosition.getType() != PieceType.KING)
            && !wouldKingBeInCheckAfterMoveTo(capturePosition)) {
          possibleMoves.add(capturePosition);
        }
      }
    }

    return possibleMoves;
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
    Pawn pawn = (Pawn) o;
    return symbol == pawn.symbol && color == pawn.color && Objects.equals(position, pawn.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }

  @Override
  public String toString() {
    return "Pawn{" + "color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }
}
