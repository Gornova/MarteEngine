package it.randomtower.engine;

import it.randomtower.engine.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class World extends BasicGameState {

	/** the game container this world belongs to */
	public GameContainer container = null;

	/** unique id for every world **/
	public int id = 0;

	/** width of the world, useful for horizontal wrapping entitites */
	public int width = 0;
	/** height of the world, useful for vertical wrapping entities */
	public int height = 0;

	/** internal list for entities **/
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> removable = new ArrayList<Entity>();
	private List<Entity> addable = new ArrayList<Entity>();

	/** current camera **/
	public Camera camera;

	public World(int id) {
		this.id = id;
	}

	public World(int id, GameContainer container) {
		this.id = id;
		this.container = container;
	}
	
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		width = container.getWidth();
		height = container.getHeight();
		//this.clear();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		ME.world = this;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		// center to camera position
		if (camera != null)
			g.translate(camera.x, camera.y);

		// render entities
		for (Entity e : entities) {
			if (ME.debugEnabled) {
				g.setColor(ME.borderColor);
				Rectangle hitBox = new Rectangle(e.x + e.hitboxOffsetX, e.y
						+ e.hitboxOffsetY, e.width, e.height);
				g.draw(hitBox);
				g.setColor(Color.white);
			}
			if (camera != null) {
				// TODO
				// if (camera.contains(e)) {
				e.render(container, g);
				// }
			} else {
				e.render(container, g);
			}
		}

		if (camera != null)
			g.translate(-camera.x, -camera.y);
		
		ME.render(container, game, g);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (container == null)
			throw new SlickException("no container set");
		
		// store the current delta in ME for anyone who's interested in it.
		ME.delta = delta;
		
		removable.clear();

		// add new entities
		if (addable.size() > 0) {
			for (Entity entity : addable) {
				entities.add(entity);
				entity.addedToWorld();
			}
			addable.clear();
			Collections.sort(entities);
		}

		// update entities
		for (Entity e : entities) {
			e.updateAlarms(delta);
			e.update(container, delta);
			// check for wrapping or out of world entities
			e.checkWorldBoundaries();
		}
		// remove signed entities
		for (Entity entity : removable) {
			entities.remove(entity);
			entity.removedFromWorld();
		}
		
		// update camera
		if (camera != null) {
			camera.update(container, delta);
		}
		
		
		ME.update(container, game, delta);
	}

	@Override
	public int getID() {
		return id;
	}

	/**
	 * Add entity to world and sort entity in z order
	 * 
	 * @param e
	 *            entity to add
	 */
	public void add(Entity e) {
		e.setWorld(this);		
		addable.add(e);
	}

	/**
	 * @return List of entities currently in this world
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * 
	 * @param type
	 * @return number of entities of the given type in this world
	 */
	public int getNrOfEntities(String type) {
		if (entities.size() > 0) {
			int number = 0;
			for (Entity entity : entities) {
				if (entity.getType().contains(type))
					number++;
			}
			return number;
		}
		return 0;
	}
	
	
	/**
	 * @param entity
	 *            to remove from game
	 * @return false if entity is already set to be remove
	 */
	public boolean remove(Entity entity) {
		if (!removable.contains(entity)) {
			return removable.add(entity);
		}
		return false;
	}

	/**
	 * @param name
	 * @return null if name is null or if no entity is found in game, entity
	 *         otherwise
	 */
	public Entity find(String name) {
		if (name == null)
			return null;
		for (Entity entity : entities) {
			if (entity.name != null && entity.name.equalsIgnoreCase(name)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Remove all entities
	 */
	public void clear() {
		for (Entity entity : entities) {
			entity.removedFromWorld();
		}
		entities.clear();
		addable.clear();
		removable.clear();
	}
	
	public void setCamera(Camera camera){
		this.camera = camera;
	}
	
	public void setCameraOn(Entity entity){
		this.camera = new Camera(entity, this.container.getWidth(), this.container.getHeight());	
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
