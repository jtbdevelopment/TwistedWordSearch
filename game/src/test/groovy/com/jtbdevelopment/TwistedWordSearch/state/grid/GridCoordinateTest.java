package com.jtbdevelopment.TwistedWordSearch.state.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Date: 8/23/16 Time: 6:58 AM
 */
public class GridCoordinateTest {

  @Test
  public void testGetSetCoordinates() {
    GridCoordinate coordinate = new GridCoordinate(30, 17);
    assertEquals(30, coordinate.getRow());
    assertEquals(17, coordinate.getColumn());

    coordinate.setRow(25);
    assertEquals(25, coordinate.getRow());
    assertEquals(17, coordinate.getColumn());

    coordinate.setColumn(5);
    assertEquals(25, coordinate.getRow());
    assertEquals(5, coordinate.getColumn());
  }

  @Test
  public void testDefaultConstructor() {
    GridCoordinate coordinate = new GridCoordinate();
    assertEquals(0, coordinate.getRow());
    assertEquals(0, coordinate.getColumn());
  }

  @Test
  public void testCopyConstructor() {
    GridCoordinate original = new GridCoordinate(10, 11);
    GridCoordinate copy = new GridCoordinate(original);
    assertEquals(10, copy.getRow());
    assertEquals(11, copy.getColumn());
    assertEquals(10, original.getRow());
    assertEquals(11, original.getColumn());
  }

  @Test
  public void testEquals() {
    assertEquals(new GridCoordinate(15, 22), new GridCoordinate(15, 22));
    assertEquals(new GridCoordinate(17, 4), new GridCoordinate(17, 4));
  }

  @Test
  public void testNotEquals() {
    assertNotEquals(new GridCoordinate(15, 22), new GridCoordinate(16, 22));
    assertNotEquals(new GridCoordinate(17, 4), new GridCoordinate(17, 5));
  }

  @Test
  public void testHashCode() {
    GridCoordinate coordinate = new GridCoordinate(10, 15);
    assertEquals(325, coordinate.hashCode());
  }

}
