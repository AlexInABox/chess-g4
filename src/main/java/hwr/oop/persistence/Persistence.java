package hwr.oop.persistence;

import java.nio.file.Path;

public interface Persistence {
  void write(Object object, Path filePath);

  Object read(Path filePath);
}
