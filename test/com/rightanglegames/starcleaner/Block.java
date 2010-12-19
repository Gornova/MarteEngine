package com.rightanglegames.starcleaner;

import org.newdawn.slick.SlickException;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Solid;

public class Block extends Solid {

	public Block(float x, float y, int w, int h) throws SlickException {
		super(x, y, w, h);
		name = "block";
		depth = 5;
		currentImage = ResourceManager.getImage("block");
	}

}
