package it.marteEngine.test.fuzzy;

import it.marteEngine.entity.Entity;
import org.newdawn.slick.Image;

public class FuzzyDestroyableBlock extends Entity {

  public static String TAPPO = "tappo";

  public FuzzyDestroyableBlock(float x, float y, Image image) {
    super(x, y, image);

    setHitBox(0, 0, image.getWidth(), image.getHeight());
    addType(SOLID, TAPPO);
  }

}
