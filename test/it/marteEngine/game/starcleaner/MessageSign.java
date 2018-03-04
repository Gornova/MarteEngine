package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class MessageSign extends Entity {

  public static final String SIGN = "sign";

  public MessageSign(float x, float y) {

    super(x, y);
    this.name = SIGN;
    this.addType(SIGN);
    depth = 12;
    setGraphic(ResourceManager.getImage("sign"));
    setHitBox(0, 0, width, height);
  }

  public void update(GameContainer container, int delta)
      throws SlickException {
    if (collide(Entity.PLAYER, x, y) != null) {
      // open the message dialog window
      if (Globals.messageWindow == null) {
        IngameState state = (IngameState) this.world;
        Globals.messageWindow = new MessageWindow(
            state.currentLevel.message, 0, 0, true);
        state.add(Globals.messageWindow);
      }
      Globals.messageWindow.visible = true;
    } else {
      // close the message dialog window
      if (Globals.messageWindow != null)
        Globals.messageWindow.visible = false;
    }

  }

}
