package it.marteEngine.test.avatar;

import it.marteEngine.ME;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MoveAvatarTest extends StateBasedGame {

  public MoveAvatarTest() {
    super("Move Avatar Test");
  }

  @Override
  public void initStatesList(GameContainer container) {
    addState(new AvatarWorld(0));
  }

  public static void main(String[] argv) throws SlickException {
    ME.keyToggleDebug = Input.KEY_1;
    ME.keyRestart = Input.KEY_R;
    AppGameContainer container = new AppGameContainer(
        new MoveAvatarTest());
    container.setDisplayMode(800, 600, false);
    container.setTargetFrameRate(60);
    container.start();
  }

}
