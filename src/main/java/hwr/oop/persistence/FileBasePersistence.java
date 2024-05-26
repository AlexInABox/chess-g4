package hwr.oop.persistence;

import hwr.oop.match.Match;
import hwr.oop.player.Player;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBasePersistence implements Persistence {

  @Override
  public void saveMatches(List<Match> matches, Path filePath) {
    try (FileOutputStream f = new FileOutputStream(String.valueOf(filePath))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(matches);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Match> loadMatches(Path filePath) {
    try {
      if (!Files.exists(filePath) || Files.size(filePath) == 0) {
        return new ArrayList<>();
      }

      try (FileInputStream f = new FileInputStream(String.valueOf(filePath))) {
        ObjectInputStream o = new ObjectInputStream(f);
        List<Match> loadedMatches = (List<Match>) o.readObject();
        return loadedMatches != null ? loadedMatches : new ArrayList<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }

  @Override
  public void savePlayers(List<Player> players, Path filePath) {
    try (FileOutputStream f = new FileOutputStream(String.valueOf(filePath))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(players);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Player> loadPlayers(Path filePath) {
    try {
      if (!Files.exists(filePath) || Files.size(filePath) == 0) {
        return new ArrayList<>();
      }

      try (FileInputStream f = new FileInputStream(String.valueOf(filePath))) {
        ObjectInputStream o = new ObjectInputStream(f);
        List<Player> loadedPlayers = (List<Player>) o.readObject();
        return loadedPlayers != null ? loadedPlayers : new ArrayList<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }
}
