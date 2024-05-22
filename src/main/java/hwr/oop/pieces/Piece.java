package hwr.oop.pieces;

import hwr.oop.Color;
import hwr.oop.Position;

import java.util.List;

public interface Piece {
  Color getColor();

  Position getPosition();

  void setPosition(Position target);

  char getSymbol();

  PieceType getType();

  void moveTo(Position target) throws IllegalMoveException;

  List<Position> possibleMoves();
}
