package com.jtbdevelopment.TwistedWordSearch.rest.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo.Detail;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Date: 7/18/16 Time: 4:26 PM
 */
public class GameFeatureInfoTest {

  private GameFeatureInfo test = new GameFeatureInfo(
      GameFeature.Grid,
      Arrays.asList(new Detail(GameFeature.Grid40X40), new Detail(GameFeature.SomeOverlap)));

  @Test
  public void testEquals() {
    assertEquals(GameFeature.Grid.hashCode(), test.hashCode());
  }

  @Test
  public void testHashCode() {
    assertFalse(new GameFeatureInfo(GameFeature.Grid, new ArrayList<>()).equals(test));
    assertFalse(
        new GameFeatureInfo(GameFeature.FillDifficulty, new ArrayList<>()).equals(test));
    assertEquals(
        new GameFeatureInfo(GameFeature.Grid,
            Arrays.asList(new Detail(GameFeature.Grid40X40), new Detail(GameFeature.SomeOverlap))
        ),
        test);
  }

  @Test
  public void testGetFeature() {
    Detail detail = test.getFeature();
    assertEquals(GameFeature.Grid.getDescription(), detail.getDescription());
    assertEquals(GameFeature.Grid.getGroupType(), detail.getGroupType());
    assertEquals(GameFeature.Grid.getLabel(), detail.getLabel());
    assertEquals(GameFeature.Grid, detail.getFeature());
  }

  @Test
  public void testGetOptions() {
    assertEquals(new ArrayList<>(
            Arrays.asList(new Detail(GameFeature.Grid40X40), new Detail(GameFeature.SomeOverlap))),
        test.getOptions());
  }

  @Test
  public void testGetOptionsGroup() {
    assertEquals(
        Arrays.asList(GameFeature.Grid40X40.getGroup(), GameFeature.SomeOverlap.getGroup()),
        test.getOptions().stream().map(Detail::getGroup).collect(Collectors.toList()));
  }

  @Test
  public void testGetOptionDetailOfOption() {
    Detail detail = test.getOptions().get(0);
    assertEquals(GameFeature.Grid40X40, detail.getFeature());
    assertEquals(GameFeature.Grid40X40.getDescription(), detail.getDescription());
    assertEquals(GameFeature.Grid40X40.getGroupType(), detail.getGroupType());
    assertEquals(GameFeature.Grid40X40.getGroupType(), detail.getGroupType());
    assertEquals(GameFeature.Grid40X40.getLabel(), detail.getLabel());
    assertEquals(GameFeature.Grid40X40.getGroup(), detail.getGroup());
  }

  @Test
  public void testGetDetailHashCode() {
    assertEquals(GameFeature.Grid40X40.hashCode(), test.getOptions().get(0).hashCode());
    assertEquals(GameFeature.SomeOverlap.hashCode(), test.getOptions().get(1).hashCode());
  }

  @Test
  public void testGetDetailEquals() {
    assertEquals(new Detail(GameFeature.Grid40X40), test.getOptions().get(0));
    assertEquals(new Detail(GameFeature.SomeOverlap), test.getOptions().get(1));
    assertNotEquals(new Detail(GameFeature.Grid40X40), test.getOptions().get(1));
    assertNotEquals(new Detail(GameFeature.SomeOverlap), test.getOptions().get(0));
  }
}
