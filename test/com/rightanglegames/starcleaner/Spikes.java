package com.rightanglegames.starcleaner;

import it.randomtower.engine.RessourceManager;
import it.randomtower.engine.entity.Entity;


public class Spikes extends Entity {

	public static final String SPIKES = "Spikes";
	
	public Spikes(float x, float y, boolean up) {
		super(x, y);
		this.name = SPIKES;
		depth = 12;
		this.addType(SPIKES);
		if (up) {
			this.setGraphic(RessourceManager.getImage("spikesup"));
			this.setHitBox(0, 25, width, 15);
		} else {
			this.setGraphic(RessourceManager.getImage("spikesdown"));
			this.setHitBox(0, 0, width, 15);
		}
	}

}
