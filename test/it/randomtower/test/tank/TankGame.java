package it.randomtower.test.tank;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.World;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * In game World for TestTest
 * 
 * @author Gornova
 */
public class TankGame extends World {

	public Tank tank;
	private AngelCodeFont font = ResourceManager.getAngelCodeFont("bradleyhanditc24");
	
	public TankGame(int id, GameContainer container) throws SlickException {
		super(id, container);
		
		// create tank controlled by player
		tank = new Tank(400, 300);
		add(tank);
		// and add a turret on top of it
		TankTurret turret = new TankTurret(tank);
		add(turret);

		// add some destructible targets
		add(new RedTarget(10,50));
		add(new RedTarget(750,50));
		add(new RedTarget(10,500));
		add(new RedTarget(750,500));
		
		// add some invulnerable targets
		add(new BlueTarget(650,450));
		add(new BlueTarget(700,450));
		add(new BlueTarget(750,450));
		add(new BlueTarget(650,482));

		container.setMouseCursor(ResourceManager.getImage("cursor"), 25, 25);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// simple gui
		font.drawString(5, 550, "WASD/arrows to move, mouse rotate turret, click to destroy Red targets");
		// render everything
		super.render(container, game, g);
	}
	
	

}
