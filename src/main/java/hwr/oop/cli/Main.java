package hwr.oop.cli;

import java.util.List;

public class Main {

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    ChessCli cli = new ChessCli(System.out);
    cli.handle(List.of(args));
  }
}
