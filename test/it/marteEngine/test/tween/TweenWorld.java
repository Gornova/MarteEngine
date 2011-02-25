package it.marteEngine.test.tween;

import it.marteEngine.World;
import it.marteEngine.tween.Ease;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * World for Tween Test
 * 
 * @author Gornova
 */
public class TweenWorld extends World {

	
	private Player player;

	public TweenWorld(int id, GameContainer container) throws SlickException {
		super(id, container);
		
		player = new Player(100,100);
		
		add(player);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);
		
		// render gui
		g.drawString("Press Right mouse button to change Ease function ", 5, 5);
		g.drawString("Press Left mouse button to move Entity ", 5, 25);
		g.drawString("Press Z to start, X to pause, C to reset current Tween", 5, 45);
		
		g.drawString("Current ease: "+Ease.getName(player.currentEase), 550, 5);
	}
	
}
