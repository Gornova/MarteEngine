package it.randomtower.test.platformer;

import it.randomtower.engine.World;
import it.randomtower.engine.entity.Solid;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.rightanglegames.starcleaner.Background;

public class PlatformerGameWorld extends World {

	public PlatformerGameWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		super.init(container, game);
		// create player & camera
		Player player = new Player(100, 100);
		add(player);

		// add some blocks to level
		// bottom
		for (int i = 0; i < 18; i++) {
			add(new Solid(20+i*32, 400, 32, 32, 10, "data/block.png"));
		}
		// left platform
		for (int i = 0; i < 5; i++) {
			add(new Solid(120+i*32, 304, 32, 32, 10, "data/block.png"));
		}
		// right platform
		for (int i = 0; i < 5; i++) {
			add(new Solid(320+i*32, 304, 32, 32, 10,"data/block.png"));
		}
		// left wall
		for (int i = 1; i < 10; i++) {
			add(new Solid(20, 400-i*32, 32, 32,10, "data/block.png"));
		}
		// right wall
		for (int i = 1; i < 10; i++) {
			add(new Solid(564, 400-i*32, 32, 32,10, "data/block.png"));
		}
		// add a background image, from http://thetutorials.wordpress.com/2008/11/26/ps-cute-cartoon-clouds-the-simple-way/
		add(new Background(0, 0,"cartoonClouds.jpg"));
		
		
	}

}
