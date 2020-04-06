package threelkdev.orreaGE.testing.ui;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformMatrix;
import threelkdev.orreaGE.tools.files.FileUtils;

public class QuadShader extends ShaderProgram {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "quad.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "quad.fragment.glsl" );
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix( "projectionViewMatrix" );
	
	public QuadShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_normal", "in_colour" );
		super.storeAllUniformLocations( projectionViewMatrix );
	}
	
	public UniformMatrix getProjectionViewMatrix() { return projectionViewMatrix; }

}
