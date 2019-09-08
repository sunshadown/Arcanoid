#version 330

uniform vec4 vCol;

out vec4 _color;

in vec3 Normal_cameraspace;
in vec3 vvertexPosition_cameraspace;
in vec3 vLightDirection_cameraspace;
in vec3 vEyeDirection_cameraspace;
in vec3 vPosition_worldspace;

struct Light
{
    vec3 pos;
    vec3 col;
};

uniform Light light;

vec3 Warm(vec4 color){
    vec3 col = color.xyz;
    return vec3(0.3,0.3,0)+0.25*col;
}

vec3 Cold(vec4 color){
    vec3 col = color.xyz;
    return vec3(0,0,0.55)+0.25*col;
}

vec3 lit(vec3 l, vec3 n, vec3 v){
    vec3 r_l = reflect(-l,n);
    float s = clamp(100.0 * dot(r_l,v) - 97.0, 0.0, 1.0);
    vec3 highlightColor = vec3(2,2,2);
    vec3 uWarmColor = Warm(vCol);
    return mix(uWarmColor, highlightColor, s);
}

vec3 unlit(vec4 color){
    return Cold(color)/2;
}

void main()
{
    float lenght = distance(light.pos,vPosition_worldspace);
    vec3 n = normalize( Normal_cameraspace );
    vec3 l = normalize( vLightDirection_cameraspace );

    vec3 a_color = unlit(vCol);
    vec3 d_color = clamp(dot(n,l),0.0,1.0) * light.col *lit(l,n,normalize(vEyeDirection_cameraspace));
    _color = vec4(a_color + d_color, 1.0f);
    //_color = vec4(1.0f,1.0f,1.0f, 1.0f);
}