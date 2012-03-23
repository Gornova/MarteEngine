package it.marteEngine.test.zombieEscape;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ZombieWorld extends World {

	private Zombie zombie;

	public ZombieWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);

		zombie = new Zombie(100, 200, ResourceManager.getImage("zombie"));
		add(zombie);

		Player player = new Player(500, 200, ResourceManager.getImage("player"));
		add(player);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);
		g.drawString(
				"Zombie State: " + zombie.stateManager.getCurrentStateName(),
				5, 560);
		g.drawString(
				"Use arrows or WASD to move into Zombie area and see state changing",
				5, 580);
	}

}
