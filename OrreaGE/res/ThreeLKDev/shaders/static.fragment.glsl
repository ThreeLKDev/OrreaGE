#version 150

in vec3 pass_colour;

out vec4 out_colour;

uniform float alpha;

void main(void){

	out_colour = vec4(pass_colour, alpha);

}