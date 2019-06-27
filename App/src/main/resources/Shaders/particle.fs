#version 450

out vec4 color;
uniform vec3 vcol;
uniform float alpha;

void main()
{
    color = vec4(vcol,alpha);
}