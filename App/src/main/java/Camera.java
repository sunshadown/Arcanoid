import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Camera {
    private Vector3f position;
    private Vector3f direction;
    private Vector3f up;
    private float MovementSpeed;
    private float RotationSpeed;
    private float MouseSensitivity;

    private boolean UseMouse;
    private double previousMouseX;
    private double previousMouseY;

    Camera(){
        // Domyslne ustawienia kamery:
        position = new Vector3f((float) -27.491451, (float) -0.044956513, (float) 40.568993);
        direction = new Vector3f((float)0.66238624, (float)0, (float)-0.7491619);
        up = new Vector3f(0, 1, 0);

        MovementSpeed = 15.0f;
        RotationSpeed = 1.0f;
        MouseSensitivity = .01f;

        UseMouse = false;

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            //glfwGetWindowSize(Application.getInstance().getWindow(), pWidth, pHeight);
            //glfwGetFramebufferSize(Application.getInstance().getWindow(),pWidth,pHeight);
            int []width = new int[1];
            int []height = new int[1];
            glfwGetFramebufferSize(Application.getInstance().getWindow(),width,height);
            //previousMouseX = (double)pWidth / 2;
            //previousMouseY = (double)pHeight / 2;
            previousMouseX = ((double)width[0]/ 2);
            previousMouseY = ((double)height[0] / 2);
            System.out.println("width : "+width[0] + ", height : "+height[0]);

            glfwSetCursorPos(Application.getInstance().getWindow(), previousMouseX, previousMouseY);
        } // the stack frame is popped automatically


    }
    Camera(Vector3f position, Vector3f direction, Vector3f up, float MouseSensitivity , float MovementSpeed, float RotationSpeed){

    }

    public void Update(float dt){
        //GLFWwindow* window = Application::GetInstance()->GetWindow();
        long window = Application.getInstance().getWindow();

        // Obliczenie przesuniecia kursora myszy:
        double []mouseX = new double[1];
        double []mouseY = new double[1];
        glfwGetCursorPos(window, mouseX, mouseY);
        double dMouseX = mouseX[0] - previousMouseX;
        double dMouseY = mouseY[0] - previousMouseY;
        previousMouseX = mouseX[0];
        previousMouseY = mouseY[0];

        float speedModifier = 1.0f;

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
        {
            speedModifier = 5.0f;
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_ALT) == GLFW_PRESS)
        {
            speedModifier = .25f;
        }

        // Ruch do przodu/tylu:
        {
            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
            {
                position.add(new Vector3f(direction).mul(MovementSpeed * dt * speedModifier));
            }
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
            {
                position.add(new Vector3f(direction).mul(-1.0f).mul(MovementSpeed * dt * speedModifier));
            }
        }

        // Ruch na boki:
        {
            Vector3f cameraPerp = new Vector3f(direction.z, 0, -direction.x); // Kierunek prostopadly do kierunku patrzenia
            cameraPerp = cameraPerp.normalize();
            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
            {
                //position = position + cameraPerp * MovementSpeed * dt * speedModifier;
                position.add(new Vector3f(cameraPerp).mul(MovementSpeed*dt*speedModifier));
            }
            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
            {
                position.add(new Vector3f(cameraPerp).mul(-1.0f).mul(MovementSpeed*dt*speedModifier));
            }
        }


        // Obrot:
        {
            double T = Math.acos(direction.y); // Aktualny kat obrotu gora/dol [rad]
            double G = Math.atan2(direction.z, direction.x); // Aktualny kat obrotu lewo/prawo [rad]

            // Uwzglednienie przesuniecia kursora myszy:
            if (UseMouse)
            {
                G += .1f * dMouseX * RotationSpeed * MouseSensitivity;
                T += .1f * dMouseY * RotationSpeed * MouseSensitivity;
            }

            // Uwzglednienie wcisnietych klawiszy:
            if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS)
            {
                G -= RotationSpeed * dt * speedModifier;
            }
            if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS)
            {
                G += RotationSpeed * dt * speedModifier;
            }
            if (glfwGetKey(window, GLFW_KEY_F) == GLFW_PRESS)
            {
                T -= RotationSpeed * dt * speedModifier;
            }
            if (glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS)
            {
                T += RotationSpeed * dt * speedModifier;
            }

            //T = __max(.01f * glm::pi<float>(), __min(.99f * glm::pi<float>(), T)); // Ograniczenie spojrzenia w dol/gore
            T = Math.max(0.01f * Math.PI, Math.min(0.99f * Math.PI,T));
            // Obliczenie zaktualizowanego, jednostkowego wektora kierunku po obrocie:
            direction.x = (float)Math.sin(T) * (float)Math.cos(G);
            direction.y = (float)Math.cos(T);
            direction.z = (float)Math.sin(T) * (float)Math.sin(G);
        }

        // Ruch w pionie:
        {
            if (glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS)
            {
                position.y = position.y + MovementSpeed * dt * speedModifier;
            }
            if (glfwGetKey(window, GLFW_KEY_V) == GLFW_PRESS)
            {
                position.y = position.y - MovementSpeed * dt * speedModifier;
            }
        }

        //Vector3f target = new Vector3f();
        //target = position.add(direction.mul(2.0f));
        //Matrix4f viewMatrix = new Matrix4f().lookAt(position,target,up);

        //std::cout << direction.x << " "<<direction.y << " "<<direction.z << std::endl;
        //glm::mat4 viewMatrix = glm::lookAt(position, position + direction * 5.0f, up);
        //Application::GetInstance()->SetViewMatrix(viewMatrix);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getUp() {
        return up;
    }

    public void setUp(Vector3f up) {
        this.up = up;
    }

    public float getMovementSpeed() {
        return MovementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        MovementSpeed = movementSpeed;
    }

    public float getRotationSpeed() {
        return RotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        RotationSpeed = rotationSpeed;
    }

    public float getMouseSensitivity() {
        return MouseSensitivity;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        MouseSensitivity = mouseSensitivity;
    }

    public boolean getUseMouse() {
        return UseMouse;
    }

    public void setUseMouse(boolean useMouse) {
        UseMouse = useMouse;
    }

    public double getPreviousMouseX() {
        return previousMouseX;
    }

    public void setPreviousMouseX(double previousMouseX) {
        this.previousMouseX = previousMouseX;
    }

    public double getPreviousMouseY() {
        return previousMouseY;
    }

    public void setPreviousMouseY(double previousMouseY) {
        this.previousMouseY = previousMouseY;
    }
}
