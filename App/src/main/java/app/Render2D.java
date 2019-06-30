package app;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Render2D extends Model{

    private int filtertype;
    private boolean isFBOtex;
    private boolean isFixedSize = false;
    private boolean isAdditive = false;
    private boolean isChangingColor = false;

    private int FBOid;
    private Texture texture;
    private Vector2f size;
    private float rotate;
    private float m_depth = 0.0f;
    private float m_alpha = 1.0f;
    private float time = 0.0f;

    public Render2D(){
        setFiltertype(1);
        isFBOtex = false;
        Init();
    }

    public Render2D(int filtertype,boolean isFBOtex,int FBOid){
        this.setFiltertype(filtertype);
        this.isFBOtex = isFBOtex;
        this.setFBOid(FBOid);
        Init();
    }

    public Render2D(int filtertype, Vector3f position, Vector2f size, float roate){
        this.setPosition(position);
        this.setSize(size);
        this.rotate = rotate;
        this.isFixedSize = true;
        this.setFiltertype(filtertype);
        Init();
    }

    public Render2D(int filtertype, Vector3f position, Vector2f size, float roate,int VAO, Texture texture, Shader shader){
        this.setPosition(position);
        this.setSize(size);
        this.rotate = rotate;
        this.isFixedSize = true;
        this.setFiltertype(filtertype);
        this.setVAO(VAO);
        this.setShader(shader);
        this.texture = texture;
    }
    @Override
    public void Init() {
        setShader(new Shader());
        InitShader();
        InitBuffer();

    }

    @Override
    public void Update(float dt) {
        time += dt;
    }

    @Override
    public void Render(Matrix4f view, Matrix4f proj,Matrix4f ortho) {
        glBindVertexArray(getVAO());
        getShader().bind();

        glUniform1i(getShader().getUniform("mySampler"), 0);
        glUniform1i(getShader().getUniform("filtertype"), getFiltertype());
        glUniform1f(getShader().getUniform("m_alpha"),m_alpha);

        glActiveTexture(GL_TEXTURE0);
        if (isFBOtex)
        {
            glBindTexture(GL_TEXTURE_2D, getFBOid());
        }
        else {
            //TextureManager::Inst()->BindTexture(id);
            texture.bind();
        }

        Matrix4f model = new Matrix4f();
        model.zero();
        model.m00(1);
        model.m11(1);
        model.m22(1);
        model.m33(1);

        glEnable(GL_BLEND);
        if(isAdditive)glBlendFunc(GL_ONE,GL_ONE);
        else glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

        if(isChangingColor()){
            glUniform1f(getShader().getUniform("time"),time);
        }
        else{
            glUniform1f(getShader().getUniform("time"),0.0f);
        }

        if(isFixedSize){
            FloatBuffer modelbuf = BufferUtils.createFloatBuffer(16);
            FloatBuffer orthobuf = BufferUtils.createFloatBuffer(16);
            model.translate(getPosition().x, getPosition().y,0.0f);
            model.translate(getSize().x *0.5f,0.5f* getSize().y,0.0f);
            model.rotate((float)Math.toRadians(rotate),0,0,1);
            model.translate(getSize().x *-0.5f,-0.5f* getSize().y,0.0f);
            model.scale(getSize().x, getSize().y,1.0f);
            model.scale(getScale());
            model.get(modelbuf);
            ortho.get(orthobuf);
            glUniformMatrix4fv(getShader().getUniform("model"),false,modelbuf);
            glUniformMatrix4fv(getShader().getUniform("projection"),false,orthobuf);
            glUniform1f(getShader().getUniform("m_depth"), getM_depth());
            glDrawArrays(GL_TRIANGLES,0,6);
        }
        else{
            glDrawArrays(GL_TRIANGLE_STRIP,0,4);
        }
        glDisable(GL_BLEND);
        getShader().unbind();
        glBindVertexArray(0);
    }

    private void InitBuffer(){
        setVAO(glGenVertexArrays());
        setVBO(glGenBuffers());
        setEBO(glGenBuffers());

        float []tab = {
                -1.0f,-1.0f,
                1.0f,-1.0f,
                -1.0f,1.0f,
                1.0f,1.0f};
        int []indicies = {0,1,2,3};

        float vertices[] = {
                // Pos      // Tex
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };
        glBindVertexArray(getVAO());
        FloatBuffer floatBuffer;
        if(isFixedSize){
            floatBuffer = BufferUtils.createFloatBuffer(vertices.length);
            floatBuffer.put(vertices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, getVBO());
            glBufferData(GL_ARRAY_BUFFER,floatBuffer,GL_STATIC_DRAW);
            glEnableVertexAttribArray(getShader().getAttrib("vPosition"));
            glVertexAttribPointer(getShader().getAttrib("vPosition"),4,GL_FLOAT,false,0,0);

        }else{
            floatBuffer = BufferUtils.createFloatBuffer(tab.length);
            floatBuffer.put(tab).flip();
            glBindBuffer(GL_ARRAY_BUFFER, getVBO());
            glBufferData(GL_ARRAY_BUFFER,floatBuffer,GL_STATIC_DRAW);
            glEnableVertexAttribArray(getShader().getAttrib("vPosition"));
            glVertexAttribPointer(getShader().getAttrib("vPosition"),2,GL_FLOAT,false,0,0);

        }


        IntBuffer intBuffer = BufferUtils.createIntBuffer(indicies.length);
        intBuffer.put(indicies).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,intBuffer,GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    private void InitShader(){
        if(isFixedSize){
            try {
                getShader().attachVertexShader(getClass().getResource("/Shaders/renderer2Dfixed.vs").getPath().substring(1));
                getShader().attachFragmentShader(getClass().getResource("/Shaders/renderer2Dfixed.fs").getPath().substring(1));
                getShader().link();
            }catch(IOException err){
                System.err.println(err);
            }
        }else{
            try {
                getShader().attachVertexShader(getClass().getResource("/Shaders/renderer2D.vs").getPath().substring(1));
                getShader().attachFragmentShader(getClass().getResource("/Shaders/renderer2D.fs").getPath().substring(1));
                getShader().link();
            }catch(IOException err){
                System.err.println(err);
            }
        }
    }

    public void LoadTex(String path){
        texture = Texture.loadTexture(path);
    }

    public int getFBOid() {
        return FBOid;
    }

    public void setFBOid(int FBOid) {
        this.FBOid = FBOid;
    }

    public int getFiltertype() {
        return filtertype;
    }

    public void setFiltertype(int filtertype) {
        this.filtertype = filtertype;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public float getM_depth() {
        return m_depth;
    }

    public void setM_depth(float m_depth) {
        this.m_depth = m_depth;
    }

    public boolean isAdditive() {
        return isAdditive;
    }

    public void setAdditive(boolean additive) {
        isAdditive = additive;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public boolean isChangingColor() {
        return isChangingColor;
    }

    public void setChangingColor(boolean changingColor) {
        isChangingColor = changingColor;
    }

    public float getM_alpha() {
        return m_alpha;
    }

    public void setM_alpha(float m_alpha) {
        this.m_alpha = m_alpha;
    }
}
