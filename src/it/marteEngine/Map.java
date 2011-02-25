package it.marteEngine;

import it.marteEngine.actor.StaticActor;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map extends TiledMap {
	private static final String BLOCKS = "BLOCKS";
	private int blocksIndex;

	public Map(String ref) throws SlickException {
		super(ref);
		blocksIndex = getLayerIndex(BLOCKS);

		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				int tileID = getTileId(i, j, blocksIndex);
				if (Boolean.valueOf(getTileProperty(tileID, "solid", "false"))) {

					StaticActor block = new StaticActor(i * 32, j * 32, 32, 32,
							"data/block.png");
					ME.world.add(block);
				}
			}
		}

	}
}
