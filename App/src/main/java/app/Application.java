package app;

import org.dyn4j.geometry.Vector2;
import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Application {
    private long window;
    private long monitor;
    private long cursor;
    private Manager manager;

    private static Application instance;
    private static boolean isWireframe = false;

    public static Application getInstance(){
        if(instance == null)instance = new Application();
        return instance;
    }

    public void run() {
        System.out.println("App 1.0 , LWJGL: " + Version.getVersion() + "!");
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(getWindow());
        glfwDestroyWindow(getWindow());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        // Oczekiwana wersja OpenGL:
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, 4); // Antialiasing (MSAA)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        //glfwWindowHint(GLFW_FOCUS_ON_SHOW,GLFW_TRUE);
        int []major = new int[5];
        int []minor = new int[5];
        int []rev = new int[5];

        glfwGetVersion(major,minor,rev);
        System.out.println("GLFW version : " + major[0] + "." + minor[0] + " rev : " + rev[0]);
        // Create the window
        setWindow(glfwCreateWindow(1920, 1080, "app.Application", NULL, NULL));
        setMonitor(glfwGetPrimaryMonitor());

        if ( getWindow() == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(getWindow(), pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    getWindow(),
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically


        glfwSetWindowMonitor(getWindow(), getMonitor(),0,0,1920,1080,60);
        glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_NORMAL);
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        // Enable v-sync
        glfwSwapInterval(1);
        glEnable(GL_DEPTH_TEST);

        String opengl_version = glGetString(GL_VERSION);
        System.out.println("OpenGL version : " + opengl_version);

        Texture.isCursor = true;
        Texture CurTex = Texture.loadTexture(getClass().getResource("/Images/cursor.png").getPath().substring(1));
        Texture.isCursor = false;

        GLFWImage cur_image = GLFWImage.create();
        cur_image.width(CurTex.getWidth());
        cur_image.height(CurTex.getHeight());
        cur_image.pixels(CurTex.getM_data());
        cursor = GLFW.glfwCreateCursor(cur_image,(95/2)-5,95/2);
        glfwSetCursor(window,cursor);

        setManager(new Manager());

        GLFWCursorPosCallback glfwCursorPosCallback = glfwSetCursorPosCallback(window,(window,xpos,ypos)->{
            getManager().getMenu().CheckFocus(xpos,ypos);
        });

        GLFWMouseButtonCallback glfwMouseButtonCallback = glfwSetMouseButtonCallback(window,(window,button,action,mods)->{
            if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS){
                if(instance.getManager().getCamera().getUseMouse())return;

                double mouse_x[], mouse_y[];
                mouse_x = new double[1];
                mouse_y = new double[1];
                glfwGetCursorPos(window,mouse_x,mouse_y);
                mouse_y[0] = 1080 - mouse_y[0];
                System.out.println("Mouse position x,y : "+mouse_x[0]+" "+mouse_y[0]);
                if(instance.getManager().getMenu().Check_Start(mouse_x[0],mouse_y[0])){
                    instance.getManager().setMenu(false);
                    instance.getManager().getLogic().getLevelManager().LoadActualLevel();
                    glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);
                    return;
                }
                if(instance.getManager().getMenu().Check_Exit(mouse_x[0],mouse_y[0])){
                    glfwSetWindowShouldClose(window,true);
                }

            }
        });
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFWKeyCallback glfwKeyCallback = glfwSetKeyCallback(getWindow(), (window, key, scancode, action, mods) -> {

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                boolean state = instance.getManager().getCamera().getUseMouse();
                //if(state)glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_NORMAL);
                //else glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);
                glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_NORMAL);
                instance.getManager().getCamera().setUseMouse(false);
                instance.getManager().setMenu(true);
            }

            if (key == GLFW_KEY_M && action == GLFW_RELEASE){
                boolean state = instance.getManager().getCamera().getUseMouse();
                if(state)glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_NORMAL);
                else glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);
                instance.getManager().getCamera().setUseMouse(!state);
            }

            if(key == GLFW_KEY_G && action == GLFW_RELEASE){
                isWireframe = !isWireframe;
                if(isWireframe){
                    glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
                }
                else glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
            }

            if(key == GLFW_KEY_N && action == GLFW_RELEASE){
                int val = instance.getManager().getFiltertype();
                if(++val > 38)val = 0;
                System.out.println("Active Filter :" + MYFilters.filterName[val]);
                instance.getManager().ChangeFilter_Renderes(val);
            }

            if(key == GLFW_KEY_U && action == GLFW_RELEASE){
                boolean val = instance.getManager().isRenderPassing();
                instance.getManager().setRenderPassing(!val);
            }

            if(key == GLFW_KEY_P && action == GLFW_RELEASE){
                int val = instance.getManager().getEdward_filter();
                if(++val > 38)val = 0;
                instance.getManager().getRenderer2().setFiltertype(val);
            }
            if(key == GLFW_KEY_Q && action == GLFW_RELEASE){
                boolean state = instance.getManager().getLogic().isPullingBall();
                instance.getManager().getLogic().setPullingBall(!state);
            }
            if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE){
                //instance.getManager().getLogic().getTest().getBody().setAutoSleepingEnabled(false);
                if(instance.getManager().getLogic().isCanPush()){
                    instance.getManager().getLogic().getTest().getBody().applyImpulse(new Vector2(0,7000));
                    instance.getManager().getLogic().setCanPush(false);
                }
            }
        });

        GLFWWindowSizeCallback glfwWindowSizeCallback = glfwSetWindowSizeCallback(getWindow(),(window,w,h)->{
            float aspect = (float)w / h; // Wspolczynnik proporcji dlugosci bokow
            instance.getManager().setProj(new Matrix4f().perspective((float)Math.toRadians(90),aspect,0.01f,1000.0f));
            instance.getManager().setOrtho(new Matrix4f().ortho(0.0f,(float)w,0.0f,(float)h,-5.0f,5.0f));
            //instance.getManager().PostRenderUpdate(w,h);
            instance.getManager().setScreen_x(w);
            instance.getManager().setScreen_y(h);
            //instance.manager = glm::ortho(0.0f, (float)w, 0.0f, (float)h, -1.0f, 1.0f);
            glViewport(0, 0, w, h); // Okreslenie wymiarow renderowanego viewportu
        });

        float aspect = (float)1920 / 1080; // Wspolczynnik proporcji dlugosci bokow
        instance.getManager().setProj(new Matrix4f().perspective((float)Math.toRadians(90),aspect,0.01f,1000.0f));
        instance.getManager().setOrtho(new Matrix4f().ortho(0.0f,(float)1920,0.0f,(float)1080,-1.0f,1.0f));
        instance.getManager().PostRenderUpdate(1920,1080);
        instance.getManager().setScreen_x(1920);
        instance.getManager().setScreen_y(1080);
        glViewport(0, 0, 1920, 1080); // Okreslenie wymiarow renderowanego viewportu
        // Make the window visible
        //glfwSetWindowAttrib(window,GLFW_FOCUS_ON_SHOW,GLFW_TRUE);
        //glfwRequestWindowAttention(window);
        glfwShowWindow(getWindow());
        glfwFocusWindow(window);
    }

    private void loop() {
        // Set the clear color
        glClearColor(0.67f, 0.67f, 0.67f, 0.0f);

        double last = glfwGetTime();
        double currentTime = 0;
        while ( !glfwWindowShouldClose(getWindow()) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            currentTime = glfwGetTime();
            float dt = (float)currentTime - (float)last;
            getManager().Update(dt);

            getManager().Render();

            last = currentTime;

            glfwSwapBuffers(getWindow()); // swap the color buffers
            glfwPollEvents();
        }
    }

    private static void key_callback(long window, int key, int scancode, int action, int mods)
    {
        if (key == GLFW_KEY_M && action == GLFW_PRESS){
            boolean state = instance.getManager().getCamera().getUseMouse();
        }
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public long getMonitor() {
        return monitor;
    }

    public void setMonitor(long monitor) {
        this.monitor = monitor;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
