package hwr.oop.chess.persistence;

import hwr.oop.chess.game.Game;
import hwr.oop.chess.player.Player;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBasedPersistence implements Persistence {
  private final Path filePathGames;
  private final Path filePathPlayers;


  public FileBasedPersistence(Path filePathGames, Path filePathPlayers) {
    this.filePathGames = filePathGames;
    this.filePathPlayers = filePathPlayers;
  }
  @Override
  public void saveGames(List<Game> games) {
    try (FileOutputStream f = new FileOutputStream(String.valueOf(filePathGames))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(games);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Game> loadGames() {
    try {
      if (!Files.exists(filePathGames) || Files.size(filePathGames) == 0) {
        return new ArrayList<>();
      }

      try (FileInputStream f = new FileInputStream(String.valueOf(filePathGames))) {
        ObjectInputStream o = new ObjectInputStream(f);
        List<Game> loadedGames = (List<Game>) o.readObject();
        return loadedGames != null ? loadedGames : new ArrayList<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }

  @Override
  public void savePlayers(List<Player> players) {
    try (FileOutputStream f = new FileOutputStream(String.valueOf(filePathPlayers))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(players);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Player> loadPlayers() {
    try {
      if (!Files.exists(filePathPlayers) || Files.size(filePathPlayers) == 0) {
        return new ArrayList<>();
      }

      try (FileInputStream f = new FileInputStream(String.valueOf(filePathPlayers))) {
        ObjectInputStream o = new ObjectInputStream(f);
        List<Player> loadedPlayers = (List<Player>) o.readObject();
        return loadedPlayers != null ? loadedPlayers : new ArrayList<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }
}
