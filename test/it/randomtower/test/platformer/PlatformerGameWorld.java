package it.randomtower.test.platformer;

import it.randomtower.engine.Camera;
import it.randomtower.engine.World;
import it.randomtower.engine.entity.Solid;

import org.newdawn.slick.GameContainer;
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
		Player player = new Player(100, 100);
		add(player);
		Camera camera = new Camera(player, 640, 480);
		setCamera(camera);

		// add some blocks to level
		for (int i = 0; i < 18; i++) {
			Solid block = new Solid(20+i*32, 400, 32, 32, "data/block.png");
			add(block);
		}
	}

}
