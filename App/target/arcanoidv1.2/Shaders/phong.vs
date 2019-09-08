#version 330

in vec3 vPosition;
//in vec3 vColor;

out vec3 vertexPosition_cameraspace;
out	vec3 LightDirection_cameraspace;
out vec3 EyeDirection_cameraspace;
out vec3 Position_worldspace;
out vec3 nColor;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform float time;

const float amplitude = 0.525;
const float frequency = 12;
const float PI = 3.14159;

struct Light {
		vec3 pos;
		};		

uniform Light light;


void main()
{
	mat4 mvp = projectionMatrix * viewMatrix * modelMatrix;

	float distance = length(vPosition);
	float y = amplitude*sin(-PI*distance*frequency+time);
	gl_Position = mvp * vec4(vPosition.x,y,vPosition.z,1.0);
	

	Position_worldspace = (modelMatrix * vec4(vPosition,1)).xyz;
	vec3 vertexPosition_cameraspace = ( viewMatrix * modelMatrix * vec4(vPosition,1)).xyz;
	EyeDirection_cameraspace = vec3(0,0,0) - vertexPosition_cameraspace;
	vec3 LightPosition_cameraspace = ( viewMatrix * vec4(light.pos,1)).xyz;
	LightDirection_cameraspace = LightPosition_cameraspace + EyeDirection_cameraspace;
    nColor = vPosition;
}