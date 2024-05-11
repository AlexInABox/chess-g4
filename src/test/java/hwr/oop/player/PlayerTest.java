package hwr.oop.player;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class PlayerTest {

  @Test
  void testOnlyNameConstructor() {
    String name = "Dummy";
    short expectedElo = 100;
    Player player = new Player(name);

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(player.getName()).isEqualTo("Dummy");
          softly.assertThat(player.getElo()).isEqualTo(expectedElo);
        });
  }

  @Test
  void testNameAndEloConstructor() throws PlayerException {
    String name = "Dummy";
    short elo = 123;
    Player player = new Player(name, elo);

    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(player.getName()).isEqualTo(name);
          softly.assertThat(player.getElo()).isEqualTo(elo);
        });
  }

  @Test
  void testSetElo() {
    String name = "Dummy";
    short initialElo = 123;
    short newElo = 100;
    short invalidElo = 99;
    Player player = new Player(name, initialElo);

    player.setElo(newElo);
    SoftAssertions.assertSoftly(
        softly -> {
          softly.assertThat(player.getName()).isEqualTo(name);
          softly.assertThat(player.getElo()).isEqualTo(newElo);
        });
    assertThrows(PlayerException.class, () -> player.setElo(invalidElo));
  }
}
