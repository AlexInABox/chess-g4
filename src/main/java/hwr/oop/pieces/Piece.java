package hwr.oop.pieces;

import hwr.oop.Color;
import hwr.oop.Position;
import hwr.oop.ChessBoard;

import java.util.ArrayList;
import java.util.Objects;

public class Piece {
  private final Color color;
  private Position position;
  private final char symbol;
  private final PieceType type;
  private final ChessBoard chessBoard;

  public Piece(PieceType type, Color color, Position position, ChessBoard chessBoard){
    this.type = type;
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = assignPieceSymbol(type, color);
  }

  private char assignPieceSymbol(PieceType type, Color color){
    switch (type) {
      case KING -> {
        if (color == Color.WHITE) return 'K';
        return 'k';
      }
      case BISHOP -> {
        if (color == Color.WHITE) return 'B';
        return 'b';
      }
      case KNIGHT -> {
        if (color == Color.WHITE) return 'N';
        return 'n';
      }
      case PAWN -> {
        if (color == Color.WHITE) return 'P';
        return 'p';
      }
      case QUEEN -> {
        if (color == Color.WHITE) return 'Q';
        return 'q';
      }
      case ROOK -> {
        if (color == Color.WHITE) return 'R';
        return 'r';
      }
    }
    return 'X';
  }
  public Color getColor() {
    return color;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public char getSymbol() {
    return symbol;
  }

  public void moveTo(Position target) {
    switch (type) {
      case KING -> {
        moveKingTo(target);
      }
      case BISHOP -> {
        //moveBishopTo(target);
      }
      case KNIGHT -> {
        //moveKnightTo(target);
      }
      case PAWN -> {
        //movePawnTo(target);
      }
      case QUEEN -> {
        //moveQueenTo(target);
      }
      case ROOK -> {
        //moveRookTo(target);
      }
    }
  }

  private void moveKingTo(Position target) {
    ArrayList<Position> possibleMoves = possibleKingMoves();

    if (possibleMoves.contains(target)){
      chessBoard.setPieceAtPosition(position, null);
      position = target;
      chessBoard.setPieceAtPosition(target, this);
    }
  }
  private ArrayList<Position> possibleKingMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if ((chessBoard.getPieceAtPosition(new Position(j + position.row(), i + position.column())).getColor() != Color.WHITE) && (chessBoard.getPieceAtPosition(new Position(j + position.row(), i + position.column())) != null)){
          possibleMoves.add(new Position(j + position.row(), i + position.column()));
        }
      }
    }
    return possibleMoves;
  }






  @Override
  public String toString() {
    return "Piece{" + "color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Piece piece = (Piece) o;
    return symbol == piece.symbol
        && color == piece.color
        && Objects.equals(position, piece.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, position, symbol);
  }
}