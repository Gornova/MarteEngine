package com.rightanglegames.starcleaner;

import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


public class Blender extends Entity {

	public static final String BLENDER_TYPE = "Blender";
	
	private Color blendColor = null;
	int alpha, millis;
	int milliStep;
	int milliCount;
	Rectangle rect;
	
	public Blender(float x, float y, int width, int height, Color col, int millisecondsToBlend) {
		super(x, y);
		depth = 255;	// on top of everything
		this.addType(BLENDER_TYPE);
		blendColor = new Color(col);
		this.alpha = 0;
		this.millis = millisecondsToBlend;
		this.milliStep = millisecondsToBlend / 255;
		if (milliStep <= 0)
			milliStep = 1;
		milliCount = 0;
		rect = new Rectangle(0, 0, width, height);
	}
	
	public void update(GameContainer container, int delta) throws SlickException {
		milliCount += delta;
		while (milliCount > milliStep) {
			milliCount -= milliStep;
			millis -= milliStep;
			alpha++;
			if (alpha > 255)
				alpha = 255;
		}
		if (millis <= 0)
			Globals.blenderDone = true;
	}
	
	public void render(GameContainer container, Graphics g) {
		blendColor.a = (float) alpha / 255.0f;
		g.setColor(blendColor);
		g.fill(rect);
		g.draw(rect);
	}
}
