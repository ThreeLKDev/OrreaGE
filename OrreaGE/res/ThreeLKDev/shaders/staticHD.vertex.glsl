#version 150

const float NEG_BRIGHT_DAMP = 0.3;
const float PI = 3.1415926535897932384626433832795;

in vec4 in_position;
in vec4 in_normal;//must be normalized
in vec4 in_colour;

out vec3 pass_colour;

uniform mat4 projectionViewMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightDirection;//must be normalized
uniform vec3 lightColour;
uniform vec2 lightBias;
uniform float time;
uniform float progression;

vec3 calculateLighting(){
	vec3 normal = in_normal.xyz * 2.0 - 1.0;
	float brightness = dot(-lightDirection, normal);
	brightness = mix(brightness, brightness * NEG_BRIGHT_DAMP, step(0.0, -brightness));
	return (lightColour * lightBias.x) + (brightness * lightColour * lightBias.y);
}

vec4 applyWind(){
	vec4 worldPosition = in_position;
	float wave = sin(2.0 * PI * time + (worldPosition.z + worldPosition.x) * 0.8);
	float wave2 = sin(2.0 * PI * (time + worldPosition.z + worldPosition.x) * 2.0);
	worldPosition.x += (wave + wave2 * 0.4) * 0.06 * in_colour.w * 10.0 * progression;
	worldPosition.z -= (wave - wave2 * 0.4) * 0.03 * in_colour.w * 10.0 * progression;
	return worldPosition;
}

void main(void){

	vec4 worldPosition = applyWind();
	gl_Position = projectionViewMatrix * worldPosition;
	
	
	vec3 lighting = calculateLighting();
	pass_colour = in_colour.rgb * lighting;

}