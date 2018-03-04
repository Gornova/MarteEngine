package it.marteEngine.entity;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TextEntity extends Entity {

  private Font font;
  private String text = "";

  public TextEntity(float x, float y, Font font, String text) {
    super(x, y);
    this.font = font;
    setText(text);
  }

  public void render(GameContainer container, Graphics g) throws SlickException {
    if (font == null) {
      font = container.getDefaultFont();
      this.calculateHitBox();
    }
    g.setFont(font);
    if (text != null) {
      if (scale != 1.0f) {
        g.translate(x, y);
        g.scale(scale, scale);
        g.drawString(text, 0, 0);
      } else {
        g.drawString(text, x, y);
      }

      if (scale != 1.0f) {
        g.resetTransform();
      }
    }
  }

  public Font getFont() {
    return font;
  }

  public void setFont(Font font) {
    this.font = font;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    calculateHitBox();
  }

  private void calculateHitBox() {
    if (font != null) {
      width = font.getWidth(text);
      height = font.getHeight(text);
      setHitBox(0, 0, width, height);
    }
  }
}
