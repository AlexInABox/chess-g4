package hwr.oop.persistence;

import hwr.oop.match.Match;

import java.nio.file.Path;
import java.util.List;

public interface Persistence {
  void save(List<Match> match, Path filePath);

  List<Match> load(Path filePath);
}
