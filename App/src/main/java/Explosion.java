import org.dyn4j.geometry.Vector2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

    private int particles_count;
    private float speedModifier;
    private float particle_scale = 1.0f;
    private float radius;
    private float time = 0.0f;
    private float time_end;
    private boolean isLooping;
    private boolean isActive = true;

    private Shader shader;
    private int bilboard_buffer;
    private int position_buffer1;
    private int position_buffer2;
    private int position_buffer3;
    private int position_buffer4;
    private int direction_buffer;
    private int color_buffer;
    private FloatBuffer models_buffer1;
    private FloatBuffer models_buffer2;
    private FloatBuffer models_buffer3;
    private FloatBuffer models_buffer4;
    private FloatBuffer models_direction;

    public Explosion(int particles_count, float speedModifier,float radius,float scale,float time_end,boolean isLooping ,Vector2f position){
        this.particles_count = particles_count;
        this.speedModifier = speedModifier;
        this.time_end = time_end;
        this.isLooping = isLooping;
        this.radius = radius;
        this.particle_scale = scale;
        this.setPosition(new Vector3f(position.x,position.y,0.0f));
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

        position_buffer1 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,position_buffer1);
        glBufferData(GL_ARRAY_BUFFER,models_buffer1,GL_STREAM_DRAW);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,4,GL_FLOAT,false, 0,0);

        position_buffer2 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,position_buffer2);
        glBufferData(GL_ARRAY_BUFFER,models_buffer2,GL_STREAM_DRAW);

        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,4,GL_FLOAT,false,0,0);

        position_buffer3 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,position_buffer3);
        glBufferData(GL_ARRAY_BUFFER,models_buffer3,GL_STREAM_DRAW);

        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3,4,GL_FLOAT,false,0,0);

        position_buffer4 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,position_buffer4);
        glBufferData(GL_ARRAY_BUFFER,models_buffer4,GL_STREAM_DRAW);

        glEnableVertexAttribArray(4);
        glVertexAttribPointer(4,4,GL_FLOAT,false,0,0);

        direction_buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,direction_buffer);
        glBufferData(GL_ARRAY_BUFFER,models_direction,GL_STREAM_DRAW);

        glEnableVertexAttribArray(5);
        glVertexAttribPointer(5,2,GL_FLOAT,false,0,0);

        glVertexAttribDivisor(0, 0);
        glVertexAttribDivisor(1, 1);
        glVertexAttribDivisor(2, 1);
        glVertexAttribDivisor(3, 1);
        glVertexAttribDivisor(4, 1);
        glVertexAttribDivisor(5, 1);

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
        models_buffer1 = BufferUtils.createFloatBuffer(particles_count*4);
        models_buffer2 = BufferUtils.createFloatBuffer(particles_count*4);
        models_buffer3 = BufferUtils.createFloatBuffer(particles_count*4);
        models_buffer4 = BufferUtils.createFloatBuffer(particles_count*4);
        models_direction = BufferUtils.createFloatBuffer(particles_count*2);
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
            Vector2 center = new Vector2(getPosition().x,getPosition().y);
            Vector2 direction = new Vector2(x,y);
            direction.normalize();
            model.translate((float)(center.x),(float)(center.y),0.0f);
            model.translate((float)(x),(float)(y),0.0f);
            AddMatrix2Buffer(model);
            AddDirection2Buffer(direction);
        }
        models_buffer1.flip();
        models_buffer2.flip();
        models_buffer3.flip();
        models_buffer4.flip();
        models_direction.flip();
    }

    private void AddMatrix2Buffer(Matrix4f model){

        // COLUMN ORDER
        models_buffer1.put(model.m00());
        models_buffer1.put(model.m01());
        models_buffer1.put(model.m02());
        models_buffer1.put(model.m03());

        models_buffer2.put(model.m10());
        models_buffer2.put(model.m11());
        models_buffer2.put(model.m12());
        models_buffer2.put(model.m13());

        models_buffer3.put(model.m20());
        models_buffer3.put(model.m21());
        models_buffer3.put(model.m22());
        models_buffer3.put(model.m23());

        models_buffer4.put(model.m30());
        models_buffer4.put(model.m31());
        models_buffer4.put(model.m32());
        models_buffer4.put(model.m33());
    }

    private void AddDirection2Buffer(Vector2 dir){
        models_direction.put((float) dir.x);
        models_direction.put((float) dir.y);
    }

    @Override
    public void Init() {
        InitShader();
        InitBuffer();
    }

    @Override
    public void Update(float dt) {
        if(isActive){
            //particle_scale += dt;
            time += dt;
            if(time >= time_end){
                if(isLooping)time = 0.0f;
                else isActive = false;
            }
        }
    }

    @Override
    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho) {
        if(isActive){
            glBindVertexArray(getVAO());
            shader.bind();

            FloatBuffer fb_proj = BufferUtils.createFloatBuffer(16);
            ortho.get(fb_proj);

            glUniform1f(shader.getUniform("speedModifier"),speedModifier);
            glUniform1f(shader.getUniform("time"),time);
            glUniform1f(shader.getUniform("screen_x"),Application.getInstance().getManager().getScreen_x());
            glUniform1f(shader.getUniform("screen_y"),Application.getInstance().getManager().getScreen_y());
            glUniform1f(shader.getUniform("scale"),particle_scale);
            glUniformMatrix4fv(shader.getUniform("projection"),false,fb_proj);


            glDrawArraysInstanced(GL_TRIANGLE_STRIP,0,4,particles_count);
            glBindVertexArray(0);
        }
    }
}
