package threelkdev.orreaGE.core.rendering.shaders;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.objects.UniformFloat;
import threelkdev.orreaGE.tools.files.FileUtils;

public class StaticShaderHd extends StaticShader {
	
	private static final File VERTEX_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "staticHD.vertex.glsl" );
	private static final File FRAGMENT_SHADER = FileUtils.getResource( FileUtils.ResourceType.SHADER, "static.fragment.glsl" );
	
	protected UniformFloat time = new UniformFloat( "time" );
	protected UniformFloat progression = new UniformFloat( "progression" );
	
	public StaticShaderHd() {
		super( VERTEX_SHADER, FRAGMENT_SHADER );
		super.storeAllUniformLocations( time, progression );
	}

	public UniformFloat getTime() {
		return time;
	}

	public UniformFloat getProgression() {
		return progression;
	}
	
}
