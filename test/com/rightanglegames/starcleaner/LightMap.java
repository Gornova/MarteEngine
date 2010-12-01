package com.rightanglegames.starcleaner;

import it.randomtower.engine.ME;
import it.randomtower.engine.entity.Entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

public class LightMap extends Entity {
	
	public static final int DAWN = 0;
	public static final int DAY = 4;
	public static final int EVENING = 16;
	public static final int NIGHT = 20;
	public static final int ENDOFDAY = 29;
	
	// dawn is between 0 and 4 (5 seconds), day is between 5 and 14, evening between 15 and 19 and night between 20 and 29
	private int dayTime = 4;	// we'll start with day
	private int incrDayTime = 1000;	// increment daytime every second
	private int counterDayTime = 0;
	
	private boolean colouredLights = true;
	private boolean lightMapChanged = true;
	
	private Image whiteSquare;
	
	private ArrayList<Light> lights = new ArrayList<Light>();
	/** 
	 * The values calculated for each vertex of the tile map, 
	 * note how it's one more to account for the bottom corner of the map. 
	 * The 3 dimension is for colour components (red, green, blue) used for coloured lighting
	 */
	private float[][][] roughLightValues;
	private float[][][] smoothLightValues;
	
	private int mapWidth, mapHeight, mapTileSize;
	
	public LightMap(float x, float y, int tilesize) {
		super(x, y);
		mapTileSize = tilesize;
		mapWidth = StarCleaner.WIDTH / tilesize;
		mapHeight = StarCleaner.HEIGHT / tilesize;
		roughLightValues = new float[mapWidth+1][mapHeight+1][4];
		smoothLightValues = new float[mapWidth+1][mapHeight+1][4];
		depth = 250; 	// lightmap is on top of all of them, but below the blender
		whiteSquare = createImage(tilesize);
	}

	
	private Image createImage(int tilesize) {
		ImageBuffer buf = new ImageBuffer(tilesize, tilesize);
		for (int x = 0; x < tilesize; x++)
			for (int y = 0; y < tilesize; y++)
				buf.setRGBA(x, y, 255, 255, 255, 255);
		return buf.getImage();
	}


	public void update(GameContainer container, int delta) throws SlickException {
		counterDayTime += delta;
		if (counterDayTime >= incrDayTime) {
			counterDayTime -= incrDayTime;
			dayTime++;
			lightMapChanged = true;
			if (dayTime > ENDOFDAY)
				dayTime = 0;
		}
		if (lightMapChanged) {
			updateLightMap();
			lightMapChanged = false;
		}
	}

	// do nothing during day time
	public void updateLightMap() {
//		Log.debug("updateLightMap() is called, dayTime is " + dayTime);
		if (dayTime >= DAWN && dayTime < DAY) {
			fillLightMap(DAY-dayTime, DAY-DAWN);
			return;
		} else if (dayTime >= EVENING && dayTime < NIGHT) {
			fillLightMap(dayTime-EVENING, NIGHT-EVENING);
			return;
		}
		// for every vertex on the map (notice the +1 again accounting for the trailing vertex)
		for (int y=0;y<mapHeight+1;y++) {
			for (int x=0;x<mapWidth+1;x++) {
				// first reset the lighting value for each component (red, green, blue, alpha)
				for (int component=0;component<4;component++) {
					roughLightValues[x][y][component] = 0;
				}
				
				// next cycle through all the lights. Ask each light how much effect
				// it'll have on the current vertex. Combine this value with the currently
				// existing value for the vertex. This lets us blend coloured lighting and 
				// brightness
				for (int i=0;i<lights.size();i++) {
					float[] effect = ((Light) lights.get(i)).getEffectAt(x*mapTileSize, y*mapTileSize, colouredLights);
//					if (effect[0] != 0 || effect[1] != 0 || effect[2] != 0)
//						Log.debug("Light " + i + ": x = " + x + ", y = " + y + ", effect[0]-[3]=" + effect[0] + ", " + effect[1] + ", " + effect[2] + ", " + effect[3]);
					for (int component=0;component<3;component++) {
						roughLightValues[x][y][component] += effect[component];
					}
					// alpha value is treated different, not added but the min value wins
					if (effect[3] > 0 )
						if (roughLightValues[x][y][3] > 0)
							roughLightValues[x][y][3] = Math.min(effect[3], roughLightValues[x][y][3] );
						else
							roughLightValues[x][y][3] = effect[3];
				}
				
				// finally clamp the components to 1, since we don't want to 
				// blow up over the colour values
				for (int component=0;component<4;component++) {
					if (roughLightValues[x][y][component] > 1) {
						roughLightValues[x][y][component] = 1;
					}
				}
			}
		}

		// smooth the light map
		float smooth = 1 / 9f;
		for (int x=0;x<mapWidth+1;x++) {
			for (int y=0;y<mapHeight+1;y++) {
				for (int i=0;i<3;i++) {
					smoothLightValues[x][y][i] = 0;
					for (int a=-1;a<2;a++) {
						for (int b=-1;b<2;b++) {
							if ((x+a) >= 0 && (y+b) >= 0 && (x+a)<mapWidth+1 && (y+b) < mapHeight+1)
								smoothLightValues[x][y][i] += (roughLightValues[x+a][y+b][i]*smooth);
						}
					}
				}
				smoothLightValues[x][y][3] = roughLightValues[x][y][3];
			}
		}
	}
	
	private void fillLightMap(int value, int range) {
//		Log.debug("fillLightMap() is called, dayTime is " + dayTime + ", value is " + value + ", range is " + range);

		float greyStep = 1.0f / (range+1);
		float greyVal = value * greyStep;
		
		for (int y=0;y<mapHeight+1;y++) {
			for (int x=0;x<mapWidth+1;x++) {
				// first reset the lighting value for each component (red, green, blue, alpha)
				for (int component=0;component<3;component++) {
//					lightValues[x][y][component] = 0;
					smoothLightValues[x][y][component] = 0;
				}
//				lightValues[x][y][3] = greyVal;
				smoothLightValues[x][y][3] = greyVal;
			}
		}
	}


	public void render(GameContainer container, Graphics g) throws SlickException {
		super.render(container, g);
		if (dayTime >= EVENING || dayTime < DAY) {
			// only during dawn, evening and night do we have some work to do.
			for (int y=0;y<mapHeight;y++) {
				for (int x=0;x<mapWidth;x++) {
					g.setColor(Color.white);
					// if lighting is on apply the lighting values we've 
					// calculated for each vertex to the image. We can apply
					// colour components here as well as just a single value.
					whiteSquare.setColor(Image.TOP_LEFT, smoothLightValues[x][y][0], smoothLightValues[x][y][1], smoothLightValues[x][y][2], smoothLightValues[x][y][3]);
					whiteSquare.setColor(Image.TOP_RIGHT, smoothLightValues[x+1][y][0], smoothLightValues[x+1][y][1], smoothLightValues[x+1][y][2], smoothLightValues[x+1][y][3]);
					whiteSquare.setColor(Image.BOTTOM_RIGHT, smoothLightValues[x+1][y+1][0], smoothLightValues[x+1][y+1][1], smoothLightValues[x+1][y+1][2], smoothLightValues[x+1][y+1][3]);
					whiteSquare.setColor(Image.BOTTOM_LEFT, smoothLightValues[x][y+1][0], smoothLightValues[x][y+1][1], smoothLightValues[x][y+1][2], smoothLightValues[x][y+1][3]);
					// draw the image with it's newly declared vertex colours
					// to the display
					whiteSquare.draw(x*mapTileSize,y*mapTileSize,mapTileSize,mapTileSize);
				}
			}
		}
		if (ME.debugEnabled) {
			g.setColor(Color.white);
			g.drawString("Lights: " + lights.size(), container.getWidth() - 110, 100);
			g.drawString("dayTime: " + dayTime, container.getWidth() - 110, 120);
		}
	}

	public int getDayTime() {
		return dayTime;
	}
	
	public boolean isDay() {
		if (dayTime >= DAY && dayTime < NIGHT)
			return true;
		return false;
	}
	
	public boolean isNight() {
		if (dayTime >= NIGHT || dayTime < DAY)
			return true;
		return false;
	}
	
	public void lightMapChanged() {
		lightMapChanged = true;
	}
	
	public void addLight(Light light) {
		lightMapChanged = true;
		light.setLightMap(this);
		lights.add(light);
		
	}
	
	public void removeLight(Light light) {
		lightMapChanged = true;
		lights.remove(light);
	}
	
}
