package app;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.geometry.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

import org.dyn4j.dynamics.World;

import java.util.ArrayList;
import java.util.List;

public class Logic{
    private int screen_x;
    private int screen_y;
    private int x_min,y_min;
    private int x_max,y_max;
    private final float speedModifier = 1600.0f;
    private World world;

    private boolean isPullingBall = false;
    private boolean isALive = true;
    private boolean canPush = true;

    private int number_of_lifes = 5;
    public static float scale = 5.0f;
    private float time = 0;
    private Panel test;
    private Panel m_Panel,panel_ball_l,panel_ball_r;
    private Panel l_wall,r_wall,b_wall,t_wall;
    private List<Panel> walls;
    private List<Panel> blocks;
    private LevelManager levelManager;
    private PostProcess postProcess;
    private List<Panel> lifes;

    private ParalaxScroll paralaxScroll;

    public Logic(Panel m_Panel){
        this.setM_Panel(m_Panel);

        setParalaxScroll(new ParalaxScroll());
        setBlocks(new ArrayList<>());


        Init();
        InitWalls();
        InitWorld();
        CreateListeners();

        setLevelManager(new LevelManager());
        setPostProcess(new PostProcess(test));
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
            getWorld().addBody(panel.getBody());
        }
    }

    private void InitWorld(){
        setWorld(new World());
        getWorld().setGravity(World.ZERO_GRAVITY);
        Settings settings = getWorld().getSettings();
        settings.setMaximumTranslation(30.0f);
        settings.setAngularTolerance(0.0f);
        double max_translation = getWorld().getSettings().getMaximumTranslation();
        double max_linear_correction = getWorld().getSettings().getMaximumLinearCorrection();
        System.out.println("max_translation : " + max_translation);
        System.out.println("max_linear_correction : " + max_linear_correction);
        InitBodies();
        InitWallsBodies();

    }

    private void InitBodies(){
        Body panel_body = new Body();
        panel_body.addFixture(Geometry.createEllipse((float) getM_Panel().getM_panel().getSize().x + 15.0f,(float) getM_Panel().getM_panel().getSize().y));
        panel_body.setMass(Mass.Type.INFINITE);
        panel_body.translate(new Vector2(getM_Panel().getCenter_x(), getM_Panel().getCenter_y() - 5.0f));

        String path = getClass().getResource("/Images/58_2-Breakout-Tiles.png").getPath().substring(1);
        panel_ball_l = new Panel(getM_Panel().getM_panel().getPosition().x ,getM_Panel().getM_panel().getPosition().y+getM_Panel().getM_panel().getSize().y,new Vector2f(25,25),path);
        panel_ball_r = new Panel(getM_Panel().getM_panel().getPosition().x + getM_Panel().getM_panel().getSize().x-25,getM_Panel().getM_panel().getPosition().y+getM_Panel().getM_panel().getSize().y,new Vector2f(25,25),path);
        Body l_ball = new Body();
        Body r_ball = new Body();
        panel_ball_l.setBody(l_ball);
        panel_ball_r.setBody(r_ball);

        l_ball.setMass(Mass.Type.INFINITE);
        r_ball.setMass(Mass.Type.INFINITE);
        l_ball.addFixture(Geometry.createHalfEllipse(panel_ball_l.getM_panel().getSize().x,panel_ball_l.getM_panel().getSize().y/2));
        r_ball.addFixture(Geometry.createHalfEllipse(panel_ball_r.getM_panel().getSize().x,panel_ball_r.getM_panel().getSize().y/2));

        l_ball.translate(new Vector2(getM_Panel().getCenter_x() - getM_Panel().getM_panel().getSize().x/2.0f , getM_Panel().getCenter_y() + 15.0f));
        r_ball.translate(new Vector2(getM_Panel().getCenter_x() + getM_Panel().getM_panel().getSize().x/2.0f , getM_Panel().getCenter_y() + 15.0f));


        world.addBody(r_ball);
        world.addBody(l_ball);
        /*DistanceJoint distanceJoint = new DistanceJoint(panel_body,r_ball,new Vector2(0,0),new Vector2(0,0));
        world.addJoint(distanceJoint);
        distanceJoint = new DistanceJoint(panel_body,l_ball,new Vector2(0,0),new Vector2(0,0));
        world.addJoint(distanceJoint);*/

        System.out.println("app.Panel body translation : "+panel_body.getTransform().getTranslationX()+" , "+panel_body.getTransform().getTranslationY());
        getM_Panel().setBody(panel_body);
        getWorld().addBody(panel_body);


        setTest(new Panel(screen_x/2,200,new Vector2f(50.0f,50.0f),getClass().getResource("/Images/58-Breakout-Tiles.png").getPath().substring(1)));
        //test.getM_panel().setAdditive(true);

        Body test_body = new Body();
        //BodyFixture fixture = new BodyFixture(Geometry.createRectangle((double)test.getM_panel().getSize().x,(double)test.getM_panel().getSize().y));
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(getTest().getM_panel().getSize().x/2));
        fixture.setDensity(300);
        fixture.setRestitution(1);
        fixture.setFriction(0);
        test_body.addFixture(fixture);
        Mass test_body_mass = new Mass(new Vector2(0,0),5,0);
        test_body_mass.setType(Mass.Type.NORMAL);
        test_body.setMass(test_body_mass);
        System.out.println(test_body.getMass());
        test_body.translate(new Vector2(getTest().getCenter_x(), getTest().getCenter_y()));
        getTest().setBody(test_body);
        getWorld().addBody(getTest().getBody());
        //test_body.applyImpulse(new Vector2(0,5000));
    }

    private void CreateListeners(){
        getWorld().addListener(new Listeners.Panel_Ball_Listerner(getM_Panel().getBody(), getTest().getBody()));
        getWorld().addListener(new Listeners.Destroyer_Listener(getBlocks()));
        getWorld().addListener(new Listeners.Ball_BWall(test.getBody(),b_wall.getBody(),l_wall.getBody(),r_wall.getBody(),t_wall.getBody()));
    }

    private void Init(){
        screen_x = 1920;
        screen_y = 1080;
        x_min = 10 ;//+ (int)m_Panel.getM_panel().getSize().x;
        y_min = 0 + (int) getM_Panel().getM_panel().getSize().y;
        x_max = screen_x - (int) getM_Panel().getM_panel().getSize().x - 10;
        y_max = screen_y - (int) getM_Panel().getM_panel().getSize().y;

        System.out.println("x_min : " + x_min +" , x_max : "+x_max);
        CreateLifes();
    }

    public void Update(float dt){
        getWorld().update(dt);
        Mover(dt);
        getTest().Update(dt);
        getPostProcess().Update(dt);

        time += dt;
        if(time >= 0.2f){
            time = 0.0f;
            Transform panel_transform = getM_Panel().getBody().getTransform();
            Transform test_transform = getTest().getBody().getTransform();
            Vector2 ndir = new Vector2(panel_transform.getTranslationX() - test_transform.getTranslationX(),panel_transform.getTranslationY() - test_transform.getTranslationY());
            Vector2 ndir_offset = new Vector2(0,25);
            ndir.add(ndir_offset);
            ndir.normalize();
            ndir.multiply(100000);
            if(isPullingBall()) getTest().getBody().applyForce(ndir);
        }

        getParalaxScroll().Update(dt);
    }


    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        getParalaxScroll().Render(view,proj,ortho);

        getM_Panel().Render(view,proj,ortho);
        getTest().Render(view,proj,ortho);
        getPostProcess().Render(view,proj,ortho);

        panel_ball_l.Render(view,proj,ortho);
        panel_ball_r.Render(view,proj,ortho);

        for (Panel panel:
             walls) {
            panel.Render(view,proj,ortho);
        }
        RenderSideBlocks(view,proj,ortho);
        RenderLifes(view,proj,ortho);
    }

    public void LifeDecrese(){
        if(getNumber_of_lifes() <= 1){
            setALive(false);
            Application.getInstance().getManager().setMenu(true);
            glfwSetInputMode(Application.getInstance().getWindow(),GLFW_CURSOR,GLFW_CURSOR_NORMAL);
            setNumber_of_lifes(5);
            levelManager.level = 1;
            levelManager.isPlaying = false;
            return;
        }
        setNumber_of_lifes(getNumber_of_lifes() - 1);
    }

    private void CreateLifes(){
        lifes = new ArrayList<>();
        Vector2f starting_pos = new Vector2f(10,10);
        Vector2f size_of_life = new Vector2f(50,50);
        float offset = 5.0f;
        String path = getClass().getResource("/Images/heart_full_32x32.png").getPath().substring(1);
        for(int i = 0; i < getNumber_of_lifes(); i++){
            Panel temp = new Panel(starting_pos.x + i*size_of_life.x+offset,starting_pos.y,size_of_life,path);
            lifes.add(temp);
        }
    }

    private void RenderLifes(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        for (int i = 0; i < getNumber_of_lifes(); i++) {
            Panel panel = lifes.get(i);
            panel.Render(view,proj,ortho);
        }
    }

    public void GenerateSideBlocks(int width, int height, int offset, Vector2f size_of_Block, Vector2f init_pos,int t){

        //if(blocks == null)blocks = new ArrayList<>();
        try{
            if(size_of_Block.x > width || size_of_Block.y > height)return;
            int max_number_of_blocks = (int) (width / (size_of_Block.x + offset));

            System.out.println("max number of blocks : " + max_number_of_blocks);
            String green = getClass().getResource("/Images/03-Breakout-Tiles.png").getPath().substring(1);
            String red = getClass().getResource("/Images/07-Breakout-Tiles.png").getPath().substring(1);
            String yellow = getClass().getResource("/Images/13-Breakout-Tiles.png").getPath().substring(1);
            String green2 = getClass().getResource("/Images/15-Breakout-Tiles.png").getPath().substring(1);
            String blue = getClass().getResource("/Images/11-Breakout-Tiles.png").getPath().substring(1);
            String gray = getClass().getResource("/Images/17-Breakout-Tiles.png").getPath().substring(1);
            String purple = getClass().getResource("/Images/05-Breakout-Tiles.png").getPath().substring(1);
            String bronze = getClass().getResource("/Images/19-Breakout-Tiles.png").getPath().substring(1);
            String path = gray;

            Texture texture_green = Texture.loadTexture(green);
            Texture texture_red = Texture.loadTexture(red);
            Texture texture_yellow = Texture.loadTexture(yellow);
            Texture texture_green2 = Texture.loadTexture(green2);
            Texture texture_blue = Texture.loadTexture(blue);
            Texture texture_gray = Texture.loadTexture(gray);
            Texture texture_purple = Texture.loadTexture(purple);
            Texture texture_bronze = Texture.loadTexture(bronze);
            int vao = test.getM_panel().getVAO();
            Shader shader = test.getM_panel().getShader();
            Texture m_texture;

            for(int i = 0 ; i < max_number_of_blocks;i++){
                int val;
                if(t > -1)val = t;
                else val = (int) ((Math.random() * 10000) % 7);
                switch (val){
                    case 0:
                        path = green;
                        m_texture = texture_green;
                        break;
                    case 1:
                        path = red;
                        m_texture = texture_red;
                        break;
                    case 2:
                        path = yellow;
                        m_texture = texture_yellow;
                        break;
                    case 3:
                        path = green2;
                        m_texture = texture_green2;
                        break;
                    case 4:
                        path = blue;
                        m_texture = texture_blue;
                        break;
                    case 5:
                        path = gray;
                        m_texture = texture_gray;
                        break;
                    case 6:
                        path = purple;
                        m_texture = texture_purple;
                        break;
                    case 7:
                        path = bronze;
                        m_texture = texture_bronze;
                        break;
                        default:
                            path = green;
                            m_texture = texture_green;
                }
                Vector2f npos = new Vector2f(init_pos.x + offset + i*size_of_Block.x,init_pos.y + offset + 0);//i*size_of_Block.y);
                //app.Panel temp = new app.Panel(npos.x,npos.y,size_of_Block,path);
                Panel temp = new Panel(npos.x,npos.y,size_of_Block,vao,m_texture,shader);
                Body body = new Body();
                body.addFixture(Geometry.createRectangle((float)temp.getM_panel().getSize().x,(float)temp.getM_panel().getSize().y));
                body.setMass(Mass.Type.INFINITE);
                body.translate(temp.getCenter_x(),temp.getCenter_y());
                temp.setBody(body);
                getBlocks().add(temp);
                getWorld().addBody(body);
            }
        }catch(Exception err){
            System.err.println(err);
        }
        System.out.println("Sideblocks number :" + getBlocks().size());
    }

    private void RenderSideBlocks(Matrix4f view, Matrix4f proj, Matrix4f ortho){
        for (Panel panel:
                getBlocks()) {
            panel.Render(view,proj,ortho);
        }
    }
    private void Mover(float dt){
        float npos_x = getM_Panel().getM_panel().getPosition().x;
        float npos_y = getM_Panel().getM_panel().getPosition().y;
        float left_ball = panel_ball_l.getM_panel().getPosition().x;
        float right_ball = panel_ball_r.getM_panel().getPosition().x;

        if(glfwGetKey(Application.getInstance().getWindow(),GLFW_KEY_RIGHT) == GLFW_PRESS){
            npos_x += 1.0f * speedModifier * dt;
            right_ball += 1.0f * speedModifier * dt;
            left_ball += 1.0f * speedModifier * dt;
        }
        if(glfwGetKey(Application.getInstance().getWindow(),GLFW_KEY_LEFT) == GLFW_PRESS){
            npos_x += -1.0f * speedModifier * dt;
            right_ball += -1.0f * speedModifier * dt;
            left_ball += -1.0f * speedModifier * dt;
        }

        int xr_max = screen_x - (int) panel_ball_r.getM_panel().getSize().x - 10;
        int npos_X = Math.max(10,Math.min(x_max,(int)npos_x));
        int lball_X = Math.max(10,Math.min(x_max,(int)left_ball));
        int rball_X = Math.max(10,Math.min(xr_max,(int)right_ball));

        getM_Panel().getM_panel().setPosition(new Vector3f(npos_X,npos_y,0.0f));
        panel_ball_l.getM_panel().setPosition(new Vector3f(lball_X,panel_ball_l.getM_panel().getPosition().y,0.0f));
        panel_ball_r.getM_panel().setPosition(new Vector3f(lball_X+m_Panel.getM_panel().getSize().x-25,panel_ball_r.getM_panel().getPosition().y,0.0f));

        getM_Panel().CalculateCenter();
        panel_ball_l.CalculateCenter();
        panel_ball_r.CalculateCenter();

        //BALL MOVEMENT, starting
        if(canPush){
            Transform transform = new Transform();
            transform.setTranslation(getM_Panel().getBody().getTransform().getTranslation().add(new Vector2(0,50)));
            test.getBody().setTransform(transform);
        }
    }

    public boolean isPullingBall() {
        return isPullingBall;
    }

    public void setPullingBall(boolean pullingBall) {
        isPullingBall = pullingBall;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isALive() {
        return isALive;
    }

    public void setALive(boolean ALive) {
        isALive = ALive;
    }

    public Panel getTest() {
        return test;
    }

    public void setTest(Panel test) {
        this.test = test;
    }

    public Panel getM_Panel() {
        return m_Panel;
    }

    public void setM_Panel(Panel m_Panel) {
        this.m_Panel = m_Panel;
    }

    public ParalaxScroll getParalaxScroll() {
        return paralaxScroll;
    }

    public void setParalaxScroll(ParalaxScroll paralaxScroll) {
        this.paralaxScroll = paralaxScroll;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public int getNumber_of_lifes() {
        return number_of_lifes;
    }

    public void setNumber_of_lifes(int number_of_lifes) {
        this.number_of_lifes = number_of_lifes;
    }

    public boolean isCanPush() {
        return canPush;
    }

    public void setCanPush(boolean canPush) {
        this.canPush = canPush;
    }

    public PostProcess getPostProcess() {
        return postProcess;
    }

    public void setPostProcess(PostProcess postProcess) {
        this.postProcess = postProcess;
    }

    public List<Panel> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Panel> blocks) {
        this.blocks = blocks;
    }
}
