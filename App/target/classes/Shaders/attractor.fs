#version 450

in vec3 color;
out vec4 mColor;

void main()
{
    mColor = vec4(color,0.7);
}