#version 150

in vec4 pass_colour;
in vec2 pass_texCoord;

out vec4 out_colour;

uniform sampler2D tex;
uniform float texBlend;

vec2 texSize;
vec2 texCoord;

void main( void ){
	texCoord = pass_texCoord;
	ivec2 texSize = textureSize(tex,0);
	texCoord.x = clamp( texCoord.x, 0.01 / texSize.x, 1.0 - ( 0.01 / texSize.x ) );
	texCoord.y = clamp( texCoord.y, 0.01 / texSize.y, 1.0 - ( 0.01 / texSize.y ) );
	out_colour = mix( pass_colour, texture( tex, texCoord ), texBlend );
}