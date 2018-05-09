package com.jtbdevelopment.TwistedWordSearch.state.masking;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.state.masking.AbstractMaskedMultiPlayerGame;
import java.util.Map;
import java.util.Set;

/**
 * Date: 7/13/16 Time: 7:06 PM
 */
public class MaskedGame extends AbstractMaskedMultiPlayerGame<GameFeature> {

  private char[][] grid;
  private Set<String> wordsToFind;
  private int hintsRemaining;
  private Set<GridCoordinate> hints;
  private Map<String, Integer> hintsTaken;
  private Map<String, Set<String>> wordsFoundByPlayer;
  private Map<String, Set<GridCoordinate>> foundWordLocations;
  private Map<String, Integer> scores;

  public char[][] getGrid() {
    return grid;
  }

  public void setGrid(char[][] grid) {
    this.grid = grid;
  }

  public Set<String> getWordsToFind() {
    return wordsToFind;
  }

  public void setWordsToFind(Set<String> wordsToFind) {
    this.wordsToFind = wordsToFind;
  }

  public int getHintsRemaining() {
    return hintsRemaining;
  }

  public void setHintsRemaining(int hintsRemaining) {
    this.hintsRemaining = hintsRemaining;
  }

  public Set<GridCoordinate> getHints() {
    return hints;
  }

  public void setHints(Set<GridCoordinate> hints) {
    this.hints = hints;
  }

  public Map<String, Integer> getHintsTaken() {
    return hintsTaken;
  }

  public void setHintsTaken(Map<String, Integer> hintsTaken) {
    this.hintsTaken = hintsTaken;
  }

  public Map<String, Set<String>> getWordsFoundByPlayer() {
    return wordsFoundByPlayer;
  }

  public void setWordsFoundByPlayer(Map<String, Set<String>> wordsFoundByPlayer) {
    this.wordsFoundByPlayer = wordsFoundByPlayer;
  }

  public Map<String, Set<GridCoordinate>> getFoundWordLocations() {
    return foundWordLocations;
  }

  public void setFoundWordLocations(Map<String, Set<GridCoordinate>> foundWordLocations) {
    this.foundWordLocations = foundWordLocations;
  }

  public Map<String, Integer> getScores() {
    return scores;
  }

  public void setScores(Map<String, Integer> scores) {
    this.scores = scores;
  }
}
