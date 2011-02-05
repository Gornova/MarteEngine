package it.randomtower.test.tiled;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.World;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

/**
 * World for Tiled test
 * 
 * @author Gornova
 */
public class TiledWorld extends World {

	private TiledMap map;
	
	public TiledWorld(int id, GameContainer container) throws SlickException {
		super(id, container);
	
		map = ResourceManager.getMap("test1");

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);
		
		map.render(300, 200);
		
		// render gui
		g.drawString("This example show how to load a map designed with Tiled (http://mapeditor.org)", 5, 5);
		g.drawString("into MarteEngine", 5, 20);		
	}
	
}
