package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.RoundedRectangle;

/**
 * The HUD class responsible for showing score and so on. Also the Game Maker
 * like controller class which deals with input that should be handled for the
 * world. This way we avoid adding input dealing code to the WorldGameState
 * class. Just add some visible or invisible controller class to your world and
 * handle input there.
 *
 * @author Thomas
 */
public class Hud extends Entity {

  private static final String CMD_NEXT = "nextLevel";
  private static final String CMD_PREVIOUS = "previousLevel";

  private Font font;

  public Hud(float x, float y) {
    super(x, y);
    this.name = "HUD";
    this.depth = 400;
    this.font = ResourceManager.getFont("bradleyhanditc24");

    bindToKey(CMD_NEXT, Input.KEY_N);
    bindToKey(CMD_PREVIOUS, Input.KEY_P);
  }

  public void render(GameContainer container, Graphics g)
      throws SlickException {
    IngameState state = (IngameState) this.world;
    RoundedRectangle r = new RoundedRectangle(x, y,
        container.getWidth() - 1, 40, 20);
    Color c = Color.blue;
    c.a = 0.3f;
    Font oldFont = g.getFont();
    g.setFont(font);
    g.setColor(c);
    g.fill(r);
    g.draw(r);
    g.setColor(Color.white);
    g.drawString("Score: " + state.score, x + 5, y + 1);
    g.drawString("Seconds left: " + state.timeLeft, x + 205, y + 1);
    g.drawString("Stars left: " + state.getNrOfEntities(Star.STAR_TYPE),
        x + 465, y + 1);

    g.setFont(oldFont);

  }

  public void update(GameContainer container, int delta)
      throws SlickException {
    if (check(CMD_NEXT)) {
      Globals.levelDone = true;
      Globals.playerCheated = true;
    }
    if (check(CMD_PREVIOUS)) {
      IngameState state = (IngameState) this.world;
      state.levelNo -= 2;
      if (state.levelNo < 0)
        state.levelNo = 0;
      Globals.levelDone = true;
      Globals.playerCheated = true;
    }

  }
}
