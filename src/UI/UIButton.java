package UI;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class UIButton extends UIComponent {

  private final String originalTexture;
  private boolean containes = false;
  private int clickCount = 0;

  public UIButton(String textureName, UIComponent parent, float rotation, float relativeXL, float relativeXR,
                  float relativeYU, float relativeYD){
    super(textureName, parent, rotation, relativeXL, relativeXR, relativeYU, relativeYD);
    originalTexture = textureName;
  }

  public UIButton(String textureName, float rotation, float relativeXL, float relativeXR,
                  float relativeYU, float relativeYD){
    super(textureName, rotation, relativeXL, relativeXR, relativeYU, relativeYD);
    originalTexture = textureName;
  }

  public void hoverOverButton(String textureName) {
    if(this.containes() && !containes){
      containes = true;
      this.setTexture(null);
      this.setTextureName(textureName);
      this.setBackground();
    }
    if(!this.containes() && containes) {
      containes = false;
      this.setTexture(null);
      this.setTextureName(originalTexture);
      this.setBackground();
    }
  }

  private boolean containes(){
    return (getAbsoluteXL() <= getMouseCoords().x) && (getAbsoluteXR() >= getMouseCoords().x) &&
            (getAbsoluteYU() <= getMouseCoords().y) && (getAbsoluteYD() >= getMouseCoords().y);
  }

  Vector2f getMouseCoords() {
      float mouseX = Mouse.getX();
      float mouseY = Mouse.getY();
      float x = mouseX / Display.getWidth();
      float y = 1- mouseY / Display.getHeight();
      return new Vector2f(x,y);
  }

  public boolean isLeftMouseClicked() {
    if(this.containes() && Mouse.isButtonDown(0)) {
      clickCount ++;
      return true;
    }
    if(clickCount > 50) {
      clickCount = 0;
    }
    return false;
  }

  public boolean isRightMouseClicked() {
    if(this.containes() && Mouse.isButtonDown(1)) {
      clickCount ++;
      return true;
    }
    if(clickCount > 50) {
      clickCount = 0;
    }
    return false;
  }
}