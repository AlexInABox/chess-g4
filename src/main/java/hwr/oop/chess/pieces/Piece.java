package hwr.oop.chess.pieces;

import hwr.oop.chess.Color;
import hwr.oop.chess.Position;

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
