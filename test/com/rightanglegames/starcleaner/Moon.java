package com.rightanglegames.starcleaner;

import it.randomtower.engine.RessourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Moon extends Entity {

	public Moon(float x, float y) {
		super(x, y);
		this.name = "Moon";
		this.addType(Sun.SUN_AND_MOON);	// sun and moon share the same type
		depth = 1;
		setGraphic( RessourceManager.getImage("moon"));
		setHitBox(0, 0, width, height);
	}
	
	public void update(GameContainer container, int delta) throws SlickException {
		Entity sun = this.world.find("Sun");
		if (sun != null && sun.visible )
			this.visible = false;
		else
			this.visible = true;
	}

}
