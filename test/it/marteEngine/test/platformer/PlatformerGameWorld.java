package it.marteEngine.test.platformer;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.game.starcleaner.Background;
import it.marteEngine.game.starcleaner.Solid;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlatformerGameWorld extends World {

	public PlatformerGameWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		super.init(container, game);
		// create player & camera
		PlatformerEntity player = new PlatformerEntity(80, 100, "player");
		add(player);

		// add some blocks to level, I will just create and add to the world
		Image block = ResourceManager.getImage("block");
		// bottom
		for (int i = 0; i < 18; i++) {
			add(new Solid(20 + i * 32, 400, 32, 32, 10, block));
		}
		// left platform
		for (int i = 0; i < 5; i++) {
			add(new Solid(120 + i * 32, 304, 32, 32, 10, block));
		}
		// right platform
		for (int i = 0; i < 5; i++) {
			add(new Solid(320 + i * 32, 304, 32, 32, 10, block));
		}
		// top platform
		for (int i = 0; i < 6; i++) {
			add(new Solid(200 + i * 32, 200, 32, 32, 10, block));
		}
		// left wall
		for (int i = 1; i < 13; i++) {
			add(new Solid(20, 400 - i * 32, 32, 32, 10, block));
		}
		// right wall
		for (int i = 1; i < 13; i++) {
			add(new Solid(564, 400 - i * 32, 32, 32, 10, block));
		}
		// add a background image, from
		// http://thetutorials.wordpress.com/2008/11/26/ps-cute-cartoon-clouds-the-simple-way/
		add(new Background(0, 0));
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);

		// render gui
		g.drawString("Press WASD or ARROWS to move, X or UP to Jump", 65, 5);

	}

}
