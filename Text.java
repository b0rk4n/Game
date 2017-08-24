package UI;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.io.File;

public class Text {
  private String input;
  private float fontSize;
  private String fontType;
  private float positionX;
  private float positionY;
  private float maxLineLength;
  private boolean centered;
  private Loader loader;
  private UIComponent background;
  GUIText text;

  public float getX() {
    return positionX;
  }

  public float getY() {
    return positionY;
  }

  float getMaxLineLength() {
    return maxLineLength;
  }

  boolean getCentered() {
    return centered;
  }

  Text(UIComponent background, Loader loader, String input, String fontType, float fontSize, float X, float Y,
              float maxLineLength, boolean centered, Vector3f colour, boolean absoluteCoords){
    this.background = background;
    this.input = input;
    this.fontType = fontType;
    this.fontSize = fontSize;
    this.positionX = X;
    this.positionY = Y;
    this.maxLineLength = maxLineLength;
    this.centered = centered;
    this.loader = loader;

    Vector3f vector = calculateCoords(absoluteCoords, X, Y, maxLineLength);

    X = vector.x;
    Y = vector.y;
    maxLineLength = vector.z;

    TextMaster.init(loader);
    FontType font = new FontType(loader.loadTexture(fontType),new File("res/" + fontType + ".fnt"));
    this.text = new GUIText(input, fontSize, font, new Vector2f(X,Y),maxLineLength,centered);
    text.setColour(colour.x,colour.y,colour.z);
  }

  void changeCoords(float X, float Y, float maxLineLength, boolean centered, boolean absoluteCoords){
    Vector3f vector = calculateCoords(absoluteCoords, X, Y, maxLineLength);

    X = vector.x;
    Y = vector.y;
    maxLineLength = vector.z;

    FontType font = new FontType(loader.loadTexture(fontType),new File("res/" + fontType + ".fnt"));
    GUIText text2 = new GUIText(input, fontSize, font, new Vector2f(X,Y),maxLineLength,centered);
    text2.setColour(text.getColour().x,text.getColour().y,text.getColour().z);
    this.text.remove();
    this.text = text2;
  }

  public void changeColour(float r, float g, float b) {
    text.setColour(r,g,b);
  }

  private Vector3f calculateCoords(boolean absoluteCoords, float X, float Y, float maxLineLength) {
    if (absoluteCoords){
      if(maxLineLength+X>1)
        return new Vector3f(X, Y,maxLineLength-X);
      else{
        return new Vector3f(X, Y, maxLineLength);
      }
    }
    else {
      float parentXL;
      float parentXR;
      float parentYU;
      float parentYD;

      parentXL = background.getAbsoluteXL();
      parentXR = background.getAbsoluteXR();
      parentYU = background.getAbsoluteYU();
      parentYD = background.getAbsoluteYD();

      X = parentXL + X * (parentXR - parentXL);
      Y = parentYU + Y * (parentYD - parentYU);
      maxLineLength = parentXR - X;

      return new Vector3f(X, Y, maxLineLength);
    }
  }
}
