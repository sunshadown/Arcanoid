package app;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;

public class Manager {
    private Camera camera;
    private BMenu menu;
    private boolean isMenu = true;

    private float screen_x;
    private float screen_y;
    private FloatBuffer fb;
    private FloatBuffer fb_view;
    private FloatBuffer fb_proj;
    private Matrix4f viewproj;
    private Matrix4f view;
    private Matrix4f proj;
    private Matrix4f ortho;
    private float time = 0;

    private SoundSource AudioSource;
    private SoundSource Audio_Bounce;
    private SoundSource Audio_Bounce2;
    private SoundSource Audio_Explosion;

    private String defaultDeviceName;
    private long device;
    private long context;
    private ALCCapabilities alcCapabilities;
    private ALCapabilities alCapabilities;

    private Attractor attractor;
    private Cube cube;
    private Model triangle1;
    private Model triangle2;
    private Model triangle3;
    private Model triangle4;
    private Model triangle5;
    private Model triangle6;
    private Render2D renderer;
    private Render2D renderer2;

    private FBO fbo;
    private Model renderPass;

    private int passes;
    private List<FBO> fbos;
    private List<Render2D> renderPasses;
    private boolean isRenderPassing;
    private int filtertype;
    private int edward_filter = 1;

    private Panel panel;
    private Logic logic;

    public Manager(){
        setCamera(new Camera());
        getCamera().AnimateStart();
        setMenu(new BMenu());

        attractor = new Attractor(100000);

        cube = new Cube(true);
        triangle1 = new Triangle(new Vector3f(0.0f,-45.0f,0.0f),new Vector3f(0,0,0),new Vector3f(1,1,1));
        triangle2 = new Triangle(new Vector3f(0.0f,45.0f,0.0f),new Vector3f(0,0,0),new Vector3f(1,1,1));
        triangle3 = new Triangle(new Vector3f(0.0f,0.0f,45.0f),new Vector3f(90,0,0),new Vector3f(1,1,1));
        triangle4 = new Triangle(new Vector3f(0.0f,0.0f,-45.0f),new Vector3f(90,0,0),new Vector3f(1,1,1));
        triangle5 = new Triangle(new Vector3f(-45.0f,0.0f,0.0f),new Vector3f(0,0,90),new Vector3f(1,1,1));
        triangle6 = new Triangle(new Vector3f(45.0f,0.0f,0.0f),new Vector3f(0,0,90),new Vector3f(1,1,1));
        renderer = new Render2D();
        setRenderer2(new Render2D(edward_filter,new Vector3f(200,100,0),new Vector2f(300,300),0.0f));
        ((Render2D) renderer).LoadTex(getClass().getResource("/Images/test3.jpg").getPath().substring(1));
        ((Render2D) getRenderer2()).LoadTex(getClass().getResource("/Images/test3.jpg").getPath().substring(1));

        //PANEL INIT
        panel = new Panel(1920/2,50,new Vector2f(200.0f,50.0f),getClass().getResource("/Images/01-Breakout-Tiles.png").getPath().substring(1));
        setLogic(new Logic(panel));
        //!!!!!!!!!!
        fbo = new FBO(1920,1080);
        setFiltertype(5);
        renderPass = new Render2D(getFiltertype(),true,fbo.getTex());
        fbos = new ArrayList<>();
        renderPasses = new ArrayList<>();
        setPasses(10);
        setRenderPassing(false);

        PostRenderInit();

        DeviceInit();
        SoundInit();

        alSourcei(Audio_Bounce.sourcePointer,AL_LOOPING,AL_FALSE);
        alSourcei(Audio_Bounce2.sourcePointer,AL_LOOPING,AL_FALSE);
        alSourcei(Audio_Explosion.sourcePointer,AL_LOOPING,AL_FALSE);
        alSourcei(AudioSource.sourcePointer,AL_LOOPING,AL_TRUE);
        alSourcef(Audio_Explosion.sourcePointer,AL_GAIN,1.0f);
        alSourcef(Audio_Bounce.sourcePointer,AL_GAIN,0.6f);
        alSourcef(AudioSource.sourcePointer,AL_GAIN,0.05f);
        getAudioSource().play();
    }

    private void DeviceInit(){
        //Initialization
        defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);
        alcCapabilities = ALC.createCapabilities(device);
        alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    private void SoundInit(){
        setAudioSource(new SoundSource());
        setAudio_Bounce(new SoundSource());
        setAudio_Explosion(new SoundSource());
        setAudio_Bounce2(new SoundSource());
        try{
            getAudioSource().LoadOgg(getClass().getResource("/Sound/rsl.ogg").getPath().substring(1));
            getAudio_Bounce().LoadOgg(getClass().getResource("/Sound/bounce1.ogg").getPath().substring(1));
            getAudio_Bounce2().LoadOgg(getClass().getResource("/Sound/bounce2.ogg").getPath().substring(1));
            getAudio_Explosion().LoadOgg(getClass().getResource("/Sound/Explosion.ogg").getPath().substring(1));
        }catch(IOException err){
            System.err.println(err);
        }
        //set
        alListener3f(AL_POSITION,    0.0f, 0.0f, -10.0f);
        alListener3f(AL_VELOCITY,    0.0f, 0.0f, 0.0f);
        alListener3f(AL_ORIENTATION, 0.0f, 1.0f, 0.0f);
    }
    public void ChangeFilter_Renderes(int filtertype){
        this.setFiltertype(filtertype);
        for(int i = 0; i < getPasses(); i++){
            renderPasses.get(i).setFiltertype(filtertype);
        }
    }

    public void PostRenderUpdate(int width,int height){
        for(int i = 0; i < getPasses(); i++){
            fbos.get(i).setWidth(width);
            fbos.get(i).setHeight(height);
            fbos.get(i).Update();
            renderPasses.get(i).setFBOid(fbos.get(i).getTex());
        }

    }
    private void PostRenderInit(){
        for(int i = 0; i < getPasses(); i++){
            FBO nfbo = new FBO(1920,1080);
            Render2D npass = new Render2D(getFiltertype(),true,nfbo.getTex());
            fbos.add(nfbo);
            renderPasses.add(npass);
        }
    }
    private void PostRender(){
        fbos.get(0).bind();
        //glDisable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderPass.Render(view,getProj(),ortho);
        fbos.get(0).unbind();
        for(int i = 1; i < getPasses(); i++){
            fbos.get(i).bind();
            //glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderPasses.get(i-1).Render(view,getProj(),ortho);
            fbos.get(i).unbind();
        }
        glDisable(GL_DEPTH_TEST); // disable depth test so screen-space quad isn't discarded due to depth test.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderPasses.get(getPasses() -1).Render(view,proj,ortho);
    }

    public void Update(float dt){
        Vector3f eye,target,up;
        eye = new Vector3f(camera.getPosition());
        target = new Vector3f(eye).add(new Vector3f(camera.getDirection()).mul(5.0f));
        up = new Vector3f(0,1,0);

        if(isMenu()){
            getMenu().Update(dt);
            getCamera().Animate(dt);
            attractor.Update(dt);
        }
        else{
            getCamera().Update(dt);
            //cube.Update(dt);
            getLogic().Update(dt);
            //panel.Update(dt);
            if(time  >= 3.0f) {
                System.out.println("position :" + getCamera().getPosition().x +" "+ getCamera().getPosition().y+" "+ getCamera().getPosition().z+", direction :" + getCamera().getDirection().x +" "+ getCamera().getDirection().y+" "+ getCamera().getDirection().z);
                //System.out.println("direction :" + getCamera().getDirection().x +" "+ getCamera().getDirection().y+" "+ getCamera().getDirection().z);
                time = 0;
            }
            time += dt;
        }
        view = new Matrix4f().lookAt(eye,target,up);
        setFb_proj(BufferUtils.createFloatBuffer(16));
        fb_view = BufferUtils.createFloatBuffer(16);
        getProj().get(getFb_proj());
        view.get(fb_view);
    }
    public void PreRender(){
        //cube.Render(view, getProj(),ortho);
        /*triangle1.Render(view,getProj(),ortho);
        triangle2.Render(view,getProj(),ortho);
        triangle3.Render(view,getProj(),ortho);
        triangle4.Render(view,getProj(),ortho);
        triangle5.Render(view,getProj(),ortho);
        triangle6.Render(view,getProj(),ortho);*/

    }

    public void AfterRender(){
        //renderer.Render(view,getProj(),ortho);
        //getRenderer2().Render(view,getProj(),ortho);
        //panel.Render(view,getProj(),ortho);
        getLogic().Render(view,getProj(),ortho);
    }

    public void Render(){
        if(isMenu()){
            glDisable(GL_DEPTH_TEST);
            attractor.Render(view,proj,ortho);
            getMenu().Render(view,getProj(),ortho);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);
        }
        else{
            if(isRenderPassing()){
                fbo.bind();
                glEnable(GL_DEPTH_TEST);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                PreRender();

                fbo.unbind();

                PostRender();

                AfterRender();
            }
            else{
                PreRender();
                AfterRender();
            }
        }

    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public FloatBuffer getFb() {
        return fb;
    }

    public void setFb(FloatBuffer fb) {
        this.fb = fb;
    }

    public Matrix4f getProj() {
        return proj;
    }

    public void setProj(Matrix4f proj) {
        this.proj = proj;
    }

    public FloatBuffer getFb_proj() {
        return fb_proj;
    }

    public void setFb_proj(FloatBuffer fb_proj) {
        this.fb_proj = fb_proj;
    }

    public Matrix4f getOrtho() {
        return ortho;
    }

    public void setOrtho(Matrix4f ortho) {
        this.ortho = ortho;
    }

    public int getFiltertype() {
        return filtertype;
    }

    public void setFiltertype(int filtertype) {
        this.filtertype = filtertype;
    }

    public boolean isRenderPassing() {
        return isRenderPassing;
    }

    public void setRenderPassing(boolean renderPassing) {
        isRenderPassing = renderPassing;
    }

    public int getEdward_filter() {
        return edward_filter;
    }

    public void setEdward_filter(int edward_filter) {
        this.edward_filter = edward_filter;
    }

    public Render2D getRenderer2() {
        return renderer2;
    }

    public void setRenderer2(Render2D renderer2) {
        this.renderer2 = renderer2;
    }

    public int getPasses() {
        return passes;
    }

    public void setPasses(int passes) {
        this.passes = passes;
    }

    public float getScreen_x() {
        return screen_x;
    }

    public void setScreen_x(float screen_x) {
        this.screen_x = screen_x;
    }

    public float getScreen_y() {
        return screen_y;
    }

    public void setScreen_y(float screen_y) {
        this.screen_y = screen_y;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public SoundSource getAudioSource() {
        return AudioSource;
    }

    public void setAudioSource(SoundSource audioSource) {
        AudioSource = audioSource;
    }

    public SoundSource getAudio_Bounce() {
        return Audio_Bounce;
    }

    public void setAudio_Bounce(SoundSource audio_Bounce) {
        Audio_Bounce = audio_Bounce;
    }

    public SoundSource getAudio_Explosion() {
        return Audio_Explosion;
    }

    public void setAudio_Explosion(SoundSource audio_Explosion) {
        Audio_Explosion = audio_Explosion;
    }

    public BMenu getMenu() {
        return menu;
    }

    public void setMenu(BMenu menu) {
        this.menu = menu;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean menu) {
        isMenu = menu;
    }

    public SoundSource getAudio_Bounce2() {
        return Audio_Bounce2;
    }

    public void setAudio_Bounce2(SoundSource audio_Bounce2) {
        Audio_Bounce2 = audio_Bounce2;
    }
}
