package it.marteEngine.test.entity;

import it.marteEngine.ME;
import it.marteEngine.World;
import it.marteEngine.actor.TopDownActor;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.TextEntity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class AngleAlphaScaleMoveTest extends StateBasedGame {

  private World world;

  public AngleAlphaScaleMoveTest() {
    super("AngleAlphaScaleMoveTest");
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    world = new World(0, container);
    loadResources();
    prepareTestScenario(container, world);
    addState(world);
  }

  private void loadResources() {
    try {
      Image triangle = new Image("data/triangle.png");
      ResourceManager.addImage("triangle", triangle);
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  private void prepareTestScenario(GameContainer container, World world) {
    Font defaultFont = container.getDefaultFont();
    // Row 1: add some entities to our world
    // images rotating, scaling and alpha
    world.add(new TextEntity(10, 50, defaultFont, "rotating"));
    world.add(new AngleAlphaScaleMoveEntity(100, 150, true, false, false));
    world.add(new TextEntity(210, 50, defaultFont, "rotating and scaling"));
    world.add(new AngleAlphaScaleMoveEntity(300, 150, true, false, true));
    world.add(new TextEntity(410, 50, defaultFont, "rotating and alpha"));
    world.add(new AngleAlphaScaleMoveEntity(500, 150, true, true, false));
    world.add(new TextEntity(610, 50, defaultFont, "rotating, scaling"));
    world.add(new TextEntity(610, 70, defaultFont, "and alpha"));
    world.add(new AngleAlphaScaleMoveEntity(700, 150, true, true, true));

    // Row 2: animations rotating, scaling and alpha
    world.add(new TextEntity(10, 350, defaultFont, "rotating"));
    addAnimatedEntity(100, 400, Entity.PLAYER);

    world.add(new TextEntity(210, 350, defaultFont, "rotating and scaling"));
    addAnimatedEntity(300, 400, "ScaledPlayer");

    world.add(new TextEntity(410, 350, defaultFont, "rotating and alpha"));
    addAnimatedEntity(500, 400, "RotatingAndAlphaPlayer");

    world.add(new TextEntity(610, 350, defaultFont, "rotating, scaling"));
    world.add(new TextEntity(610, 370, defaultFont, "and alpha"));
    addAnimatedEntity(700, 400, "RotatingAndScalingAndAlphaPlayer");
  }

  private void addAnimatedEntity(float x, float y, String name) {
    Entity entity = new TopDownActor(x, y, "data/link.png");
    entity.name = name;
    entity.setCentered(true);
    world.add(entity);
  }

  public static void main(String[] args) throws SlickException {
    ME.keyRestart = Input.KEY_R;
    ME.debugEnabled = true;

    AppGameContainer container = new AppGameContainer(
        new AngleAlphaScaleMoveTest(), 800, 600, false);
    container.setTargetFrameRate(60);
    container.start();
  }

}
