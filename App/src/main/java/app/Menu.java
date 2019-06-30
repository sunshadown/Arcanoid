package app;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.*;
import org.liquidengine.legui.border.SimpleLineBorder;
import org.liquidengine.legui.component.*;
import org.liquidengine.legui.event.CursorEnterEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.CursorEnterEventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.listener.processor.EventProcessor;
import org.liquidengine.legui.font.*;
import org.liquidengine.legui.color.*;
import org.liquidengine.legui.system.context.CallbackKeeper;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.context.DefaultCallbackKeeper;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessor;
import org.liquidengine.legui.system.*;
import org.liquidengine.legui.system.renderer.Renderer;
import org.liquidengine.legui.system.renderer.nvg.NvgRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Menu {
    private Frame frame;
    private Context context;
    private CallbackKeeper keeper;
    private SystemEventProcessor systemEventProcessor;
    private Renderer renderer;
    private DefaultInitializer initializer;
    private int width,height;
    private long window;

    Menu(long window,int width, int height){
        this.width = width;
        this.height = height;
        this.window = window;
        Init();
    }

    public void Destroy_Legui(){
        renderer.destroy();
    }

    private void Init(){

        Init_Legui();
    }

    private void Init_Legui(){
        // Firstly we need to create frame component for window.
        frame = new Frame(width, height);
        // we can add elements here or on the fly
        //createGuiElements(frame);


        // We need to create legui instance one for window
        // which hold all necessary library components
        // or if you want some customizations you can do it by yourself.
        initializer = new DefaultInitializer(window, frame);
        context = initializer.getContext();

        //keeper = new DefaultCallbackKeeper();
        // register callbacks for window. Note: all previously binded callbacks will be unbinded.
        //CallbackKeeper.registerCallbacks(window, initializer.getCallbackKeeper());

        //Add your callbacks here
        //GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
        //GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;
        //keeper.getChainKeyCallback().add(glfwKeyCallbackI);
        //keeper.getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);

        // Event processor for system events. System events should be processed and translated to gui events.
        //systemEventProcessor = new SystemEventProcessor();
        //systemEventProcessor.addDefaultCallbacks(keeper);

        //renderer = new NvgRenderer();
        renderer = initializer.getRenderer();
        renderer.initialize();
    }

    public void Update(float dt){
        // Also we can do it in one line
        context.updateGlfwWindow();
    }

    public void PostUpdate(){
        // Run animations. Should be also called cause some components use animations for updating state.
        Animator.getInstance().runAnimations();
        //AnimatorProvider.getAnimator().runAnimations(); deprecated

        // Now we need to process events. Firstly we need to process system events.
        //systemEventProcessor.processEvents(frame, context);
        initializer.getSystemEventProcessor().processEvents(frame,context);
        initializer.getGuiEventProcessor().processEvents();

        // When system events are translated to GUI events we need to process them.
        // This event processor calls listeners added to ui components
        //EventProcessor.getInstance().processEvents();

        // When everything done we need to relayout components.
        //LayoutManager.getInstance().layout(frame);


    }

    public void Render(){
        Vector2i windowSize = context.getWindowSize();
        // Clear screen
        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        renderer.render(frame,context);
    }

    private static void createGuiElements(Frame frame) {
        // Set background color for frame
        Vector4f color = frame.getContainer().getBackgroundColor();
        color = new Vector4f(255,0,0,0);
        frame.getContainer().setFocused(false);

        Button button = new Button("Add components", 20, 20, 160, 30);
        SimpleLineBorder border = new SimpleLineBorder(ColorConstants.black(), 1);
        button.setBorder(border);

        boolean[] added = {false};
        button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
            if (!added[0]) {
                added[0] = true;
                for (Component c : generateOnFly()) {
                    frame.getContainer().add(c);
                }
            }
        });

        button.getListenerMap().addListener(CursorEnterEvent.class, (CursorEnterEventListener) System.out::println);

        frame.getContainer().add(button);
    }

    private static List<Component> generateOnFly() {
        List<Component> list = new ArrayList<>();

        Label label = new Label(20, 60, 200, 20);
        label.getTextState().setText("Generated on fly label");
        label.getTextState().setTextColor(ColorConstants.red());

        RadioButtonGroup group = new RadioButtonGroup();
        RadioButton radioButtonFirst = new RadioButton("First", 20, 90, 200, 20);
        RadioButton radioButtonSecond = new RadioButton("Second", 20, 110, 200, 20);

        radioButtonFirst.setRadioButtonGroup(group);
        radioButtonSecond.setRadioButtonGroup(group);

        list.add(label);
        list.add(radioButtonFirst);
        list.add(radioButtonSecond);

        return list;
    }
}
