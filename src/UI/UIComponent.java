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
  private float relativeYL;   // y left
  private float relativeYR;   // y right

  private float absoluteXL;   // x left
  private float absoluteXR;   // x right
  private float absoluteYL;   // y left
  private float absoluteYR;   // y right

  private boolean visible;


  private UITexture background;

  private List<UIComponent> childs = new ArrayList<>();

  private float getAbsoluteXL() {
    return absoluteXL;
  }

  private float getAbsoluteXR() {
    return absoluteXR;
  }

  private float getAbsoluteYL() { return absoluteYL; }

  private float getAbsoluteYR() {
    return absoluteYR;
  }

  private void setBackground() {
    if(background == null) {
      background = new UITexture(
          Loader.getLoader().loadTexture(textureName),
          new Vector2f(absoluteXL + absoluteXR - 1,
              1 - absoluteYL - absoluteYR),
          new Vector2f(absoluteXR - absoluteXL, absoluteYR - absoluteYL));
    } else {
      background.setCoords(new Vector2f(absoluteXL + absoluteXR - 1,
              1 - absoluteYL - absoluteYR),
          new Vector2f(absoluteXR - absoluteXL, absoluteYR - absoluteYL));
    }
  }

  public UIComponent(String textureName, UIComponent parent, float relativeXL, float relativeXR,
      float relativeYL, float relativeYR) {
    this.textureName = textureName;
    parent.add(this,relativeXL,relativeXR,relativeYL,relativeYR);
    setBackground();
  }

  public UIComponent(String textureName, float relativeXL, float relativeXR, float relativeYL,
      float relativeYR) {
    setPosition(relativeXL,relativeXR,relativeYL,relativeYR);
    this.parentComponent = null;
    computeAbsolutePosition();
    this.visible = true;
    this.textureName = textureName;
    setBackground();
  }

  public UIComponent(String textureName) {
    this(textureName,0,0,0,0);
    this.visible = false;
  }

  private void setPosition(float relativeXL, float relativeXR, float relativeYL,
      float relativeYR) {
    this.relativeXL = relativeXL;
    this.relativeXR = relativeXR;
    this.relativeYL = relativeYL;
    this.relativeYR = relativeYR;
    computeAbsolutePosition();
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  private void setParent(UIComponent parent) {
    parentComponent = parent;
  }

  public void add(UIComponent child, float relativeXL, float relativeXR,
      float relativeYL, float relativeYR){
      child.setParent(this);
      child.setPosition(relativeXL, relativeXR, relativeYL, relativeYR);
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
    for (int i = 0; i < parentComponent.childs.size(); i++) {
      if(parentComponent.childs.get(i) == this) {
        parentComponent.childs.remove(i);
        parentComponent  = null;
        setVisible(false);
        computeAbsolutePosition();
        return;
      }
    }
    assert true;
  }

  private void computeAbsolutePosition() {
    float parentXL;
    float parentXR;
    float parentYL;
    float parentYR;

    if(parentComponent == null) {
      parentXL = 0;
      parentXR = 1;
      parentYL = 0;
      parentYR = 1;
    } else {
      parentXL = parentComponent.getAbsoluteXL();
      parentXR = parentComponent.getAbsoluteXR();
      parentYL = parentComponent.getAbsoluteYL();
      parentYR = parentComponent.getAbsoluteYR();
    }

    absoluteXL = parentXL + relativeXL * (parentXR - parentXL);
    absoluteXR = parentXL + relativeXR * (parentXR - parentXL);
    absoluteYL = parentYL + relativeYL * (parentYR - parentYL);
    absoluteYR = parentYL + relativeYR * (parentYR - parentYL);
  }

  public void getTextures(List<UITexture> textures) {
    textures.add(background);
    for(UIComponent child:childs) {
      child.getTextures(textures);
    }
  }
}
