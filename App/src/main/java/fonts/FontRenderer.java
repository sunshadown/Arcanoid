package fonts;

import fonts.GUIText;

import java.util.List;
import java.util.Map;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}

	public void cleanUp(){

	}

	public void render(Map<FontType, List<GUIText>>texts){
		prepare();
		for(FontType font:texts.keySet()){
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D,font.getTextureAtlas());
			for(GUIText text:texts.get(font)){
				renderText(text);
			}
		}

	}

	private void prepare(){
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		shader.getShader().bind();
	}
	
	private void renderText(GUIText text){
		glBindVertexArray(text.getMesh());
		glUniform3f(shader.getShader().getUniform("color"),text.getColour().x,text.getColour().y,text.getColour().z);
		glUniform2f(shader.getShader().getUniform("translation"),text.getPosition().x,text.getPosition().y);
		glDrawArrays(GL_TRIANGLES,0,text.getVertexCount());
	}
	
	private void endRendering(){
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glBindVertexArray(0);
	}

}
