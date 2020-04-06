package threelkdev.orreaGE.core.rendering.terrain;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.objects.ICamera;
import threelkdev.orreaGE.core.rendering.objects.Light;
import threelkdev.orreaGE.core.rendering.shaders.TerrainShader;
import threelkdev.orreaGE.tools.colours.Colour;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;

public class TerrainRenderer {
	
	private final TerrainShader shader;
	
	public TerrainRenderer() {
		this.shader = new TerrainShader();
	}
	
	public void render( TerrainMesh terrain, ICamera camera, Light light ) {
		if( !terrain.isLoaded() )
			return;
		shader.start();
		prepare( camera, light, terrain.getGrassColour() );
		terrain.getVao().bind();
		GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, terrain.getVertexCount() );
		terrain.getVao().unbind();
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare( ICamera camera, Light light, Colour grassCol ) {
		doGeneralSettings();
		loadLightVariables( light );
		shader.getGrassColour().loadVec3( grassCol.getVector() );
		shader.getProjectionViewMatrix().loadMatrix( camera.getProjectionViewMatrix() );
	}
	
	private void loadLightVariables( Light light ) {
		shader.getLightBias().loadVec2( light.getBias() );
		shader.getLightColour().loadVec3( light.getColour().getVector() );
		shader.getLightDirection().loadVec3( light.getDirection() );
	}
	
	private void doGeneralSettings() {
		OpenGLUtils.cullBackFaces( false );
		OpenGLUtils.enableAlphaBlending();
		OpenGLUtils.enableDepthTest( true );
		OpenGLUtils.antialias( true );
	}
	
}
