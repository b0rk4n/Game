package UI;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import java.util.ArrayList;
import java.util.List;

public class UITextbox extends UIBordedComponent {

  List<Text> texts = new ArrayList<>();

  public UITextbox(String Texture, String cornerTexture, String borderTexture, UIComponent parent,
                   float XL, float XR, float YU, float YD, float thickness){
      super(Texture, cornerTexture, borderTexture, parent,  XL, XR, YU,  YD, thickness);
  }

  public UITextbox(String Texture, String cornerTexture, String borderTexture,
                   float XL, float XR, float YU, float YD, float thickness){
    super(Texture, cornerTexture, borderTexture,  XL, XR, YU,  YD, thickness);
  }

  void drawText(Loader loader, String input, String fontType, float fontSize, boolean absoluteCoords,
                          float X, float Y, float maxLineLength, boolean centered, Vector3f colour){

    Text text = new Text(this,loader,input,fontType,fontSize,X,Y,maxLineLength,centered,colour,absoluteCoords);
    texts.add(text);
  }



  public void removetext(){
    for(Text teext : texts) {
      texts.remove(teext);
      teext.text.remove();
    }
    texts.clear();
  }

}
