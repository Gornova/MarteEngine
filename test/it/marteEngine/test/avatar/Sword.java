package it.marteEngine.test.avatar;

import it.marteEngine.ME;
import it.marteEngine.actor.StaticActor;
import it.marteEngine.actor.TopDownActor;
import it.marteEngine.entity.Entity;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class Sword extends Entity {

  public static final String NAME = "sword";

  private static final String ATTACK = "attack";

  // images
  private Image upSword;
  private Image downSword;
  private Image rightSword;
  private Image leftSword;

  // images positions relative to player
  private static final Vector2f SWORD_DOWN_POS = new Vector2f(16, 15);
  private static final Vector2f SWORD_UP_POS = new Vector2f(15, 3);
  private static final Vector2f SWORD_LEFT_POS = new Vector2f(5, 10);
  private static final Vector2f SWORD_RIGHT_POS = new Vector2f(16, 12);

  private static final int WIDTH = 4;

  private static final int HEIGHT = 14;

  // parent entity (player)
  private TopDownActor parent;

  // animations
  private int downTimer = -1;
  private int rightTimer = -1;
  private int upTimer = -1;
  private int leftTimer = -1;

  private int[] down = {0, 1, 2, 2, 1, -1, -2, -2, -1};
  private int[] right = {0, 1, 2, 2, 1, -1, -2, -2, -1};
  private int[] up = {0, -1, -2, -2, -1, 1, 2, 2, 1};
  private int[] left = {0, -1, -2, -2, -1, 1, 2, 2, 1};

  public Sword(float x, float y, String ref, TopDownActor parent) {
    super(x + SWORD_DOWN_POS.x, y + SWORD_DOWN_POS.y);

    // set id
    name = NAME;

    // set parent
    this.parent = parent;

    // setup sword image
    setupAnimations(ref);
    setGraphic(downSword);

    // define collision box and type
    setHitBox(1, 1, WIDTH, HEIGHT + 2);
    addType(NAME);

    // define labels for the key
    defineControls();
  }

  public void setupAnimations(String ref) {
    try {
      upSword = new Image(ref);
      downSword = upSword.copy();
      downSword.rotate(180);
      rightSword = upSword.copy();
      rightSword.rotate(90);
      leftSword = upSword.copy();
      leftSword.rotate(-90);
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  private void defineControls() {
    bindToKey(ATTACK, Input.KEY_SPACE);
  }

  @Override
  public void update(GameContainer container, int delta)
      throws SlickException {

    if (parent.isUpMoving()) {
      setGraphic(upSword);

      x = SWORD_UP_POS.x + parent.x;
      y = SWORD_UP_POS.y + parent.y;

      setHitBox(1, 1, WIDTH, HEIGHT + 2);

      depth = parent.depth - 2;
    } else if (parent.isDownMoving()) {
      setGraphic(downSword);

      x = SWORD_DOWN_POS.x + parent.x;
      y = SWORD_DOWN_POS.y + parent.y;

      setHitBox(1, 1, WIDTH, HEIGHT + 2);

      depth = parent.depth + 2;
    }

    if (parent.isRightMoving()) {
      setGraphic(rightSword);

      x = SWORD_RIGHT_POS.x + parent.x;
      y = SWORD_RIGHT_POS.y + parent.y;

      setHitBox(-HEIGHT / 3, WIDTH + 3, HEIGHT, WIDTH);

      depth = parent.depth + 2;
    } else if (parent.isLeftMoving()) {
      setGraphic(leftSword);

      x = SWORD_LEFT_POS.x + parent.x;
      y = SWORD_LEFT_POS.y + parent.y;

      setHitBox(-HEIGHT / 3, WIDTH + 3, HEIGHT, WIDTH);

      depth = parent.depth + 2;
    }

    // attack
    updateAttack();

    // collision
    collide(StaticActor.NAME, x, y);
  }

  private void updateAttack() {
    // update animation's timers
    if (downTimer >= 0) {
      downTimer += 1;
      if (downTimer > down.length) {
        downTimer = -1;
      }
    }
    if (rightTimer >= 0) {
      rightTimer += 1;
      if (rightTimer > right.length) {
        rightTimer = -1;
      }
    }
    if (upTimer >= 0) {
      upTimer += 1;
      if (upTimer > up.length) {
        upTimer = -1;
      }
    }
    if (leftTimer >= 0) {
      leftTimer += 1;
      if (leftTimer > left.length) {
        leftTimer = -1;
      }
    }

    if (pressed(ATTACK)) {
      if (parent.isDownMoving() || parent.isDownStanding()) {
        downTimer = 0;
        parent.attacking = true;
      }
      if (parent.isUpMoving() || parent.isUpStanding()) {
        upTimer = 0;
        parent.attacking = true;
      }
      if (parent.isRightMoving() || parent.isRightStanding()) {
        rightTimer = 0;
        parent.attacking = true;
      }
      if (parent.isLeftMoving() || parent.isLeftStanding()) {
        leftTimer = 0;
        parent.attacking = true;
      }
    } else {
      parent.attacking = false;
    }
  }

  @Override
  public void render(GameContainer container, Graphics g)
      throws SlickException {

    if (downTimer >= 0 && downTimer < down.length) {
      y += down[downTimer];
    }
    if (rightTimer >= 0 && rightTimer < right.length) {
      x += right[rightTimer];
    }
    if (upTimer >= 0 && upTimer < up.length) {
      y += up[upTimer];
    }
    if (leftTimer >= 0 && leftTimer < left.length) {
      x += left[leftTimer];
    }
    super.render(container, g);
  }

  @Override
  public void collisionResponse(Entity entity) {

    if (entity.name.equalsIgnoreCase(StaticActor.NAME) && parent.attacking) {
      ME.world.remove(entity);
    }

  }

}
