#version 450

layout(location = 0) in vec3 vPos;
layout(location = 1) in vec4 col1;
layout(location = 2) in vec4 col2;
layout(location = 3) in vec4 col3;
layout(location = 4) in vec4 col4;
layout(location = 5) in vec3 vColor;
layout(location = 6) in vec3 direction;

out vec3 color;

uniform mat4 projection;
uniform mat4 view;
uniform float time;
mat4 model;

void main()
{
    model[0] = col1;
    model[1] = col2;
    model[2] = col3;
    model[3] = col4;
    vec4 pos = vec4(vPos + direction * time,1.0);
    pos =  model * pos;
    pos = projection * view * pos;
    gl_Position = pos;
    color = vColor;
}
