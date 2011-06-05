package it.marteEngine.test.scrollingPlatformer;

import it.marteEngine.Camera;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.entity.Solid;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;


public class ScrollingPlatformerGameWorld extends World {

	public ScrollingPlatformerGameWorld(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		
		TiledMap map = ResourceManager.getMap("map");
		
		loadEntityFromMap(map);

		PlatformerEntity player = loadPlayer(map,"player");
		setCamera(new Camera(this, player, 800, 600));
		
		// add a background image, from http://thetutorials.wordpress.com/2008/11/26/ps-cute-cartoon-clouds-the-simple-way/
		//add(new Background(0, 0));
	}
	
	/**
	 * Load player position from layer with given name
	 * @param map
	 * @param layerName
	 * @return 
	 * @throws SlickException 
	 */
	private PlatformerEntity loadPlayer(TiledMap map, String layerName) throws SlickException {
		int layerIndex = map.getLayerIndex(layerName);
		for (int w = 0; w < map.getWidth(); w++) {
			for (int h = 0; h < map.getHeight(); h++) {
				Image img = map.getTileImage(w, h, layerIndex);
				if (img!=null){
					int x = h * img.getWidth();
					int y = w * img.getHeight() ;
					// create player & camera
					PlatformerEntity player = new PlatformerEntity(x, y, "player");
					add(player);
					return player;
				}
			}
		}
		return null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);
		
		// render gui
		g.drawString("Press WASD or ARROWS to move, X or UP to Jump", 65, 5);
		
	}	

}
