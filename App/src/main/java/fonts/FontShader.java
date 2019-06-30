package fonts;

import app.Model;
import app.Shader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;

public class FontShader extends Model {

	private static  String VERTEX_FILE = "/Shaders/fontVertex.txt";
	private static  String FRAGMENT_FILE = "/Shaders/fontFragment.txt";
	public int texcord_vbo;

	public FontShader() {
		FontShader.VERTEX_FILE = getClass().getResource("/Shaders/fontVertex.txt").getPath().substring(1);
		FontShader.FRAGMENT_FILE = getClass().getResource("/Shaders/fontFragment.txt").getPath().substring(1);
		Init();
	}

	public static int LoadVAO(float[] positions, float[]texcords){
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int vbo1 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,vbo1);
		FloatBuffer position_buffer = storeDataInFloatBuffer(positions);
		glBufferData(GL_ARRAY_BUFFER,position_buffer,GL_STREAM_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0,2,GL_FLOAT,false,0,0);

		int vbo2 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,vbo2);
		FloatBuffer texcord_buffer = storeDataInFloatBuffer(texcords);
		glBufferData(GL_ARRAY_BUFFER,texcord_buffer,GL_STREAM_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

		glBindVertexArray(0);
		return vao;
	}

	public void InitBuffer(float[] positions, float[]texcords){
		setVAO(glGenVertexArrays());
		glBindVertexArray(getVAO());

		setVBO(glGenBuffers());
		glBindBuffer(GL_ARRAY_BUFFER,getVBO());
		FloatBuffer position_buffer = storeDataInFloatBuffer(positions);
		glBufferData(GL_ARRAY_BUFFER,position_buffer,GL_STREAM_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0,2,GL_FLOAT,false,0,0);

		texcord_vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,texcord_vbo);
		FloatBuffer texcord_buffer = storeDataInFloatBuffer(texcords);
		glBufferData(GL_ARRAY_BUFFER,texcord_buffer,GL_STREAM_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

		glBindVertexArray(0);
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	@Override
	public void Init() {
		setShader(new Shader());
		try {
			getShader().attachVertexShader(getClass().getResource("/Shaders/fontVertex.txt").getPath().substring(1));
			getShader().attachFragmentShader(getClass().getResource("/Shaders/fontFragment.txt").getPath().substring(1));
			getShader().link();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Update(float dt) {

	}

	@Override
	public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho) {

	}
}
