#version 330

in vec2 vPosition;
out vec2 TexCord;

void main()
{
	gl_Position = vec4(vPosition,0.0,1.0);
	TexCord = (vPosition + 1.0)/2.0;
}