package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import java.util.ArrayList;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MessageWindow extends Entity {

	public static final String MESSAGEWINDOW = "messageWindow";
	
	private AngelCodeFont font = null;
	private ArrayList<String> messageLines;
	private Image background;
	
	public MessageWindow(ArrayList<String> message, int x, int y, boolean centerOnScreen) {
		super(x, y);
		background = ResourceManager.getImage("message");
		this.name = MESSAGEWINDOW;
		this.addType(MESSAGEWINDOW);
		messageLines = message;
		setGraphic(background);
		font = ResourceManager.getAngelCodeFont("bradleyhanditc24");
		setHitBox(0, 0, width, height);
		depth = 100;
		if (centerOnScreen) {
			this.x = StarCleaner.WIDTH / 2 - background.getWidth() / 2;
			this.y = StarCleaner.HEIGHT / 2 - background.getHeight() / 2;
		}
	}
	
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (!visible)
			return;
		super.render(container, g);
		g.setColor(Color.black);
		Font oldFont = g.getFont();
		g.setFont(font);
		int startx = (int) x + 5;
		int starty = (int)y + 5;
		for (String line : messageLines) {
			g.drawString(line, startx, starty);
			starty += 25;
		}
		g.setColor(Color.white);
		g.setFont(oldFont);

	}

}
