package it.randomtower.engine;

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

	/** unique id for every world **/
	public int id = 0;

	/** game container **/
	public static GameContainer container;

	/** internal list for entities **/
	private final List<Entity> entities = new ArrayList<Entity>();
	private final List<Entity> removeable = new ArrayList<Entity>();
	private final List<Entity> addable = new ArrayList<Entity>();

	/** current camera **/
	public static Camera camera;

	public World(int id) {
		this.id = id;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		ME.world = this;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// render entities
		for (Entity e : entities) {
			if (ME.debugEnabled) {
				g.setColor(ME.borderColor);
				Rectangle hitBox = new Rectangle(e.x + e.xOffset, e.y
						+ e.yOffset, e.width, e.height);
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
		
		ME.render(container, game, g);

	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (container == null)
			throw new SlickException("no container set");
		removeable.clear();

		// add new entities
		for (Entity entity : addable) {
			entities.add(entity);
		}
		addable.clear();

		// update entities
		for (Entity e : entities) {
			e.update(container, delta);
		}
		// remove signed entities
		for (Entity entity : removeable) {
			entities.remove(entity);
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
		// sort in z order
		if (entities.size() > 1) {
			Collections.sort(entities);
		}
		addable.add(e);
	}

	/**
	 * @return List of entities currently in game
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * @param entity
	 *            to remove from game
	 * @return false if entity is already set to be remove
	 */
	public boolean remove(Entity entity) {
		if (!removeable.contains(entity)) {
			return removeable.add(entity);
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
			if (entity.name.equalsIgnoreCase(name)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Remove all entities
	 */
	public void clear() {
		removeable.clear();
		removeable.addAll(entities);
	}

}
