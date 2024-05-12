package hwr.oop.pieces;

import hwr.oop.board.ChessBoard;
import hwr.oop.Color;
import hwr.oop.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Piece implements Serializable {
  private final Color color;
  private final char symbol;
  private final PieceType type;
  private final ChessBoard chessBoard;
  private Position position;

  public Piece(PieceType type, Color color, Position position, ChessBoard chessBoard) {
    this.type = type;
    this.color = color;
    this.position = position;
    this.chessBoard = chessBoard;
    this.symbol = assignPieceSymbol(type, color);
  }

  private char assignPieceSymbol(PieceType type, Color color) {
    char pieceSymbol = ' ';
    switch (type) {
      case KING -> {
        if (color == Color.WHITE) pieceSymbol = 'K';
        else pieceSymbol = 'k';
      }
      case BISHOP -> {
        if (color == Color.WHITE) pieceSymbol = 'B';
        else pieceSymbol = 'b';
      }
      case KNIGHT -> {
        if (color == Color.WHITE) pieceSymbol = 'N';
        else pieceSymbol = 'n';
      }
      case PAWN -> {
        if (color == Color.WHITE) pieceSymbol = 'P';
        else pieceSymbol = 'p';
      }
      case QUEEN -> {
        if (color == Color.WHITE) pieceSymbol = 'Q';
        else pieceSymbol = 'q';
      }
      case ROOK -> {
        if (color == Color.WHITE) pieceSymbol = 'R';
        else pieceSymbol = 'r';
      }
    }
    return pieceSymbol;
  }

  public Color getColor() {
    return color;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position target) {
    chessBoard.setPieceAtPosition(position, null);
    this.position = target;
    chessBoard.setPieceAtPosition(target, this);
  }

  public char getSymbol() {
    return symbol;
  }

  public PieceType getType() {
    return type;
  }

  public void moveTo(Position target) throws IllegalMoveException {

    ArrayList<Position> possibleMoves = new ArrayList<>();
    switch (type) {
      case KING -> possibleMoves = possibleKingMoves();
      case BISHOP -> possibleMoves = possibleBishopMoves();
      case KNIGHT -> possibleMoves = possibleKnightMoves();
      case PAWN -> possibleMoves = possiblePawnMoves();
      case QUEEN -> possibleMoves = possibleQueenMoves();
      case ROOK -> possibleMoves = possibleRookMoves();
    }
    if (possibleMoves.contains(target)) {
      // TODO: Check if a piece was captured
      setPosition(target);
    } else throw new IllegalMoveException("Illegal move");
  }

  private ArrayList<Position> possibleKingMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
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

  private ArrayList<Position> possibleBishopMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
    int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    for (int[] direction : directions) {
      int newRow = position.row();
      int newCol = position.column();

      while (true) {
        newRow += direction[0];
        newCol += direction[1];

        if (!chessBoard.isValidPosition(newRow, newCol)) {
          break;
        }

        Position newPosition = new Position(newRow, newCol);
        Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

        if (pieceAtNewPosition == null || pieceAtNewPosition.getColor() != color) {
          possibleMoves.add(newPosition);
        } else break;
      }
    }
    return possibleMoves;
  }

  private ArrayList<Position> possibleKnightMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
    int[][] moveOffsets = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

    int currentRow = position.row();
    int currentCol = position.column();

    for (int[] offset : moveOffsets) {
      int newRow = currentRow + offset[0];
      int newCol = currentCol + offset[1];

      if (!chessBoard.isValidPosition(newRow, newCol)) {
        continue;
      }
      Position newPosition = new Position(newRow, newCol);
      Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

      if (pieceAtNewPosition == null || pieceAtNewPosition.getColor() != color) {
        possibleMoves.add(newPosition);
      }
    }
    return possibleMoves;
  }

  private ArrayList<Position> possiblePawnMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
    int rowDirection = (color == Color.WHITE) ? 1 : -1;

    int currentRow = position.row();
    int currentColumn = position.column();

    int newRow = currentRow + rowDirection;
    if (chessBoard.isValidPosition(newRow, currentColumn)
        && chessBoard.getPieceAtPosition(new Position(newRow, currentColumn)) == null) {
      possibleMoves.add(new Position(newRow, currentColumn));

      if ((color == Color.WHITE && currentRow == 1) || (color == Color.BLACK && currentRow == 6)) {
        int jumpRow = currentRow + 2 * rowDirection;
        if (chessBoard.getPieceAtPosition(new Position(jumpRow, currentColumn)) == null) {
          possibleMoves.add(new Position(jumpRow, currentColumn));
        }
      }
    }

    int[] captureColumns = {currentColumn - 1, currentColumn + 1};
    for (int col : captureColumns) {
      if (chessBoard.isValidPosition(newRow, col)) {
        Piece capturePiece = chessBoard.getPieceAtPosition(new Position(newRow, col));
        if (capturePiece != null && capturePiece.getColor() != color) {
          possibleMoves.add(new Position(newRow, col));
        }
      }
    }

    return possibleMoves;
  }

  private ArrayList<Position> possibleRookMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    for (int[] direction : directions) {
      int newRow = position.row();
      int newCol = position.column();

      while (true) {
        newRow += direction[0];
        newCol += direction[1];

        if (!chessBoard.isValidPosition(newRow, newCol)) {
          break;
        }

        Position newPosition = new Position(newRow, newCol);
        Piece pieceAtNewPosition = chessBoard.getPieceAtPosition(newPosition);

        if (pieceAtNewPosition == null || pieceAtNewPosition.getColor() != color) {
          possibleMoves.add(newPosition);
        } else break;
      }
    }
    return possibleMoves;
  }

  private ArrayList<Position> possibleQueenMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();

    possibleMoves.addAll(possibleRookMoves());
    possibleMoves.addAll(possibleBishopMoves());

    return possibleMoves;
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

  @Override
  public String toString() {
    return "Piece{"
        + "color="
        + color
        + ", symbol="
        + symbol
        + ", type="
        + type
        + ", position="
        + position
        + '}';
  }
}
