package hwr.oop.pieces;

import hwr.oop.board.ChessBoard;
import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.pieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class King implements Piece, Serializable {
  private final Color color;
  private final char symbol;
  private final PieceType type = PieceType.KING;
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
    if (!isNonContestedPosition(target)) {
      throw new IllegalMoveException("Illegal move");
    }

    List<Position> possibleMoves = possibleMoves();
    if (!possibleMoves.contains(target)) {
      throw new IllegalMoveException("Illegal move");
    }
    setPosition(target);
  }

  private boolean isNonContestedPosition(Position target) {
    // Check for enemy knights
    Knight dummyKnight = new Knight(color, target, chessBoard);
    for (Position visiblePosition : dummyKnight.possibleMoves()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if ((pieceAtPosition.getColor() != color)
          && (pieceAtPosition.getType() == PieceType.KNIGHT)) {
        return false;
      }
    }

    // Check for enemy Pawns
    Pawn dummyPawn = new Pawn(color, target, chessBoard);
    for (Position visiblePosition : dummyPawn.possibleMoves()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if ((pieceAtPosition.getColor() != color) && (pieceAtPosition.getType() == PieceType.PAWN)) {
        return false;
      }
      // TODO: Don't respect double moving pawns. They dont threaten the target position!!
    }

    // Check for enemy Queens, Rooks, Bishops and King
    Queen dummyQueen = new Queen(color, target, chessBoard);
    for (Position visiblePosition : dummyQueen.possibleMoves()) {
      Piece pieceAtPosition = chessBoard.getPieceAtPosition(visiblePosition);
      if ((pieceAtPosition.getColor() != color)
          && (pieceAtPosition.getType() == PieceType.QUEEN
              || pieceAtPosition.getType() == PieceType.ROOK
              || pieceAtPosition.getType() == PieceType.BISHOP
              || pieceAtPosition.getType() == PieceType.KING)) {
        return false;
      }
    }
    //TODO: Manually check if any of the sourrounding positons has an enemy king on them, since the king cannot be captured and therefore is not included in any of the prior visiblePositions.

    return true;
  }

  @Override
  public List<Position> possibleMoves() {
    List<Position> possibleMoves = new ArrayList<>();
    int[] directions = {-1, 0, 1};

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
          possibleMoves.add(newPosition);
        }
      }
    }
    return possibleMoves;
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
