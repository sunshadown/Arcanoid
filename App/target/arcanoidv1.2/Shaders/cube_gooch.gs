#version 330

layout(triangles) in ;
layout(triangle_strip,max_vertices = 3) out;

in vec3 te_vertexPosition_cameraspace[];
in vec3 te_LightDirection_cameraspace[];
in vec3 te_EyeDirection_cameraspace[];
in vec3 te_Position_worldspace[];

out vec3 Normal_cameraspace;
out vec3 vvertexPosition_cameraspace;
out vec3 vLightDirection_cameraspace;
out vec3 vEyeDirection_cameraspace;
out vec3 vPosition_worldspace;

uniform mat4 model;
uniform mat4 view;

vec3 GetNormal()
{
   vec3 a = vec3(gl_in[0].gl_Position) - vec3(gl_in[1].gl_Position);
   vec3 b = vec3(gl_in[2].gl_Position) - vec3(gl_in[1].gl_Position);
   return normalize(cross(a, b));
}

void main()
{
	vec3 normal = GetNormal();
	Normal_cameraspace = (	view * model * vec4(normal,0)).xyz;
	vvertexPosition_cameraspace = te_vertexPosition_cameraspace[0];
	vLightDirection_cameraspace = te_LightDirection_cameraspace[0];
	vEyeDirection_cameraspace = te_EyeDirection_cameraspace[0];
	vPosition_worldspace = te_Position_worldspace[0];
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();

	Normal_cameraspace = (	view * model * vec4(normal,0)).xyz;
	vvertexPosition_cameraspace = te_vertexPosition_cameraspace[1];
	vLightDirection_cameraspace = te_LightDirection_cameraspace[1];
	vEyeDirection_cameraspace = te_EyeDirection_cameraspace[1];
	vPosition_worldspace = te_Position_worldspace[1];
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();

	Normal_cameraspace = (	view * model * vec4(normal,0)).xyz;
	vvertexPosition_cameraspace = te_vertexPosition_cameraspace[2];
	vLightDirection_cameraspace = te_LightDirection_cameraspace[2];
	vEyeDirection_cameraspace = te_EyeDirection_cameraspace[2];
	vPosition_worldspace = te_Position_worldspace[2];
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();

	EndPrimitive();


}