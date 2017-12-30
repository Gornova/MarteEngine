package it.marteEngine.test.tiled;

import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Test for Tiled (http://mapeditor.org) support
 * 
 * @author Gornova
 */
public class TiledTest extends StateBasedGame {

	public TiledTest() {
		super("Tiled Integration Test");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/tiled/resources.xml");
		addState(new TiledWorld(1, container));
	}

	public static void main(String[] argv) throws SlickException {
			AppGameContainer container = new AppGameContainer(new TiledTest());
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
	}

}
