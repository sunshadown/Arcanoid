package app;

import static org.lwjgl.openal.AL10.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;


public class BMenu {
    private Panel m_start;
    private Panel m_exit;
    private Panel m_background;
    private Panel m_settings;
    private boolean isFocused = false;
    private boolean lastFocused = false;
    private int state = 0;
    private boolean isPlayed = false;
    private SoundSource focus_sound;

    public BMenu(){
        Init();
    }

    public void InitSound(){
        focus_sound = new SoundSource();

        try {
            focus_sound.LoadOgg(Application.getInstance().getApp_path().concat("Sound/select.ogg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        alSourcei(focus_sound.sourcePointer,AL_LOOPING,AL_FALSE);
        alSourcef(focus_sound.sourcePointer,AL_GAIN,1.0f);
    }

    private void Init(){
        m_background = new Panel(550,500,new Vector2f(800,400),Application.getInstance().getApp_path().concat("Images/arcanoid.png"));
        m_background.getM_panel().setM_alpha(0.0f);
        m_start = new Panel(100,50,new Vector2f(100,100),Application.getInstance().getApp_path().concat("Images/14.png"));
        m_exit = new Panel(250,50,new Vector2f(100,100),Application.getInstance().getApp_path().concat("Images/13.png"));
        m_settings = new Panel(400,50,new Vector2f(100,100),Application.getInstance().getApp_path().concat("Images/11.png"));
    }

    public void Update(float dt){
        float alpha = m_background.getM_panel().getM_alpha()+0.05f*dt;
        if(alpha >= 1.0f)alpha = 1.0f;
        m_background.getM_panel().setM_alpha(alpha);
    }

    private void CheckState(){

    }

    public void CheckFocus(double xpos,double ypos){
        isFocused = false;

        if(Check_Start(xpos,1080-ypos)){
            m_start.getM_panel().setScale(new Vector3f(1.5f,1.5f,1.5f));
            isFocused = true;
        }
        else {
            m_start.getM_panel().setScale(new Vector3f(1,1,1));
            isPlayed = false;
        }

        if(Check_Exit(xpos,1080-ypos)){
            m_exit.getM_panel().setScale(new Vector3f(1.5f,1.5f,1.5f));
            isFocused = true;
        }
        else {
            m_exit.getM_panel().setScale(new Vector3f(1,1,1));
            isPlayed = false;
        }

        if(Check_Settings(xpos,1080-ypos)){
            m_settings.getM_panel().setScale(new Vector3f(1.5f,1.5f,1.5f));
            isFocused = true;
        }
        else {
            m_settings.getM_panel().setScale(new Vector3f(1,1,1));
            isPlayed = false;
        }

        if(isFocused && !isPlayed){
            focus_sound.play();
            isPlayed = true;
        }
    }

    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        m_background.Render(view,proj,ortho);
        m_start.Render(view,proj,ortho);
        m_exit.Render(view,proj,ortho);
        m_settings.Render(view,proj,ortho);
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
