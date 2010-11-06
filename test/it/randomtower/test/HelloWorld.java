package it.randomtower.test;

import it.randomtower.engine.ME;
import it.randomtower.engine.StaticActor;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class HelloWorld extends BasicGame {

	public HelloWorld(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		StaticActor helloWorld = new StaticActor(100, 100, 35, 35,
				"data/cross.png");
		ME.add(helloWorld);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		ME.update(container,delta );
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		ME.render(container, g);
	}

	public static void main(String[] argv) {
		try {
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
