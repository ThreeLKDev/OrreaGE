package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformFloat;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformMatrix;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec2;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;

public abstract class StaticShader extends ShaderProgram {
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix( "projectionViewMatrix" );
	protected UniformVec3 lightDirection = new UniformVec3( "lightDirection" );
	protected UniformVec3 lightColour = new UniformVec3( "lightColour" );
	protected UniformVec2 lightBias = new UniformVec2( "lightBias" );
	protected UniformFloat alpha = new UniformFloat( "alpha" );
	
	public StaticShader( File vertexFile, File fragmentFile ) {
		super( vertexFile, fragmentFile, "in_position", "in_normal", "in_colour" );
		super.storeSomeUniformLocations( projectionViewMatrix, lightDirection, lightColour, lightBias, alpha );
	}

	public UniformMatrix getProjectionViewMatrix() {
		return projectionViewMatrix;
	}

	public UniformVec3 getLightDirection() {
		return lightDirection;
	}

	public UniformVec3 getLightColour() {
		return lightColour;
	}

	public UniformVec2 getLightBias() {
		return lightBias;
	}

	public UniformFloat getAlpha() {
		return alpha;
	}
	
	
	
}
