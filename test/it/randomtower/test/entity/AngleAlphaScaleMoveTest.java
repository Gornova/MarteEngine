package it.randomtower.test.entity;

import it.randomtower.engine.ME;
import it.randomtower.engine.World;
import it.randomtower.engine.actors.TopDownActor;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AngleAlphaScaleMoveTest extends StateBasedGame {

	private TopDownActor player = null;
	private TopDownActor scaledPlayer = null;
	
	public AngleAlphaScaleMoveTest() {
		super("AngleAlphaScaleMoveTest");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		World world = new World(0,container); 
		prepareTestScenario(world);
		addState(world);
	}
	
	private void prepareTestScenario(World world) {
		// add some entities to our world
		world.add(new AngleAlphaScaleMoveEntity(400, 300, true, false, false, false));
		world.add(new AngleAlphaScaleMoveEntity(100, 200, true, false, true, false));
		player = new TopDownActor(400, 400, "data/link.png");
		player.name = Entity.PLAYER;
		player.setCentered(true);
		world.add(player);

		scaledPlayer = new TopDownActor(100, 400, "data/link.png");
		scaledPlayer.name = "ScaledPlayer";
		scaledPlayer.setCentered(true);
		world.add(scaledPlayer);
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
