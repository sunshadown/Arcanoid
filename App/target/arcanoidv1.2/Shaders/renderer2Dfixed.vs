#version 330

in vec4 vPosition;
out vec2 TexCord;

uniform mat4 projection;
uniform mat4 model;
uniform float m_depth;

void main()
{
    gl_Position = projection * model * vec4(vPosition.xy,m_depth,1.0);
    TexCord = vPosition.zw;
}  