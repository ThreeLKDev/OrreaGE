#version 150

in vec2 in_position;
in vec2 in_textureCoords;

out vec2 pass_textureCoords;

uniform vec3 transform;
uniform vec2 displaySize;

void main(void){
	
	vec2 screenPosition = in_position;
	screenPosition /= displaySize;
	screenPosition = screenPosition * transform.z + transform.xy;
	screenPosition.x = screenPosition.x * 2.0 - 1.0;
	screenPosition.y = screenPosition.y * -2.0 + 1.0;

	gl_Position = vec4(screenPosition, 0.0, 1.0);
	pass_textureCoords = in_textureCoords;

}
