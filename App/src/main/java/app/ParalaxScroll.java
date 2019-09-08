package app;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ParalaxScroll {
    private Panel l_1,l_12;
    private Panel l_2,l_22;
    private Panel l_3;
    private Panel l_4,l_42;
    private Panel l_5;
    private Panel l_6;
    private Panel pl_1;
    private Panel pl_2;

    private float multiplayer_1 = 0.1f;
    private float multiplayer_2 = 0.3f;
    private float multiplayer_3 = 0.5f;
    private float multiplayer_4 = 0.7f;
    private List<Panel> panels;

    public ParalaxScroll(){
        Init();
    }

    private void Init(){
        Level_1();
    }

    public void LoadRandom(){
        int val = (int) ((Math.random()*1000)%3);
        switch (val){
            case 0:Level_1();break;
            case 1:Level_2();break;
            case 2:Level_3();break;
            default:Level_1();
        }
    }

    public void Level_1(){
        l_1 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_1.png"));
        l_1.getM_panel().setM_depth(-0.9f);
        l_12 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_1.png"));
        l_12.getM_panel().setM_depth(-0.95f);

        l_2 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_2.png"));
        l_2.getM_panel().setM_depth(-0.8f);
        l_22 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_2.png"));
        l_22.getM_panel().setM_depth(-0.8f);

        l_3 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_3.png"));
        l_3.getM_panel().setM_depth(-0.7f);
        l_4 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_4.png"));
        l_4.getM_panel().setM_depth(-0.6f);

        l_42 = null;

        l_5 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_5.png"));
        l_5.getM_panel().setM_depth(-0.5f);
        l_6 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_summer/l_6.png"));
        l_6.getM_panel().setM_depth(-0.4f);

        panels = new ArrayList<>();

        panels.add(l_1);
        panels.add(l_12);
        panels.add(l_2);
        panels.add(l_22);

        panels.add(l_2);
        panels.add(l_4);
        panels.add(l_5);
        panels.add(l_6);
    }

    public void Level_2(){
        l_1 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_1.png"));
        l_1.getM_panel().setM_depth(-0.9f);
        l_12 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_1.png"));
        l_12.getM_panel().setM_depth(-0.9f);

        l_2 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_2.png"));
        l_2.getM_panel().setM_depth(-0.8f);
        l_22 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_2.png"));
        l_22.getM_panel().setM_depth(-0.8f);

        l_3 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_3.png"));
        l_3.getM_panel().setM_depth(-0.7f);
        l_4 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_4.png"));
        l_4.getM_panel().setM_depth(-0.6f);


        l_42 = null;

        l_5 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_back_winter/l_5.png"));
        l_5.getM_panel().setM_depth(-0.5f);

        panels = new ArrayList<>();

        panels.add(l_1);
        panels.add(l_12);
        panels.add(l_2);
        panels.add(l_22);

        panels.add(l_2);
        panels.add(l_4);
        panels.add(l_5);
    }

    public void Level_3(){
        l_1 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_1.png"));
        l_1.getM_panel().setM_depth(-0.9f);
        l_12 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_1.png"));
        l_12.getM_panel().setM_depth(-0.9f);

        l_2 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_2.png"));
        l_2.getM_panel().setM_depth(-0.8f);
        l_22 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_2.png"));
        l_22.getM_panel().setM_depth(-0.8f);

        l_3 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_3.png"));
        l_3.getM_panel().setM_depth(-0.7f);

        l_4 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_4.png"));
        l_4.getM_panel().setM_depth(-0.6f);
        l_42 = new Panel(-1920,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_4.png"));
        l_42.getM_panel().setM_depth(-0.6f);

        l_5 = new Panel(0,0,new Vector2f(1920,1080),Application.getInstance().getApp_path().concat("Images/cga_2back_dungeon/1_5.png"));
        l_5.getM_panel().setM_depth(-0.5f);

        panels = new ArrayList<>();

        panels.add(l_1);
        panels.add(l_12);
        panels.add(l_2);
        panels.add(l_22);

        panels.add(l_2);
        panels.add(l_4);
        panels.add(l_42);
        panels.add(l_5);
    }

    public void Update(float dt){
        Vector3f pos1 = l_1.getM_panel().getPosition();
        Vector3f pos2 = l_12.getM_panel().getPosition();
        Vector3f pos3 = l_2.getM_panel().getPosition();
        Vector3f pos4 = l_22.getM_panel().getPosition();
        if(l_42 != null){
            Vector3f pos5 = l_4.getM_panel().getPosition();
            Vector3f pos6 = l_42.getM_panel().getPosition();
            pos5.x = pos5.x + dt * multiplayer_1 *100.0f;;
            pos6.x = pos6.x + dt * multiplayer_1 *100.0f;;
        }

        pos1.x = pos1.x + dt * multiplayer_1 *1000.0f;
        pos2.x = pos2.x + dt * multiplayer_1 *1000.0f;
        pos3.x = pos3.x + dt * multiplayer_1 *100.0f;
        pos4.x = pos4.x + dt * multiplayer_1 *100.0f;

        CheckBounds();
    }

    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        for (Panel panel:
             panels) {
            panel.Render(view,proj,ortho);
        }
    }

    private void CheckBounds(){
        if(l_1.getM_panel().getPosition().x >= 1920)l_1.getM_panel().setPosition(new Vector3f(0,0,0));
        if(l_12.getM_panel().getPosition().x >= 0)l_12.getM_panel().setPosition(new Vector3f(-1920,0,0));
        if(l_2.getM_panel().getPosition().x >= 1920)l_2.getM_panel().setPosition(new Vector3f(0,0,0));
        if(l_22.getM_panel().getPosition().x >= 0)l_22.getM_panel().setPosition(new Vector3f(-1920,0,0));

        if(l_42 != null){
            if(l_4.getM_panel().getPosition().x >= 1920)l_4.getM_panel().setPosition(new Vector3f(0,0,0));
            if(l_42.getM_panel().getPosition().x >= 0)l_42.getM_panel().setPosition(new Vector3f(-1920,0,0));
        }
    }
}
