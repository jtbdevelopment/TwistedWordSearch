package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Date: 8/25/16 Time: 7:06 AM
 */
public class WordLayoutTest {

  @Test
  public void testPerRowLetterMovement() {
    Map<WordLayout, Integer> map = new LinkedHashMap<>();
    map.put(WordLayout.HorizontalForward, 0);
    map.put(WordLayout.HorizontalBackward, 0);
    map.put(WordLayout.VerticalDown, 1);
    map.put(WordLayout.VerticalUp, -1);
    map.put(WordLayout.SlopingDownForward, 1);
    map.put(WordLayout.SlopingDownBackward, -1);
    map.put(WordLayout.SlopingUpForward, -1);
    map.put(WordLayout.SlopingUpBackward, 1);
    assertEquals(map,
        Arrays.stream(WordLayout.values()).collect(Collectors.toMap(
            l -> l,
            WordLayout::getPerLetterRowMovement)));
  }

  @Test
  public void testPerColumnLetterMovement() {
    Map<WordLayout, Integer> map = new LinkedHashMap<>();
    map.put(WordLayout.HorizontalForward, 1);
    map.put(WordLayout.HorizontalBackward, -1);
    map.put(WordLayout.VerticalDown, 0);
    map.put(WordLayout.VerticalUp, 0);
    map.put(WordLayout.SlopingDownForward, 1);
    map.put(WordLayout.SlopingDownBackward, -1);
    map.put(WordLayout.SlopingUpForward, 1);
    map.put(WordLayout.SlopingUpBackward, -1);
    assertEquals(map,
        Arrays.stream(WordLayout.values()).collect(Collectors.toMap(
            l -> l,
            WordLayout::getPerLetterColumnMovement)));
  }

}
