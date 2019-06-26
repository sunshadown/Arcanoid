import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;

public class Shader {
    //Program ID
    private int program;
    // Vertex Shader ID
    private int vertexShaderID;
    // Geometry Shader ID
    private int geometryShaderID;
    // Fragment Shader ID
    private int fragmentShaderID;
    // Tesselation Evaluation ID
    private int tesID;
    // Tesselation Control ID
    private int tcsID;

    private Map<String,Integer>attributes;
    private Map<String,Integer>uniforms;

    public Shader(){
        program = glCreateProgram();
        attributes = new HashMap<String, Integer>();
        uniforms = new HashMap<String,Integer>();
    }

    public void attachVertexShader(String name) throws IOException {
        // Load the source
        String vertexShaderSource =  new String(Files.readAllBytes(Paths.get(name)));
        // Create the shader and set the source
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSource);

        // Compile the shader
        glCompileShader(vertexShaderID);

        // Check for errors
        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error creating vertex shader\n"
                    + glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));
        }

        // Attach the shader
        glAttachShader(program, vertexShaderID);
    }

    public void attachTesselationEvaluationShader(String name)throws IOException{
        // Load the source
        String tesShaderSource =  new String(Files.readAllBytes(Paths.get(name)));
        // Create the shader and set the source
        tesID = glCreateShader(GL_TESS_EVALUATION_SHADER);
        glShaderSource(tesID, tesShaderSource);

        // Compile the shader
        glCompileShader(tesID);

        // Check for errors
        if (glGetShaderi(tesID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error creating tes shader\n"
                    + glGetShaderInfoLog(tesID, glGetShaderi(tesID, GL_INFO_LOG_LENGTH)));
        }

        // Attach the shader
        glAttachShader(program, tesID);
    }

    public void attachTesselationControlShader(String name)throws IOException{
        // Load the source
        String tcsShaderSource =  new String(Files.readAllBytes(Paths.get(name)));
        // Create the shader and set the source
        tcsID = glCreateShader(GL_TESS_CONTROL_SHADER);
        glShaderSource(tcsID, tcsShaderSource);

        // Compile the shader
        glCompileShader(tcsID);

        // Check for errors
        if (glGetShaderi(tcsID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error creating tcs shader\n"
                    + glGetShaderInfoLog(tcsID, glGetShaderi(tcsID, GL_INFO_LOG_LENGTH)));
        }

        // Attach the shader
        glAttachShader(program, tcsID);
    }

    public void attachGeometryShader(String name) throws IOException {
        // Load the source
        String geometryShaderSource =  new String(Files.readAllBytes(Paths.get(name)));
        // Create the shader and set the source
        geometryShaderID = glCreateShader(GL_GEOMETRY_SHADER);
        glShaderSource(geometryShaderID, geometryShaderSource);

        // Compile the shader
        glCompileShader(geometryShaderID);

        // Check for errors
        if (glGetShaderi(geometryShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error creating vertex shader\n"
                    + glGetShaderInfoLog(geometryShaderID, glGetShaderi(geometryShaderID, GL_INFO_LOG_LENGTH)));
        }

        // Attach the shader
        glAttachShader(program, geometryShaderID);
    }

    public void attachFragmentShader(String name) throws IOException {
        // Read the source
        String fragmentShaderSource = new String(Files.readAllBytes(Paths.get(name)));

        // Create the shader and set the source
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);

        // Compile the shader
        glCompileShader(fragmentShaderID);

        // Check for errors
        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error creating fragment shader\n"
                    + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));
        }

        // Attach the shader
        glAttachShader(program, fragmentShaderID);
    }

    public void link() {
        // Link this program
        glLinkProgram(program);

        // Check for linking errors
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {

            throw new RuntimeException("Unable to link shader program:" + glGetProgramInfoLog(program,glGetProgrami(program,GL_INFO_LOG_LENGTH)));
        }
    }

    public void bind(){glUseProgram(program);}
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Dispose the program and shaders.
     */
    public void dispose() {
        // Unbind the program
        unbind();

        // Detach the shaders
        glDetachShader(program, vertexShaderID);
        glDetachShader(program, fragmentShaderID);

        // Delete the shaders
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        // Delete the program
        glDeleteProgram(program);
    }

    /**
     * @return The ID of this program.
     */
    public int getID() {
        return program;
    }

    public int getAttrib(String name){
        return glGetAttribLocation(program,name);
        //return attributes.get(name).intValue();
    }

    public int getUniform(String name){
        return glGetUniformLocation(program,name);
        //return uniforms.get(name).intValue();
    }

    public void setAttrib(String name){
        int val = glGetAttribLocation(program,name);

        attributes.put(name,val);
    }

    public void setUniform(String name){
        uniforms.put(name,glGetUniformLocation(program,name));
    }
}
