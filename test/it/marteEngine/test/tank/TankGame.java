package it.marteEngine.test.tank;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * In game World for the tank entity
 *
 * @author Gornova
 */
public class TankGame extends World {

	public Tank tank;
	private Font font;
	private Image background;

	public TankGame(int id, GameContainer container) throws SlickException {
		super(id, container);

		// create tank controlled by player
		tank = new Tank(400, 300);
		add(tank);
		// and add a turret on top of it
		TankTurret turret = new TankTurret(tank);
		tank.setTurret(turret);
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
		font = ResourceManager.getFont("bradleyhanditc24");
		background = ResourceManager.getImage("background");

		container.setMouseCursor(ResourceManager.getImage("cursor"), 25, 25);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawImage(background, 0, 0);
		// render everything
		super.render(container, game, g);
		// simple gui
		font.drawString(5, 570,
				"WASD/arrows to move, mouse rotate turret, click to destroy Red targets");
	}

}
