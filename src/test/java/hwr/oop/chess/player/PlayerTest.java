package hwr.oop.chess.player;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

  @Test
  void testOnlyNameConstructor() {
    String name = "Dummy";
    short expectedElo = 1200;
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

  @Test
  void testEquals_IdenticalInstances() throws PlayerException {
    Player player1 = new Player("White", (short) 1200);
    Player player2 = new Player("White", (short) 1200);
    assertThat(player1.equals(player2)).isTrue();
  }

  @SuppressWarnings("EqualsWithItself")
  @Test
  void testEquals_SameInstance() throws PlayerException {
    Player player = new Player("White", (short) 1200);
    assertThat(player.equals(player)).isTrue();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void testEquals_InstanceNull() throws PlayerException {
    Player player = new Player("White", (short) 1200);
    assertThat(player.equals(null)).isFalse();
  }

  @SuppressWarnings("EqualsBetweenInconvertibleTypes")
  @Test
  void testEquals_DifferentClass() throws PlayerException {
    Player player = new Player("White", (short) 1200);
    String differentClassInstance = "Not a Player";
    assertThat(player.equals(differentClassInstance)).isFalse();
  }

  @Test
  void testHashCode_IdenticalHashCode() throws PlayerException {
    Player player1 = new Player("White", (short) 1200);
    Player player2 = new Player("White", (short) 1200);
    assertThat(player1.hashCode()).isEqualTo(player2.hashCode());
  }

  @Test
  void testHashCode_DifferentHashCode() throws PlayerException {
    Player player1 = new Player("White", (short) 1200);
    Player player2 = new Player("Black", (short) 1200);
    assertThat(player1.hashCode()).isNotEqualTo(player2.hashCode());
  }

  @Test
  void testToString() throws PlayerException {
    Player player = new Player("White", (short) 1200);
    String expectedString = "Player{name='White', elo=1200}";
    assertThat(player.toString()).isEqualTo(expectedString);
  }
}
