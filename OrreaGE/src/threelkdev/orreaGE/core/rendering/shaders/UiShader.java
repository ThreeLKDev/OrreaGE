package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformBoolean;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformFloat;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformSampler;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec4;
import threelkdev.orreaGE.tools.files.FileUtils;

public class UiShader extends ShaderProgram {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "ui.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "ui.fragment.glsl" );

	public UniformVec4 transform = new UniformVec4( "transform" );
	public UniformVec3 overrideColour = new UniformVec3( "overrideColour" );
	public UniformFloat alpha = new UniformFloat( "alpha" );
	public UniformSampler blurTexture = new UniformSampler( "blurTexture" ); 
	public UniformSampler guiTexture = new UniformSampler( "guiTexture" );
	public UniformBoolean flipTexture = new UniformBoolean( "flipTexture" );
	public UniformBoolean useBlur = new UniformBoolean( "useBlur" );
	public UniformBoolean useOverrideColour = new UniformBoolean( "useOverrideColour" );
	public UniformBoolean useTexture = new UniformBoolean( "useTexture" );
	public UniformFloat uiWidth = new UniformFloat( "uiWidth" );
	public UniformFloat uiHeight = new UniformFloat( "uiHeight" );
	public UniformFloat radius = new UniformFloat( "uiRadius" );
	public UniformVec4 borderColour = new UniformVec4( "borderColour" );
	public UniformFloat borderWidth = new UniformFloat( "borderWidth" );
	
	public UiShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position" );
		super.storeAllUniformLocations( transform, alpha, flipTexture, overrideColour, useOverrideColour, blurTexture,
				guiTexture, useBlur, useTexture, uiWidth, uiHeight, radius, borderColour, borderWidth );
		super.start();
		guiTexture.loadTexUnit( 0 );
		blurTexture.loadTexUnit( 1 );
		super.stop();
	}
	
}
