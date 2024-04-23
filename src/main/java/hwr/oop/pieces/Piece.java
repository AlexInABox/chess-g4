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

    ArrayList<Position> possibleMoves = null;
    switch (type) {
      case KING   -> possibleMoves = possibleKingMoves();
      case BISHOP -> possibleMoves = possibleBishopMoves();
      case KNIGHT -> possibleMoves = possibleKnightMoves();
      case PAWN   -> possibleMoves = possiblePawnMoves();
      case QUEEN  -> possibleMoves = possibleQueenMoves();
      case ROOK   -> possibleMoves = possibleRookMoves();
    }
    if (possibleMoves.contains(target)){
      //TODO: Check if a piece was captured
      chessBoard.setPieceAtPosition(position, null);
      position = target;
      chessBoard.setPieceAtPosition(target, this);
    }
  }
  private ArrayList<Position> possibleKingMoves() {
    ArrayList<Position> possibleMoves = new ArrayList<>();
    int[] directions = {-1, 0, 1};

    for (int rowChange : directions) {
      for (int colChange : directions) {
        int newRow = position.row() + rowChange;
        int newCol = position.column() + colChange;

        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
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

        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
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
    int[][] moveOffsets = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    int currentRow = position.row();
    int currentCol = position.column();

    for (int[] offset : moveOffsets) {
      int newRow = currentRow + offset[0];
      int newCol = currentCol + offset[1];

      if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
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
    int rowDirection = (color == Color.WHITE) ? 1 : -1; // White pawns move up (incrementing row), black pawns move down (decrementing row)

    // Get current position
    int currentRow = position.row();
    int currentColumn = position.column();

    // Move one square forward
    int newRow = currentRow + rowDirection;
    if (isValidPosition(newRow, currentColumn) && chessBoard.getPieceAtPosition(new Position(newRow, currentColumn)) == null) {
      possibleMoves.add(new Position(newRow, currentColumn));

      // Check if pawn is in its initial position to possibly move two squares forward
      if ((color == Color.WHITE && currentRow == 1) || (color == Color.BLACK && currentRow == 6)) {
        int jumpRow = currentRow + 2 * rowDirection;
        if (chessBoard.getPieceAtPosition(new Position(jumpRow, currentColumn)) == null) {
          possibleMoves.add(new Position(jumpRow, currentColumn));
        }
      }
    }

    // Capturing moves: pawns capture diagonally
    int[] captureColumns = {currentColumn - 1, currentColumn + 1};
    for (int col : captureColumns) {
      if (isValidPosition(newRow, col)) {
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

        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
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

  private boolean isValidPosition(int row, int column) {
    return row >= 0 && row < 8 && column >= 0 && column < 8;
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