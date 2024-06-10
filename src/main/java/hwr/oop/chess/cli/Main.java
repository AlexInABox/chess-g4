package hwr.oop.chess.cli;

import hwr.oop.chess.GameLogic;
import hwr.oop.chess.persistence.FileBasedPersistence;
import hwr.oop.chess.persistence.Persistence;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Main {
  private static final String ALL_GAMES_PATH = "data/allGames.txt";
  private static final String ALL_PLAYERS_PATH = "data/allPlayers.txt";
  static File fileGames = new File(ALL_GAMES_PATH);
  static File filePlayers = new File(ALL_PLAYERS_PATH);
  static Path pathGames = fileGames.toPath();
  static Path pathPlayers = filePlayers.toPath();
  static Persistence persistence = new FileBasedPersistence(pathGames, pathPlayers);

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    GameLogic gameLogic = new GameLogic(persistence);
    ChessCli cli = new ChessCli(System.out, gameLogic);
    cli.handle(List.of(args));
  }
}
