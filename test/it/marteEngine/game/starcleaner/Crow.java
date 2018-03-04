package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Crow extends Entity {

  public static final String CROW = "crow";

  private int frame = 0;
  private int nextFrame = 500; // change frames every 500 msecs
  private int frameCount = 0;
  private float speed = 0.1f;

  public Crow(float x, float y) {
    super(x, y);
    depth = 20;
    name = CROW;
    addType(CROW);
    sheet = ResourceManager.getSpriteSheet("crow");
    setGraphic(sheet.getSprite(frame, 0));
    wrapHorizontal = true;
    setHitBox(4, 8, 32, 24);
  }

  public void update(GameContainer container, int delta)
      throws SlickException {
    super.update(container, delta);
    frameCount += delta;
    if (frameCount >= nextFrame) {
      frameCount -= nextFrame;
      frame++;
      if (frame > 1)
        frame = 0;
    }
    x += (speed * delta);
  }
}
