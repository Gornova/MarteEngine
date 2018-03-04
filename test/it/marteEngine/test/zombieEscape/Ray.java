package it.marteEngine.test.zombieEscape;

import it.marteEngine.entity.Entity;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

public class Ray {

  private Vector2f start, end;
  public Line line;
  private Shape shape;

  public Ray(Entity one, Entity two) {
    calculateLine(one, two);
  }

  public void calculateLine(Entity one, Entity two) {
    float w = one.width;
    shape = new Rectangle(one.x, one.y, w, distance(one, two));
    float centerOneX = one.x + one.width / 2;
    float centerTwoX = two.x + two.width / 2;

    float centerOneY = one.y + one.height / 2;
    float centerTwoY = two.y + two.height / 2;

    start = new Vector2f(centerOneX, centerOneY);
    end = new Vector2f(centerTwoX, centerTwoY);
    float heading = calculateAngle(start.x, start.y, end.x, end.y);
    Transform t = Transform.createRotateTransform(heading, centerOneX,
        centerOneY);
    shape = shape.transform(t);
    line = new Line(centerOneX, centerOneY, centerTwoX, centerTwoY);
  }

  public Vector2f getDirection() {
    Vector2f d = new Vector2f();
    d.x = end.x - start.x;
    d.y = end.y - start.y;
    return d.normalise();
  }

  public void render(Graphics g) {
    g.draw(line);
  }

  public void update(Entity one, Entity two) {
    calculateLine(one, two);
  }

  /**
   * Returns the distance squared between the two body's centers.
   */
  public static float distance2(Entity one, Entity two) {
    float x = one.x + one.width / 2 - two.x + two.width / 2;
    x = x * x;
    float y = one.y + one.height / 2 - two.y + two.height / 2;
    y = y * y;
    return (x + y);
  }

  public static float distance(Entity one, Entity two) {
    return (float) Math.sqrt(distance2(one, two));
  }

  public static float calculateAngle(Entity one, float x2, float y2) {
    float x1 = one.x + one.width / 2;
    float y1 = one.y + one.height / 2;
    return calculateAngle(x1, y1, x2, y2);
  }

  public static float calculateAngle(float x, float y, float x1, float y1) {
    double angle = Math.atan2(y - y1, x - x1);
    return (float) angle + 1.5f;
  }

}
