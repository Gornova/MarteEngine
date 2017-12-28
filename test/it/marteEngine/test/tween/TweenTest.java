package it.marteEngine.test.tween;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Test for Tweening
 * 
 * @author Gornova
 */
public class TweenTest extends StateBasedGame {

	public TweenTest() {
		super("Tween Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/tween/resources.xml");
		TweenWorld inGameState = new TweenWorld(0, container);
		addState(inGameState);
	}

	public static void main(String[] argv) throws SlickException {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new TweenTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
	}

}
