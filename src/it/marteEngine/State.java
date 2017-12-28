package it.marteEngine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface State {

	void init();

	void update(GameContainer container, int delta);

	void render(Graphics g);

	String getName();

}
