package it.marteEngine.test.helloWorld;

import it.marteEngine.World;
import it.marteEngine.actor.StaticActor;
import it.marteEngine.entity.TextEntity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TextOnlyWorld extends World {
  public TextOnlyWorld(int id, GameContainer container) {
    super(id, container);
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    super.init(container, game);
    bindToKey("scaleDown", Input.KEY_SUBTRACT);
    bindToKey("scaleUp", Input.KEY_ADD);

    Image helloImg = ResourceManager.getImage("hello");
    StaticActor helloWorld = new StaticActor(300, 250, 35, 35, helloImg);

    TextEntity textEntity = new TextEntity(10, 10, container.getDefaultFont(), "This is a scalable text entity!");
    textEntity.name = "text1";

    add(textEntity);
    add(helloWorld);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    super.update(container, game, delta);

    if (check("scaleDown")) {
      find("text1").scale -= 0.025f;
    } else if (check("scaleUp")) {
      find("text1").scale += 0.025f;
    }
  }
}
