package app;

import org.joml.Matrix4f;
import org.joml.Vector2f;


public class GUI {
    private final float time_life = 25.0f;
    private float time_dt_life = 0.01f;
    private Panel life_proggres;
    private Panel life_proggres_bar;
    private float life_proggres_bar_max;

    GUI()
    {
        life_proggres = new Panel(10,10,new Vector2f(550,100),Application.getInstance().getApp_path().concat("/Images/18.png"));
        life_proggres_bar = new Panel(60,30,new Vector2f(460,15),Application.getInstance().getApp_path().concat("/Images/progress_bar.png"));
        life_proggres_bar.getM_panel().setM_depth(0.1f);
        life_proggres_bar_max = life_proggres_bar.getSize().x();
    }

    void Reset()
    {
        time_dt_life = 0.0f;
    }
    void Update(float dt)
    {
        time_dt_life += dt;
        CheckLifeBonus();
    }

    void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho)
    {
        life_proggres.Render(view,proj,ortho);
        life_proggres_bar.Render(view, proj, ortho);
    }

    void CheckLifeBonus()
    {
        if(time_dt_life >= time_life)
        {
            Application.getInstance().getManager().getLogic().LifeIncrease();
            time_dt_life = 0.01f;
        }
        float divider = time_dt_life/time_life;
        Vector2f nsize = life_proggres_bar.getSize();
        float nsize_x = divider * life_proggres_bar_max;
        nsize.x = nsize_x;
        //life_proggres_bar.setSize(nsize);
        //life_proggres_bar.setM_panel_size();
    }
}
