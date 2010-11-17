package it.randomtower.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity implements Comparable<Entity> {

	/** unique identifier **/
	public String name;

	/** x position **/
	public float x;
	/** y position **/
	public float y;

	/** speed vector (x,y) **/
	public Vector2f speed;

	/** spritesheet that holds animations **/
	protected SpriteSheet sheet;
	public Hashtable<String, Animation> animations = new Hashtable<String, Animation>();
	public String currentAnim;
	public int duration = 200;
	public int depth = -1;

	/** static image for not-animated entity **/
	protected Image currentImage;
	/** true if this entity should be visible, false otherwise */
	public boolean visible = true;
	
	/** available commands for entity **/
	public Hashtable<String, int[]> commands = new Hashtable<String, int[]>();

	/** collision type for entity **/
	private HashSet<String> type = new HashSet<String>();
	/** true for collidable entity, false otherwise **/
	public boolean collidable = true;
	protected float xOffset;
	protected float yOffset;
	/** width of entity **/
	protected int width;
	/** height of entity **/
	protected int height;

	/** stateManager for entity **/
	public StateManager stateManager;
	
	/**
	 * create a new entity setting initial position (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		stateManager = new StateManager();
	}

	/**
	 * Update entity animation
	 * 
	 * @param container
	 * @param delta
	 * @throws SlickException
	 */
	public void update(GameContainer container, int delta)
			throws SlickException {
		if (stateManager!=null && stateManager.currentState()!=null){
			stateManager.update(container, delta);
			return;
		}
		if (animations != null) {
			if (currentAnim != null) {
				Animation anim = animations.get(currentAnim);
				if (anim != null) {
					anim.update(delta);
				}
			}
		}
	}

	/**
	 * Render entity
	 * 
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (!visible)
			return;
		if (stateManager!=null && stateManager.currentState()!=null){
			stateManager.render(g);
			return;
		}		
		if (currentAnim != null) {
			animations.get(currentAnim).draw(x, y);
		} else if (currentImage != null) {
			g.drawImage(currentImage, x, y);
		}
	}

	/**
	 * Set an image as graphic
	 * @param image
	 */
	public void setGraphic(Image image) {
		this.currentImage = image;
	}

	/**
	 * Set a sprite sheet as graphic
	 * @param sheet
	 */
	public void setGraphic(SpriteSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * Add animation to entity, first animation added is current animation
	 * 
	 * @param name
	 * @param loop
	 * @param row
	 * @param frames
	 */
	public void add(String name, boolean loop, int row, int... frames) {
		Animation anim = new Animation(false);
		anim.setLooping(loop);
		for (int i = 0; i < frames.length; i++) {
			anim.addFrame(sheet.getSprite(frames[i], row), duration);
		}
		if (animations.size() == 0) {
			currentAnim = name;
		}
		animations.put(name, anim);
	}

	/**
	 * define commands
	 * 
	 * @param key
	 * @param keys
	 */
	public void define(String command, int... keys) {
		commands.put(command, keys);
	}

	/**
	 * Check if a command is down
	 * 
	 * @param key
	 * @return
	 */
	public boolean check(String command) {
		int[] checked = commands.get(command);
		if (checked == null)
			return false;
		for (int i = 0; i < checked.length; i++) {
			if (ME.container.getInput().isKeyDown(checked[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if a command is pressed
	 * 
	 * @param key
	 * @return
	 */
	public boolean pressed(String command) {
		int[] checked = commands.get(command);
		if (checked == null)
			return false;
		for (int i = 0; i < checked.length; i++) {
			if (ME.container.getInput().isKeyPressed(checked[i])) {
				return true;
			} else if (checked[i] == Input.MOUSE_LEFT_BUTTON
					|| checked[i] == Input.MOUSE_RIGHT_BUTTON) {
				if (ME.container.getInput().isMousePressed(checked[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Compare to another entity on zLevel
	 */
	public int compareTo(Entity o) {
		if (depth == o.depth)
			return 0;
		if (depth > o.depth)
			return 1;
		return -1;
	}

	/**
	 * Set hitbox for collision (by default if and entity have an hitbox, is
	 * collidable against other entities)
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param width
	 * @param height
	 */
	public void setHitBox(float xOffset, float yOffset, int width, int height) {
		setHitBox(xOffset, yOffset, width, height, true);
	}

	/**
	 * Set hitbox for collision and set if is collidable against other entities
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param width
	 * @param height
	 * @param collidable
	 */
	public void setHitBox(float xOffset, float yOffset, int width, int height,
			boolean collidable) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.collidable = true;
	}

	/**
	 * Add collision types to entity
	 * 
	 * @param types
	 * @return
	 */
	public boolean addType(String... types) {
		return type.addAll(Arrays.asList(types));
	}

	/**
	 * check collision with another entity of given type
	 * 
	 * @param type
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean collide(String type, float x, float y) {
		if (type == null || type.isEmpty())
			return false;
		// offset
		for (Entity entity : ME.getEntities()) {
			if (entity.collidable && entity.type.contains(type)) {
				if (!entity.equals(this)
						&& x + xOffset + width > entity.x + entity.xOffset
						&& y + yOffset + height > entity.y + entity.yOffset
						&& x + xOffset < entity.x + entity.xOffset
								+ entity.width
						&& y + yOffset < entity.y + entity.yOffset
								+ entity.height) {
					this.collisionResponse(entity);
					entity.collisionResponse(this);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Response to a collision with another entity
	 * 
	 * @param other
	 */
	public void collisionResponse(Entity other) {

	}

	public Image getCurrentImage() {
		return currentImage;
	}

}
