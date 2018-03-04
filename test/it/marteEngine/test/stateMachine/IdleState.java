package it.marteEngine.test.stateMachine;

import it.marteEngine.State;
import it.marteEngine.entity.Entity;
import org.newdawn.slick.*;

public class IdleState implements State {

  private Image image;

  private Entity entity;

  public IdleState(Entity e) {
    this.entity = e;
  }

  public void init() {
    try {
      image = new Image("data/face-sleep.png");
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  public void render(Graphics g) {
    g.drawImage(entity.getCurrentImage(), entity.x, entity.y);
    // render status image on top left of parent image
    g.drawImage(image, entity.x - 10, entity.y - 10);
  }

  public void update(GameContainer container, int delta) {
    Input input = container.getInput();
    if (input.isKeyPressed(Input.KEY_SPACE)) {
      entity.stateManager.enter("move_state");
    }
  }

  @Override
  public String getName() {
    return "idle_state";
  }

}
