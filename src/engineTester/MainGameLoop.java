package engineTester;


import UI.UIComponent;
import UI.UIRenderer;
import UI.UITexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.MousePicker;

/**
 * Created by paul on 17/02/17.
 */

public class MainGameLoop {

  private static final int dx1[] = {0,1,1,0,-1,-1};
  private static final int dy1[] = {1,0,-1,-1,-1,0};
  private static final int dx2[] = {0,1,1,0,-1,-1};
  private static final int dy2[] = {1,1,0,-1,0,1};

  private static boolean hasRoad[][] = new boolean[40][25];
  private static List<Entity>[][] roadParts = new List[40][25];


  private static float getCoordX(int x, int y) {
    return 7.5f *x;
  }

  private static float getCoordY(int x, int y) {
    return y*8.64f + (x%2 == 0 ? 0 : 4.32f);
  }

  private static String toBinaryString(int x) {
    String s = Integer.toBinaryString(x);
    while(s.length() != 6) {
      s= 0 +s;
    }
    return s;
  }

  private static List<Entity> generateRoadParts(int x, int y, Loader loader, ModelTexture sand) {
    if(!hasRoad[x][y])
      return new ArrayList<>();
    List<Entity> roadParts = new ArrayList<>();
    int last = -1;
    int first = -1;
    RawModel rawModel;
    TexturedModel model;
    int[] dx = x%2 ==0 ? dx1 : dx2;
    int[] dy = x%2 ==0 ? dy1 : dy2;
    for (int i = 0; i < 6; i++) {

      if ((x+dx[i] >=0 && x+dx[i] <40 && y+dy[i] >=0 && y+ dy[i] <25) && hasRoad[x + dx[i]][y + dy[i]]) {
        rawModel = OBJLoader.loadObjModel("roads/road" + i, loader);
        model = new TexturedModel(rawModel, sand);
        roadParts.add(
            new Entity(model, new Vector3f(getCoordX(x, y), getCoordY(x, y), 0),
                0, 90, 90, 1));
        if (last != -1) {
          //System.out.println(toBinaryString((1 << last) + (1 << i)));
          rawModel = OBJLoader.loadObjModel("roads/" + toBinaryString((1 << (5-last)) + (1 << (5-i))), loader);
          model = new TexturedModel(rawModel, sand);
          roadParts.add(new Entity(model,
              new Vector3f(getCoordX(x, y), getCoordY(x, y), 0), 0, 90, 90, 1));
        } else {
          first = i;
        }
        last = i;
      }
    }
    if(first != last) {
      rawModel = OBJLoader.loadObjModel("roads/" + toBinaryString((1<<(5-last)) + (1<<(5-first))),loader);
      model = new TexturedModel(rawModel,sand);
      roadParts.add(new Entity(model,new Vector3f(getCoordX(x,y),getCoordY(x,y),0),0,90,90,1));
    }
    return roadParts;

  }

  public static void renderRoad(int x, int y,MasterRenderer renderer) {
    for (Entity part: roadParts[x][y]) {
      renderer.processEntity(part);
    }
  }


  public static void main(String[] args) {

    DisplayManager.createDisplay();

    Loader loader = Loader.getLoader();


    RawModel plainRaw = OBJLoader.loadObjModel("hex",loader);
    ModelTexture texture = new ModelTexture(loader.loadTexture("hex"));
    TexturedModel plain = new TexturedModel(plainRaw, texture);
    RawModel hillRaw = OBJLoader.loadObjModel("hex_hill2", loader);
    TexturedModel hill = new TexturedModel(hillRaw, texture);

    RawModel castleRaw = OBJLoader.loadObjModel("castle",loader);
    ModelTexture texture1 = new ModelTexture(loader.loadTexture("cobble"));
    TexturedModel castle = new TexturedModel(castleRaw,texture1);
    Entity entity = new Entity(castle, new Vector3f(0,0,0), 0 ,90, 90,1 );


    RawModel roadsRaw = OBJLoader.loadObjModel("roads/road5",loader);
    ModelTexture sand = new ModelTexture(loader.loadTexture("sand"));

    RawModel carutaRaw = OBJLoader.loadObjModel("caruta",loader);
    ModelTexture brown = new ModelTexture(loader.loadTexture("brown"));
    TexturedModel carutaTex= new TexturedModel(carutaRaw,brown);
    Entity caruta = new Entity(carutaTex, new Vector3f(0,0,0), 0 ,90, 90, 1);

    Random randomGenerator = new Random();
    Entity[][] tiles = new Entity[40][25];
    for (int i = 0; i < 40; i++) {
      for (int j = 0; j < 25; j++) {
        TexturedModel model = plain;//(i+j) % 2 == 0 ? plain : hill;
        tiles[i][j] = new Entity(model, new Vector3f(i*7.5f,j*8.64f + (i%2 == 0 ? 0 : 4.32f),0),0,90,90,1);
        hasRoad[i][j] = randomGenerator.nextBoolean();
      }
    }
    for (int i = 0; i < 40; i++) {
      for (int j = 0; j < 25; j++) {
        roadParts[i][j] = generateRoadParts(i,j,loader,sand);
      }
    }

    Camera camera = new Camera();

    Light light = new Light(new Vector3f(400,400,700),new Vector3f(1f,1f,1f));

    long totalTime = 0;
    int fps = 0;

    MasterRenderer renderer = new MasterRenderer();


    TextMaster.init(loader);
    FontType font = new FontType(loader.loadTexture("sans"),new File("res/sans.fnt"));
    GUIText text = new GUIText("text", 1, font, new Vector2f(0,0),1f,true);
    MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix());

    // UI stuff
    /*List<UITexture> uis = new ArrayList<>();
    UITexture uiTexture = new UITexture(loader.loadTexture("cobble"), new Vector2f(0.5f,0.5f),new Vector2f(0.25f,0.25f));
    uis.add(uiTexture);
    */
    List<UIComponent> uis = new ArrayList<>();
    UIComponent ui = new UIComponent("cobble",0.5f,0.5f,0.25f,0.25f);
    UIComponent ui2 = new UIComponent("cobble",ui,0.5f,0.5f,0.5f,0.5f);
    UIComponent ui3 = new UIComponent("cobble",ui2,0.5f,0.5f,0.5f,1f);

    uis.add(ui);
    UIRenderer uiRenderer = new UIRenderer(loader);

    while(!Display.isCloseRequested()) {
      long startTime = System.nanoTime();
      camera.move();
      System.out.println(picker.getCurrentRay());
      picker.update();
      for (int i = 0; i < 40; i++) {
        for (int j = 0; j < 25; j++) {
          renderer.processEntity(tiles[i][j]);
          renderRoad(i,j,renderer);
        }
      }
      renderer.processEntity(entity);
      renderer.processEntity(caruta);
      renderer.render(light, camera);

      // UI stuff
      List<UITexture> uiTextures = new ArrayList<>();
      for(UIComponent uiComponent: uis) {
        uiComponent.getTextures(uiTextures);
      }
      System.out.println(uiTextures.size() + "\n\n\n\n\n");
      uiRenderer.render(uiTextures);

      long endTime = System.nanoTime();
      long duration = (endTime - startTime);
      totalTime += duration;
      ++fps;
      if(totalTime > 1000000000) {
        totalTime = 0;
        System.out.println(fps);
        fps = 0;
      }
      TextMaster.render();
      DisplayManager.updateDisplay();

    }
    // UI stuff
    uiRenderer.cleanUP();

    TextMaster.cleanUP();
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();


  }
}
