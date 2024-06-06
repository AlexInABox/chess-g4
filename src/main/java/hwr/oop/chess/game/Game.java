package hwr.oop.chess.game;

import hwr.oop.chess.Color;
import hwr.oop.chess.GameOutcome;
import hwr.oop.chess.Position;
import hwr.oop.chess.board.ChessBoard;
import hwr.oop.chess.pieces.*;
import hwr.oop.chess.player.Player;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable {
  private final String id;
  private Player playerWhite;
  private Player playerBlack;

  private final ChessBoard board;
  private String fenNotation;

  private Color nextToMove = Color.WHITE;
  private short moveCount = 0;
  private boolean gameEnded = false;
  private GameOutcome winner;

  public GameOutcome getWinner() {
    return winner;
  }

  public short getMoveCount() {
    return moveCount;
  }

  public boolean isGameEnded() {
    return gameEnded;
  }

  public Game(Player playerWhite, Player playerBlack, String id) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.board = new ChessBoard();
    this.id = id;
    winner = GameOutcome.NOT_FINISHED_YET;
  }

  public Game(Player playerWhite, Player playerBlack, String fenNotation, String id)
      throws FENException {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.fenNotation = fenNotation;
    board = convertFENToBoard(fenNotation);
    this.id = id;
    winner = GameOutcome.NOT_FINISHED_YET;
  }

  public Player getPlayerWhite() {
    return playerWhite;
  }

  public Player getPlayerBlack() {
    return playerBlack;
  }

  public String getId() {
    return id;
  }

  public String getFEN() {
    return fenNotation;
  }

  public Color getNextToMove() {
    return nextToMove;
  }

  public void toggleNextToMove() {
    this.nextToMove = (this.nextToMove == Color.WHITE) ? Color.BLACK : Color.WHITE;
    moveCount++;
  }

  public void declareWinner(GameOutcome winner) {
    this.winner = winner;
    gameEnded = true;
  }

  public void updatePlayers(Player playerWhite, Player playerBlack) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
  }

  private StringBuilder buildFENPositionsFromBoard(){
    StringBuilder fen = new StringBuilder();
    for (int row = 7; row >= 0; row--) {
      int emptyCount = 0;
      for (int col = 0; col < 8; col++) {
        Piece piece = board.getPieceAtPosition(new Position(row, col));
        if (piece == null) {
          emptyCount++;
        } else {
          if (emptyCount > 0) {
            fen.append(emptyCount);
            emptyCount = 0;
          }
          fen.append(piece.getSymbol());
        }
      }
      if (emptyCount > 0) {
        fen.append(emptyCount);
      }
      if (row != 0) {
        fen.append("/");
      }
    }
    return fen;
  }
  public ChessBoard getBoard() {
    return board;
  }

  public String convertBoardToFEN() {
    StringBuilder fen = buildFENPositionsFromBoard();
    fen.append(" ");
    fen.append(nextToMove == Color.WHITE ? "w" : "b");
    fen.append(" ");
    fen.append(moveCount);
    return fen.toString();
  }

  private Piece createPieceFromFEN(char fenChar, Position position) {
    return switch (fenChar) {
      case 'P' -> new Pawn(Color.WHITE, position, board);
      case 'N' -> new Knight(Color.WHITE, position, board);
      case 'B' -> new Bishop(Color.WHITE, position, board);
      case 'R' -> new Rook(Color.WHITE, position, board);
      case 'Q' -> new Queen(Color.WHITE, position, board);
      case 'K' -> new King(Color.WHITE, position, board);
      case 'p' -> new Pawn(Color.BLACK, position, board);
      case 'n' -> new Knight(Color.BLACK, position, board);
      case 'b' -> new Bishop(Color.BLACK, position, board);
      case 'r' -> new Rook(Color.BLACK, position, board);
      case 'q' -> new Queen(Color.BLACK, position, board);
      case 'k' -> new King(Color.BLACK, position, board);
      default -> null;
    };
  }

  private String[] getPartsFromFEN(String fenNotation){
    String[] parts = fenNotation.split(" ");
    if (parts.length < 2) {
      throw new FENException(
              "Invalid FEN format: expected at least 2 parts (board layout and active color)");
    }
    return parts;
  }
  private String[] getRowsFromFEN(String[] parts){
    String[] rows = parts[0].split("/");
    if (rows.length != 8) {
      throw new FENException("Invalid FEN format: 8 rows expected");
    }
    return rows;
  }

  private void setActiveColorFromFEN(String activeColor){
    if (activeColor.equals("w")) {
      nextToMove = Color.WHITE;
    } else {
      nextToMove = Color.BLACK;
    }
  }
  public ChessBoard convertFENToBoard(String fenNotation) throws FENException {
    ChessBoard newBoard = new ChessBoard();
    newBoard.clearChessboard();
    String[] parts = getPartsFromFEN(fenNotation);
    String[] rows = getRowsFromFEN(parts);

    for (int i = 0; i < 8; i++) {
      int col = 0;
      for (char c : rows[7 - i].toCharArray()) {
        if (Character.isDigit(c)) {
          col += Character.getNumericValue(c);
        } else {
          Piece piece = createPieceFromFEN(c, new Position(i, col));
          if (piece != null) {
            newBoard.setPieceAtPosition(new Position(i, col), piece);
          } else {
            throw new FENException("FEN notation contains invalid Piece");
          }
          col++;
        }
      }
    }

    // Get the active color part
    String activeColor = parts[1];
    setActiveColorFromFEN(activeColor);
    if (parts.length >= 3) {
      try {
        moveCount = Short.parseShort(parts[2]);
      } catch (NumberFormatException e) {
        throw new FENException("Invalid FEN format: total number of moves is not a valid number");
      }
    }
    return newBoard;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Game game = (Game) o;
    return moveCount == game.moveCount
        && gameEnded == game.gameEnded
        && Objects.equals(id, game.id)
        && Objects.equals(playerWhite, game.playerWhite)
        && Objects.equals(playerBlack, game.playerBlack)
        && Objects.equals(board, game.board)
        && Objects.equals(fenNotation, game.fenNotation)
        && nextToMove == game.nextToMove;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, playerWhite, playerBlack, board, fenNotation, nextToMove, moveCount, gameEnded);
  }

  @Override
  public String toString() {
    return "Game{"
        + "id='"
        + id
        + '\''
        + ", playerWhite="
        + playerWhite
        + ", playerBlack="
        + playerBlack
        + ", board="
        + board
        + ", fenNotation='"
        + fenNotation
        + '\''
        + ", nextToMove="
        + nextToMove
        + ", moveCount="
        + moveCount
        + ", gameEnded="
        + gameEnded
        + '}';
  }
}
