import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;


public class BMenu {
    private Panel m_start;
    private Panel m_exit;
    private Panel m_background;
    private Panel m_settings;

    private Font font;

    public BMenu(){
        Init();
    }

    private void Init(){
        m_background = new Panel(550,500,new Vector2f(800,400),getClass().getResource("/Images/arcanoid.png").getPath().substring(1));
        m_background.getM_panel().setM_alpha(0.0f);
        m_start = new Panel(100,50,new Vector2f(100,100),getClass().getResource("/Images/14.png").getPath().substring(1));
        m_exit = new Panel(250,50,new Vector2f(100,100),getClass().getResource("/Images/13.png").getPath().substring(1));
        m_settings = new Panel(400,50,new Vector2f(100,100),getClass().getResource("/Images/11.png").getPath().substring(1));

        try {
            font = new Font(getClass().getResource("/LucidaBrightItalic.ttf").getPath().substring(1),24);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Update(float dt){
        float alpha = m_background.getM_panel().getM_alpha()+0.05f*dt;
        if(alpha >= 1.0f)alpha = 1.0f;
        m_background.getM_panel().setM_alpha(alpha);
    }

    public void CheckFocus(double xpos,double ypos){
        if(Check_Start(xpos,1080-ypos))m_start.getM_panel().setScale(new Vector3f(1.5f,1.5f,1.5f));
        else m_start.getM_panel().setScale(new Vector3f(1,1,1));

        if(Check_Exit(xpos,1080-ypos))m_exit.getM_panel().setScale(new Vector3f(1.5f,1.5f,1.5f));
        else m_exit.getM_panel().setScale(new Vector3f(1,1,1));

        if(Check_Settings(xpos,1080-ypos))m_settings.getM_panel().setScale(new Vector3f(1.5f,1.5f,1.5f));
        else m_settings.getM_panel().setScale(new Vector3f(1,1,1));
    }

    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        m_background.Render(view,proj,ortho);
        m_start.Render(view,proj,ortho);
        m_exit.Render(view,proj,ortho);
        m_settings.Render(view,proj,ortho);
        //font.drawText("Hello world",0,0); old , dont work,
    }

    public boolean Check_Start(double mouse_x,double mouse_y){
        Vector3f start_pos = m_start.getM_panel().getPosition();
        float x_min = start_pos.x;
        float x_max = start_pos.x + m_start.getM_panel().getSize().x;
        float y_min = start_pos.y;
        float y_max = start_pos.y + m_start.getM_panel().getSize().y;
        if(mouse_x >= x_min && mouse_x <= x_max && mouse_y >= y_min && mouse_y <= y_max)return true;
        else return false;
    }

    public boolean Check_Exit(double mouse_x, double mouse_y){
        Vector3f start_pos = m_exit.getM_panel().getPosition();
        float x_min = start_pos.x;
        float x_max = start_pos.x + m_exit.getM_panel().getSize().x;
        float y_min = start_pos.y;
        float y_max = start_pos.y + m_exit.getM_panel().getSize().y;
        if(mouse_x >= x_min && mouse_x <= x_max && mouse_y >= y_min && mouse_y <= y_max)return true;
        else return false;
    }

    public boolean Check_Settings(double mouse_x, double mouse_y){
        Vector3f start_pos = m_settings.getM_panel().getPosition();
        float x_min = start_pos.x;
        float x_max = start_pos.x + m_settings.getM_panel().getSize().x;
        float y_min = start_pos.y;
        float y_max = start_pos.y + m_settings.getM_panel().getSize().y;
        if(mouse_x >= x_min && mouse_x <= x_max && mouse_y >= y_min && mouse_y <= y_max)return true;
        else return false;
    }
}
