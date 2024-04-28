package hwr.oop.persistence;

public interface Persistence {
  void write(Object object, String filePath);

  Object read(String filePath);
}
