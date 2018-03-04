package it.marteEngine;

import it.marteEngine.entity.Entity;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * The camera shows a game in 2D perspective. It represents the area which is
 * seen to the player.
 */
public class Camera {
  /**
   * Camera position
   */
  private float x, y;
  /**
   * Camera dimension
   */
  private int width, height;
  /**
   * The rectangle that covers the camera, anything that is not within this
   * rectangle is not visible.
   */
  private Rectangle visibleRect;
  /**
   * The rectangle in which the entity can move without making the camera
   * scroll.
   */
  private Rectangle deadzone;
  /**
   * The offset between the camera position and the dead zone position. It is
   * used to update the dead zone position after the camera moved.
   */
  private float deadzoneXOffset, deadzoneYOffset;
  /**
   * The bounds of the scene. The camera cannot move outside of the scene.
   */
  private Rectangle scene;
  /**
   * The scroll speed
   */
  private Vector2f speed;
  /**
   * The destination to scroll to
   */
  private Vector2f target;

  private Entity entityToFollow;
  private CameraFollowStyle followStyle;

  /**
   * Create a static camera that has the same size as the screen.
   *
   * @param width  The width of the screen
   * @param height The height of the screen
   */
  public Camera(int width, int height) {
    this(width, height, width, height);
  }

  /**
   * Create a camera that can move within a scene.
   *
   * @param cameraWidth  The width of the camera
   * @param cameraHeight The height of the camera
   * @param sceneWidth   the width of the area in which the camera can move
   * @param sceneHeight  the height of the area in which the camera can move
   */
  public Camera(int cameraWidth, int cameraHeight, int sceneWidth,
                int sceneHeight) {
    this.width = cameraWidth;
    this.height = cameraHeight;
    this.scene = new Rectangle(0, 0, sceneWidth, sceneHeight);
    this.visibleRect = new Rectangle(x, y, cameraWidth, cameraHeight);
    this.deadzone = new Rectangle(x, y, cameraWidth, cameraHeight);
    this.speed = new Vector2f();
    this.target = new Vector2f();

    if (cameraWidth > sceneWidth || cameraHeight > sceneHeight) {
      throw new IllegalArgumentException(
          "The camera cannot be larger then the scene");
    }
  }

  public void update(int delta) {
    if (isFollowingEntity()) {
      followEntity();
    }

    if (x != target.x || y != target.y) {
      if (followStyle == CameraFollowStyle.SCREEN_BY_SCREEN) {
        scrollToNextScreen();
      } else {
        scroll(delta);
      }
      updateZones();
    }
  }

  private void followEntity() {
    if (!isEntityWithinDeadzone()) {
      focus(entityToFollow);
    } else {
      target.set(x, y);
    }
  }

  private void scrollToNextScreen() {
    if (entityToFollow.x > x + width) {
      setPosition(x + width, y);
    } else if (entityToFollow.x < x) {
      setPosition(x - width, y);
    }

    if (entityToFollow.y > y + height) {
      setPosition(0, y + height);
    } else if (entityToFollow.y < y) {
      setPosition(0, y - height);
    }
  }

  private void scroll(int delta) {
    if (speed.x == 0 && speed.y == 0) {
      setPosition(target.x, target.y);
    } else {
      float scrollX = 0, scrollY = 0;

      float distanceX = Math.abs(target.x - x);
      if (distanceX != 0 && distanceX >= speed.x) {
        scrollX = target.x > x ? speed.x : -speed.x;
      } else {
        x = target.x;
      }

      float distanceY = Math.abs(target.y - y);
      if (distanceY != 0 && distanceY >= speed.y) {
        scrollY = target.y > y ? speed.y : -speed.y;
      } else {
        y = target.y;
      }

      if (ME.useDeltaTiming) {
        x += scrollX * delta;
        y += scrollY * delta;
      } else {
        x += scrollX;
        y += scrollY;
      }
    }
  }

  private void updateZones() {
    visibleRect.setLocation(x, y);
    deadzone.setLocation(x + deadzoneXOffset, y + deadzoneYOffset);
  }

  /**
   * Position the camera so that it keeps on following the entity.
   * <p>
   * The camera movement is affected by the dead zone. An area in which the
   * entity can move without causing camera movement.
   * {@link #setDeadZone(int, int, int, int)}
   * <p>
   * The entity is immediately centered in the camera.
   *
   * @param entity      The entity to follow
   * @param followStyle One of the existing dead zone presets. If you use a custom
   *                    dead zone, manually specify the dead zone after calling this
   *                    method.
   */
  public void follow(Entity entity, CameraFollowStyle followStyle) {
    // Calculate the position of the camera
    // so that the entity is centered
    focus(entity);
    // Move immediately to that position
    setPosition(target.x, target.y);
    entityToFollow = entity;
    applyFollowStyle(entity, followStyle);
  }

  private void applyFollowStyle(Entity entity, CameraFollowStyle followStyle) {
    this.followStyle = followStyle;
    followStyle.createDeadzone(this, entity);
  }

  public void stopFollowingEntity() {
    entityToFollow = null;
  }

  /**
   * Move the camera so that the given entity is in the center of the camera.
   */
  public void focus(Entity entity) {
    focus(entity.x + entity.width / 2, entity.y + entity.height / 2);
  }

  /**
   * Move the camera so that the given point is in the center of the camera.
   *
   * @param x X coordinate to center
   * @param y Y coordinate to center
   */
  public void focus(float x, float y) {
    float centerX = x - (width / 2);
    float centerY = y - (height / 2);
    moveTo(centerX, centerY);
  }

  /**
   * Move the camera with the x and y offset. Using the speed of the camera.
   * See {@link #setSpeed} This is the same as calling following method
   * moveTo(camera.getX() + xOffset, camera.getY() + yOffset)
   *
   * @param xOffset The amount of pixels the camera should move on the x axis
   * @param yOffset The amount of pixels the camera should move on the y axis
   */
  public void scroll(float xOffset, float yOffset) {
    if (xOffset == 0 && yOffset == 0)
      return;
    moveTo(x + xOffset, y + yOffset);
  }

  /**
   * Move the camera smoothly to the target position. Using the speed of the
   * camera. If no speed is set the camera will moves directly to the targetX,
   * targetY position.
   *
   * @param targetX The x coordinate the camera should move to
   * @param targetY The y coordinate the camera should move to
   * @see #setSpeed(float, float)
   */
  public void moveTo(float targetX, float targetY) {
    if (x == targetX && y == targetY)
      return;

    if (targetX < scene.getX() || !canMoveHorizontally()) {
      targetX = scene.getX();
    }

    if (targetY < scene.getY() || !canMoveVertically()) {
      targetY = scene.getY();
    }

    // Make sure the camera wraps at the edge
    targetX = wrapHorizontal(targetX);
    targetY = wrapVertical(targetY);
    target.set(targetX, targetY);
  }

  private float wrapHorizontal(float targetX) {
    if (canMoveHorizontally()) {
      if (!isWithinScene(targetX, 0)) {
        targetX = scene.getX();
      }
      if (!isWithinScene(targetX + width, 0)) {
        targetX = scene.getWidth() - width;
      }
    }
    return targetX;
  }

  private float wrapVertical(float targetY) {
    if (canMoveVertically()) {
      if (!isWithinScene(0, targetY)) {
        targetY = scene.getY();
      }
      if (!isWithinScene(0, targetY + height)) {
        targetY = scene.getHeight() - height;
      }
    }
    return targetY;
  }

  /**
   * Immediately move the camera to the x,y coordinate. Ignoring the speed.
   *
   * @param x The new x position of the camera
   * @param y The new y position of the camera
   */
  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;

    // Overwrite target
    target.set(x, y);
    updateZones();
  }

  public boolean isEntityWithinDeadzone() {
    return entityToFollow != null
        && deadzone.contains(entityToFollow.x + entityToFollow.width
        / 2, entityToFollow.y + entityToFollow.height / 2);
  }

  public boolean contains(Entity e) {
    Rectangle entity = new Rectangle(e.x, e.y, e.width, e.height);
    return visibleRect.intersects(entity);
  }

  /**
   * Specifies x and y camera movement per update call in pixels. If no speed
   * is given (0,0) the camera will jump towards the destination.
   */
  public void setSpeed(float dx, float dy) {
    speed.set(dx, dy);
  }

  /**
   * Allows to define a custom dead zone. The dead zone is a rectangle in
   * which the entity can move without causing camera movement. The dead zone
   * is only used when following an entity.
   * <p>
   * There is only 1 dead zone active at any time. The previous values will be
   * overwritten.
   *
   * @param x      The start position of the rectangle, relative to the camera
   *               0,0 position.
   * @param y      The start position of the rectangle, relative to the camera
   *               0,0 position.
   * @param width  the width of the dead zone
   * @param height the height of the dead zone
   */
  public void setDeadZone(int x, int y, int width, int height) {
    deadzone.setBounds(this.x + x, this.y + y, width, height);
    deadzoneXOffset = Math.abs(x);
    deadzoneYOffset = Math.abs(y);
  }

  /**
   * Enlarge the scene on all sides by offset pixels.
   *
   * @param offset The offset to add to the scene
   */
  public void setSceneOffset(int offset) {
    setSceneBounds((int) scene.getX() - offset,
        (int) scene.getY() - offset, (int) scene.getWidth() + offset,
        (int) scene.getHeight() + offset);
  }

  /**
   * Set the scene boundaries. These boundaries define where the camera stops
   * scrolling. Most of the time the scene and game have equal size.
   * <p>
   * To allow the camera to move outside of the game world by 50 pixels you
   * would set minX and minY to -50 and width and height to worldWidth+50 and
   * worldHeight+50.
   *
   * @param minX        The smallest x value of your scene (usually 0).
   * @param minY        The smallest y value of your scene (usually 0).
   * @param sceneWidth  The largest x value of your scene (usually the game width).
   * @param sceneHeight The largest y value of your scene (usually the game height).
   */
  public void setSceneBounds(int minX, int minY, int sceneWidth,
                             int sceneHeight) {
    scene.setLocation(minX, minY);
    setSceneWidth(sceneWidth);
    setSceneHeight(sceneHeight);
  }

  public void setSceneWidth(int sceneWidth) {
    scene.setWidth(sceneWidth);
  }

  public void setSceneHeight(int sceneHeight) {
    scene.setHeight(sceneHeight);
  }

  private boolean isWithinScene(float x, float y) {
    return x >= scene.getX() && x <= scene.getWidth() && y >= scene.getY()
        && y <= scene.getHeight();
  }

  private boolean canMoveHorizontally() {
    return x >= scene.getX() && width < scene.getWidth();
  }

  private boolean canMoveVertically() {
    return y >= scene.getY() && height < scene.getHeight();
  }

  public int getX() {
    return (int) x;
  }

  public int getY() {
    return (int) y;
  }

  public boolean isFollowingEntity() {
    return entityToFollow != null;
  }

  public Rectangle getVisibleRect() {
    return visibleRect;
  }

  public Rectangle getScene() {
    return scene;
  }

  public Rectangle getDeadzone() {
    return deadzone;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public CameraFollowStyle getCurrentFollowStyle() {
    return followStyle;
  }

  public String toString() {
    return String.format(
        "Camera: (%.0f,%.0f) Target(%.0f,%.0f) Following entity:%s", x,
        y, target.x, target.y, isFollowingEntity());
  }
}
