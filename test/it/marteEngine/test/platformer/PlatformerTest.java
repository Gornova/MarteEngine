package it.marteEngine.test.platformer;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Platformer Test
 * 
 * @author Gornova
 */
public class PlatformerTest extends StateBasedGame {

	public static final int GAME_STATE = 0;

	public PlatformerTest() {
		super("PlatformerTest MarteEngine");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/platformer/resources.xml");
		PlatformerGameWorld platformerGameWorld = new PlatformerGameWorld(GAME_STATE);
		addState(platformerGameWorld);
	}

	public static void main(String[] argv) throws SlickException {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(
					new PlatformerTest());
			container.setDisplayMode(640, 480, false);
			container.setTargetFrameRate(60);
			container.setShowFPS(false);
			container.start();
	}

}
