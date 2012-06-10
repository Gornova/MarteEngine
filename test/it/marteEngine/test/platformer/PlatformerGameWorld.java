package it.marteEngine.test.platformer;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.PlatformerEntity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlatformerGameWorld extends World {

	private Image background;

	public PlatformerGameWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		super.init(container, game);
		// create player
		PlatformerEntity player = new PlatformerEntity(80, 100, "player");
		add(player);

		// add some blocks to level, I will just create and add to the world
		// bottom
		for (int i = 0; i < 18; i++) {
			add(new Block(20 + i * 32, 400));
		}
		// left platform
		for (int i = 0; i < 5; i++) {
			add(new Block(120 + i * 32, 304));
		}
		// right platform
		for (int i = 0; i < 5; i++) {
			add(new Block(320 + i * 32, 304));
		}
		// top platform
		for (int i = 0; i < 6; i++) {
			add(new Block(200 + i * 32, 200));
		}
		// left wall
		for (int i = 1; i < 13; i++) {
			add(new Block(20, 400 - i * 32));
		}
		// right wall
		for (int i = 1; i < 13; i++) {
			add(new Block(564, 400 - i * 32));
		}
		// add a background image, from
		// http://thetutorials.wordpress.com/2008/11/26/ps-cute-cartoon-clouds-the-simple-way/
		background = ResourceManager.getImage("background");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawImage(background, 0, 0);
		super.render(container, game, g);

		// render gui
		g.drawString("Press WASD or ARROWS to move, X or UP to Jump", 65, 5);

	}

}
