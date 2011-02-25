package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.geom.Vector2f;


public class Globals {
	public static StarCleaner game = null;

	public static boolean playerDead = false;
	public static boolean levelDone = false;
	public static boolean blenderDone = false;
	
	public static LightMap lightMap = null;
	public static Entity messageWindow = null;
	
	public static Vector2f originalPlayerMaxSpeed = null;
	
	public static boolean playerCheated = false;
}
