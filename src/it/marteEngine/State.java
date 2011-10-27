package it.marteEngine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface State {

	public void init();

	public void update(GameContainer container, int delta);

	public void render(Graphics g);

}