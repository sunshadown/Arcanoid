package app;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;

public class Attractor  extends  Model{

    private int count;
    private float a =(2.01f);
    private float b =(-2.53f);
    private float c =(1.61f);
    private float d =(-0.33f);
    private float radius = 10.0f;
    private float time = 0.0f;
    private float time_count = 5.0f;

    private FloatBuffer col_buffer0;
    private FloatBuffer col_buffer1;
    private FloatBuffer col_buffer2;
    private FloatBuffer col_buffer3;
    private FloatBuffer color_buffer;
    private FloatBuffer direction_buffer;
    private int direction;
    private int col0;
    private int col1;
    private int col2;
    private int col3;
    private int color;

    private FBO fbo;
    private Render2D renderpass;

    public Attractor(int count){
        this.count = count;
        Init();
        fbo = new FBO(1920,1080);
        renderpass = new Render2D(1,true,fbo.getTex());
    }

    private void InitBuffer(){
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

        // Create a FloatBuffer of vertices
        verticesBuffer = BufferUtils.createFloatBuffer(tab.length);
        verticesBuffer.put(tab).flip();

        setVAO(glGenVertexArrays());
        glBindVertexArray(getVAO());

        setVBO(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER,getVBO());
        glBufferData(GL_ARRAY_BUFFER,verticesBuffer,GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);

        col0 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,col0);
        glBufferData(GL_ARRAY_BUFFER,col_buffer0,GL_STREAM_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,4,GL_FLOAT,false,0,0);

        col1 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,col1);
        glBufferData(GL_ARRAY_BUFFER,col_buffer1,GL_STREAM_DRAW);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,4,GL_FLOAT,false,0,0);

        col2 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,col2);
        glBufferData(GL_ARRAY_BUFFER,col_buffer2,GL_STREAM_DRAW);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3,4,GL_FLOAT,false,0,0);

        col3 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,col3);
        glBufferData(GL_ARRAY_BUFFER,col_buffer3,GL_STREAM_DRAW);
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(4,4,GL_FLOAT,false,0,0);

        color = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,color);
        glBufferData(GL_ARRAY_BUFFER,color_buffer,GL_STREAM_DRAW);
        glEnableVertexAttribArray(5);
        glVertexAttribPointer(5,3,GL_FLOAT,false,0,0);


        direction = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,direction);
        glBufferData(GL_ARRAY_BUFFER,direction_buffer,GL_STREAM_DRAW);
        glEnableVertexAttribArray(6);
        glVertexAttribPointer(6,3,GL_FLOAT,false,0,0);

        glVertexAttribDivisor(0,0);
        glVertexAttribDivisor(1,1);
        glVertexAttribDivisor(2,1);
        glVertexAttribDivisor(3,1);
        glVertexAttribDivisor(4,1);
        glVertexAttribDivisor(5,1);
        glVertexAttribDivisor(6,1);
    }

    private void InitShader(){
        setShader(new Shader());
        try {
            getShader().attachVertexShader(Application.getInstance().getApp_path().concat("Shaders/attractor.vs"));
            getShader().attachFragmentShader(Application.getInstance().getApp_path().concat("Shaders/attractor.fs"));
            getShader().link();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GenerateBuffers(){
        col_buffer0 = BufferUtils.createFloatBuffer(4*count);
        col_buffer1 = BufferUtils.createFloatBuffer(4*count);
        col_buffer2 = BufferUtils.createFloatBuffer(4*count);
        col_buffer3 = BufferUtils.createFloatBuffer(4*count);
        color_buffer = BufferUtils.createFloatBuffer(3 * count);
        direction_buffer = BufferUtils.createFloatBuffer(3*count);
    }
    private void GenerateAttractor(){
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;
        Vector3f vColor = new Vector3f(1.0f,0.0f,0.0f);
        float ip = (float) (1.0f/(2.0f*Math.PI));
        int j = 0;
        for(int i = 0 ; i < count;i++,j++){
            Matrix4f model = new Matrix4f();
            model.zero();
            model.m00(1);
            model.m11(1);
            model.m22(1);
            model.m33(1);

            float nx = (float) (Math.sin(a*y) - Math.cos(b*x));
            float ny = (float) (Math.sin(c*x) - Math.cos(d*y));
            nx *= radius;
            ny *= radius;
            model.translate(getPosition().x,getPosition().y,0.0f);
            model.translate(nx,ny,0.0f);
            model.scale(0.01f,0.01f,0.01f);
            AddMatrix2Buffer(model);

            float v = (float) Math.sqrt((x - Math.pow(nx,2)+(y - Math.pow(ny,2))*Math.atan2(ny,nx)*ip));
            float r = (float) ((vColor.x + Math.abs(Math.sin(v*Math.PI*3.2f-Math.PI*0.2f)))*0.5f);
            float g = (float) ((vColor.y + Math.abs(Math.sin(v*Math.PI*12.3f+Math.PI*1.5f)*0.5f+0.5f))*0.5f);
            float bc = (float)((vColor.z + Math.sin(v*Math.PI*3.84f+Math.PI/2.0f-0.6f)*0.5f+0.5f)*0.5f);
            if(j >= 10000){
                vColor.x = (float) Math.random();
                vColor.y = (float) 0.5f;
                vColor.z = (float) 0.2f;
                j = 0;
            }

            Vector3f vDir = new Vector3f(0,0,0);
            vDir.x = (float) Math.random();
            vDir.y = (float) Math.random();
            AddDir2Buffer(vDir);
            AddColor2Buffer(vColor);
            x = nx;
            y = ny;
        }
        col_buffer0.flip();
        col_buffer1.flip();
        col_buffer2.flip();
        col_buffer3.flip();
        color_buffer.flip();
        direction_buffer.flip();
    }

    private void UpdatePosition(){

    }

    private void AddDir2Buffer(Vector3f vDirection){
        direction_buffer.put(vDirection.x);
        direction_buffer.put(vDirection.y);
        direction_buffer.put(vDirection.z);
    }

    private void AddColor2Buffer(Vector3f vColor){
        color_buffer.put(vColor.x);
        color_buffer.put(vColor.y);
        color_buffer.put(vColor.z);
    }

    private void AddMatrix2Buffer(Matrix4f model){

        // COLUMN ORDER
        col_buffer0.put(model.m00());
        col_buffer0.put(model.m01());
        col_buffer0.put(model.m02());
        col_buffer0.put(model.m03());

        col_buffer1.put(model.m10());
        col_buffer1.put(model.m11());
        col_buffer1.put(model.m12());
        col_buffer1.put(model.m13());

        col_buffer2.put(model.m20());
        col_buffer2.put(model.m21());
        col_buffer2.put(model.m22());
        col_buffer2.put(model.m23());

        col_buffer3.put(model.m30());
        col_buffer3.put(model.m31());
        col_buffer3.put(model.m32());
        col_buffer3.put(model.m33());
    }
    @Override
    public void Init() {
        setPosition(new Vector3f(5,0,0));
        InitShader();
        GenerateBuffers();
        GenerateAttractor();
        InitBuffer();
    }

    @Override
    public void Update(float dt) {
        time+= dt;
        if(time >= time_count){
            //time = 0.0f;
           /* a += dt;
            d += dt;
            b -= dt;
            c += dt;*/


            /*GenerateAttractor();
            glBindBuffer(GL_ARRAY_BUFFER,col0);
            glBufferSubData(GL_ARRAY_BUFFER,0,col_buffer0);

            glBindBuffer(GL_ARRAY_BUFFER,col1);
            glBufferSubData(GL_ARRAY_BUFFER,0,col_buffer1);

            glBindBuffer(GL_ARRAY_BUFFER,col2);
            glBufferSubData(GL_ARRAY_BUFFER,0,col_buffer2);

            glBindBuffer(GL_ARRAY_BUFFER,col3);
            glBufferSubData(GL_ARRAY_BUFFER,0,col_buffer3);*/
        }
    }

    @Override
    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho) {
        FloatBuffer view_buffer,proj_buffer;
        view_buffer = BufferUtils.createFloatBuffer(16);
        proj_buffer = BufferUtils.createFloatBuffer(16);

        view.get(view_buffer);
        proj.get(proj_buffer);

        glBindVertexArray(getVAO());
        getShader().bind();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE);
        glUniform1f(getShader().getUniform("time"),time);
        glUniformMatrix4fv(getShader().getUniform("view"),false,view_buffer);
        glUniformMatrix4fv(getShader().getUniform("projection"),false,proj_buffer);

        fbo.bind();
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glDrawArraysInstanced(GL_TRIANGLES,0,36,count);
        glDisable(GL_BLEND);
        fbo.unbind();

        renderpass.Render(view,proj,ortho);

        glBindVertexArray(0);
    }
}
