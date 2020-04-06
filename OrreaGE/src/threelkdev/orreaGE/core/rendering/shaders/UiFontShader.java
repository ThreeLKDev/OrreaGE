package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformBoolean;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformFloat;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformSampler;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec2;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec4;
import threelkdev.orreaGE.tools.files.FileUtils;

public class UiFontShader extends ShaderProgram {
	
	private static final File VERTEX_SHADER = FileUtils.getShader( "uiFont.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getShader( "uiFont.fragment.glsl" );
	
	public UniformVec4 colour = new UniformVec4( "colour" );
	public UniformSampler fontTexture = new UniformSampler( "fontTexture" );
	public UniformVec3 transform = new UniformVec3( "transform" );
	public UniformVec2 displaySize = new UniformVec2( "displaySize" );
	
	public UniformSampler blurTexture = new UniformSampler( "blurTexture" );
	public UniformSampler guiTexture = new UniformSampler( "guiTexture" );
	public UniformBoolean flipTexture = new UniformBoolean( "flipTexture" );
	public UniformBoolean useBlur = new UniformBoolean( "useBlur" );
	public UniformBoolean useOverrideColour = new UniformBoolean( "useOverrideColour" );
	public UniformBoolean useTexture = new UniformBoolean( "useTexture" );
	public UniformFloat uiRadius = new UniformFloat( "uiRadius" );
	public UniformVec4 borderColour = new UniformVec4( "borderColour" );
	public UniformFloat borderWidth = new UniformFloat( "borderWidth" );
	
	public UiFontShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_textureCoords" );
		super.storeAllUniformLocations( colour, fontTexture, transform, displaySize );
		super.start();
		guiTexture.loadTexUnit( 0 );
		blurTexture.loadTexUnit( 1 );
		fontTexture.loadTexUnit( 2 );
		super.stop();		
		
	}
	
}
