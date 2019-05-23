#version 330

in vec3 vPos;

out vec3 vertexPosition_cameraspace;
out	vec3 LightDirection_cameraspace;
out vec3 EyeDirection_cameraspace;
out vec3 Position_worldspace;

uniform mat4 view;
uniform mat4 projection;
uniform mat4 model;

struct Light {
		vec3 pos;
		vec3 col;
		};

uniform Light light;

void main()
{
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(vPos,1.0);

    Position_worldspace = (model * vec4(vPos,1)).xyz;
    vec3 vertexPosition_cameraspace = ( view * model * vec4(vPos,1)).xyz;
    EyeDirection_cameraspace = vec3(0,0,0) - vertexPosition_cameraspace;
    vec3 LightPosition_cameraspace = ( view * vec4(light.pos,1)).xyz;
    LightDirection_cameraspace = LightPosition_cameraspace + EyeDirection_cameraspace;
}