package com.rightanglegames.starcleaner;

import it.randomtower.engine.World;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TitleState extends World {

	public TitleState(int id) {
		super(id);
	}

	private Title myTitle = null;
	private MessageWindow myMessage = null;
	
	private String[] titlemsg = {
			"Welcome to Star Cleaner!",
			"",
			"Collect all stars and avoid",
			"all obstacles!",
			"Press X, W, or Up to jump,",
			"A and D or Left and right",
			"to move.",
			"",
			"Press X, W, or Up to start!"
	};
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		StarCleaner.initRessources();
		super.init(container, game);
		container.setTargetFrameRate(60);
	}

	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
//		this.debugEnabled = true;
		this.clear();
		myTitle = new Title(0,0);
		myMessage = new MessageWindow(new ArrayList<String>(Arrays.asList(titlemsg)), 310, 210, false);
		this.add(myTitle);
		this.add(myMessage);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// render all entities
		super.render(container, game, g);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// update all entities
		super.update(container, game, delta);
	}
}
