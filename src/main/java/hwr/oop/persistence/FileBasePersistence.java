package hwr.oop.persistence;

import java.io.*;
import java.nio.file.Path;

public class FileBasePersistence implements Persistence {

  @Override
  public void save(Object object, Path filePath) {

    try (FileOutputStream f = new FileOutputStream(String.valueOf((filePath)))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(object);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public Object load(Path filePath) {
    try (FileInputStream f = new FileInputStream(String.valueOf(filePath))) {
      ObjectInputStream o = new ObjectInputStream(f);
      return o.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }
}
