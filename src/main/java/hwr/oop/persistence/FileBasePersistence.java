package hwr.oop.persistence;

import hwr.oop.match.Match;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class FileBasePersistence implements Persistence {

  @Override
  public void save(List<Match> match, Path filePath) {

    try (FileOutputStream f = new FileOutputStream(String.valueOf((filePath)))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(match);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public List<Match> load(Path filePath) {
    try (FileInputStream f = new FileInputStream(String.valueOf(filePath))) {
      ObjectInputStream o = new ObjectInputStream(f);
      return (List<Match>) o.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }
}
