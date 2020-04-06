#version 150

in vec2 pass_textureCoords;
out vec4 out_colour;

uniform sampler2D fontTexture;
uniform vec4 colour;


void main(void){

	float alpha = texture(fontTexture, pass_textureCoords).a;
	out_colour = vec4(colour.rgb, alpha * colour.a);
	
}
