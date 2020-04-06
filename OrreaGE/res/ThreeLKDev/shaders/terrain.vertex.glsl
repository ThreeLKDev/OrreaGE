#version 150

const float NEG_BRIGHT_DAMP = 0.4;

in vec4 in_position;
in vec4 in_normal;
in vec4 in_colour;

out vec3 pass_colour;

uniform mat4 projectionViewMatrix;
uniform vec3 lightDirection;
uniform vec3 lightColour;
uniform vec2 lightBias;
uniform vec3 grassColour;


vec3 calculateLighting(){
	vec3 normal = in_normal.xyz * 2.0 - 1.0;
	float brightness = dot(-lightDirection, in_normal.xyz);
	brightness = mix(brightness, brightness * NEG_BRIGHT_DAMP, step(0.0, -brightness));
	return (lightColour * lightBias.x) + (brightness * lightColour * lightBias.y);
}

void main(void){

	gl_Position = projectionViewMatrix * in_position;
	vec3 lighting = calculateLighting();
	vec3 terrainColour = mix(grassColour, in_colour.rgb, step(0.5, in_colour.a));
	pass_colour = terrainColour * lighting;

}
