package it.marteEngine.test.avatar;

import it.marteEngine.CameraFollowStyle;
import it.marteEngine.World;
import it.marteEngine.actor.StaticActor;
import it.marteEngine.actor.TopDownActor;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Test the camera. There are 3 camera modes: Follow entity (keep entity
 * centered, with different dead zones) Scroll (move n pixels on an axis) Focus
 * (jump to a location)
 */
public class AvatarWorld extends World {
	private Vector2f playerSpeed;
	private CameraMode cameraMode = CameraMode.FOLLOW_ENTITY;
	private TopDownActor player;

	private static enum CameraMode {
		FOLLOW_ENTITY, SCROLL, FOCUS_ON_RIGHT_CLICK;

		public CameraMode getNext() {
			return values()[(ordinal() + 1) % values().length];
		}
	}

	public AvatarWorld(int id) {
		super(id);
		playerSpeed = new Vector2f(2, 2);
		cameraMode = CameraMode.FOLLOW_ENTITY;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		// make the world a bit bigger than the screen to force camera scrolling
		setWidth(2000);
		setHeight(2000);

		// Allow the camera to move outside of the world bounds
		// by 150 pixels on all sides
		camera.setSceneOffset(150);

		// create player
		player = new TopDownActor(250, 300, "data/link.png");
		// create sword relative to player
		Sword sword = new Sword(player.x, player.y, "data/sword.png", player);

		// add entities
		add(player);
		add(sword);
		add(createTemple(80, 50));
		add(createTemple(128, 50));
		add(createTemple(550, 50));
		add(createTemple(550, 550));
		add(createTemple(1000, 1000));
		add(createTemple(1880, 1880));

		define("change_mode", Input.KEY_M);
		define("change_follow_style", Input.KEY_F);
		define("followEntity", Input.KEY_F);
		define("focus", Input.MOUSE_RIGHT_BUTTON);
		nextMode();
	}

	private StaticActor createTemple(float x, float y) {
		return new StaticActor(x, y, 48, 48, "data/tiles.png", 0, 6);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);
		Input input = container.getInput();

		if (pressed("change_mode")) {
			nextMode();
		} else if (pressed("change_follow_style")) {
			nextFollowStyle();
		} else {
			switch (cameraMode) {
				case SCROLL :
					scroll(input);
					break;
				case FOCUS_ON_RIGHT_CLICK :
          focus(input);
          break;
			}
		}
	}

  private void focus(Input input) {
    // Center on the mouse location
    if (pressed("focus")) {
      camera.focus(input.getMouseX() + camera.getX(),
          input.getMouseY() + camera.getY());
    }
  }

  private void scroll(Input input) {
		// Scroll 30 pixels
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			camera.scroll(30, 0);
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			camera.scroll(-30, 0);
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			camera.scroll(0, -30);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			camera.scroll(0, 30);
		}
	}

	private void nextMode() {
		this.cameraMode = cameraMode.getNext();

		switch (cameraMode) {
			case FOLLOW_ENTITY :
				camera.follow(player, CameraFollowStyle.TOPDOWN);
				player.mySpeed.set(playerSpeed.x, playerSpeed.y);
				camera.setSpeed(playerSpeed.x * 2, playerSpeed.y * 2);
				break;
			case FOCUS_ON_RIGHT_CLICK :
				camera.stopFollowingEntity();
				camera.setSpeed(0, 0);
				break;
			case SCROLL :
				camera.stopFollowingEntity();
				player.mySpeed.set(0, 0);
				camera.setSpeed(5, 5);
				break;
			default :
				throw new IllegalStateException("No camera mode for "
						+ cameraMode);
		}
	}

	public void nextFollowStyle() {
		if (camera.isFollowingEntity()) {
			CameraFollowStyle nextStyle = camera.getCurrentFollowStyle().next();
			camera.follow(player, nextStyle);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		Color origColor = g.getColor();

		g.setColor(Color.gray);
		g.fill(camera.getScene());

		g.setColor(Color.green.darker(0.9f));
		g.fillRect(-camera.getX(), -camera.getY(), width, height);

		g.setColor(Color.black);
		g.drawString("World [" + width + "," + height + "]", -camera.getX(),
				-camera.getY());

		float boundsX = camera.getScene().getX();
		float boundsY = camera.getScene().getY();
		float boundsWidth = camera.getScene().getWidth();
		float boundsHeight = camera.getScene().getHeight();

		g.drawString("Camera bounds [" + boundsX + "," + boundsY + ","
				+ boundsWidth + "," + boundsHeight + "]", -camera.getX()
				+ boundsX, -camera.getY() + boundsY);

		g.setColor(origColor);
		super.render(container, game, g);

		g.drawString(camera.toString(), 10, 50);
		g.drawString("Entity: " + player.x + "," + player.y, 10, 70);
		g.drawString("CameraMode: " + cameraMode + " (m)", 10, 90);

		if (camera.isFollowingEntity()) {
			g.drawString("Follow style: " + camera.getCurrentFollowStyle()
					+ "(f)", 10, 110);
		}
	}
}
