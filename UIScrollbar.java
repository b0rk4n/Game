package UI;

import org.lwjgl.input.Mouse;

public class UIScrollbar extends UIButton {

  private float mouseX;
  private float mouseY;
  private boolean wasPressed = false;
  private float originalXL;
  private float originalXR;
  private float originalYU;
  private float originalYD;

  public UIScrollbar(String textureName, UIComponent parent, float rotation, float relativeXL, float relativeXR,
                   float relativeYU, float relativeYD){
    super(textureName, parent, rotation, relativeXL, relativeXR, relativeYU, relativeYD);
    originalXL = relativeXL;
    originalXR = relativeXR;
    originalYU = relativeYU;
    originalYD = relativeYD;
  }

  public UIScrollbar(String textureName, float rotation, float relativeXL, float relativeXR,
                   float relativeYU, float relativeYD){
    super(textureName, rotation, relativeXL, relativeXR, relativeYU, relativeYD);
    originalXL = relativeXL;
    originalXR = relativeXR;
    originalYU = relativeYU;
    originalYD = relativeYD;
  }

  private boolean isPressed(){
    if(this.containes() && Mouse.isButtonDown(0) && !wasPressed) {
      mouseX = super.getMouseCoords().x;
      mouseY = super.getMouseCoords().y;
      wasPressed = true;
      return true;
    }
    else if(Mouse.isButtonDown(0) && wasPressed) {
      return  true;
    }
    originalXL = getRelativeXL();
    originalXR = getRelativeXR();
    originalYU = getRelativeYU();
    originalYD = getRelativeYD();
    wasPressed = false;
    return false;
  }

  void scrollVeritival() {
    if(this.isPressed()) {
      float YU = originalYU;
      if(this.getParentComponent() != null) {
        YU += (super.getMouseCoords().y - mouseY)/(this.getParentComponent().getAbsoluteYD() - this.getParentComponent().getAbsoluteYU());
      }
      float YD = YU + originalYD - originalYU;
      if(YU < 0) {
        YU = 0;
        YD = YU + originalYD - originalYU;
      }
      if(YD > 1) {
        YD = 1;
        YU = YD - originalYD + originalYU;
      }
      this.setPosition(this.getRelativeXL(), this.getRelativeXR(), YU, YD);
      this.setBackground();
    }
  }

  void scrollHorizontal() {
    if(this.isPressed()) {
      float XL = originalXL;
      if(this.getParentComponent() != null) {
        XL += (super.getMouseCoords().x - mouseX)/(this.getParentComponent().getAbsoluteXR() - this.getParentComponent().getAbsoluteXL());
      }
      float XR = XL + originalXR - originalXL;
      if(XL < 0) {
        XL = 0;
        XR = XL + originalXR - originalXL;
      }
      if(XR > 1) {
        XR = 1;
        XL = XR - originalXR + originalXL;
      }
      this.setPosition(XL, XR, this.getRelativeYU(), this.getRelativeYD());
      this.setBackground();
    }
  }

  private boolean containes(){
      return (getAbsoluteXL() <= getMouseCoords().x) && (getAbsoluteXR() >= getMouseCoords().x) &&
              (getAbsoluteYU() <= getMouseCoords().y) && (getAbsoluteYD() >= getMouseCoords().y);
  }
}