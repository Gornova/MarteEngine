package it.randomtower.test.resize;

import it.randomtower.engine.World;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ResizeGameState extends World {

	public ResizeGameState(int id, GameContainer container) throws SlickException {
		//super.init(container, game);
		super(id, container);

		Image cursor = new Image("data/cross.png", false, 0, Color.white);

		container.setMouseCursor(cursor, 0, 0);

		// create square
		ResizeSquare square = new ResizeSquare(100, 100, 8, 8, "data/8.png");
		add(square);
		
		Turret turret = new Turret(square);
		add(turret);
		
		
		//TODO
		//new Map("data/level1.tmx");	
		
		
		// set screen camera
		//setCamera(new Camera(square, container.getWidth(), container
			//	.getHeight()));		
		
	}
	
	//@Override
//	public void mouseClicked(int button, int x, int y, int clickCount) {
////		if (button == 0) {
////			ResizeSquare rs = ((ResizeSquare) find(ResizeSquare.NAME));
////			Bullet b = new Bullet(rs.x, rs.y, "data/bullet.png",
////					calculateDirection(x, y, container.getInput()
////							.getMouseX(), container.getInput().getMouseY()));
////			ME.world.add(b);
////		}
//	}

//	public Vector2f calculateDirection(float x, float y, int mousex, int mousey) {
//		float rotation = ((ResizeSquare) ME.world.find(ResizeSquare.NAME)).rotation;
//		float xForce = 10f * (float) Math.sin(Math.toRadians(rotation));
//		float yForce = 10f * (float) Math.cos(Math.toRadians(rotation));
//		return new Vector2f(-xForce, yForce);
//	}

}
