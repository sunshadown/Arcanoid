package app;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Panel{
    private Render2D m_panel;
    private Vector2f size;
    private float screen_x;
    private float screen_y;
    private float center_x;
    private float center_y;
    private String filepath;
    private Body body;

    public Panel(float screen_x, float screen_y, Vector2f size, int VAO, Texture texture, Shader shader){
        this.screen_x = screen_x;
        this.screen_y = screen_y;
        setCenter_x((screen_x + size.x/2));
        setCenter_y((screen_y + size.y/2));
        this.size = size;

        setM_panel(new Render2D(0,new Vector3f((float)screen_x,screen_y,0.0f),size,0.0f,VAO,texture,shader));
    }

    public Panel(float screen_x, float screen_y, Vector2f size,String filepath){
        this.screen_x = screen_x;
        this.screen_y = screen_y;
        setCenter_x((screen_x + size.x/2));
        setCenter_y((screen_y + size.y/2));
        this.size = size;
        this.setFilepath(filepath);
        Setup();
    }

    private void Setup(){
        setM_panel(new Render2D(0,new Vector3f((float)screen_x,screen_y,0.0f),size,0.0f));
        getM_panel().LoadTex(getFilepath());
    }

    public void Update(float dt){
        Transform transform = body.getTransform();
        //transform.setTranslation(transform.getTranslationX()*5.0f,transform.getTranslationY()*5.0f);
        setCenter_x((float)transform.getTranslationX());
        setCenter_y((float)transform.getTranslationY());
        screen_x = (getCenter_x() - size.x/2);
        screen_y = (getCenter_y() - size.y/2);
        m_panel.setPosition(new Vector3f(screen_x,screen_y,(float)0));
        //System.out.println(screen_x+" , "+screen_y);
        //System.out.println(transform.getTranslationX()+" , "+transform.getTranslationY());
    }

    public void Render(Matrix4f view, Matrix4f proj,Matrix4f ortho){
        getM_panel().Render(view,proj,ortho);
    }

    public Render2D getM_panel() {
        return m_panel;
    }

    public void setM_panel(Render2D m_panel) {
        this.m_panel = m_panel;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void CalculateCenter(){
        center_x = (m_panel.getPosition().x + size.x/2);
        center_y = (m_panel.getPosition().y + size.y/2);
        body.getTransform().setTranslationX(center_x);
        body.getTransform().setTranslationY(center_y);
        //System.out.println(body.getTransform().getTranslationX()+" , "+body.getTransform().getTranslationY());
        //System.out.println(body.getTransform().getTranslationX()*app.Logic.scale+" , "+body.getTransform().getTranslationY()*app.Logic.scale);
    }

    public float getCenter_x() {
        return center_x;
    }

    public void setCenter_x(float center_x) {
        this.center_x = center_x;
    }

    public float getCenter_y() {
        return center_y;
    }

    public void setCenter_y(float center_y) {
        this.center_y = center_y;
    }
}
