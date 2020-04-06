#version 150

const float NEG_BRIGHT_DAMP = 0.4;
const float PI = 3.1415926535897932384626433832795;

in vec4 in_position;
in vec4 in_normal;//normalized
in vec4 in_colour;

out vec3 pass_colour;

uniform mat4 projectionViewMatrix;
uniform vec3 lightDirection;//normalized
uniform vec3 lightColour;
uniform vec2 lightBias;
uniform float time;

vec3 calculateLighting(){
	//vec3 normal = in_normal.xyz * 2.0 - 1.0; //check if this is needed, not sure but probably, right?
	float brightness = dot(-lightDirection, in_normal.xyz);
	brightness = mix(brightness, brightness * NEG_BRIGHT_DAMP, step(0.0, -brightness));
	return (lightColour * lightBias.x) + (brightness * lightColour * lightBias.y);
}

void main(void){

	gl_Position = projectionViewMatrix * in_position;
	vec3 lighting = calculateLighting();
	pass_colour = in_colour.rgb * lighting;

}
