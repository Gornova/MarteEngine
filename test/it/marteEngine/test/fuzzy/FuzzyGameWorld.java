package it.marteEngine.test.fuzzy;

import it.marteEngine.Camera;
import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.game.starcleaner.Background;

import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;


public class FuzzyGameWorld extends World {

	private int stars;
	private int total = -1;
	private boolean levelEnd = false;

	private int levelIndex = 1;
	private int levelNumbers = 3;
	private boolean gameEnd = false;
	private boolean showTutorialPanel=true;
	private Sound allpickedup;
	public static boolean playerDead = false;
	private boolean volumeOn = true;
	private Rectangle volumeControl = new Rectangle(600, 5, 32, 34);
	
	public FuzzyGameWorld(int id) {
		super(id);
		
		allpickedup = ResourceManager.getSound("allpickedup");
		
		ME.ps = new ParticleSystem(ResourceManager.getImage("particle"));		
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		
		playerDead = false;
		
		TiledMap map = ResourceManager.getMap("map"+levelIndex);
		loadEntityFromMap(map, Arrays.asList("entity","background","star"));

		PlatformerEntity player = loadPlayer(map,"player");
		setCamera(new Camera(this, player, container.getWidth(), container.getHeight()));
		
		// make the world a bit bigger than the screen to force camera scrolling		
		computeWorldSize(map.getWidth(),map.getHeight());
		
		add(new Background(0, 0),BELOW);
	}
	
	private void computeWorldSize(int width, int height) {
		this.setWidth(width * 32);
		this.setHeight(height * 32);		
	}

	/**
	 * Load player position from layer with given name
	 * @param map
	 * @param layerName
	 * @return 
	 * @throws SlickException 
	 */
	private PlatformerEntity loadPlayer(TiledMap map, String layerName) throws SlickException {
		int layerIndex = map.getLayerIndex(layerName);
		for (int w = 0; w < map.getWidth(); w++) {
			for (int h = 0; h < map.getHeight(); h++) {
				Image img = map.getTileImage(w, h, layerIndex);
				if (img!=null){
					int x = h * img.getWidth();
					int y = w * img.getHeight() ;
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
		String text = "Collected stars "+(total-stars)+"/"+total;
		ME.showMessage(container, g, 5, 5, 200, 35, 5, Color.darkGray, text,5);
		
		if (volumeOn){
			g.drawImage(ResourceManager.getImage("volumeOn"), 600, 5);
		} else {
			g.drawImage(ResourceManager.getImage("volumeOff"), 600, 5);
		}
		
		if (showTutorialPanel){
			String instructions = "Press WASD or ARROWS to move, X or UP to Jump";
			ME.showMessage(container, g, 65, 440, 430, 35, 5, Color.darkGray, instructions,5);
		}
		
		if (levelEnd){
			ME.showMessage(container, g, 100, 200, 430, 35, 5, Color.darkGray, "LEVEL COMPLETED, press space to continue",5);
			if (!allpickedup.playing()){
				allpickedup.play();
			}
		}
		if (gameEnd){
			ME.showMessage(container, g, 100, 200, 430, 35, 5, Color.darkGray, "GAME COMPLETED, press space to continue",5);		
		} 		
		
		if (playerDead){
			ME.showMessage(container, g, 100, 200, 430, 35, 5, Color.darkGray, "YOU LOSE, press space to continue",5);
		}
	}	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (volumeOn){
			ResourceManager.setMusicVolume(1.0f);
			ResourceManager.setSfxVolume(1.0f);
		} else {
			ResourceManager.setMusicVolume(0f);
			ResourceManager.setSfxVolume(0f);
		}
		
		if (gameEnd){
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)){
				Log.info("Start from first level...");
				levelIndex=0;
				gameEnd = false;
				nextLevel(container,game);
			}
			return;
		}
		if (levelEnd){
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)){
				Log.info("Load next level...");
				nextLevel(container,game);
			}
			return;
		}
		if (playerDead){
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)){
				Log.info("Load next level...");
				reloadLevel(container,game);
			}
			return;
		}
		super.update(container, game, delta);
		
		if (container.getInput().isKeyPressed(Input.KEY_F1)){
			showTutorialPanel = showTutorialPanel ? false : true;
		}
		
		
		stars = getNrOfEntities(Star.STAR);
		if (total < 0){
			total = stars;
		}
		
		if (stars==0){
			Log.info("Level end");
			levelEnd = true;
			if (levelIndex++ == levelNumbers){
				gameEnd  = true;
				levelEnd = false;
			}
		}
		
		//volume on/off
		if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if (volumeControl.contains(container.getInput().getMouseX(), container.getInput().getMouseY())){
				volumeOn = volumeOn ? false : true;
			}
		}
		
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			game.enterState(FuzzyMain.MENU_STATE, new FadeOutTransition(), new FadeInTransition());
		}
	}
	
	private void nextLevel(GameContainer container, StateBasedGame game) throws SlickException{
		clear();
		levelIndex++;
		total = -1;
		levelEnd = false;
		playerDead = false;		
		init(container, game);
	}
	
	private void reloadLevel(GameContainer container, StateBasedGame game) throws SlickException{
		clear();
		total = -1;
		levelEnd = false;
		playerDead = false;
		init(container, game);
	}	

}
