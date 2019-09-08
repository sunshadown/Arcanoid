#version 330

layout(triangles) in ;
layout(triangle_strip,max_vertices = 3) out;


in vec3 vertexPosition_cameraspace[];
in vec3 LightDirection_cameraspace[];
in vec3 EyeDirection_cameraspace[];
in vec3 Position_worldspace[];
in vec3 nColor[];

out vec3 Normal_cameraspace;
out vec3 vvertexPosition_cameraspace;
out vec3 vLightDirection_cameraspace;
out vec3 vEyeDirection_cameraspace;
out vec3 vPosition_worldspace;
out vec3 vnColor;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

vec3 GetNormal()
{
   vec3 a = vec3(gl_in[0].gl_Position) - vec3(gl_in[1].gl_Position);
   vec3 b = vec3(gl_in[2].gl_Position) - vec3(gl_in[1].gl_Position);
   return normalize(cross(a, b));
}  

void main()
{
	vec3 normal = GetNormal();
	Normal_cameraspace = (	viewMatrix * modelMatrix * vec4(normal,0)).xyz;
	vvertexPosition_cameraspace = vertexPosition_cameraspace[0];
	vLightDirection_cameraspace = LightDirection_cameraspace[0];
	vEyeDirection_cameraspace = EyeDirection_cameraspace[0];
	vPosition_worldspace = Position_worldspace[0];
    vnColor = nColor[0];
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();

	Normal_cameraspace = (	viewMatrix * modelMatrix * vec4(normal,0)).xyz;
	vvertexPosition_cameraspace = vertexPosition_cameraspace[1];
	vLightDirection_cameraspace = LightDirection_cameraspace[1];
	vEyeDirection_cameraspace = EyeDirection_cameraspace[1];
	vPosition_worldspace = Position_worldspace[1];
    vnColor = nColor[1];
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();

	Normal_cameraspace = (	viewMatrix * modelMatrix * vec4(normal,0)).xyz;
	vvertexPosition_cameraspace = vertexPosition_cameraspace[2];
	vLightDirection_cameraspace = LightDirection_cameraspace[2];
	vEyeDirection_cameraspace = EyeDirection_cameraspace[2];
	vPosition_worldspace = Position_worldspace[2];
    vnColor = nColor[2];
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();

    
	EndPrimitive();


}