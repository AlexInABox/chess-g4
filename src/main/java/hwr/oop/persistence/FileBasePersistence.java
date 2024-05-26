package hwr.oop.persistence;

import hwr.oop.match.Match;
import hwr.oop.player.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBasePersistence implements Persistence {

  @Override
  public void saveMatches(List<Match> match, Path filePath) {

    try (FileOutputStream f = new FileOutputStream(String.valueOf((filePath)))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(match);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Match> loadMatches(Path filePath) {
    try {
      long fileSize = Files.size(filePath);
      if (fileSize == 0) {
        return Collections.emptyList();
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
  public void savePlayers(List<Player> match, Path filePath) {
    try (FileOutputStream f = new FileOutputStream(String.valueOf((filePath)))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(match);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Player> loadPlayers(Path filePath) {
    try {
      long fileSize = Files.size(filePath);
      if (fileSize == 0) {
        return Collections.emptyList();
      }

      try (FileInputStream f = new FileInputStream(String.valueOf(filePath))) {
        ObjectInputStream o = new ObjectInputStream(f);
        List<Player> loadedMatches = (List<Player>) o.readObject();
        return loadedMatches != null ? loadedMatches : new ArrayList<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }
}
