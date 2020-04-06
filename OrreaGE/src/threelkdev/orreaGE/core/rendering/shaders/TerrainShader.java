package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformMatrix;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec2;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;
import threelkdev.orreaGE.tools.files.FileUtils;

public class TerrainShader extends ShaderProgram {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "terrain.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "terrain.fragment.glsl" );
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix( "projectionViewMatrix" );
	protected UniformVec3 lightDirection = new UniformVec3( "lightDirection" );
	protected UniformVec3 lightColour = new UniformVec3( "lightColour" );
	protected UniformVec2 lightBias = new UniformVec2( "lightBias" );
	protected UniformVec3 grassColour = new UniformVec3( "grassColour" );
	
	public TerrainShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_normal", "in_colour" );
		super.storeAllUniformLocations( projectionViewMatrix, lightDirection, lightColour, lightBias, grassColour );
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

	public UniformVec3 getGrassColour() {
		return grassColour;
	}
	
}
