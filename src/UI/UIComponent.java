package UI;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.Loader;

/**
 * Coordinates from 0 to 1, left to right(X), top to bottom(Y);
 */
public class UIComponent {

  private UIComponent parentComponent;
  private String textureName;

  private float relativeXL;   // x left
  private float relativeXR;   // x right
  private float relativeYU;   // y up
  private float relativeYD;   // y down

  private float absoluteXL;   // x left
  private float absoluteXR;   // x right
  private float absoluteYU;   // y up
  private float absoluteYD;   // y down

  private boolean visible;
  private float rotation;

  private UITexture background;

  private List<UIComponent> childs = new ArrayList<>();

  UIComponent getParentComponent() {
    return parentComponent;
  }

  float getAbsoluteXL() {
    return absoluteXL;
  }

  float getAbsoluteXR() {
    return absoluteXR;
  }

  float getAbsoluteYU() {
    return absoluteYU;
  }

  float getAbsoluteYD() {
    return absoluteYD;
  }

  float getRelativeXL() {
    return relativeXL;
  }

  float getRelativeXR() {
    return relativeXR;
  }

  float getRelativeYU() {
    return relativeYU;
  }

  float getRelativeYD() {
    return relativeYD;
  }

  public UITexture getTexture() {
    return background;
  }

  void setTexture(UITexture texture) {
    background = texture;
  }

  void setTextureName(String texture) {
    textureName = texture;
  }

  void setBackground() {
    if(background == null) {
      background = new UITexture(
          Loader.getLoader().loadTexture(textureName),
          new Vector2f(absoluteXL + absoluteXR - 1,
              1 - absoluteYU - absoluteYD),
          new Vector2f(absoluteXR - absoluteXL, absoluteYD - absoluteYU),
              rotation);
    } else {
      background.setCoords(new Vector2f(absoluteXL + absoluteXR - 1,
              1 - absoluteYU - absoluteYD),
          new Vector2f(absoluteXR - absoluteXL, absoluteYD - absoluteYU),
              rotation);
    }
  }

  public UIComponent() {
  }

  public UIComponent(String textureName, UIComponent parent, float rotation, float relativeXL, float relativeXR,
      float relativeYU, float relativeYD) {
    this.rotation = rotation;
    this.textureName = textureName;
    parent.add(this,relativeXL,relativeXR,relativeYU,relativeYD);
    setBackground();
  }

  public UIComponent(String textureName, float rotation, float relativeXL, float relativeXR, float relativeYU,
      float relativeYD) {
    this.rotation = rotation;
    setPosition(relativeXL,relativeXR,relativeYU,relativeYD);
    this.parentComponent = null;
    computeAbsolutePosition();
    this.visible = true;
    this.textureName = textureName;
    setBackground();
  }

  UIComponent(String textureName, UIComponent parent) {
    this(textureName, parent, 0,0,0,0,0);
    this.visible = false;
  }

  UIComponent(String textureName) {
    this(textureName,0,0,0,0,0);
    this.visible = false;
  }

  void setPosition(float relativeXL, float relativeXR, float relativeYU, float relativeYD) {
    this.relativeXL = relativeXL;
    this.relativeXR = relativeXR;
    this.relativeYU = relativeYU;
    this.relativeYD = relativeYD;
    computeAbsolutePosition();
  }

  private void setVisible(boolean visible) {
    this.visible = visible;
  }

  private void setParent(UIComponent parent) {
    parentComponent = parent;
  }

  public void add(UIComponent child, float relativeXL, float relativeXR,
    float relativeYU, float relativeYD){
    child.setParent(this);
    child.setPosition(relativeXL, relativeXR, relativeYU, relativeYD);
    child.setBackground();
    childs.add(child);
    child.setVisible(true);
    child.recomputeAbsolutePosition();
  }

  private void recomputeAbsolutePosition() {
    this.computeAbsolutePosition();
    this.setBackground();

    if(this.childs != null) {
      for (UIComponent child : this.childs) {
        child.recomputeAbsolutePosition();
      }
    }
  }

  public void remove() {
    setPosition(0,0,0,0);
    if(parentComponent != null) {
      for (int i = 0; i < parentComponent.childs.size(); i++) {
        if (parentComponent.childs.get(i) == this) {
          parentComponent.childs.remove(i);
          parentComponent = null;

          break;
        }
      }
    }
    setVisible(false);
    setBackground();
    if(!this.childs.isEmpty()) {
      for (UIComponent child : this.childs) {
        child.remove();
      }
    }
  }

  private void computeAbsolutePosition() {
    float parentXL;
    float parentXR;
    float parentYU;
    float parentYD;

    if(parentComponent == null) {
      parentXL = 0;
      parentXR = 1;
      parentYU = 0;
      parentYD = 1;
    } else {
      parentXL = parentComponent.getAbsoluteXL();
      parentXR = parentComponent.getAbsoluteXR();
      parentYU = parentComponent.getAbsoluteYU();
      parentYD = parentComponent.getAbsoluteYD();
    }

    absoluteXL = parentXL + relativeXL * (parentXR - parentXL);
    absoluteXR = parentXL + relativeXR * (parentXR - parentXL);
    absoluteYU = parentYU + relativeYU * (parentYD - parentYU);
    absoluteYD = parentYU + relativeYD * (parentYD - parentYU);
  }

  public void getTextures(List<UITexture> textures) {
    textures.add(background);
    for(UIComponent child:childs) {
      child.getTextures(textures);
    }
  }
}
