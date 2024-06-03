package hwr.oop.chess.cli;

import hwr.oop.chess.GameLogic;
import hwr.oop.chess.persistence.FileBasePersistence;
import hwr.oop.chess.persistence.Persistence;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Main {
  private static final String ALL_MATCHES_PATH = "data/allMatches2.txt";
  private static final String ALL_PLAYERS_PATH = "data/allPlayers2.txt";
  static File fileMatches = new File(ALL_MATCHES_PATH);
  static File filePlayers = new File(ALL_PLAYERS_PATH);
  static Path pathMatches = fileMatches.toPath();
  static Path pathPlayers = filePlayers.toPath();
  static Persistence persistence = new FileBasePersistence(pathMatches, pathPlayers);

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {

    GameLogic gameLogic = new GameLogic(persistence);
    ChessCli cli = new ChessCli(System.out, gameLogic);
    cli.handle(List.of(args));
  }
}
