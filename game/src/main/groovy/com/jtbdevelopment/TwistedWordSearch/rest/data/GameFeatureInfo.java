package com.jtbdevelopment.TwistedWordSearch.rest.data;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeatureGroupType;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

/**
 * Date: 4/28/15 Time: 6:54 PM
 */
public class GameFeatureInfo {

  private Detail feature;
  private List<Detail> options = new ArrayList<>();

  @SuppressWarnings("unused")
  public GameFeatureInfo() {
  }

  public GameFeatureInfo(final GameFeature feature, final List<Detail> options) {
    this.feature = new Detail(feature);
    this.options.addAll(options);
  }

  public boolean equals(final Object o) {
    if (DefaultGroovyMethods.is(this, o)) {
      return true;
    }
    if (!getClass().equals(o.getClass())) {
      return false;
    }

    final GameFeatureInfo that = (GameFeatureInfo) o;

    return options.equals(that.options) && feature.equals(that.feature);
  }

  public int hashCode() {
    return feature.hashCode();
  }

  public Detail getFeature() {
    return feature;
  }

  public List<Detail> getOptions() {
    return options;
  }

  public static class Detail {

    private GameFeatureGroupType groupType;
    private GameFeature feature;
    private GameFeature group;
    private String label;
    private String description;

    @SuppressWarnings("unused")
    public Detail() {
    }

    public Detail(final GameFeature feature) {
      this.feature = feature;
      this.description = feature.getDescription();
      this.label = feature.getLabel();
      this.groupType = feature.getGroupType();
      this.group = feature.getGroup();
    }

    public boolean equals(final Object o) {
      if (DefaultGroovyMethods.is(this, o)) {
        return true;
      }
      if (!getClass().equals(o.getClass())) {
        return false;
      }

      final Detail detail = (Detail) o;

      return feature.equals(detail.feature);
    }

    public int hashCode() {
      return feature.hashCode();
    }

    public GameFeatureGroupType getGroupType() {
      return groupType;
    }

    public GameFeature getFeature() {
      return feature;
    }

    public GameFeature getGroup() {
      return group;
    }

    public void setGroup(GameFeature group) {
      this.group = group;
    }

    public String getLabel() {
      return label;
    }

    public String getDescription() {
      return description;
    }
  }
}
