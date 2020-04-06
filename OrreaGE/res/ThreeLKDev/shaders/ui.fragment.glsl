#version 150

in vec2 pass_textureCoords;
in vec2 pass_blurTextureCoords;

out vec4 out_colour;

uniform sampler2D guiTexture;
uniform sampler2D blurTexture;
uniform vec3 overrideColour;
uniform float alpha;

uniform float useOverrideColour;
uniform float useBlur;
uniform float useTexture;

uniform float uiWidth;
uniform float uiHeight;
uniform float uiRadius;

uniform vec4 borderColour;
uniform float borderWidth;

const float cornerSmoothFactor = 0.55;
vec2 pixelPos = pass_textureCoords * vec2( uiWidth, uiHeight );

float square(float val) {
	return val * val;
}

float distanceSquared(vec2 p1, vec2 p2) {
	vec2 vector = p2 - p1;
	return dot(vector, vector);
}


vec4 calcBorder(vec4 col) {
	vec2 min = vec2( borderWidth, borderWidth );
	vec2 max = vec2( uiWidth - min.x, uiHeight - min.y );;
	
	if( pixelPos.x < max.x && pixelPos.x > min.x &&
		pixelPos.y < max.y && pixelPos.y > min.y ) {
		return col;
	} else {
		return vec4( mix( col.rgb, borderColour.rgb, borderColour.a ), col.a );
	}
}

vec4 calcRoundedCorners( vec4 col ) {
	vec2 minCorner = vec2(uiRadius, uiRadius);
	vec2 maxCorner = vec2(uiWidth - uiRadius, uiHeight - uiRadius);

	vec2 cornerPoint = clamp(pixelPos, minCorner, maxCorner);
	float lowerBound = square(uiRadius - cornerSmoothFactor);
	float upperBound = square(uiRadius + cornerSmoothFactor);
	
	col.a *= smoothstep(upperBound, lowerBound, distanceSquared(pixelPos, cornerPoint));
	return col;
	
}

vec4 calcRoundedBorder( vec4 col ) {
	vec2 minBorder = vec2( uiRadius + borderWidth );
	vec2 maxBorder = vec2( uiWidth - minBorder.x, uiHeight - minBorder.y );
	
	vec2 borderPoint = clamp( pixelPos, minBorder, maxBorder );
	float lowerBound = square( uiRadius - cornerSmoothFactor );
	float upperBound = square( uiRadius + cornerSmoothFactor );
	
	float border = smoothstep( upperBound, lowerBound, distanceSquared( pixelPos, borderPoint ) );
	vec3 border_colour = mix( col.rgb, borderColour.rgb, borderColour.a );
	col.xyz = mix( border_colour, col.xyz, border );
	return col;
}

void main(void) {

	out_colour = vec4(1.0);

	if (useTexture > 0.5) {
		out_colour = texture(guiTexture, pass_textureCoords);
	}

	out_colour.rgb = mix(out_colour.rgb, overrideColour * out_colour.rgb, useOverrideColour);

	if (useBlur > 0.5) {
		vec4 blurColour = texture(blurTexture, pass_blurTextureCoords);
		out_colour.rgb = mix(blurColour.rgb, out_colour.rgb, alpha);
	} else {
		out_colour.a *= alpha;
	}
	if( uiRadius > 0 ) {
		out_colour = calcRoundedCorners( out_colour );
		if( borderWidth > 0 ) {
			out_colour = calcRoundedBorder( out_colour );
		}
	} else if ( borderWidth > 0 ) {
		out_colour = calcBorder( out_colour );
	}
}
