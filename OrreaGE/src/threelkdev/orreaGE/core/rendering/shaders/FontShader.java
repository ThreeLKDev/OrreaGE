package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.ShaderProgram;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformSampler;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec2;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec3;
import threelkdev.orreaGE.core.rendering.openGL.objects.UniformVec4;
import threelkdev.orreaGE.tools.files.FileUtils;

public class FontShader extends ShaderProgram {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "font.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "font.fragment.glsl" );
	
	protected UniformVec4 colour = new UniformVec4( "colour" );
	protected UniformSampler fontTexture = new UniformSampler( "fontTexture" );
	protected UniformVec3 transform = new UniformVec3( "transform" );
	protected UniformVec2 displaySize = new UniformVec2( "displaySize" );
	
	public FontShader() {
		super( VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_textureCoords" );
		super.storeAllUniformLocations( colour, fontTexture, transform, displaySize );
	}

	public UniformVec4 getColour() { return colour; }
	public UniformSampler getFontTexture() { return fontTexture; }
	public UniformVec3 getTransform() { return transform; }
	public UniformVec2 getDisplaySize() { return displaySize; }
	
}
