package app;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.*;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;

public class Cube {
    private Shader shader;
    private int VAO,VBO,EBO;
    private FloatBuffer verticesBuffer;
    private Vector3f position = new Vector3f(5.0f,0.0f,0.0f);
    private Vector3f rotation = new Vector3f(0.0f,0.0f,0.0f);
    private boolean gooch;

    public Cube(boolean flag){ // Gooch lighting effect
        shader = new Shader();
        gooch = true;
        InitShader();
        InitBuffers();
        //Test();
    }

    private void Test(){
        try {
            shader.attachVertexShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.vs"));
            shader.attachTesselationControlShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.tc"));
            shader.attachTesselationEvaluationShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.te"));
            shader.attachFragmentShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.fs"));
            shader.link();
        }catch(IOException err){
            System.err.println(err);
        }
    }
    private void InitShader(){
       /* shader.setAttrib("vPos");
        shader.setUniform("viewproj");
        shader.setUniform("modelMatrix");*/
        if(gooch){
            try {
                shader.attachVertexShader(Application.getInstance().getApp_path().concat("Shaders/cube_gooch.vs"));
                shader.attachTesselationControlShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.tc"));
                shader.attachTesselationEvaluationShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.te"));
                shader.attachGeometryShader(Application.getInstance().getApp_path().concat("Shaders/cube_gooch.gs"));
                shader.attachFragmentShader(Application.getInstance().getApp_path().concat("Shaders/cube_gooch.fs"));
                shader.link();
            }catch(IOException err){
                System.err.println(err);
            }
        }
        else{
            try {
                //shader.attachVertexShader(getClass().getResource("/Shaders/vertex.vs").getPath().substring(1));
                //shader.attachFragmentShader(getClass().getResource("/Shaders/fragment.fs").getPath().substring(1));
                shader.attachVertexShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.vs"));
                shader.attachTesselationControlShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.tc"));
                shader.attachTesselationEvaluationShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.te"));
                shader.attachFragmentShader(Application.getInstance().getApp_path().concat("Shaders/cube_fix.fs"));
                shader.link();
            }catch(IOException err){
                System.err.println(err);
            }
        }
    }
    private void InitBuffers(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER,VBO);

        // The vertices of our app.Triangle
        float[] vertices = new float[]{
                +0.0f, +2.0f, 0.0f, 1.0f, // Top coordinate
                -2.0f, -2.0f, 0.0f, 1.0f, // Bottom-left coordinate
                +2.0f, -2.0f, 0.0f, 1.0f // Bottom-right coordinate
        };
        float tab[] = {-1.0f,  1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                -1.0f,  1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                1.0f, -1.0f,  1.0f
        };
        float[] colors = new float[]{
                1, 0, 0, 1,
                0, 1, 0, 1,
                0, 0, 1, 1
        };
        // Create a FloatBuffer of vertices
        verticesBuffer = BufferUtils.createFloatBuffer(tab.length);
        verticesBuffer.put(tab).flip();
        glBufferData(GL_ARRAY_BUFFER,verticesBuffer,GL_STATIC_DRAW);

        glEnableVertexAttribArray(glGetAttribLocation(shader.getID(),"vPos"));
        //glEnableVertexAttribArray(1);

        glVertexAttribPointer(glGetAttribLocation(shader.getID(),"vPos"), 3, GL_FLOAT, false, 0, 0);
        //glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
    }

    public void Update(float dt){

    }

    public void Render(Matrix4f view,Matrix4f proj,Matrix4f ortho){
        glBindVertexArray(VAO);
        shader.bind();

        Matrix4f model = new Matrix4f();
        model.zero();
        model.m00(1);
        model.m11(1);
        model.m22(1);
        model.m33(1);
        model.translate(position.x, position.y, position.z).rotate(rotation.y+=0.008f,0.0f,1.0f,0.0f).rotate(rotation.x+=0.003f,1.0f,0.0f,0.0f).scale(10,10,10);
        FloatBuffer fb_view = BufferUtils.createFloatBuffer(16);
        FloatBuffer fb_proj = BufferUtils.createFloatBuffer(16);
        FloatBuffer fb_model = BufferUtils.createFloatBuffer(16);
        model.get(fb_model);
        view.get(fb_view);
        proj.get(fb_proj);

        glUniformMatrix4fv(shader.getUniform("view"),false,fb_view);
        glUniformMatrix4fv(shader.getUniform("projection"),false,fb_proj);
        glUniformMatrix4fv(shader.getUniform("model"),false,fb_model);
        glUniform4f(shader.getUniform("vCol"),0.6f,0.6f,0.0f,1.0f);
        glUniform3f(shader.getUniform("light.pos"),0.0f,3.0f,-5.0f);
        glUniform3f(shader.getUniform("light.col"),1.0f,1.0f,1.0f);
        glUniform3f(shader.getUniform("tessLevelOuter"),4.0f,4.0f,4.0f);
        glUniform1f(shader.getUniform("tessLevelInner"),8.0f);
        /*if(gooch){
            glDrawArrays(GL_TRIANGLES, 0, 36);
        }
        else{
            glUniform3f(shader.getUniform("tessLevelOuter"),4.0f,4.0f,4.0f);
            glUniform1f(shader.getUniform("tessLevelInner"),4.0f);
            glDrawArrays(GL_PATCHES,0,36);
        }*/
        // Draw a triangle of 3 vertices
        glPatchParameteri(GL_PATCH_VERTICES,36);
        glDrawArrays(GL_PATCHES,0,36);

        shader.unbind();
        glBindVertexArray(0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
