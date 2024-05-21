package hwr.oop.persistence;

import hwr.oop.match.Match;

import java.nio.file.Path;

public interface Persistence {
  void save(Match match, Path filePath);

  Match load(Path filePath);
}
