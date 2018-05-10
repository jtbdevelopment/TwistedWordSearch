package com.jtbdevelopment.TwistedWordSearch.state;

import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.mongo.state.AbstractMongoMultiPlayerGame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Date: 7/13/16 Time: 7:04 PM
 */
@Document(collection = "game")
public class TWSGame extends AbstractMongoMultiPlayerGame<GameFeature> {

  private Grid grid;
  private int numberOfWords;
  private int wordAverageLengthGoal;
  private int hintsRemaining;
  private int usableSquares;
  private Set<String> words;
  private Map<String, GridCoordinate> wordStarts = new HashMap<>();
  private Map<String, GridCoordinate> wordEnds = new HashMap<>();
  private Set<String> wordsToFind;
  private Map<String, GridCoordinate> hintsGiven = new HashMap<>();
  private Map<ObjectId, Integer> hintsTaken = new HashMap<>();
  private Map<ObjectId, Set<String>> wordsFoundByPlayer;
  private Map<String, Set<GridCoordinate>> foundWordLocations;
  private Map<ObjectId, Integer> scores;
  private List<ObjectId> winners;

  public Grid getGrid() {
    return grid;
  }

  public void setGrid(Grid grid) {
    this.grid = grid;
  }

  public int getNumberOfWords() {
    return numberOfWords;
  }

  public void setNumberOfWords(int numberOfWords) {
    this.numberOfWords = numberOfWords;
  }

  public int getWordAverageLengthGoal() {
    return wordAverageLengthGoal;
  }

  public void setWordAverageLengthGoal(int wordAverageLengthGoal) {
    this.wordAverageLengthGoal = wordAverageLengthGoal;
  }

  public int getHintsRemaining() {
    return hintsRemaining;
  }

  public void setHintsRemaining(int hintsRemaining) {
    this.hintsRemaining = hintsRemaining;
  }

  public int getUsableSquares() {
    return usableSquares;
  }

  public void setUsableSquares(int usableSquares) {
    this.usableSquares = usableSquares;
  }

  public Set<String> getWords() {
    return words;
  }

  public void setWords(Set<String> words) {
    this.words = words;
  }

  public Map<String, GridCoordinate> getWordStarts() {
    return wordStarts;
  }

  public void setWordStarts(Map<String, GridCoordinate> wordStarts) {
    this.wordStarts = wordStarts;
  }

  public Map<String, GridCoordinate> getWordEnds() {
    return wordEnds;
  }

  @SuppressWarnings("unused")
  public void setWordEnds(Map<String, GridCoordinate> wordEnds) {
    this.wordEnds = wordEnds;
  }

  public Set<String> getWordsToFind() {
    return wordsToFind;
  }

  public void setWordsToFind(Set<String> wordsToFind) {
    this.wordsToFind = wordsToFind;
  }

  public Map<String, GridCoordinate> getHintsGiven() {
    return hintsGiven;
  }

  public void setHintsGiven(Map<String, GridCoordinate> hintsGiven) {
    this.hintsGiven = hintsGiven;
  }

  public Map<ObjectId, Integer> getHintsTaken() {
    return hintsTaken;
  }

  public void setHintsTaken(Map<ObjectId, Integer> hintsTaken) {
    this.hintsTaken = hintsTaken;
  }

  public Map<ObjectId, Set<String>> getWordsFoundByPlayer() {
    return wordsFoundByPlayer;
  }

  public void setWordsFoundByPlayer(Map<ObjectId, Set<String>> wordsFoundByPlayer) {
    this.wordsFoundByPlayer = wordsFoundByPlayer;
  }

  public Map<String, Set<GridCoordinate>> getFoundWordLocations() {
    return foundWordLocations;
  }

  public void setFoundWordLocations(Map<String, Set<GridCoordinate>> foundWordLocations) {
    this.foundWordLocations = foundWordLocations;
  }

  public Map<ObjectId, Integer> getScores() {
    return scores;
  }

  public void setScores(Map<ObjectId, Integer> scores) {
    this.scores = scores;
  }

  public List<ObjectId> getWinners() {
    return winners;
  }

  public void setWinners(List<ObjectId> winners) {
    this.winners = winners;
  }
}
