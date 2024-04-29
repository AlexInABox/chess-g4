package hwr.oop.persistence;

import java.io.*;

public class PersistenceImpl implements Persistence {

  @Override
  public void write(Object object, String filePath) {

    try (FileOutputStream f = new FileOutputStream((filePath))) {
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(object);
    } catch (IOException e) {
      throw new PersistenceException("Cannot write.");
    }
  }

  @Override
  public Object read(String filePath) {
    try (FileInputStream f = new FileInputStream(filePath)) {
      ObjectInputStream o = new ObjectInputStream(f);
      return o.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new PersistenceException("Cannot read.");
    }
  }
}
