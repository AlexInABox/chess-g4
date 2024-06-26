package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Queen implements Piece, Serializable {
  private final Color color;
  private final ChessBoard chessBoard;
  private final char symbol;
  private Position position;

  public Queen(Color color, Position position, ChessBoard chessBoard) {
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = color == Color.WHITE ? 'Q' : 'q';
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
    return PieceType.QUEEN;
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

    ChessBoard copiedBoard = chessBoard;
    Bishop dummyBishop = new Bishop(color, position, copiedBoard);
    Rook dummyRook = new Rook(color, position, copiedBoard);

    visiblePositions.addAll(dummyBishop.visiblePositions());
    visiblePositions.addAll(dummyRook.visiblePositions());

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
    Queen queen = (Queen) o;
    return symbol == queen.symbol
        && color == queen.color
        && Objects.equals(position, queen.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }

  @Override
  public String toString() {
    return "Queen{" + "color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }
}
