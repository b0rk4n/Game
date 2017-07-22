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

  private float relativeX;
  private float relativeY;
  private float relativeW;
  private float relativeH;

  private float absoluteX;
  private float absoluteY;
  private float absoluteW;
  private float absoluteH;

  private boolean visible;


  private UITexture background;

  private List<UIComponent> childs = new ArrayList<>();

  private float getAbsoluteX() {
    return absoluteX;
  }

  private float getAbsoluteY() {
    return absoluteY;
  }

  private float getAbsoluteW() {
    return absoluteW;
  }

  private float getAbsoluteH() {
    return absoluteH;
  }

  private void setBackground() {
    if(background == null) {
      background = new UITexture(
          Loader.getLoader().loadTexture(textureName),
          new Vector2f(2 * absoluteX - 1 + absoluteW,
              2 * (1 - absoluteY) - 1 - absoluteH),
          new Vector2f(absoluteW, absoluteH));
    } else {
      background.setCoords(new Vector2f(2 * absoluteX - 1 + absoluteW,
              2 * (1 - absoluteY) - 1 - absoluteH),
          new Vector2f(absoluteW, absoluteH));
    }
  }

  public UIComponent(String textureName, UIComponent parent, float relativeX, float relativeY,
      float relativeW, float relativeH) {
    parent.add(this,relativeX,relativeY,relativeW,relativeH);
    this.textureName = textureName;
    setBackground();

  }

  public UIComponent(String textureName, float relativeX, float relativeY, float relativeW,
      float relativeH) {
    setPosition(relativeX,relativeY,relativeW,relativeH);
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

  public void setPosition(float relativeX, float relativeY, float relativeW,
      float relativeH) {
    this.relativeX = relativeX;
    this.relativeY = relativeY;
    this.relativeW = relativeW;
    this.relativeH = relativeH;
    computeAbsolutePosition();
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  private void setParent(UIComponent parent) {
    parentComponent = parent;
  }

  public void add(UIComponent child, float relativeX, float relativeY,
      float relativeW, float relativeH){
      child.setParent(this);
      child.setPosition(relativeX, relativeY, relativeW, relativeH);
      childs.add(child);
      child.computeAbsolutePosition();
      child.setVisible(true);

      //TODO: !!!!IMPORTANT!!!! need a recursive funtion to recompute the absolute coords of all childs
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
    float parentX, parentY, parentW, parentH;
    if(parentComponent == null) {
      parentX = 0;
      parentY = 0;
      parentW = 1;
      parentH = 1;
    } else {
      parentX = parentComponent.getAbsoluteX();
      parentY = parentComponent.getAbsoluteY();
      parentW = parentComponent.getAbsoluteW();
      parentH = parentComponent.getAbsoluteH();
    }

    absoluteH = parentH * relativeH;
    absoluteW = parentW * relativeW;
    absoluteX = parentX + relativeX * parentW;
    absoluteY = parentY + relativeY * parentH;
  }

  public void getTextures(List<UITexture> textures) {
    textures.add(background);
    for(UIComponent child:childs) {
      child.getTextures(textures);
    }
  }
}
