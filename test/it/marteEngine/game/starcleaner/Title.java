package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Title extends Entity {

  private static final String CMD_START = "start";

  public Title(float x, float y) {
    super(x, y);
    name = "title";
    depth = 2;
    setGraphic(ResourceManager.getImage("title"));
    bindToKey(CMD_START, Input.KEY_X, Input.KEY_W, Input.KEY_UP);
  }

  public void update(GameContainer container, int delta)
      throws SlickException {
    super.update(container, delta);
    if (check(CMD_START))
      Globals.game.enterState(StarCleaner.INGAME_STATE,
          new FadeOutTransition(Color.white), new FadeInTransition(
              Color.white));
  }
}
