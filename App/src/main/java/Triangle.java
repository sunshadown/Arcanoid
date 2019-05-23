import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class Triangle extends Model {

    private static float time = 0.0f;
    private int[] indexes;
    private float[] vertexes;
    IntBuffer intBuffer;

    public Triangle(){
        Init();
    }
    public Triangle(Vector3f position,Vector3f rotation,Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        Init();
    }
    @Override
    public void Init() {
        shader = new Shader();
        InitShader();
        GenerateWaterTriangles(100,100);
        //InitBuffer();
        InitBufferWater();
    }

    @Override
    public void Update(float dt) {

    }

    @Override
    public void Render(Matrix4f view, Matrix4f proj,Matrix4f ortho) {
        glBindVertexArray(VAO);
        shader.bind();

        Matrix4f model = new Matrix4f();
        model.zero();
        model.m00(1);
        model.m11(1);
        model.m22(1);
        model.m33(1);
        model.translate(position.x, position.y, position.z).rotate((float)Math.toRadians(rotation.x),1,0,0).rotate((float)Math.toRadians(rotation.x),0,1,0).rotate((float)Math.toRadians(rotation.z),0,0,1).scale(scale.x,scale.y,scale.z);
        FloatBuffer fb_view = BufferUtils.createFloatBuffer(16);
        FloatBuffer fb_proj = BufferUtils.createFloatBuffer(16);
        FloatBuffer fb_model = BufferUtils.createFloatBuffer(16);
        model.get(fb_model);
        view.get(fb_view);
        proj.get(fb_proj);
        glUniformMatrix4fv(shader.getUniform("viewMatrix"),false,fb_view);
        glUniformMatrix4fv(shader.getUniform("projectionMatrix"),false,fb_proj);
        glUniformMatrix4fv(shader.getUniform("modelMatrix"),false,fb_model);
        glUniform3f(shader.getUniform("light.pos"),0.0f,3.0f,-5.0f);
        glUniform1f(shader.getUniform("time"),time+=0.003f);

        // Draw a triangle of 3 vertices
        //glDrawArrays(GL_POINTS, 0, 1000);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glDrawElements(GL_TRIANGLES,indexes.length,GL_UNSIGNED_INT,0);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        shader.unbind();
        glBindVertexArray(0);
    }

    private void InitBufferWater(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER,VBO);

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertexes.length);
        floatBuffer.put(vertexes).flip();
        glBufferData(GL_ARRAY_BUFFER,floatBuffer,GL_STATIC_DRAW);

        glEnableVertexAttribArray(glGetAttribLocation(shader.getID(),"vPosition"));
        glVertexAttribPointer(glGetAttribLocation(shader.getID(),"vPosition"), 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,EBO);

        intBuffer = BufferUtils.createIntBuffer(indexes.length);
        intBuffer.put(indexes).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,intBuffer,GL_STATIC_DRAW);

        glBindVertexArray(0);
    }
    private void InitBuffer(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER,VBO);

        // The vertices of our Triangle
        float[] vertices = new float[]{
                +0.0f, +2.0f, 0.0f, // Top coordinate
                -2.0f, -2.0f, 0.0f, // Bottom-left coordinate
                +2.0f, -2.0f, 0.0f, // Bottom-right coordinate
        };

        float[] colors = new float[]{
                1, 0, 0,
                0, 1, 0,
                0, 0, 1,
        };


        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vertices.length);
        floatBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER,floatBuffer,GL_STATIC_DRAW);

        glEnableVertexAttribArray(glGetAttribLocation(shader.getID(),"vPosition"));
        glVertexAttribPointer(glGetAttribLocation(shader.getID(),"vPosition"), 3, GL_FLOAT, false, 0, 0);
        //COLORS
        int VBO_COLOR = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,VBO_COLOR);
        FloatBuffer floatBuffer2 = BufferUtils.createFloatBuffer(vertices.length);
        floatBuffer2.put(colors).flip();
        glBufferData(GL_ARRAY_BUFFER,floatBuffer2,GL_STATIC_DRAW);

        glEnableVertexAttribArray(glGetAttribLocation(shader.getID(),"vColor"));
        glVertexAttribPointer(glGetAttribLocation(shader.getID(),"vColor"), 3, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
    }

    private void InitShader(){
        try {
            shader.attachVertexShader(getClass().getResource("/Shaders/phong.vs").getPath().substring(1));
            shader.attachGeometryShader(getClass().getResource("/Shaders/phong.gs").getPath().substring(1));
            shader.attachFragmentShader(getClass().getResource("/Shaders/phong.fs").getPath().substring(1));
            shader.link();
        }catch(IOException err){
            System.err.println(err);
        }
    }

    private void GenerateWaterTriangles(int NUM_Z, int NUM_X){
        float HALF_SIZE_X = (float)NUM_X / 2;
        float HALF_SIZE_Z = (float)NUM_Z / 2;
        int i = 0, j = 0;


        List<Vector3f>tab = new ArrayList<>();
        for (j = 0; j <= NUM_Z; j++)
        {
            for (i = 0; i <= NUM_X; i++)
            {
                float x = (((float)i / ((float)NUM_X - 1.0f)) * 2.0f - 1.0f)*HALF_SIZE_X;
                float y = 0;
                float z = (((float)j / ((float)NUM_Z - 1.0f)) * 2.0f - 1.0f)*HALF_SIZE_Z;
                Vector3f temp = new Vector3f(x,y,z);
                tab.add(temp);
            }
        }

        List<Integer>indicies = new ArrayList<>();
        for (i = 0; i < NUM_Z; i++)
        {
            for (j = 0; j < NUM_X; j++)
            {
                int i0 = i * (NUM_X + 1) + j;
                int i1 = i0 + 1;
                int i2 = i0 + (NUM_X + 1);
                int i3 = i2 + 1;

                if (((j + i) % 2) >= 1)
                {

                    indicies.add(i0);
                    indicies.add(i2);
                    indicies.add(i1);

                    indicies.add(i1);
                    indicies.add(i2);
                    indicies.add(i3);

                }
                else
                {
                    indicies.add(i0);
                    indicies.add(i2);
                    indicies.add(i3);

                    indicies.add(i0);
                    indicies.add(i3);
                    indicies.add(i1);
                }
            }
        }

        vertexes = new float[tab.size()*3];
        for(int z = 0 ; z < tab.size();z+=3){
            vertexes[z] = tab.get(z).x;
            vertexes[z+1] = tab.get(z).y;
            vertexes[z+2] = tab.get(z).z;
        }
        indexes = new int[indicies.size()];
        for(int z = 0 ; z < indicies.size();z++){
            indexes[z] = indicies.get(z).intValue();
        }
    }
}