#version 330

out vec4 Color;
in vec3 Normal_cameraspace;
in vec3 vvertexPosition_cameraspace;
in vec3 vLightDirection_cameraspace;
in vec3 vEyeDirection_cameraspace;
in vec3 vPosition_worldspace;
in vec3 vnColor;

struct Light
{
    vec3 pos;
};		

uniform Light light;

void main()
{
    float lenght = distance(light.pos,vPosition_worldspace);
    vec3 n = normalize( Normal_cameraspace );
    vec3 l = normalize( vLightDirection_cameraspace );
    float cosTheta = clamp( dot( n,l ), 0,1 );

    vec3 E = normalize(vEyeDirection_cameraspace);
    vec3 R = reflect(-l,n);
    float cosAlpha = clamp( dot( E,R ), 0,1 );

    vec3 ambient = vec3(0.3,0.3,0.3) * vec3(0.6,0.6,0.6);
    vec3 diffuse = vec3(0.6,0.6,0.6) * 100 *cosTheta / (lenght*lenght);
    vec3 specular = vec3(1.0,1.0,1.0) * 1000 * pow(cosAlpha,2) / (lenght*lenght);
    vec3 mColor = clamp(ambient + diffuse + specular,0,1);
    //mColor =clamp(mix(mColor,vec3(0.2,0.2,0.6),10),0,1);

    Color = vec4(mColor,1.0);
}