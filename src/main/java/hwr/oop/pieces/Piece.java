package hwr.oop.pieces;

import hwr.oop.Color;
import hwr.oop.Position;

import java.util.Objects;

public class Piece {
  private final Color color;
  private Position position;
  private final char symbol;
  private final PieceType type;

  public Piece(PieceType type, Color color, Position position){
    this.type = type;
    this.color = color;
    this.position = position;
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
    return 0;
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

  public PieceType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Piece{type=" + type + ", color=" + color + ", position=" + position + ", symbol=" + symbol + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Piece piece = (Piece) o;
    return type == piece.type
        && symbol == piece.symbol
        && color == piece.color
        && Objects.equals(position, piece.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, color, position, symbol);
  }
}