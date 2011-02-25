package it.marteEngine.test.tiled;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
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

	private boolean hideTiles = false;

	public TiledWorld(int id, GameContainer container) throws SlickException {
		super(id, container);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		// load entities from map (see resource.xml of this example in
		// data/tiled)
		map = ResourceManager.getMap("test1");
		loadEntityFromMap(map);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// center grapchis
		g.translate(300, 200);
		// render all except entities
		if (!hideTiles) {
			for (int l = 0; l < map.getLayerCount(); l++) {
				String value = map.getLayerProperty(l, "type", null);
				if (value == null || !value.equalsIgnoreCase("entity")) {
					for (int w = 0; w < map.getWidth(); w++) {
						for (int h = 0; h < map.getHeight(); h++) {
							Image img = map.getTileImage(w, h, l);
							if (img != null) {
								g.drawImage(img, w * img.getWidth(),
										h * img.getHeight());
							}
						}
					}
				}
			}
		}
		// render entities
		super.render(container, game, g);
		g.translate(-300, -200);

		// render gui
		g.drawString(
				"This example show how to load a map designed with Tiled (http://mapeditor.org)",
				5, 5);
		g.drawString("into MarteEngine", 5, 20);
		g.drawString(
				"Press SPACEBAR to show/hide tiles and see only entities (trees) loaded",
				5, 580);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);

		Input input = container.getInput();
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			hideTiles = hideTiles ? false : true;
		}
	}

}
