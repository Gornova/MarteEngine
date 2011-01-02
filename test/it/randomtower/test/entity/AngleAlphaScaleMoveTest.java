package it.randomtower.test.entity;

import it.randomtower.engine.ME;
import it.randomtower.engine.World;
import it.randomtower.engine.actors.TopDownActor;
import it.randomtower.engine.entity.Entity;
import it.randomtower.engine.entity.TextEntity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AngleAlphaScaleMoveTest extends StateBasedGame {

	private TopDownActor player = null;
	private TopDownActor scaledPlayer = null;
	private TopDownActor rotatingAndAlphaPlayer = null;
	private TopDownActor rotatingAndScalingAndAlphaPlayer = null;
	
	public AngleAlphaScaleMoveTest() {
		super("AngleAlphaScaleMoveTest");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		World world = new World(0,container); 
		prepareTestScenario(container, world);
		addState(world);
	}
	
	private void prepareTestScenario(GameContainer container, World world) {
		Font defaultFont = container.getDefaultFont();
		// add some entities to our world
		// images rotating, scaling and alpha
		world.add(new TextEntity(10,50,defaultFont,"rotating"));
		world.add(new AngleAlphaScaleMoveEntity(100, 150, true, false, false, false));
		world.add(new TextEntity(210,50,defaultFont,"rotating and scaling"));
		world.add(new AngleAlphaScaleMoveEntity(300, 150, true, false, true, false));
		world.add(new TextEntity(410,50,defaultFont,"rotating and alpha"));
		world.add(new AngleAlphaScaleMoveEntity(500, 150, true, true, false, false));
		world.add(new TextEntity(610,50,defaultFont,"rotating, scaling"));
		world.add(new TextEntity(610,70,defaultFont,"and alpha"));
		world.add(new AngleAlphaScaleMoveEntity(700, 150, true, true, true, false));
		
		// animations rotating, scaling and alpha
		world.add(new TextEntity(10,350,defaultFont,"rotating"));
		player = new TopDownActor(100, 400, "data/link.png");
		player.name = Entity.PLAYER;
		player.setCentered(true);
		world.add(player);

		world.add(new TextEntity(210,350,defaultFont,"rotating and scaling"));
		scaledPlayer = new TopDownActor(300, 400, "data/link.png");
		scaledPlayer.name = "ScaledPlayer";
		scaledPlayer.setCentered(true);
		world.add(scaledPlayer);

		world.add(new TextEntity(410,350,defaultFont,"rotating and alpha"));
		rotatingAndAlphaPlayer = new TopDownActor(500, 400, "data/link.png");
		rotatingAndAlphaPlayer.name = "RotatingAndAlphaPlayer";
		rotatingAndAlphaPlayer.setCentered(true);
		world.add(rotatingAndAlphaPlayer);

		world.add(new TextEntity(610,350,defaultFont,"rotating, scaling"));
		world.add(new TextEntity(610,370,defaultFont,"and alpha"));
		rotatingAndScalingAndAlphaPlayer = new TopDownActor(700, 400, "data/link.png");
		rotatingAndScalingAndAlphaPlayer.name = "RotatingAndScalingAndAlphaPlayer";
		rotatingAndScalingAndAlphaPlayer.setCentered(true);
		world.add(rotatingAndScalingAndAlphaPlayer);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ME.keyRestart = Input.KEY_R;
			ME.debugEnabled = true;
			
			AppGameContainer container = new AppGameContainer(
					new AngleAlphaScaleMoveTest(), 800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
