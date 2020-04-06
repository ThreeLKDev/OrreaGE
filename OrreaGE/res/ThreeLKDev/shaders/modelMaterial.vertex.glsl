#version 150

const float NEG_BRIGHT_DAMP = 0.4;

in vec3 in_position;
in vec3 in_normal;
in vec2 in_texCoord;

out vec4 pass_colour;
out vec2 pass_texCoord;

uniform mat4	projectionViewMatrix;
uniform vec3	lightDirection; //normalised
uniform vec3	lightColour;
uniform vec2	lightBias;
uniform vec3	colourDiffuse;
uniform float	alpha = 1f;

vec3 calculateLighting() {
	float brightness = dot( -lightDirection, in_normal.xyz );
	brightness = mix( brightness, brightness * NEG_BRIGHT_DAMP, step( 0.0, -brightness ) );
	return ( lightColour * lightBias.x ) + ( brightness * lightColour * lightBias.y );
}

void main( void ){
	gl_Position = projectionViewMatrix * vec4( in_position, 1f ) ;
	vec3 lighting = calculateLighting();
	pass_colour = vec4( colourDiffuse * lighting, alpha );
	pass_texCoord = in_texCoord;
}