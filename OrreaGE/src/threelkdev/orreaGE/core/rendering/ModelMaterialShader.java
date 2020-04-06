package threelkdev.orreaGE.core.rendering;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformFloat;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformMatrix;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformSampler;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec2;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;
import threelkdev.orreaGE.tools.files.FileUtils;
import threelkdev.orreaGE.tools.files.FileUtils.ResourceType;

public class ModelMaterialShader extends ShaderProgram implements IMaterialUniforms {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( ResourceType.SHADER, "modelMaterial.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( ResourceType.SHADER, "modelMaterial.fragment.glsl" );
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix( "projectionViewMatrix" );
	protected UniformVec3 lightDirection = new UniformVec3( "lightDirection" );
	protected UniformVec3 lightColour = new UniformVec3( "lightColour" );
	protected UniformVec2 lightBias = new UniformVec2( "lightBias" );
	
	// Material Uniforms
	protected UniformVec3 colourDiffuse	= new UniformVec3( "colourDiffuse" );
	protected UniformFloat alpha		= new UniformFloat( "alpha" );
	protected UniformSampler texture	= new UniformSampler( "tex" );
	protected UniformFloat texBlend		= new UniformFloat( "texBlend" );
	
	public ModelMaterialShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_normal", "in_texCoord" );
		super.storeAllUniformLocations( projectionViewMatrix, lightDirection, lightColour, lightBias, colourDiffuse, alpha, texture, texBlend );
	}
	
	public UniformMatrix getProjectionViewMatrix() { return projectionViewMatrix; }
	public UniformVec3 getLightDirection() { return lightDirection; }
	public UniformVec3 getLightColour() { return lightColour; }
	public UniformVec2 getLightBias() { return lightBias; }
	
	public UniformVec3 getDiffuse() { return colourDiffuse; }
	public UniformFloat getAlpha() { return alpha; }

	@Override
	public void loadMaterial(Material material) {
		if( material.hasFlag( Material.COLOUR_DIFFUSE ) )
			colourDiffuse.loadVec3( material.colour );
		if( material.hasFlag( Material.ALPHA ) )
			alpha.loadFloat( material.alpha );
		if( material.hasFlag( Material.TEXTURE ) ) {
			texture.loadTexUnit( 0 );
			texBlend.loadFloat( 1f );
		} else {
			texBlend.loadFloat( 0f );
		}
	}
	
}
