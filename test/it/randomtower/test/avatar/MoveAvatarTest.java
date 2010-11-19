package it.randomtower.test.avatar;

import it.randomtower.engine.ME;
import it.randomtower.engine.World;
import it.randomtower.engine.actors.StaticActor;
import it.randomtower.engine.actors.TopDownActor;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MoveAvatarTest extends StateBasedGame {

	public MoveAvatarTest() {
		super("Move Avatar Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ME.container = container;
		
		World gameWorld = new World(0);
		
		// create player
		TopDownActor player = new TopDownActor(400, 400, "data/link.png");
		// create sword relative to player
		Sword sword = new Sword(player.x, player.x, "data/sword.png", player);
		// create temple
		StaticActor temple = new StaticActor(150, 150, 48, 48,
				"data/tiles.png", 0, 6);

		// add entities
		gameWorld.add(player);
		gameWorld.add(temple);
		gameWorld.add(sword);

		// set screen camera to follo player
		gameWorld.setCameraOn(player);
		
		ME.world = gameWorld;

		addState(gameWorld);
	}
	

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			ME.keyRestart = Input.KEY_R;
			AppGameContainer container = new AppGameContainer(
					new MoveAvatarTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
