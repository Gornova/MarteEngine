package it.marteEngine.test.collision;

import it.marteEngine.ME;
import it.marteEngine.World;
import it.marteEngine.actor.TopDownActor;

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

		World gameWorld = new World(0, container);
		// make the world a bit bigger than the screen to force camera scrolling
		gameWorld.setWidth(2000);
		gameWorld.setHeight(2000);

		// create player
		TopDownActor player = new TopDownActor(400, 400, "data/link.png");
		// create sword relative to player

		// add entities
		gameWorld.add(player);

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
