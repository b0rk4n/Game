package UI;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by paul on 23/05/17.
 */
public class UITexture {
  private int texture;
  private Vector2f position;
  private Vector2f scale;
  private float rotation;

  public UITexture(int texture, Vector2f position,
      Vector2f scale, float rotation) {
    this.texture = texture;
    this.position = position;
    this.scale = scale;
    this.rotation = rotation;
  }

  public int getTexture() {
    return texture;
  }

  public Vector2f getPosition() {
    return position;
  }

  public Vector2f getScale() {
    return scale;
  }

  public float getRotation() {
    return rotation;
  }

  public void setCoords(Vector2f position, Vector2f scale, float rotation){
    this.scale = scale;
    this.position = position;
    this.rotation = rotation;
  }
}
