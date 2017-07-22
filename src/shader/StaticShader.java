package shader;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

/**
 * Created by paul on 17/02/17.
 */
public class StaticShader extends ShaderProgram {

  private static final String VERTEX_FILE = "src/shader/vertexShader.txt";
  private static final String FRAGMENT_FILE = "src/shader/fragmentShader.txt";

  private int location_transformationMatrix;
  private int location_projectionMatrix;
  private int location_viewMatrix;
  private int location_lightPosition;
  private int location_lightColour;

  public StaticShader() {
    super(VERTEX_FILE, FRAGMENT_FILE);
  }

  @Override
  protected void getAllUniformLocations() {
    location_transformationMatrix =
        super.getUniformLocation("transformationMatrix");
    location_projectionMatrix =
        super.getUniformLocation("projectionMatrix");
    location_viewMatrix =
        super.getUniformLocation("viewMatrix");
    location_lightPosition =
        super.getUniformLocation("lightPosition");
    location_lightColour =
        super.getUniformLocation("lightColour");
  }


  public void loadLight(Light light) {
    super.loadVector(location_lightPosition, light.getPosition());
    super.loadVector(location_lightColour, light.getColor());
  }

  @Override
  protected void bindAttributes() {
    super.bindAttribute(0, "position");
    super.bindAttribute(1, "textureCoords");
    super.bindAttribute(2, "normal");

  }

  public void loadTransformationMatrix(Matrix4f matrix) {
    super.loadMatrix(location_transformationMatrix, matrix);
  }

  public void loadProjectionMatrix(Matrix4f matrix) {
    super.loadMatrix(location_projectionMatrix, matrix);
  }

  public void loadViewMatrix(Camera camera) {
    Matrix4f viewMatrix = Maths.createViewMatrix(camera);
   // System.out.println(viewMatrix.toString());
    super.loadMatrix(location_viewMatrix, viewMatrix);
  }
}
