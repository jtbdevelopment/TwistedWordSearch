package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts;

import java.util.List;
import java.util.Random;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.springframework.stereotype.Component;

/**
 * Date: 8/25/16 Time: 7:13 AM
 */
@Component
public class RandomLayoutPicker {

  private Random random = new Random();
  private List<WordLayout> layouts = DefaultGroovyMethods.toList(WordLayout.values());
  private int upperBound = layouts.size();

  public WordLayout getRandomLayout() {
    return layouts.get(random.nextInt(upperBound));
  }
}
