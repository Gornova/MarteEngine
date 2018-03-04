package it.marteEngine;

import it.marteEngine.entity.Entity;
import it.marteEngine.entity.InputManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//TODO addAll() muss intern add() aufrufen, um korrekt nach flags in die listen einzusortieren
public class World extends BasicGameState {

  public static final int BELOW = -1;
  public static final int GAME = 0;
  public static final int ABOVE = 1;

  /**
   * the game container this world belongs to
   */
  public GameContainer container = null;

  /**
   * unique id for every world
   **/
  public int id;

  /**
   * width of the world, useful for horizontal wrapping entitites
   */
  public int width = 0;
  /**
   * height of the world, useful for vertical wrapping entities
   */
  public int height = 0;

  /**
   * internal list for entities
   **/
  private List<Entity> entities = new ArrayList<Entity>();
  private List<Entity> removable = new ArrayList<Entity>();
  private List<Entity> addable = new ArrayList<Entity>();

  /**
   * two lists to contain objects that are rendered before and after camera
   * stuff is rendered
   */
  private List<Entity> belowCamera = new ArrayList<Entity>();
  private List<Entity> aboveCamera = new ArrayList<Entity>();

  /**
   * current camera
   **/
  public Camera camera;

  public int renderedEntities;

  /**
   * available commands for world
   **/
  protected InputManager input;

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
    input = new InputManager(container.getInput());

    if (width == 0)
      width = container.getWidth();
    if (height == 0)
      height = container.getHeight();
    camera = new Camera(width, height);
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game)
      throws SlickException {
    ME.world = this;
  }

  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {

    renderedEntities = 0;
    // first render entities below camera
    for (Entity e : belowCamera) {
      if (!e.visible)
        continue;
      renderEntity(e, g, container);
    }
    g.translate(-camera.getX(), -camera.getY());

    // render entities
    for (Entity e : entities) {
      if (!e.visible)
        continue; // next entity. this one stays invisible
      if (camera.contains(e)) {
        renderEntity(e, g, container);
      }
    }

    // render particle system
    if (ME.ps != null && ME.renderParticle) {
      ME.ps.render();
    }

    if (ME.debugEnabled) {
      g.draw(camera.getDeadzone());
      g.draw(camera.getVisibleRect());
    }

    g.translate(camera.getX(), camera.getY());

    // finally render entities above camera
    for (Entity e : aboveCamera) {
      if (!e.visible)
        continue;
      renderEntity(e, g, container);
    }

    ME.render(container, game, g);
  }

  private void renderEntity(Entity e, Graphics g, GameContainer container)
      throws SlickException {
    renderedEntities++;
    e.render(container, g);

    if (ME.debugEnabled && e.collidable) {
      e.renderDebug(g);
    }
  }

  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    if (container == null)
      throw new SlickException("no container set");

    // store the current delta in ME for anyone who's interested in it.
    ME.delta = delta;

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
    for (Entity e : belowCamera) {
      e.alarms.update(delta);
      if (e.active)
        e.update(container, delta);
    }
    for (Entity e : entities) {
      e.alarms.update(delta);
      if (e.active) {
        e.update(container, delta);
        e.checkWorldBoundaries();
      }
    }
    for (Entity e : aboveCamera) {
      e.alarms.update(delta);
      if (e.active)
        e.update(container, delta);
    }

    // update particle system
    if (ME.ps != null) {
      ME.ps.update(delta);
    }

    // remove signed entities
    for (Entity entity : removable) {
      entities.remove(entity);
      belowCamera.remove(entity);
      aboveCamera.remove(entity);
      entity.active = false;
      entity.removedFromWorld();
    }
    removable.clear();
    camera.update(delta);

    ME.update(container, game, delta);
  }

  @Override
  public int getID() {
    return id;
  }

  /**
   * Add entity to world and sort entity in z order
   *
   * @param e entity to add
   */
  public void add(Entity e, int... flags) {
    e.setWorld(this);
    if (flags.length == 1) {
      switch (flags[0]) {
        case BELOW:
          belowCamera.add(e);
          break;
        case GAME:
          addable.add(e);
          break;
        case ABOVE:
          aboveCamera.add(e);
          break;
      }
    } else
      addable.add(e);
  }

  public void addAll(Collection<Entity> e, int... flags) {
    for (Entity entity : e) {
      this.add(entity, flags);
    }
  }

  /**
   * @return List of entities currently in this world
   */
  public List<Entity> getEntities() {
    return entities;
  }

  /**
   * @param type The entity type to count
   * @return number of entities of the given type in this world
   */
  public int getNrOfEntities(String type) {
    if (entities.size() > 0) {
      int number = 0;
      for (Entity entity : entities) {
        if (entity.isType(type))
          number++;
      }
      return number;
    }
    return 0;
  }

  public List<Entity> getEntities(String type) {
    if (entities.size() > 0) {
      List<Entity> res = new ArrayList<Entity>();
      for (Entity entity : entities) {
        if (entity.isType(type))
          res.add(entity);
      }
      return res;
    }
    return new ArrayList<Entity>();
  }

  /**
   * Register the given entities for removal from this world. They are removed
   * in the next update cycle.
   *
   * @param entities The entities to be removed
   */
  public void removeAll(Collection<Entity> entities) {
    for (Entity entity : entities) {
      remove(entity);
    }
  }

  /**
   * Register the given entity for removal from this world. The entity is
   * removed in the next update cycle.
   *
   * @param entity the entity to be removed
   */
  public void remove(Entity entity) {
    removable.add(entity);
  }

  /**
   * @param name The name of the entity
   * @return null if name is null or if no entity is found in game, entity
   * otherwise
   */
  public Entity find(String name) {
    if (name == null)
      return null;
    for (Entity entity : entities) {
      if (entity.name != null && entity.name.equalsIgnoreCase(name)) {
        return entity;
      }
    }
    // also look in addable list
    for (Entity entity : addable) {
      if (entity.name != null && entity.name.equalsIgnoreCase(name)) {
        return entity;
      }
    }
    // and look in aboveCamera and belowCamera list
    for (Entity entity : aboveCamera) {
      if (entity.name != null && entity.name.equalsIgnoreCase(name)) {
        return entity;
      }
    }
    for (Entity entity : belowCamera) {
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
    belowCamera.clear();
    aboveCamera.clear();
    entities.clear();
    addable.clear();
    removable.clear();
  }

  /**
   * Intersect finds all the entities in this world
   * that intersect with the given shape.
   * If no entity is found then an empty list is returned.
   *
   * @param shape The shape to check for intersection
   * @return The entities that intersect with their hitboxes into the given
   * shape
   */
  public List<Entity> intersect(Shape shape) {
    List<Entity> result = new ArrayList<Entity>();
    for (Entity entity : getEntities()) {
      if (entity.collidable) {
        Rectangle rec = new Rectangle(entity.x, entity.y, entity.width,
            entity.height);
        if (shape.intersects(rec)) {
          result.add(entity);
        }
      }
    }
    return result;
  }

  public boolean contains(Entity entity) {
    return contains(entity.x, entity.y, entity.width, entity.height);
  }

  public boolean contains(float x, float y, int width, int height) {
    return x + width >= 0 && y + height >= 0 && x <= this.width
        && y <= this.height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
    camera.setSceneWidth(width);
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
    camera.setSceneHeight(height);
  }

  public List<Entity> findEntityWithType(String type) {
    if (type == null) {
      Log.error("Parameter must be not null");
      return new ArrayList<Entity>();
    }
    List<Entity> result = new ArrayList<Entity>();
    for (Entity entity : entities) {
      if (entity.isType(type)) {
        result.add(entity);
      }
    }
    return result;
  }

  /**
   * @return true if an entity is already in position
   */
  public boolean isEmpty(int x, int y, int depth) {
    Rectangle rect;
    for (Entity entity : entities) {
      rect = new Rectangle(entity.x, entity.y, entity.width,
          entity.height);
      if (entity.depth == depth && rect.contains(x, y)) {
        return false;
      }
    }
    return true;
  }

  public Entity find(int x, int y) {
    Rectangle rect;
    for (Entity entity : entities) {
      rect = new Rectangle(entity.x, entity.y, entity.width,
          entity.height);
      if (rect.contains(x, y)) {
        return entity;
      }
    }
    return null;
  }

  /**
   * @return get number of entities in this world
   */
  public int getCount() {
    return entities.size();
  }

  /**
   * @deprecated As of release 0.3, replaced by
   * {@link #bindToKey(String, int...)} and
   * {@link #bindToMouse(String, int...)}
   */
  public void define(String command, int... keys) {
    bindToKey(command, keys);
  }

  /**
   * @see InputManager#bindToKey(String, int...)
   */
  public void bindToKey(String command, int... keys) {
    input.bindToKey(command, keys);
  }

  /**
   * @see InputManager#bindToMouse(String, int...)
   */
  public void bindToMouse(String command, int... buttons) {
    input.bindToMouse(command, buttons);
  }

  /**
   * @see InputManager#isDown(String)
   */
  public boolean check(String command) {
    return input.isDown(command);
  }

  /**
   * @see InputManager#isPressed(String)
   */
  public boolean pressed(String command) {
    return input.isPressed(command);
  }

}