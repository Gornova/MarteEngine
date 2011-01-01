package it.randomtower.engine.entity;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TextEntity extends Entity {

	private Font font = null;
	private String text = null;
	
	public TextEntity(float x, float y, Font font, String text) {
		super(x, y);
		this.font = font;
		this.text = text;
	}
	
	public void render(GameContainer container, Graphics g)	throws SlickException {
		if (font == null) {
			font = container.getDefaultFont();
		}
		g.setFont(font);
		if (text != null)
			g.drawString(text, x, y);
	}
}
