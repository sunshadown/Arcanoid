#version 330

layout(location = 0) in vec3 vPos;
layout(location = 1) in vec4 col1;
layout(location = 2) in vec4 col2;
layout(location = 3) in vec4 col3;
layout(location = 4) in vec4 col4;
layout(location = 5) in vec2 direction;

uniform mat4 projection;
uniform float scale;
uniform float time;
uniform float screen_x;
uniform float screen_y;
uniform float speedModifier;

void main()
{
    vec2 center = vec2(screen_x/2.0,screen_y/2.0);
    vec3 pos = vPos;

    pos = vPos * scale;
    mat4 vMat;
    vMat[0] = col1;
    vMat[1] = col2;
    vMat[2] = col3;
    vMat[3] = col4;
    vec4 npos = vMat * vec4(pos.xy,0.8,1.0);
    npos.xy = npos.xy + direction * speedModifier * time * time;
    gl_Position = projection * npos;
}