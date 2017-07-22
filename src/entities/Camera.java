package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by paul on 18/02/17.
 */
public class Camera {
  private Vector3f position = new Vector3f(0,-60,500);
  private float pitch = 0;//-(float)(180/Math.PI * Math.atan2(60, 50));
  private float yaw = 0;
  private float roll = 0;
  private float zoom = 0;
  private float deltaZoom = 0.01f;

  public Camera() {
  }

  public void move() {

    if(Keyboard.isKeyDown(Keyboard.KEY_W)){
      position.y += 0.5f*(1-zoom) + 0.1;
    }

    if(Keyboard.isKeyDown(Keyboard.KEY_A)){
      position.x -= 0.5f*(1-zoom) + 0.1;
    }

    if(Keyboard.isKeyDown(Keyboard.KEY_S)){
      position.y -= 0.5f*(1-zoom) + 0.1;
    }

    if(Keyboard.isKeyDown(Keyboard.KEY_D)){
      position.x += 0.5f*(1-zoom) + 0.1;
    }

    if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
      if(zoom - deltaZoom < 0) {
        zoom = 0;
        position.z = 50;
        pitch = -(float) (180 / Math.PI * Math.atan2(60, position.z));
      } else {
        zoom -= deltaZoom;
        position.z = 10 + (float) Math.cos(Math.PI/2*zoom) * 40;
        position.y += (Math.sin(Math.PI/2*(zoom-deltaZoom)) - Math.sin(Math.PI/2*zoom)) * 50;
        float relativeY = 60 - (float)(Math.sin(Math.PI/2 * zoom) * 60);
        pitch = -(float)(180/Math.PI * Math.atan2(relativeY, position.z));
      }
    }

    if(Keyboard.isKeyDown(Keyboard.KEY_X)){
      if(zoom + deltaZoom > 0.8) {

      } else {
        zoom += deltaZoom;
        position.z = 10 + (float) Math.cos(Math.PI/2*zoom) * 40;
        position.y += (Math.sin(Math.PI/2*zoom) - Math.sin(Math.PI/2*(zoom - deltaZoom))) * 50;
        float relativeY = 60 - (float)(Math.sin(Math.PI/2 * zoom) * 60);
        pitch = -(float)(180/Math.PI * Math.atan2(relativeY, position.z));
      }
    }


  }

  public Vector3f getPosition() {
    return position;
  }

  public float getPitch() {
    return pitch;
  }

  public float getYaw() {
    return yaw;
  }

  public float getRoll() {
    return roll;
  }
}
