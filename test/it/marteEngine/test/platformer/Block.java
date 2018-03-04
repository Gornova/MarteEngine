package it.marteEngine.test.platformer;

import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.SlickException;

public class Block extends Entity {

  public Block(float x, float y) throws SlickException {
    super(x, y);
    name = "block";
    depth = 5;
    setGraphic(ResourceManager.getImage("block"));
    addType(SOLID);
    setHitBox(0, 0, width, height);
  }

}
