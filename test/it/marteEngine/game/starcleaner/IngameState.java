package it.marteEngine.game.starcleaner;

import it.marteEngine.World;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class IngameState extends World {

	public IngameState(int id) {
		super(id);
	}

	public int levelNo = 1;
	public int score = 0;
	public int timeLeft = 0;
	public Level currentLevel = null;
	private int timer = 0;
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		StarCleaner.initRessources();
		super.init(container, game);
	}

	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		this.resetGame();
//		this.debugEnabled = true;
	}


	private void resetGame() throws SlickException {
		this.levelNo = 1;
		this.score  = 0;
		this.resetLevel();
		this.timeLeft = this.currentLevel.timeToFinish;
		timer = 0;
	}
	
	private void resetLevel() throws SlickException {
		this.clear();
		// add the hud
		this.add(new Hud(1, 1));
		
		this.currentLevel = Level.load(levelNo, this);
		// check which level we loaded
		this.levelNo = this.currentLevel.levelNo;
		// if we completed the last level we start with the full time for the next level
		if (Globals.levelDone)
			this.timeLeft = this.currentLevel.timeToFinish;
		if (Globals.playerCheated) {
			this.score = 0;
			// reset score to 0 every new level. cheating is not nice and won't be forgotten ;-)
		}
		Globals.playerDead = false;
		Globals.levelDone = false;
		Globals.blenderDone = false;
		Globals.messageWindow = null;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// render all entities
		super.render(container, game, g);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// update all entities
		super.update(container, game, delta);
		timer += delta;
		if (timer >= 1000) {
			timer -= 1000;
			timeLeft --;
			if (timeLeft < 0)
				timeLeft = 0;	// no more bonus points when time is over
		}
		if (Globals.playerDead || Globals.levelDone) {
			if (this.getNrOfEntities(Blender.BLENDER_TYPE) == 0) {
				Globals.blenderDone = false;
				this.add(new Blender(0,0,StarCleaner.WIDTH, StarCleaner.HEIGHT, Color.white, 1000));
			}
			if (Globals.blenderDone) {
				if (Globals.levelDone) {
					this.levelNo ++;
					this.score += (1000 + timeLeft * 10);
				}
				this.resetLevel();
			}
		}

	}

	@Override
	public int getID() {
		return StarCleaner.INGAME_STATE;
	}

}
