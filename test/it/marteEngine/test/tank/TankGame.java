package it.marteEngine.test.tank;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * In game World for the tank entity
 *
 * @author Gornova
 */
public class TankGame extends World {

	public Tank tank;
	private Font font = ResourceManager.getFont("bradleyhanditc24");

	public TankGame(int id, GameContainer container) throws SlickException {
		super(id, container);

		// create tank controlled by player
		tank = new Tank(400, 300);
		add(tank);
		// and add a turret on top of it
		TankTurret turret = new TankTurret(tank);
		add(turret);

		// add some destructible targets
		add(new RedTarget(10, 50));
		add(new RedTarget(750, 50));
		add(new RedTarget(10, 500));
		add(new RedTarget(750, 500));

		// add some invulnerable targets
		add(new BlueTarget(650, 450));
		add(new BlueTarget(700, 450));
		add(new BlueTarget(750, 450));
		add(new BlueTarget(650, 482));

		// add background from
		// http://www.flickr.com/photos/fredmikerudy/4136592583/
		Background bg = new Background(0, 0);
		add(bg);

		container.setMouseCursor(ResourceManager.getImage("cursor"), 25, 25);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// render everything
		super.render(container, game, g);
		// simple gui
		font.drawString(5, 570,
				"WASD/arrows to move, mouse rotate turret, click to destroy Red targets");
	}

}
