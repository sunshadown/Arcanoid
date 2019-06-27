import org.dyn4j.geometry.Vector2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PostProcess {
    private List<Panel> panels;
    private Panel father;
    private int number;
    private Explosion explosion;
    private List<Explosion>explosions;

    public PostProcess(Panel father){
        number = 50;
        panels = new ArrayList<>();
        this.father = father;
        GenerateFollowers();
        explosion = new Explosion(100,80.0f,1.0f,1.0f,2.0f,true,new Vector3f(1,1,1), new Vector2f(100,100));
        explosions = new ArrayList<>();
    }

    public void Update(float dt){
        for(int i = 0 ,j = 0; i < panels.size() ; i++){
            Vector3f pos_ball = new Vector3f(father.getM_panel().getPosition());
            Vector3f pos_panel = new Vector3f(panels.get(i).getM_panel().getPosition());


            Vector2 p_ball = new Vector2(pos_ball.x+25.0f,pos_ball.y+25.0f);
            Vector2 p_panel = new Vector2(pos_panel.x,pos_panel.y);
            Vector2 p_dir = new Vector2(p_ball.x - p_panel.x,p_ball.y - p_panel.y);
            double mag = p_dir.getMagnitudeSquared();
            p_dir = p_dir.getNormalized();

            Vector3f last = new Vector3f((float)p_dir.x,(float)p_dir.y,0);
            if(j <= 20)j++;
            if(mag <= 10)last.mul(0.01f);
            else last.mul(120.0f * (i-j+1)* dt);
            pos_panel.add(last);
            panels.get(i).getM_panel().setPosition(pos_panel);
            panels.get(i).getM_panel().Update(dt);
        }

        List<Explosion>found = new ArrayList<>();
        for (Explosion exp:
             explosions) {
            exp.Update(dt);
            if(!exp.isActive())found.add(exp);
        }
        explosions.removeAll(found);
    }

    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        for (Panel panel:
             panels) {
            Vector3f pos_ball = new Vector3f(father.getM_panel().getPosition());
            Vector3f pos_panel = new Vector3f(panel.getM_panel().getPosition());


            Vector2 p_ball = new Vector2(pos_ball.x+25.0f,pos_ball.y+25.0f);
            Vector2 p_panel = new Vector2(pos_panel.x,pos_panel.y);
            Vector2 p_dir = new Vector2(p_ball.x - p_panel.x,p_ball.y - p_panel.y);
            double mag = p_dir.getMagnitudeSquared();
            //System.out.println(mag);
            if(mag > 3000) panel.Render(view,proj,ortho);
        }
        for (Explosion exp:
                explosions) {
            exp.Render(view, proj, ortho);
        }
    }

    public Vector3f CalculateDirection(Panel panel){
        Vector3f f_dir = new Vector3f(father.getM_panel().getPosition());
        Vector3f k_dir = new Vector3f(panel.getM_panel().getPosition());

        Vector3f l_pos = new Vector3f(f_dir).min(k_dir);
        l_pos = new Vector3f(l_pos.normalize());
        //System.out.println(l_pos);
        return l_pos;
    }

    public void GenerateFollowers(){
        Vector3f pos = father.getM_panel().getPosition();
        Vector2f size = new Vector2f(father.getM_panel().getSize());
        size.mul(0.3f);
        String path = getClass().getResource("/Images/58_2-Breakout-Tiles.png").getPath().substring(1);
        for(int i = 0 ; i < number ;i++){
            Panel panel = new Panel(pos.x,pos.y,size,path);
            panel.getM_panel().setAdditive(true);
            //panel.getM_panel().setChangingColor(true);
            panel.getM_panel().setM_depth(0+i*0.01f+0.01f);
            panels.add(panel);
        }
    }

    public void AddExp(int particles_count, float speedModifier,float radius,float scale,float time_end,boolean isLooping ,Vector3f color ,Vector2f position){
        Explosion exp = new Explosion(particles_count,speedModifier,radius,scale,time_end,isLooping,color,position);
        explosions.add(exp);
    }
}
