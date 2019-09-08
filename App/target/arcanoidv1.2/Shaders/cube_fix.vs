#version 330

in vec3 vPos;

uniform mat4 view;
uniform mat4 projection;
uniform mat4 model;

struct Light {
		vec3 pos;
		vec3 col;
		};

uniform Light light;

void main(){
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(vPos,1.0);
}