package UI;

import fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class ScrollableComponent extends UITextbox {
  private UIComponent firstBackground;
  private UIComponent secondBackground;
  private UIComponent scrollBarBackground;
  private UIScrollbar scrollBar;
  private final float originalYU;
  private final float originalYD;
  private final float height;
  private final float thickness;

  public ScrollableComponent(String textureName, String background, String cornerTexture, String borderTexture, UIComponent parent,
               float relativeXL, float relativeXR, float relativeYU, float relativeYD, float additionalHeight, float thickness){
      super(textureName, cornerTexture, borderTexture, parent, relativeXL, relativeXR, relativeYU, relativeYD + additionalHeight, thickness);
      firstBackground = new UIComponent(background, this,0,
              0,1,-this.getRelativeYU()/(this.getRelativeYD() - this.getRelativeYU()),0);
      secondBackground = new UIComponent(background, this,0, 0,1,
              (relativeYD - relativeYU)/(this.getRelativeYD() - this.getRelativeYU()),
              1+(1-this.getRelativeYD())/(this.getRelativeYD() - this.getRelativeYU()));
      originalYU = relativeYU;
      originalYD = relativeYD + additionalHeight;
      height = originalYD - originalYU;
      this.thickness = thickness;
  }

  public ScrollableComponent(String textureName, String background, String cornerTexture, String borderTexture,
                             float relativeXL, float relativeXR, float relativeYU, float relativeYD, float additionalHeight, float thickness){
      super(textureName, cornerTexture, borderTexture, relativeXL, relativeXR, relativeYU, relativeYD, thickness);
      float screenRatio = (float) DisplayManager.getWidth()/DisplayManager.getHeight();
      setPosition(this.getRelativeXL(), this.getRelativeXR(),this.getRelativeYU(),this.getRelativeYD() + additionalHeight);
      firstBackground = new UIComponent(background, this,0,
              0,1,-this.getRelativeYU()/(this.getRelativeYD() - this.getRelativeYU()),
              -thickness*screenRatio/(this.getRelativeYD() - this.getRelativeYU()));
      secondBackground = new UIComponent(background, this,0, 0,1,
              (relativeYD + thickness*screenRatio - relativeYU)/(this.getRelativeYD() - this.getRelativeYU()),
             1+(1-this.getRelativeYD())/(this.getRelativeYD() - this.getRelativeYU()));
      this.drawText(Loader.getLoader(),"TEXT TEXT TEXT TEXT TEXT TEXT TEXT TEXT TEXT TEXT TEXT", "sans", 1f, false, 0.5f,
              0.5f,1f, false, new Vector3f(1,0,0));
      scrollBarBackground = new UIComponent("cobble", this, 0, 1 + thickness/this.getWidth(), 1 + thickness/this.getWidth() + 0.1f,
              0,(relativeYD - relativeYU)/(this.getRelativeYD() - this.getRelativeYU()));
      scrollBar = new UIScrollbar("brown", scrollBarBackground, 0 , 0, 1,
        0, (relativeYD - relativeYU)/(this.getRelativeYD() - this.getRelativeYU()));
      System.out.println(this.getAbsoluteYD() + "  " + this.getRelativeYU() + "  " + relativeYD);
      originalYU = relativeYU;
      originalYD = relativeYD + additionalHeight;
      height = originalYD - originalYU;
      this.thickness = thickness;

      System.out.println(this.getAbsoluteYU()-thickness + "   " + secondBackground.getAbsoluteYU() + "   " + secondBackground.getAbsoluteYD());
    }

    public void Scroll() {
      float scrollBarBackgroundHeight = scrollBarBackground.getAbsoluteYD() - scrollBarBackground.getAbsoluteYU();
      float scrollBarHeight = scrollBar.getAbsoluteYD() - scrollBar.getAbsoluteYU();
      float scrollBarPercent = (scrollBar.getAbsoluteYU() - scrollBarBackground.getAbsoluteYU()) / (scrollBarBackgroundHeight - scrollBarHeight);
      scrollBar.scrollVeritival();
      float YU = scrollBarBackground.getAbsoluteYU() - ( height - scrollBarBackgroundHeight) * scrollBarPercent;
      float YD = this.getRelativeYU() + height;
      this.setPosition(getRelativeXL(),getRelativeXR(),YU,YD);
      this.setBackground();
      for(Text text : texts) {
          text.changeCoords(text.getX(), text.getY(), text.getMaxLineLength(), text.getCentered(), false);
          TextMaster.render(text.text.getFont());
      }

      //renderer.render(firstBackground.getTexture());
      //renderer.render(secondBackground.getTexture());
    }
}