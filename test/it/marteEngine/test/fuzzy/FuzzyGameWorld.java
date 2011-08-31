package it.marteEngine.test.fuzzy;

import it.marteEngine.Camera;
import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.actor.StaticActor;
import it.marteEngine.entity.Alarm;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.game.starcleaner.Background;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

public class FuzzyGameWorld extends World {

	private static final int TILESIZE = 32;
	public static final int NO_SOLID = -1;
	private static final int SOLID = 1;
	public static int stars;
	public static int total = -1;
	private boolean levelEnd = false;

	// level game starts from
	private int levelIndex = 1;
	// number of levels (always levelIndex+1)
	private int levelNumbers = 12;
	// prefix for map names
	private static final String LEVEL_PREFIX = "level";
	private static final String FADE_TUTORIAL = "fadeTutorial";

	private boolean gameEnd = false;
	private boolean showTutorialPanel = true;
	public static boolean playerDead = false;
	private Rectangle volumeControl = new Rectangle(600, 5, 32, 34);
	private int widthInTiles;
	private int heightInTiles;
	private int[][] blocked;

	private long time = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

	private Music musicOne;
	private Sound soundVictory;

	private Image heart;
	private int starsNumber;
	private Alarm fadeTutorial;
	private boolean victory;

	public static int points = 0;

	public FuzzyGameWorld(int id) {
		super(id);

		try {
			ME.ps = ParticleIO
					.loadConfiguredSystem("data/fuzzy/invulnerableEmitter.xml");
			ME.renderParticle = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);

		stars = 0;
		starsNumber = 0;
		playerDead = false;

		int lastLevel = FuzzyUtil.loadLevel();
		if (lastLevel != -1) {
			levelIndex = lastLevel;
		}

		TiledMap map = ResourceManager.getMap(LEVEL_PREFIX + levelIndex);
		Log.info("Load map" + levelIndex);
		// make the world a bit bigger than the screen to force camera scrolling
		computeWorldSize(map);
		blocked = new int[widthInTiles][heightInTiles];

		loadEntityFromMap(map, Arrays.asList("entity", "background", "star",
				"enemies", "spikes"));

		PlatformerEntity player = loadPlayer(map, "player");
		// old camera
		// setCamera(new Camera(this, player, container.getWidth(),
		// container.getHeight()));
		setCamera(new Camera(this, player, container.getWidth(),
				container.getHeight(), 550, 170, player.maxSpeed));

		add(new Background(0, 0), BELOW);

		time = 0;

		heart = ResourceManager.getImage("heart").copy();
		FuzzyPlayer.life = 3;

		define("layer", Input.KEY_L);

		if (stars <= 0) {
			stars = starsNumber;
		}
		if (total <= 0) {
			total = stars;
		}

		points = 0;

		if (ResourceManager.getInt("bossLevel") == levelIndex) {
			musicOne = ResourceManager.getMusic("song2");
		} else
			musicOne = ResourceManager.getMusic("song1");

		soundVictory = ResourceManager.getSound("victory");
		victory = true;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

		if (ME.playMusic) {
			musicOne.play();
			musicOne.setVolume(0.5f);
		}
	}

	private void computeWorldSize(TiledMap map) {
		if (map == null)
			return;
		int width = map.getWidth();
		int height = map.getHeight();
		this.widthInTiles = width;
		this.heightInTiles = height;
		this.setWidth(width * 32);
		this.setHeight(height * 32);
	}

	/**
	 * Load entity from a tiled map into current World
	 * 
	 * @param map
	 * @throws SlickException
	 */
	public void loadEntityFromMap(TiledMap map, List<String> types)
			throws SlickException {
		if (map == null) {
			Log.error("unable to load map information");
			return;
		}
		if (types == null || types.isEmpty()) {
			Log.error("no types defined to load");
			return;
		}
		starsNumber = 0;
		for (String type : types) {
			// try to find a layer with property type set to entity
			int layerIndex = -1;
			for (int l = 0; l < map.getLayerCount(); l++) {
				String value = map.getLayerProperty(l, "type", null);
				if (value != null && value.equalsIgnoreCase(type)) {
					layerIndex = l;
					break;
				}
			}
			if (layerIndex != -1) {
				Log.debug("Entity layer found on map");
				int loaded = 0;
				for (int w = 0; w < map.getWidth(); w++) {
					for (int h = 0; h < map.getHeight(); h++) {
						int tid = map.getTileId(w, h, layerIndex);
						Image img = map.getTileImage(w, h, layerIndex);
						if (type.equals("entity"))
							blocked[w][h] = NO_SOLID;
						if (img != null) {
							if (type.equalsIgnoreCase("background")) {
								// background
								StaticActor te = new StaticActor(w
										* img.getWidth(), h * img.getHeight(),
										img.getWidth(), img.getHeight(), img);
								te.collidable = false;
								te.depth = -100;
								te.collidable = false;
								te.setAlpha(0.4f);
								add(te);
							} else if (type.equalsIgnoreCase("star")) {
								starsNumber++;
								// stars
								Star star = new Star(w * img.getWidth(), h
										* img.getHeight());
								add(star);
							} else if (type.equalsIgnoreCase("enemies")) {
								String enemyType = map.getTileProperty(tid,
										"type", null);
								if (enemyType != null) {
									if (enemyType.equalsIgnoreCase("slime")) {
										// slime
										add(new FuzzyGreenSlime(w * 32, h * 32));
									} else if (enemyType
											.equalsIgnoreCase("bat")) {
										// slime
										add(new FuzzyBat(w * 32, h * 32));
									} else if (enemyType
											.equalsIgnoreCase("arrowTrap")) {
										// slime
										add(new FuzzyArrowTrap(w * 32, h * 32));
									} else if (enemyType
											.equalsIgnoreCase("boss1")) {
										// slime
										add(new FuzzyBoss(w * 32, h * 32));
									}
								}
							} else {
								String tileType = map.getTileProperty(tid,
										"type", null);
								if (tileType != null
										&& tileType.equals("spikes")) {
									// spike
									Spike spike = new Spike(w * img.getWidth(),
											h * img.getHeight());
									add(spike);
								} else if (tileType != null
										&& tileType.equals("fuzzyBlock")) {
									// fuzzyBlock
									FuzzyBlock fz = new FuzzyBlock(w
											* img.getWidth(), h
											* img.getHeight(), img);
									add(fz);
								} else if (tileType != null
										&& tileType.equals("tappo")) {
									// FuzzyDestroyableBlock
									FuzzyDestroyableBlock fd = new FuzzyDestroyableBlock(
											w * img.getWidth(), h
													* img.getHeight(), img);
									add(fd);
								} else if (tileType != null
										&& tileType.equals("targetBlock")) {
									// targetBlock
									TargetBlock fz = new TargetBlock(w
											* img.getWidth(), h
											* img.getHeight(), img);
									add(fz);
								} else {

									// blocks
									StaticActor te = new StaticActor(w
											* img.getWidth(), h
											* img.getHeight(), img.getWidth(),
											img.getHeight(), img);
									if (type.equals("entity")) {
										blocked[w][h] = SOLID;
									}
									add(te);
								}
							}
							loaded++;
						}
					}
				}
				Log.debug("Loaded " + loaded + " entities");
			} else {
				// Log.info("Entity layer not found on map");
			}
		}
	}

	/**
	 * Load player position from layer with given name
	 * 
	 * @param map
	 * @param layerName
	 * @return
	 * @throws SlickException
	 */
	private PlatformerEntity loadPlayer(TiledMap map, String layerName)
			throws SlickException {
		if (map == null)
			return null;
		int layerIndex = map.getLayerIndex(layerName);
		for (int w = 0; w < map.getWidth(); w++) {
			for (int h = 0; h < map.getHeight(); h++) {
				Image img = map.getTileImage(w, h, layerIndex);
				if (img != null) {
					int x = w * img.getWidth();
					int y = h * img.getHeight();
					// create player & camera
					FuzzyPlayer player = new FuzzyPlayer(x, y, "player");
					add(player);
					return player;
				}
			}
		}
		return null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);

		// render gui
		String text = "Stars " + (total - stars) + "/" + total;
		ME.showMessage(container, g, 5, 5, 95, 35, 5, Color.darkGray, text, 5);

		int base = 120;
		for (int i = 0; i <= FuzzyPlayer.life - 1; i++) {
			g.drawImage(heart, base + i * 50, 5);
		}

		text = "Points " + points;
		ME.showMessage(container, g, 270, 5, 140, 35, 5, Color.darkGray, text,
				5);

		text = "Time " + sdf.format(new Date(time));
		ME.showMessage(container, g, 480, 5, 110, 35, 5, Color.darkGray, text,
				5);

		if (!ME.playMusic) {
			g.drawImage(ResourceManager.getImage("volumeOff"), 600, 5);
		} else {
			g.drawImage(ResourceManager.getImage("volumeOn"), 600, 5);
		}

		if (showTutorialPanel) {
			String instructions = "Press WASD/ARROWS to move, X/UP to Jump, M mute/unmute music";
			ME.showMessage(container, g, 40, 440, 550, 35, 5, Color.darkGray,
					instructions, 5);
			if (fadeTutorial == null) {
				fadeTutorial = new Alarm(FADE_TUTORIAL, 600, true);
				fadeTutorial.start();
			}
		}

		if (levelEnd) {
			ME.showMessage(container, g, 100, 200, 430, 35, 5, Color.darkGray,
					"LEVEL COMPLETED, press space to continue", 5);
			// if (!allpickedup.playing()) {
			// allpickedup.play();
			// }
		}
		if (gameEnd) {
			ME.showMessage(container, g, 100, 200, 430, 35, 5, Color.darkGray,
					"GAME COMPLETED, press space to continue", 5);
		}

		if (playerDead) {
			ME.showMessage(container, g, 100, 200, 430, 35, 5, Color.darkGray,
					"YOU LOSE, press space to continue", 5);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		ME.muteMusic();
		ME.renderParticle = true;

		if (gameEnd) {
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
				Log.info("Start from first level...");
				levelIndex = 0;
				gameEnd = false;
				nextLevel(container, game);
			}
			return;
		}
		if (levelEnd) {
			if (musicOne.playing()) {
				musicOne.stop();
			}
			if (victory && !soundVictory.playing()) {
				victory = false;
				soundVictory.play();
			}
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
				Log.info("Load next level...");
				nextLevel(container, game);
			}
			return;
		}
		if (playerDead) {
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
				Log.info("Load next level...");
				reloadLevel(container, game);
			}
			return;
		}
		super.update(container, game, delta);
		time += delta;

		if (container.getInput().isKeyPressed(Input.KEY_F1)) {
			showTutorialPanel = showTutorialPanel ? false : true;
		}

		if (stars == 0 && getCount() > 0) {
			Log.info("Level end");
			levelEnd = true;
			if (levelIndex + 1 > levelNumbers) {
				gameEnd = true;
				levelEnd = false;
			}
		}

		// volume on/off
		if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (volumeControl.contains(container.getInput().getMouseX(),
					container.getInput().getMouseY())) {
				ME.playMusic = ME.playMusic ? false : true;
			}
		}

		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			game.enterState(FuzzyMain.MENU_STATE, new FadeOutTransition(),
					new FadeInTransition());
		}

		if (ME.playMusic && !musicOne.playing()) {
			musicOne.play();
		}

		if (pressed("layer")) {
			Log.info("layer switch!");
			switchLayer();
		}

		if (fadeTutorial != null && fadeTutorial.update(delta)) {
			fadeTutorial = null;
			showTutorialPanel = false;
		}

	}

	private void nextLevel(GameContainer container, StateBasedGame game)
			throws SlickException {
		clear();
		stars = -1;
		levelIndex++;
		FuzzyUtil.saveLevel(levelIndex);
		if (levelIndex < levelNumbers) {
			total = -1;
			levelEnd = false;
			playerDead = false;
			init(container, game);
		} else {
			// level finished, player have won!
			game.enterState(FuzzyMain.WIN_STATE, new FadeOutTransition(),
					new FadeInTransition());
			return;
		}
	}

	private void reloadLevel(GameContainer container, StateBasedGame game)
			throws SlickException {
		clear();
		starsNumber = 0;
		stars = -1;
		total = -1;
		levelEnd = false;
		playerDead = false;
		init(container, game);
	}

	public boolean blocked(float x, float y) {
		int tx = (int) x / TILESIZE;
		int ty = (int) y / TILESIZE;

		if (tx < 0 || tx >= widthInTiles)
			return false;
		if (ty < 0 || ty >= heightInTiles)
			return false;
		return blocked[tx][ty] != NO_SOLID;
	}

	private void switchLayer() {
		for (Entity entity : getEntities()) {
			if (entity instanceof StaticActor) {
				entity.collidable = entity.collidable ? false : true;
				if (entity.collidable) {
					entity.setAlpha(1);
				} else {
					entity.setAlpha(0.4f);
				}
			}
		}
	}

	public static void addPoints(int i) {
		FuzzyGameWorld.points += 100;
	}

	public Vector2f getPlayerCenter() {
		Entity ent = find(FuzzyPlayer.PLAYER);
		if (ent != null) {
			return new Vector2f(ent.x + ent.width / 2, ent.y + ent.height / 2);
		}
		return null;
	}

}
