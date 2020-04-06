package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.tools.files.FileUtils;

public class StaticShaderLd extends StaticShader {

	private static final File VERTEX_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "staticLD.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "static.fragment.glsl" );
	
	public StaticShaderLd() {
		super( VERTEX_SHADER, FRAGMENT_SHADER );
		super.storeAllUniformLocations();
	}
}
