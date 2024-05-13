package hwr.oop.persistence;

import java.nio.file.Path;

public interface Persistence {
  void save(Object object, Path filePath);

  Object load(Path filePath);
}
