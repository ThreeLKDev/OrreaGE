#version 150

in vec4 pass_colour;
in vec2 pass_texCoord;

out vec4 out_colour;

uniform sampler2D tex;
uniform float texBlend;

void main( void ){
	out_colour = mix( pass_colour, texture( tex, pass_texCoord ), texBlend );
}