package UI;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.Loader;

/**
 * Coordinates from 0 to 1, left to right(X), top to bottom(Y);
 */
public class UIComponent {

  protected UIComponent parentComponent;
  protected String textureName;

  protected float relativeXL;   // x left
  protected float relativeXR;   // x right
  protected float relativeYU;   // y up
  protected float relativeYD;   // y down

  protected float absoluteXL;   // x left
  protected float absoluteXR;   // x right
  protected float absoluteYU;   // y up
  protected float absoluteYD;   // y down

  protected boolean visible;


  private UITexture background;

  private List<UIComponent> childs = new ArrayList<>();

  private float getAbsoluteXL() {
    return absoluteXL;
  }

  private float getAbsoluteXR() {
    return absoluteXR;
  }

  private float getAbsoluteYU() {
    return absoluteYU;
  }

  private float getAbsoluteYD() {
    return absoluteYD;
  }

  private void setBackground() {
    if(background == null) {
      background = new UITexture(
          Loader.getLoader().loadTexture(textureName),
          new Vector2f(absoluteXL + absoluteXR - 1,
              1 - absoluteYU - absoluteYD),
          new Vector2f(absoluteXR - absoluteXL, absoluteYD - absoluteYU));
    } else {
      background.setCoords(new Vector2f(absoluteXL + absoluteXR - 1,
              1 - absoluteYU - absoluteYD),
          new Vector2f(absoluteXR - absoluteXL, absoluteYD - absoluteYU));
    }
  }

  public UIComponent(String textureName, UIComponent parent, float relativeXL, float relativeXR,
      float relativeYU, float relativeYD) {
    this.textureName = textureName;
    parent.add(this,relativeXL,relativeXR,relativeYU,relativeYD);
    setBackground();
  }

  public UIComponent(String textureName, float relativeXL, float relativeXR, float relativeYU,
      float relativeYD) {
    setPosition(relativeXL,relativeXR,relativeYU,relativeYD);
    this.parentComponent = null;
    computeAbsolutePosition();
    this.visible = true;
    this.textureName = textureName;
    setBackground();
  }

  public UIComponent(String textureName, UIComponent parent) {
    this(textureName, parent,0,0,0,0);
    this.visible = false;
  }

  private void setPosition(float relativeXL, float relativeXR, float relativeYU,
      float relativeYD) {
    this.relativeXL = relativeXL;
    this.relativeXR = relativeXR;
    this.relativeYU = relativeYU;
    this.relativeYD = relativeYD;
    computeAbsolutePosition();
  }

  public void setVisible(boolean visible) {
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
