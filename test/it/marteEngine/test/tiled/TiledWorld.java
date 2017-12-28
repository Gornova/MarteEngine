package it.marteEngine.test.tiled;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.actor.StaticActor;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import java.util.Collections;
import java.util.List;

/**
 * World for Tiled test
 *
 * @author Gornova
 */
public class TiledWorld extends World {

	private TiledMap map;

	private boolean hideTiles = false;

	public TiledWorld(int id, GameContainer container) {
		super(id, container);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		// load entities from map (see resource.xml of this example in data/tiled)
		map = ResourceManager.getMap("test1");
		// load entities from map defined with Tiled
		loadEntityFromMap(map, Collections.singletonList("entity"));
	}

	/**
	 * Load entity from a tiled map into current World
	 */
	public void loadEntityFromMap(TiledMap map, List<String> types) {
		if (map == null) {
			Log.error("unable to load map information");
			return;
		}
		if (types == null || types.isEmpty()) {
			Log.error("no types defined to load");
			return;
		}
		// layer have property type, so check it
		for (String type : types) {
			// try to find a layer with property type set to entity
			int layerIndex = -1;
			for (int l = 0; l < map.getLayerCount(); l++) {
				String value = map.getLayerProperty(l, "type", null);
				if (value != null && value.equalsIgnoreCase(type)) {
					layerIndex = l;
					break;
				}
			}
			if (layerIndex != -1) {
				Log.debug("Entity layer found on map");
				int loaded = 0;
				for (int w = 0; w < map.getWidth(); w++) {
					for (int h = 0; h < map.getHeight(); h++) {
						Image img = map.getTileImage(w, h, layerIndex);
						if (img != null) {
							// load entity from Tiled map position and set Image
							// for static actor using image reference stored
							// into tiled map
							StaticActor te = new StaticActor(
									w * img.getWidth(), h * img.getHeight(),
									img.getWidth(), img.getHeight(), img);
							add(te);
							loaded++;
						}
					}
				}
				Log.debug("Loaded " + loaded + " entities");
			} else {
				Log.info("Entity layer not found on map");
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// center graphics
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
			hideTiles = !hideTiles;
		}
	}

}
