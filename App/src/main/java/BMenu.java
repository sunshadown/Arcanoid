import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;


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
        m_background = new Panel(0,0,new Vector2f(1920,1080),getClass().getResource("/Images/background.jpg").getPath().substring(1));
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
