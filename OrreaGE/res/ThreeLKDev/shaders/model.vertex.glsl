#version 150

const float NEG_BRIGHT_DAMP = 0.4;

in vec4 in_position;
in vec4 in_normal;
in vec4 in_colour;

out vec4 pass_colour;

uniform mat4 projectionViewMatrix;
uniform vec3 lightDirection; //normalised
uniform vec3 lightColour;
uniform vec2 lightBias;

vec3 calculateLighting() {
	float brightness = dot( -lightDirection, in_normal.xyz );
	brightness = mix( brightness, brightness * NEG_BRIGHT_DAMP, step( 0.0, -brightness ) );
	return ( lightColour * lightBias.x ) + ( brightness * lightColour * lightBias.y );
}

void main( void ){
	gl_Position = projectionViewMatrix * in_position;
	vec3 lighting = calculateLighting();
	pass_colour = vec4( in_colour.rgb * lighting, 0.5 );
}