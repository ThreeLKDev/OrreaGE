package threelkdev.orreaGE.core.rendering;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformFloat;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformMatrix;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec2;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;
import threelkdev.orreaGE.tools.files.FileUtils;
import threelkdev.orreaGE.tools.files.FileUtils.ResourceType;

public class ModelShader extends ShaderProgram implements IMaterialUniforms {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( ResourceType.SHADER, "model.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( ResourceType.SHADER, "model.fragment.glsl" );
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix( "projectionViewMatrix" );
	protected UniformVec3 lightDirection = new UniformVec3( "lightDirection" );
	protected UniformVec3 lightColour = new UniformVec3( "lightColour" );
	protected UniformVec2 lightBias = new UniformVec2( "lightBias" );
	
	//Material uniforms
	protected UniformVec3 colourDiffuse	= new UniformVec3( "colourDiffuse" );
	protected UniformFloat alpha		= new UniformFloat( "alpha" );
	
	
	public ModelShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_normal" );
		super.storeSomeUniformLocations( colourDiffuse, alpha );
		super.storeAllUniformLocations( projectionViewMatrix, lightDirection, lightColour, lightBias );
	}
	
	public UniformMatrix getProjectionViewMatrix() { return projectionViewMatrix; }
	public UniformVec3 getLightDirection() { return lightDirection; }
	public UniformVec3 getLightColour() { return lightColour; }
	public UniformVec2 getLightBias() { return lightBias; }
	
	public UniformVec3 getDiffuse() { return colourDiffuse; }
	public UniformFloat getAlpha() { return alpha; }

	@Override
	public void loadMaterial(Material material) {
		// TODO Auto-generated method stub
		
	}
	
}
