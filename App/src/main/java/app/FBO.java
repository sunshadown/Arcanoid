package app;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

public class FBO {
    private int id;
    private int tex;
    private int rbo;
    private int width;
    private int height;

    public FBO(int width, int height){
        this.width = width;
        this.height = height;
        GenTexture();
        GenFBO();
    }

    public void Update(){
        GenTexture();
        GenFBO();
    }
    private void GenTexture(){
        if (tex != 0) {
            glDeleteTextures(tex);
        }
        setTex(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, getWidth(), getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, 0L);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void GenFBO(){
        if (id != 0) {
            glDeleteFramebuffersEXT(id);
            glDeleteRenderbuffersEXT(rbo);
        }
        setId(glGenFramebuffersEXT());
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, getId());
        setRbo(glGenRenderbuffersEXT());
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, getRbo());
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, getWidth(), getHeight());
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, getRbo());
        glBindTexture(GL_TEXTURE_2D, getTex());
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, getTex(), 0);

        int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
        if (status != GL_FRAMEBUFFER_COMPLETE_EXT) {
            throw new AssertionError("Incomplete framebuffer: " + status);
        }
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
    }

    public void bind(){
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, getId());
    }
    public void unbind(){
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTex() {
        return tex;
    }

    public void setTex(int tex) {
        this.tex = tex;
    }

    public int getRbo() {
        return rbo;
    }

    public void setRbo(int rbo) {
        this.rbo = rbo;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
