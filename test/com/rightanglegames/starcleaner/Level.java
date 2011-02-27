package com.rightanglegames.starcleaner;

import it.randomtower.engine.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;


public class Level {
	public static final char BLOCK = '=';
	public static final char STAR = '*';
	public static final char ANGEL = 'P';
	public static final char CROW = '>';
	public static final char CLOUDLEFT = 'w';
	public static final char CLOUDRIGHT = 'W';
	public static final char SUNANDMOON = 'O';
	public static final char SPIKES_UP = '^';
	public static final char SPIKES_DOWN = 'v';
	public static final char SIGN = '?';
	public static final char PLATFORM = '-';
	
	
	private ArrayList<String> lines = null;
	private int nrOfStars = 0;
	public int levelNo = 0;
	public int timeToFinish = 0;
	public ArrayList<String> message = new ArrayList<String>();

	public static Level load(int levelNo, World world) throws SlickException {
		world.add(new Background(0,0));

		// now load the level file and create the entities from the file
		DecimalFormat formatter = new DecimalFormat("###000");
		String level = "data/starcleaner/res/levels/level" + formatter.format(levelNo) + ".dat";

		boolean levelExists = true;
		try {
			ResourceLoader.getResourceAsStream(level);
		} catch (RuntimeException e) {
			levelExists = false;
		}
		if (!levelExists) {
			levelNo = 1;
			level = "data/starcleaner/res/levels/level" + formatter.format(levelNo) + ".dat";
			
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsStream(level)));
		Level loadedLevel = new Level();
		int width = 0;
		loadedLevel.nrOfStars = 0;
		loadedLevel.lines = new ArrayList<String>();
		loadedLevel.levelNo = levelNo;
		
		// read through the lines recording them into a list and
		// determining the maximum width.
		int lineno = 0;
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				lineno ++;
				if (line == null) {
					break;
				}
			
				if (lineno <= 12) {
					width = Math.max(line.length(), width);
					loadedLevel.lines.add(line);
				} else if (lineno == 13) {
					loadedLevel.timeToFinish = Integer.parseInt(line);
				} else if (lineno > 13) {
					loadedLevel.message.add(line);
				}
			}
		} catch (IOException e) {
			throw new SlickException("Failed to load map: "+level, e);
		}
		
		int height = loadedLevel.lines.size();
		
		loadedLevel.createEntities(loadedLevel, width, height, world);
		
		return loadedLevel;
	}

	private void createEntities(Level level, int width, int height, World world) throws SlickException {
		// create the lightmap entity
		LightMap lightMap = new LightMap(0,0, 20);
		Globals.lightMap = lightMap;
		world.add(lightMap);
		
		Light light;
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				char c = lines.get(y).charAt(x);
				float xpos = x * StarCleaner.TILESIZE;
				float ypos = y * StarCleaner.TILESIZE;
				
				switch (c) {
				case BLOCK:
					world.add(new Block(xpos, ypos, StarCleaner.TILESIZE, StarCleaner.TILESIZE));
					break;
				case STAR:
					light = new Light(xpos + StarCleaner.TILESIZE / 2, ypos + StarCleaner.TILESIZE / 2, 120f, Color.yellow);
					lightMap.addLight(light);
					world.add(new Star(xpos, ypos, light));
					level.nrOfStars ++;
					break;
				case ANGEL:
					light = new Light(xpos + StarCleaner.TILESIZE / 2, ypos + StarCleaner.TILESIZE / 2, 60f, Color.yellow);
					lightMap.addLight(light);
					world.add(new Angel(xpos, ypos, light));
					break;
				case CROW:
					world.add(new Crow(xpos, ypos));
					break;
				case CLOUDLEFT:
					world.add(new Cloud(xpos, ypos, false));
					break;
				case CLOUDRIGHT:
					world.add(new Cloud(xpos, ypos, true));
					break;
				case SUNANDMOON:
					light = new Light(xpos + StarCleaner.TILESIZE / 2, ypos + StarCleaner.TILESIZE / 2, 120f, Color.yellow);
					lightMap.addLight(light);
					world.add(new Sun(xpos, ypos));
					world.add(new Moon(xpos, ypos));
					break;
				case SIGN:
					world.add(new MessageSign(xpos, ypos));
					break;
				case SPIKES_UP:
					world.add(new Spikes(xpos, ypos, true));
					break;
				case SPIKES_DOWN:
					world.add(new Spikes(xpos, ypos, false));
					break;
				case PLATFORM:
					world.add(new MovingPlatform(xpos, ypos));
					break;
				}
			}
		}
	}

}
