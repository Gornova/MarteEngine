package it.randomtower.test;

import it.randomtower.engine.ME;
import it.randomtower.engine.StaticActor;
import it.randomtower.engine.WorldGameState;

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
		StaticActor helloWorld = new StaticActor(100, 100, 35, 35,
				"data/cross.png");
		WorldGameState world = new WorldGameState(0);
		world.add(helloWorld);
		
		addState(world);
		
	}

	public static void main(String[] argv) {
		try {
			ME.debugEnabled = true;
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
