import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.Sys;

import static org.lwjgl.glfw.GLFW.*;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.MotorJoint;

import java.util.ArrayList;
import java.util.List;

public class Logic{
    private int screen_x;
    private int screen_y;
    private int x_min,y_min;
    private int x_max,y_max;
    private final float speedModifier = 800.0f;
    private World world;

    public static float scale = 5.0f;
    private float time = 0;
    private Panel test;
    private Panel m_Panel;
    private Panel l_wall,r_wall,b_wall,t_wall;
    private List<Panel> walls;

    public Logic(Panel m_Panel){
        this.m_Panel = m_Panel;
        Init();
        InitWalls();
        InitWorld();
    }

    private void InitWalls(){
        String path = getClass().getResource("/Images/07-Breakout-Tiles.png").getPath().substring(1);
        l_wall = new Panel(0,0,new Vector2f((float)10,(float)1080),path);
        r_wall = new Panel(1910,0,new Vector2f((float)10,(float)1080),path);
        b_wall = new Panel(10,0,new Vector2f((float)1900,(float)10),path);
        t_wall = new Panel(10,1070,new Vector2f((float)1900,(float)10),path);

        Body l_wall_body = new Body();
        l_wall_body.addFixture(Geometry.createRectangle((float)l_wall.getM_panel().getSize().x,(float)l_wall.getM_panel().getSize().y));
        l_wall_body.setMass(Mass.Type.INFINITE);
        l_wall_body.translate(l_wall.getCenter_x(),l_wall.getCenter_y());
        l_wall.setBody(l_wall_body);
        Body r_wall_body = new Body();
        r_wall_body.addFixture(Geometry.createRectangle((float)r_wall.getM_panel().getSize().x,(float)r_wall.getM_panel().getSize().y));
        r_wall_body.setMass(Mass.Type.INFINITE);
        r_wall_body.translate(r_wall.getCenter_x(),r_wall.getCenter_y());
        r_wall.setBody(r_wall_body);
        Body b_wall_body = new Body();
        b_wall_body.addFixture(Geometry.createRectangle((float)b_wall.getM_panel().getSize().x,(float)b_wall.getM_panel().getSize().y));
        b_wall_body.setMass(Mass.Type.INFINITE);
        b_wall_body.translate(b_wall.getCenter_x(),b_wall.getCenter_y());
        b_wall.setBody(b_wall_body);
        Body t_wall_body = new Body();
        t_wall_body.addFixture(Geometry.createRectangle((float)t_wall.getM_panel().getSize().x,(float)t_wall.getM_panel().getSize().y));
        t_wall_body.setMass(Mass.Type.INFINITE);
        t_wall_body.translate(t_wall.getCenter_x(),t_wall.getCenter_y());
        t_wall.setBody(t_wall_body);

        walls = new ArrayList<>();
        walls.add(l_wall);
        walls.add(r_wall);
        walls.add(b_wall);
        walls.add(t_wall);
    }

    private void InitWallsBodies(){
        for (Panel panel:
             walls) {
            world.addBody(panel.getBody());
        }
    }

    private void InitWorld(){
        world = new World();
        world.setGravity(World.ZERO_GRAVITY);
        InitBodies();
        InitWallsBodies();
    }

    private void InitBodies(){
        Body panel_body = new Body();
        panel_body.addFixture(Geometry.createRectangle((float)m_Panel.getM_panel().getSize().x,(float)m_Panel.getM_panel().getSize().y));
        panel_body.setMass(Mass.Type.INFINITE);
        panel_body.translate(new Vector2(m_Panel.getCenter_x(),m_Panel.getCenter_y()));

        System.out.println("Panel body translation : "+panel_body.getTransform().getTranslationX()+" , "+panel_body.getTransform().getTranslationY());
        m_Panel.setBody(panel_body);
        world.addBody(panel_body);


        test = new Panel(screen_x/2,200,new Vector2f(50.0f,50.0f),getClass().getResource("/Images/58-Breakout-Tiles.png").getPath().substring(1));
        Body test_body = new Body();
        //BodyFixture fixture = new BodyFixture(Geometry.createRectangle((double)test.getM_panel().getSize().x,(double)test.getM_panel().getSize().y));
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(test.getM_panel().getSize().x/2));
        fixture.setDensity(300);
        fixture.setRestitution(1);
        fixture.setFriction(0);
        test_body.addFixture(fixture);
        Mass test_body_mass = new Mass(new Vector2(0,0),5,0);
        test_body_mass.setType(Mass.Type.NORMAL);
        test_body.setMass(test_body_mass);
        System.out.println(test_body.getMass());
        test_body.translate(new Vector2(test.getCenter_x(),test.getCenter_y()));
        test.setBody(test_body);
        world.addBody(test.getBody());
        test_body.applyImpulse(new Vector2(0,800));
        //test_body.setLinearVelocity(0,100);
        //test.getBody().setLinearVelocity(0,300);

    }

    private void Init(){
        screen_x = 1920;
        screen_y = 1080;
        x_min = 10 ;//+ (int)m_Panel.getM_panel().getSize().x;
        y_min = 0 + (int)m_Panel.getM_panel().getSize().y;
        x_max = screen_x - (int)m_Panel.getM_panel().getSize().x - 10;
        y_max = screen_y - (int)m_Panel.getM_panel().getSize().y;

        System.out.println("x_min : " + x_min +" , x_max : "+x_max);
    }

    public void Update(float dt){
        world.update(dt);
        Mover(dt);
        test.Update(dt);

        time += dt;
        if(time >= 0.2f){
            time = 0.0f;
            Transform panel_transform = m_Panel.getBody().getTransform();
            Transform test_transform = test.getBody().getTransform();
            Vector2 ndir = new Vector2(panel_transform.getTranslationX() - test_transform.getTranslationX(),panel_transform.getTranslationY() - test_transform.getTranslationY());
            Vector2 ndir_offset = new Vector2(0,25);
            ndir.add(ndir_offset);
            ndir.normalize();
            ndir.multiply(10000);
            test.getBody().applyForce(ndir);
        }

    }


    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        m_Panel.Render(view,proj,ortho);
        test.Render(view,proj,ortho);

        for (Panel panel:
             walls) {
            panel.Render(view,proj,ortho);
        }
    }

    private void Mover(float dt){
        float npos_x = m_Panel.getM_panel().getPosition().x;
        float npos_y = m_Panel.getM_panel().getPosition().y;

        if(glfwGetKey(Application.getInstance().getWindow(),GLFW_KEY_RIGHT) == GLFW_PRESS){
            npos_x += 1.0f * speedModifier * dt;
        }
        if(glfwGetKey(Application.getInstance().getWindow(),GLFW_KEY_LEFT) == GLFW_PRESS){
            npos_x += -1.0f * speedModifier * dt;
        }

        int npos_X = Math.max(10,Math.min(x_max,(int)npos_x));

        m_Panel.getM_panel().setPosition(new Vector3f(npos_X,npos_y,0.0f));
        m_Panel.CalculateCenter();
    }
}
