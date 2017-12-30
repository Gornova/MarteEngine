package it.marteEngine.test.tank;

import it.marteEngine.ME;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Tank test is an effort to build a little tank-fighting game
 * 
 * @author Gornova
 */
public class TankTest extends StateBasedGame {
	public TankTest() {
		super("Tank Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/tank/resources.xml");
		TankGame inGameState = new TankGame(0, container);
		addState(inGameState);
	}

	public static void main(String[] argv) throws SlickException {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new TankTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
	}

}
