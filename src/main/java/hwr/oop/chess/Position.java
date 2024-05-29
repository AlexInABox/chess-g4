package hwr.oop.chess;

import java.io.Serializable;

public record Position(int row, int column) implements Serializable {}
