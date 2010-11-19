package it.randomtower.test.resize;

import it.randomtower.engine.Camera;
import it.randomtower.engine.ME;
import it.randomtower.engine.World;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class ResizeGameState extends World {

	public ResizeGameState(int id) throws SlickException {
		super(id);

		Image cursor = new Image("data/cross.png", false, 0, Color.white);

		ME.container.setMouseCursor(cursor, 0, 0);
		// create square
		ResizeSquare square = new ResizeSquare(100, 100, 8, 8, "data/8.png");

		// add entities
		add(square);

		//TODO
		//new Map("data/level1.tmx");	
		
		// set screen camera
		setCamera(new Camera(square, ME.container.getWidth(), ME.container
				.getHeight()));		
		
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (button == 0) {
			ResizeSquare rs = ((ResizeSquare) find(ResizeSquare.NAME));
			Bullet b = new Bullet(rs.x, rs.y, "data/bullet.png",
					calculateDirection(x, y, ME.container.getInput()
							.getMouseX(), ME.container.getInput().getMouseY()));
			ME.world.add(b);
		}
	}

	public Vector2f calculateDirection(float x, float y, int mousex, int mousey) {
		float rotation = ((ResizeSquare) ME.world.find(ResizeSquare.NAME)).rotation;
		float xForce = 10f * (float) Math.sin(Math.toRadians(rotation));
		float yForce = 10f * (float) Math.cos(Math.toRadians(rotation));
		return new Vector2f(-xForce, yForce);
	}

}
