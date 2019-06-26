import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;

public class Explosion extends Model{

    private static int particles_count = 100;
    private static float particle_scale = 2.0f;
    private static float radius = 50.0f;
    private Shader shader;
    private int bilboard_buffer;
    private int position_buffer;
    private int color_buffer;
    private FloatBuffer models_buffer;

    public Explosion(){
        Init();
    }

    private void InitBuffer(){
        float tab[] = {-1.0f,-1.0f,1.0f,
                        1.0f,-1.0f,1.0f,
                        -1.0f,1.0f,1.0f,
                        1.0f,1.0f,1.0f};

        FloatBuffer tab_buf = BufferUtils.createFloatBuffer(tab.length);
        tab_buf.put(tab).flip();

        setVAO(glGenVertexArrays());
        glBindVertexArray(getVAO());

        bilboard_buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,bilboard_buffer);
        glBufferData(GL_ARRAY_BUFFER,tab_buf,GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);


        CreateModelMatrixesBuffer();
        CreateModelMatrixes();
        position_buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,position_buffer);
        glBufferData(GL_ARRAY_BUFFER,models_buffer,GL_STREAM_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,16,GL_FLOAT,false,0,0);


        glVertexAttribDivisor(0, 0);
        glVertexAttribDivisor(1, 1);

        glBindVertexArray(0);
    }

    private void InitShader(){
        shader = new Shader();
        try {
            shader.attachVertexShader(getClass().getResource("/Shaders/particle.vs").getPath().substring(1));
            shader.attachFragmentShader(getClass().getResource("/Shaders/particle.fs").getPath().substring(1));
            shader.link();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CreateModelMatrixesBuffer(){
        models_buffer = BufferUtils.createFloatBuffer(particles_count*16);
    }

    private void CreateModelMatrixes(){
        for(int i = 0 ; i < particles_count ; i++){
            Matrix4f model = new Matrix4f();
            model.zero();
            model.m00(1);
            model.m11(1);
            model.m22(1);
            model.m33(1);
            float angle = (float)i/(float)particles_count*360.0f;
            float x = (float) (Math.sin(angle)*radius);
            float y = (float) (Math.cos(angle)*radius);
            model.translate((float)(i),(float)(i),0.0f);
            AddMatrix2Buffer(model);
            System.out.println(model);
        }
        models_buffer.flip();
        /*for(int i = 0,j = 0 ; i < models_buffer.capacity();i++,j++){
            System.out.print(models_buffer.get(i)+", ");
            if(j == 16){
                System.out.println(" ");
                j = 0;
            }
        }*/
    }

    private void AddMatrix2Buffer(@org.jetbrains.annotations.NotNull Matrix4f model){
         
    }

    @Override
    public void Init() {
        InitShader();
        InitBuffer();
    }

    @Override
    public void Update(float dt) {

    }

    @Override
    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho) {
        glBindVertexArray(getVAO());
        shader.bind();

        FloatBuffer fb_proj = BufferUtils.createFloatBuffer(16);
        ortho.get(fb_proj);

        glUniform1f(shader.getUniform("scale"),particle_scale);
        glUniformMatrix4fv(shader.getUniform("projection"),false,fb_proj);


        glDrawArraysInstanced(GL_TRIANGLE_STRIP,0,4,particles_count);
        glBindVertexArray(0);
    }
}
