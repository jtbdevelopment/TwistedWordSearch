package com.jtbdevelopment.TwistedWordSearch.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Transient;

/**
 * Date: 7/11/16 Time: 6:57 PM
 */
public enum GameFeature {
  Grid(1, GameFeatureGroupType.Difficulty, "Grid", "Type of grid to play on."),
  Grid20X20(2, "Square x20", "20 x 20 square grid.", Grid),
  Grid30X30(3, "Square x30", "30 x 30 square grid.", Grid),
  Grid40X40(4, "Square x40", "40 x 40 square grid.", Grid),
  Grid50X50(5, "Square x50", "50 x 50 square grid.", Grid),
  CircleX31(7, "Circle x31", "31 letter diameter circle.", Grid),
  CircleX41(8, "Circle x41", "41 letter diameter circle.", Grid),
  CircleX51(9, "Circle x51", "51 letter diameter circle.", Grid),
  PyramidX40(10, "Pyramid x40", "Pyramid with 40 letter wide base.", Grid),
  PyramidX50(11, "Pyramid x50", "Pyramid with 50 letter wide base.", Grid),
  Diamond30X30(12, "Diamond x30", "30x30 diamond.", Grid),
  Diamond40X40(13, "Diamond x40", "40x40 diamond.", Grid),
  Diamond50X50(14, "Diamond x50", "50x50 diamond.", Grid),
  //
  WordDifficulty(2, GameFeatureGroupType.Difficulty, "Word Difficulty", "What kind of words?"),
  SimpleWords(1, "Simple", "Simpler words.", WordDifficulty),
  StandardWords(2, "Standard", "Standard words.", WordDifficulty),
  HardWords(3, "Hard", "Time to get the dictionary.", WordDifficulty),
  //
  WordSpotting(3, GameFeatureGroupType.Difficulty, "Number & Length of Words",
      "How hard to find words?"),
  EasiestDifficulty(1, "Easiest", "Fewer, longer words.", WordSpotting),
  StandardDifficulty(2, "Standard", "More words that are a little shorter than easiest.",
      WordSpotting),
  HarderDifficulty(3, "Harder", "Words get ever shorter while there are more to find.",
      WordSpotting),
  FiendishDifficulty(4, "Fiendish", "Many, many short words.", WordSpotting),
  //
  WordWrap(4, GameFeatureGroupType.Difficulty, "Word Wrap", "Words can wrap around edges."),
  WordWrapNo(1, "No", "Prevents words from wrapping around edges.", WordWrap),
  WordWrapYes(2, "Yes", "Allows words to wrap around edges.", WordWrap),
  //
  FillDifficulty(5, GameFeatureGroupType.Difficulty, "Fill Difficulty",
      "How random are fill letters vs words?"),
  RandomFill(1, "Random", "Fill letters are random", FillDifficulty),
  SomeOverlap(2, "Less random",
      "Fill letters will use word letters some what more often than randomly", FillDifficulty),
  StrongOverlap(3, "Word Letters", "Fill letters will fill mostly with letters from words",
      FillDifficulty),
  WordChunks(4, "Word Chunks", "Fill with chunks of words.", FillDifficulty);

  /*
JumbleOnFind(3, GameFeatureGroupType.Difficulty, 'Jumble', 'Finding a word causes the puzzle to re-jumble remaining letters.'),
JumbleOnFindNo(1, 'No', 'Puzzle not rearranged after finding a word.', JumbleOnFind),
JumbleOnFindYes(2, 'Yes', 'Puzzle is re-jumbled after each word find.', JumbleOnFind),

HideWordLetters(6, GameFeatureGroupType.Difficulty, 'Partial words.', 'Hide some of the letters in the words to find.'),
HideWordLettersNone(1, 'None', 'Words to find are shown completely.', HideWordLetters),
HideWordLettersSome(2, 'Some', 'Words to find show more than 75% of their letters.', HideWordLetters),
HideWordLettersMany(3, 'Many', 'Words to find show less than 75% of their letters.', HideWordLetters),
*/

  private static final Map<GameFeature, List<GameFeature>> groupedFeatures = new LinkedHashMap<>();

  static {
    Arrays.stream(values()).forEach(value -> {
      if (value.equals(value.getGroup())) {
        groupedFeatures.putIfAbsent(value.group, new ArrayList<>());
      } else {
        groupedFeatures.get(value.group).add(value);
      }
    });

    groupedFeatures.values()
        .forEach(options -> options.sort(Comparator.comparingInt(a -> a.order)));
  }

  @Transient
  private final GameFeatureGroupType groupType;
  @Transient
  private final GameFeature group;
  @Transient
  private final String label;
  @Transient
  private final String description;
  @Transient
  private final int order;

  GameFeature(final int order, final GameFeatureGroupType groupType, final String label,
      final String description) {
    this.order = order;
    this.label = label;
    this.description = description;
    this.group = this;
    this.groupType = groupType;
  }

  GameFeature(final int order, final String label, final String description,
      final GameFeature group) {
    this.order = order;
    this.description = description;
    this.group = group;
    this.label = label;
    this.groupType = group.groupType;
  }

  public static Map<GameFeature, List<GameFeature>> getGroupedFeatures() {
    return groupedFeatures;
  }

  @Transient
  @java.beans.Transient
  public final GameFeatureGroupType getGroupType() {
    return groupType;
  }

  @Transient
  @java.beans.Transient
  public final GameFeature getGroup() {
    return group;
  }

  @Transient
  @java.beans.Transient
  public final String getLabel() {
    return label;
  }

  @Transient
  @java.beans.Transient
  public final String getDescription() {
    return description;
  }

  @Transient
  @java.beans.Transient
  public final int getOrder() {
    return order;
  }
}
