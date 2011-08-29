package it.marteEngine.entity;

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
		this.setText(text);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (font == null) {
			font = container.getDefaultFont();
			this.calculateHitBox();
		}
		g.setFont(font);
		if (text != null)
			g.drawString(text, x, y);
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.calculateHitBox();
	}

	private void calculateHitBox() {
		if (font != null && text != null) {
			int w = font.getWidth(text);
			int h = font.getHeight(text);
			this.setHitBox(0, 0, w, h);
		}
	}
}
