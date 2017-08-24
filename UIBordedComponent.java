package UI;

import renderEngine.DisplayManager;

public class UIBordedComponent extends UIComponent {
    private String cornerTexture;
    private String borderTexture;
    private float width;
    private float height;
    private float thickness;

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return  width;
    }

    public UIBordedComponent(String Texture, String cornerTexture, String borderTexture,
                             float XL, float XR, float YU, float YD, float thickness) {
        super(Texture,0, XL, XR, YU, YD);
        this.cornerTexture = cornerTexture;
        this.borderTexture = borderTexture;
        this.width = XR - XL;
        this.height = YD - YU;
        this.thickness = thickness;
        this.drawBorders();
    }

    public UIBordedComponent(String Texture, String cornerTexture, String borderTexture, UIComponent parent,
                             float XL, float XR, float YU, float YD, float thickness) {
        super(Texture, parent, 0, XL, XR, YU, YD);
        this.cornerTexture = cornerTexture;
        this.borderTexture = borderTexture;
        this.width = XR - XL;
        this.height = YD - YU;
        this.thickness = thickness;
        this.drawBorders();
    }

    private void drawBorders() {
        float screenRatio = (float)DisplayManager.getWidth()/DisplayManager.getHeight();
        //List<UITexture> textures = new ArrayList<>();

        int numberOfSquaresX = Math.round(width/thickness);
        int numberOfSquaresY = Math.round(height/thickness/screenRatio);

        UIComponent upBorder, downBorder, leftBorder, rightBorder;

        for(float i = 0; i < 1; i+= (float)1/numberOfSquaresX) {
            upBorder = new UIComponent(borderTexture, this, 0, i, i+(float)1/numberOfSquaresX,-thickness*screenRatio/height, 0);
            downBorder = new UIComponent(borderTexture, this, 0, i, i+(float)1/numberOfSquaresX,1, 1+thickness*screenRatio/height);
        }

        for(float i = 0; i < 1; i+= (float)1/numberOfSquaresY)/**/ {
            leftBorder = new UIComponent(borderTexture, this, 0,-thickness/width, 0, i, i+(float)1/numberOfSquaresY);
            rightBorder = new UIComponent(borderTexture, this, 0, 1, 1+thickness/width, i, i+(float)1/numberOfSquaresY);
        }

        UIComponent corner1 = new UIComponent(cornerTexture, this, 0,
                - thickness/width, 0,-thickness*screenRatio/height, 0);
        UIComponent corner2 = new UIComponent(cornerTexture, this, 90,
                (1 - this.getAbsoluteYD() - this.getAbsoluteXL())/width - thickness*screenRatio/width,
                (1 - this.getAbsoluteYD() - this.getAbsoluteXL())/width,
                (this.getAbsoluteXL() - this.getAbsoluteYU())/height - thickness/height ,
                (this.getAbsoluteXL() - this.getAbsoluteYU())/height);
        UIComponent corner3 = new UIComponent(cornerTexture, this, 180,
                (1 - this.getAbsoluteXR() - this.getAbsoluteXL())/width - thickness/width ,
                (1 - this.getAbsoluteXR() - this.getAbsoluteXL())/width,
                (1 - this.getAbsoluteYD() - this.getAbsoluteYU())/height - thickness*screenRatio/height,
                (1 - this.getAbsoluteYD() - this.getAbsoluteYU())/height);
        UIComponent corner4 = new UIComponent(cornerTexture, this, 270,
                (this.getAbsoluteYU() - this.getAbsoluteXL())/width - thickness*screenRatio/width,
                (this.getAbsoluteYU() - this.getAbsoluteXL())/width,
                (1 - this.getAbsoluteXR() - this.getAbsoluteYU())/height - thickness/height,
                (1 - this.getAbsoluteXR() - this.getAbsoluteYU())/height);
    }
}