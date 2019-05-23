#version 330

in vec4 vPosition;
out vec2 TexCord;

uniform mat4 projection;
uniform mat4 model;

void main()
{
    gl_Position = projection * model * vec4(vPosition.xy,0.0,1.0);
    TexCord = vPosition.zw;
}  