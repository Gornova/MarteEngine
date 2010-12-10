package com.rightanglegames.starcleaner;

import org.newdawn.slick.Color;

/**
 * A single light in the example. It's capable of determining how much effect
 * it will have in any given point on the tile map. Note that all coordinates
 * are given in pixel coordinates and not in tile coordinates.
 * 
 * @author kevin
 */
class Light {
	/** The x coordinate of the position the light has in the world */
	private float xpos;
	/** The y coordinate of the position the light has in the world */
	private float ypos;
	/** The intensity of the light, this specifies in pixels how far the light will shine */
	private float intensity; 
	/** The color the light should apply */
	private Color col;
	
	private LightMap lightMap;
	
	/**
	 * Create a new light in the world
	 * 
	 * @param x The x coordinate of the position the light has in the world
	 * @param y The y coordinate of the position the light has in the world
	 * @param str The strength of the light, this specifies in pixels how far the light will shine
	 * @param col The color the light should apply
	 */
	public Light(float x, float y, float intensity, Color col) {
		xpos = x;
		ypos = y;
		this.intensity = intensity;
		this.col = col;
	}
	
	/**
	 * Set the location of the light in the world
	 * 
	 * @param x The x coordinate of the position the light has in the world
	 * @param y The y coordinate of the position the light has in the world
	 */
	public void setLocation(float x, float y) {
		xpos = x;
		ypos = y;
	}
	
	/**
	 * Get the effect the light should apply to a given location
	 * 
	 * @param x The x coordinate of the location being considered for lighting
	 * @param y The y coordinate of the location being considered for lighting
	 * @param colouredLights True if we're supporting coloured lights
	 * 
	 * @return The effect on a given location of the light in terms of colour components (all
	 * the same if we don't support coloured lights)
	 */
	public float[] getEffectAt(float x, float y, boolean colouredLights) {
		// first work out what propotion of the strength distance the light
		// is from the point. This is a value from 0-1 where 1 is the centre of the
		// light (i.e. full brightness) and 0 is the very edge (or outside) the lights
		// range
		float dx = (x - xpos);
		float dy = (y - ypos);
		float distance = (float)Math.hypot(dx, dy);
		float effect = 1.0f - (distance / intensity);
		
		if (effect < 0) {
			effect = 0;
		}
		if (effect > 1)
			effect = 1;
        // if we doing coloured lights then multiple the colour of the light by the effect.
        // Otherwise just use calculated alpha and clear all color components
        if (colouredLights) {
            // when using colored, stop the falloff in the center so light can be colored and not transparent
            float alpha = 1.0f- effect;
            if (alpha < 0)
            	alpha = 0;
            return new float[] {
                            col.r * effect,
                            col.g * effect,
                            col.b * effect,
                            alpha };
        }
        else {
            return new float[] { 0, 0, 0, 1-effect };
        }
	}

	public LightMap getLightMap() {
		return lightMap;
	}

	public void setLightMap(LightMap lightMap) {
		this.lightMap = lightMap;
	}
}
