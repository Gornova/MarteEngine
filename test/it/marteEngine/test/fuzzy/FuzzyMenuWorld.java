package it.marteEngine.test.fuzzy;

import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.Entity;
import it.marteEngine.game.starcleaner.Background;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class FuzzyMenuWorld extends World {

	private AngelCodeFont font;
	public static boolean gotoGame = false;
	
	public FuzzyMenuWorld(int id) {
		super(id);
		font = ResourceManager.getAngelCodeFont("font");
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		add(new Background(0, 0, ResourceManager.getImage("menu")));
		add(new Space(220, 400));
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		gotoGame = false;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);
		
		font.drawString(90, 50, "Fuzzy Platformer");
		
		drawScaled(g,0.5f,"http://randomtower.blogspot.com",200, 880);
	}

	private void drawScaled(Graphics g, float scale, String text, float x, float y) {
		g.scale(scale, scale);
		font.drawString(x,y, text);
		g.resetTransform();
	}	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);
		
		if (gotoGame){
			game.enterState(FuzzyMain.GAME_STATE, new FadeOutTransition(), new FadeInTransition());
		}
	}
	
	private class Space extends Entity{

		private boolean up = false;
		
		public Space(float x, float y) {
			super(x, y);
		}
		
		@Override
		public void addedToWorld() {
			setAlarm("jump", 15, false, true);
		}

		@Override
		public void render(GameContainer container, Graphics g)
				throws SlickException {
			super.render(container, g);
			
			drawScaled(g,0.6f,"Press Space to start",x, y);			
		}

		@Override
		public void alarmTriggered(String name) {
			if (name.equalsIgnoreCase("jump")){
				up = up ? false : true;
			}
		}
		
		@Override
		public void update(GameContainer container, int delta)
				throws SlickException {
			super.update(container, delta);
			
			if (up){
				y--;
			} else {
				y++;
			}
			
			if (container.getInput().isKeyPressed(Input.KEY_SPACE)){
				FuzzyMenuWorld.gotoGame = true;
			}
		}
		
	}
	
}
