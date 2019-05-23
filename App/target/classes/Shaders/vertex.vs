#version 330 core

in vec3 vPos;

uniform mat4 view;
uniform mat4 projection;
uniform mat4 modelMatrix;

void main()
{
    vec4 npos = projection * view * modelMatrix * vec4(vPos,1.0);
    gl_Position = vec4( npos);
}