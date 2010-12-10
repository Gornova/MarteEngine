package com.rightanglegames.starcleaner;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;


public class Spikes extends Entity {

	public static final String SPIKES = "Spikes";
	
	public Spikes(float x, float y, boolean up) {
		super(x, y);
		this.name = SPIKES;
		depth = 12;
		this.addType(SPIKES);
		if (up) {
			this.setGraphic(ResourceManager.getImage("spikesup"));
			this.setHitBox(0, 25, width, 15);
		} else {
			this.setGraphic(ResourceManager.getImage("spikesdown"));
			this.setHitBox(0, 0, width, 15);
		}
	}

}
