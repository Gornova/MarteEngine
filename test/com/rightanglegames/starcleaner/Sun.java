package com.rightanglegames.starcleaner;

import it.randomtower.engine.RessourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;


public class Sun extends Entity {
	
	public static final String SUN_AND_MOON = "SunAndMoon";

	public Sun(float x, float y) {
		super(x, y);
		this.name = "Sun";
		this.addType(SUN_AND_MOON);
		depth = 1;
		setGraphic( RessourceManager.getImage("sun"));
		setHitBox(0, 0, width, height);
	}

	public void update(GameContainer container, int delta) throws SlickException {
		if (Globals.lightMap.isNight())
			this.visible = false;
		else
			this.visible = true;
	}
}
