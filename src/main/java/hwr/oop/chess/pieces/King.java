package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class King implements Piece, Serializable {
  private static final PieceType type = PieceType.KING;
  private final Color color;
  private final char symbol;
  private final ChessBoard chessBoard;
  private Position position;

  public King(Color color, Position position, ChessBoard chessBoard) {
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = (color == Color.WHITE) ? 'K' : 'k';
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
    return type;
  }

  @Override
  public void moveTo(Position target) throws IllegalMoveException {
    if (isContestedPosition(target)) {
      throw new IllegalMoveException("Illegal move");
    }

    List<Position> possibleMoves = possibleMoves();
    if (!possibleMoves.contains(target)) {
      throw new IllegalMoveException("Illegal move");
    }
    setPosition(target);
  }

  private boolean isContestedPosition(Position target) {
    chessBoard.setPieceAtPosition(position, null);
    boolean isContested =
        (knightThreatensPosition(target)
            || pawnThreatensPosition(target)
            || rookThreatensPosition(target)
            || bishopThreatensPosition(target)
            || kingThreatensPosition(target));
    chessBoard.setPieceAtPosition(position, this);
    return isContested;
  }

  private boolean knightThreatensPosition(Position target) {
    Knight dummyKnight = new Knight(color, target, chessBoard);
    for (Position visiblePosition : dummyKnight.visiblePositions()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if (pieceAtPosition == null) continue;
      if ((pieceAtPosition.getColor() != color)
          && (pieceAtPosition.getType() == PieceType.KNIGHT)) {
        return true;
      }
    }
    return false;
  }

  private boolean pawnThreatensPosition(Position target) {
    int directionForPawnCheck = color == Color.WHITE ? 1 : -1;
    Position leftToCheck = new Position(target.row() + directionForPawnCheck, target.column() - 1);
    Position rightToCheck = new Position(target.row() + directionForPawnCheck, target.column() + 1);

    Piece pieceAtLeftPosition = chessBoard.getPieceAtPosition(leftToCheck);
    Piece pieceAtRightPosition = chessBoard.getPieceAtPosition(rightToCheck);

    if (pieceAtLeftPosition != null
        && pieceAtLeftPosition.getColor() != color
        && pieceAtLeftPosition.getType() == PieceType.PAWN) {
      return true;
    }
    return pieceAtRightPosition != null
        && pieceAtRightPosition.getColor() != color
        && pieceAtRightPosition.getType() == PieceType.PAWN;
  }

  private boolean rookThreatensPosition(Position target) {
    Rook dummyRook = new Rook(color, target, chessBoard);
    for (Position visiblePosition : dummyRook.visiblePositions()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if (pieceAtPosition == null) continue;
      if ((pieceAtPosition.getColor() != color)
          && (pieceAtPosition.getType() == PieceType.QUEEN
              || pieceAtPosition.getType() == PieceType.ROOK)) {
        return true;
      }
    }
    return false;
  }

  private boolean bishopThreatensPosition(Position target) {
    Bishop dummyBishop = new Bishop(color, target, chessBoard);
    for (Position visiblePosition : dummyBishop.visiblePositions()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if (pieceAtPosition == null) continue;
      if ((pieceAtPosition.getColor() != color)
          && (pieceAtPosition.getType() == PieceType.QUEEN
              || pieceAtPosition.getType() == PieceType.BISHOP)) {
        return true;
      }
    }
    return false;
  }

  private boolean kingThreatensPosition(Position target) {
    Position oldPosition = position;
    position = target;
    for (Position visiblePosition : visiblePositions()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if (pieceAtPosition == null) continue;
      if ((pieceAtPosition.getColor() != color) && (pieceAtPosition.getType() == PieceType.KING)) {
        position = oldPosition;
        return true;
      }
    }
    position = oldPosition;
    return false;
  }

  @Override
  public List<Position> possibleMoves() {
    List<Position> possibleMoves = new ArrayList<>();
    List<Position> visiblePositions = visiblePositions();

    for (Position visiblePosition : visiblePositions) {
      if (!isContestedPosition(visiblePosition)) {
        possibleMoves.add(visiblePosition);
      }
    }
    return possibleMoves;
  }

  public List<Position> visiblePositions() {
    List<Position> visiblePositions = new ArrayList<>();
    List<Integer> directions = Arrays.asList(-1, 0, 1);

    for (int rowChange : directions) {
      for (int colChange : directions) {
        int newRow = position.row() + rowChange;
        int newCol = position.column() + colChange;

        if (!chessBoard.isValidPosition(newRow, newCol)) {
          continue;
        }

        Position newPosition = new Position(newRow, newCol);
        Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

        if (pieceAtNewPosition == null || pieceAtNewPosition.getColor() != color) {
          visiblePositions.add(newPosition);
        }
      }
    }
    return visiblePositions;
  }

  public boolean isInCheck() {
    return (knightThreatensPosition(position)
        || pawnThreatensPosition(position)
        || rookThreatensPosition(position)
        || bishopThreatensPosition(position)
        || kingThreatensPosition(position));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    King king = (King) o;
    return symbol == king.symbol && color == king.color && position.equals(king.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, symbol, position);
  }

  @Override
  public String toString() {
    return "King{" + "color=" + color + ", symbol=" + symbol + ", position=" + position + '}';
  }
}
