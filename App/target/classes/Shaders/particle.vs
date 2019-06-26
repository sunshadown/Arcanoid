#version 450

layout(location = 0) in vec3 vPos;
layout(location = 1) in mat4 vMat;

uniform mat4 projection;
uniform float scale;

void main()
{
    vec3 pos = vPos * scale;
    pos.xy = pos.xy + gl_InstanceID;
    gl_Position = projection * vMat * vec4(pos.xy,0.8,1.0);
}