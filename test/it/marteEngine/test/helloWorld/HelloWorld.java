package it.marteEngine.test.helloWorld;

import it.marteEngine.ME;
import it.marteEngine.World;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class HelloWorld extends StateBasedGame {

	public HelloWorld(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadResources("data/helloWorld/resources.xml");
		World world = new TextOnlyWorld(0, container);
		addState(world);
	}

	public static void main(String[] argv) throws SlickException {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(
					new HelloWorld("Hello World Marte Engine")
			);
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
	}
}
