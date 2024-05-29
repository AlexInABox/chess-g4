package hwr.oop.persistence;

import hwr.oop.match.Match;
import hwr.oop.player.Player;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBasePersistence implements Persistence {
  private final Path filePathMatches;
  private final Path filePathPlayers;


  public FileBasePersistence(Path filePathMatches, Path filePathPlayers) {
    this.filePathMatches = filePathMatches;
    this.filePathPlayers = filePathPlayers;
  }
  @Override
  public void saveMatches(List<Match> matches) {
    try (FileOutputStream f = new FileOutputStream(String.valueOf(filePathMatches))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(matches);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Match> loadMatches() {
    try {
      if (!Files.exists(filePathMatches) || Files.size(filePathMatches) == 0) {
        return new ArrayList<>();
      }

      try (FileInputStream f = new FileInputStream(String.valueOf(filePathMatches))) {
        ObjectInputStream o = new ObjectInputStream(f);
        List<Match> loadedMatches = (List<Match>) o.readObject();
        return loadedMatches != null ? loadedMatches : new ArrayList<>();
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
