#version 150

in vec4 in_position;
in vec4 in_normal;
in vec4 in_colour;

out vec3 pass_colour;

uniform mat4 projectionViewMatrix;

void main( void ){
	gl_Position = projectionViewMatrix * in_position;
	pass_colour = in_colour.rgb;
}