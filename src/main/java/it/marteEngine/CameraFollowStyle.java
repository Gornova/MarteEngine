package it.marteEngine;

import it.marteEngine.entity.Entity;

/**
 * Dead zone presets. A dead zone is an area in which an entity can move in
 * without making the camera move.
 */
public enum CameraFollowStyle {
  /**
   * No dead zone, just tracks the focus object directly.
   */
  LOCKON {
    void createDeadzone(Camera camera, Entity entity) {
      int cameraWidth = camera.getWidth();
      int cameraHeight = camera.getHeight();
      int w = entity.width;
      int h = entity.height;
      camera.setDeadZone((cameraWidth - w) / 2,
          (int) ((cameraHeight - h) / 2 - h * 0.25f), w, h);
    }
  },
  /**
   * Narrow but tall rectangle
   */
  PLATFORMER {
    @Override
    void createDeadzone(Camera camera, Entity entity) {
      int cameraWidth = camera.getWidth();
      int cameraHeight = camera.getHeight();
      int w = cameraWidth / 8;
      int h = cameraHeight / 3;
      camera.setDeadZone((cameraWidth - w) / 2,
          (int) ((cameraHeight - h) / 2 - h * 0.25), w, h);
    }
  },
  /**
   * A medium-size square around the focus object.
   */
  TOPDOWN {
    void createDeadzone(Camera camera, Entity entity) {
      int cameraWidth = camera.getWidth();
      int cameraHeight = camera.getHeight();
      int helper = Math.max(cameraWidth, cameraHeight) / 4;
      camera.setDeadZone((cameraWidth - helper) / 2,
          (cameraHeight - helper) / 2, helper, helper);
    }
  },
  /**
   * A small square around the focused object.
   */
  TOPDOWN_TIGHT {
    void createDeadzone(Camera camera, Entity entity) {
      int cameraWidth = camera.getWidth();
      int cameraHeight = camera.getHeight();
      int helper = Math.max(cameraWidth, cameraHeight) / 8;
      camera.setDeadZone((cameraWidth - helper) / 2,
          (cameraHeight - helper) / 2, helper, helper);
    }
  },
  /**
   * Move screen by screen.
   */
  SCREEN_BY_SCREEN {
    void createDeadzone(Camera camera, Entity entity) {
      int cameraWidth = camera.getWidth();
      int cameraHeight = camera.getHeight();
      camera.setDeadZone(0, 0, cameraWidth, cameraHeight);
    }
  };

  /**
   * Creates a dead zone and set it to the camera.
   */
  abstract void createDeadzone(Camera camera, Entity entity);

  /**
   * @return The next CameraFollowStyle in the order they're declared.
   */
  public CameraFollowStyle next() {
    CameraFollowStyle[] values = values();
    return values[(ordinal() + 1) % values.length];
  }
}
