import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public abstract class Model {
    private Shader shader;
    private int VAO;
    protected int VBO;
    protected int EBO;
    protected FloatBuffer verticesBuffer;
    private Vector3f position = new Vector3f(0.0f,0.0f,0.0f);
    protected Vector3f rotation = new Vector3f(0.0f,0.0f,0.0f);
    protected Vector3f scale = new Vector3f(1.0f,1.0f,1.0f);
    public abstract void Init();
    public abstract void Update(float dt);
    public abstract void Render(Matrix4f view, Matrix4f proj,Matrix4f ortho);

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public int getVAO() {
        return VAO;
    }

    public void setVAO(int VAO) {
        this.VAO = VAO;
    }
}
