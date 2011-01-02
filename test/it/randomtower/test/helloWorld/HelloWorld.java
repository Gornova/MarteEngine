package it.randomtower.test.helloWorld;

import java.io.IOException;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.World;
import it.randomtower.engine.actors.StaticActor;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class HelloWorld extends StateBasedGame {

	private static boolean ressourcesInited;

	public HelloWorld(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		initRessources();
		
		StaticActor helloWorld = new StaticActor(300, 250, 35, 35,
				ResourceManager.getImage("hello"));
		World world = new World(0, container);
		world.add(helloWorld);

		addState(world);

	}

	public static void initRessources() throws SlickException {
		if (ressourcesInited)
			return;
		try {
			ResourceManager.loadResources("data/helloWorld/resources.xml");
		} catch (IOException e) {
			Log.error("failed to load ressource file 'data/helloWorld/resources.xml': "
					+ e.getMessage());
			throw new SlickException("Resource loading failed!");
		}

		ressourcesInited = true;
	}

	public static void main(String[] argv) {
		try {
			ME.keyToggleDebug = Input.KEY_1;
			AppGameContainer container = new AppGameContainer(new HelloWorld(
					"Hello World Marte Engine"));
			container.setDisplayMode(800, 600, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
