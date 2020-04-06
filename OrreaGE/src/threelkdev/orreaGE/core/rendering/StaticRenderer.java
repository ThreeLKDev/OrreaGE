package threelkdev.orreaGE.core.rendering;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.objects.ICamera;
import threelkdev.orreaGE.core.rendering.objects.Light;
import threelkdev.orreaGE.core.rendering.openGL.objects.IStaticBatch;
import threelkdev.orreaGE.core.rendering.shaders.StaticShader;
import threelkdev.orreaGE.core.rendering.shaders.StaticShaderHd;
import threelkdev.orreaGE.core.rendering.shaders.StaticShaderLd;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;

public class StaticRenderer {
	
	private static final float WIND_SPEED = 0.32f;
	
	private final StaticShaderHd hdShader;
	private final StaticShaderLd ldShader;
	private StaticShader shader;
	
	private float time = 0;
	
	public StaticRenderer() {
		this.hdShader = new StaticShaderHd();
		this.ldShader = new StaticShaderLd();
	}
	
	public void render( Iterable< ? extends IStaticBatch > batches, ICamera camera, Light light, boolean highDef ) {
		chooseShader( highDef );
		shader.start();
		prepare( camera, light, highDef );
		for( IStaticBatch batch : batches ) {
			if( batch.isVisible() ) {
				batch.getVao().bind();
				loadBatchUniforms( batch, highDef );
				GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, batch.getVertexCount() );
				batch.getVao().unbind();
			}
		}
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare( ICamera camera, Light light, boolean hd ) {
		doGeneralSettings();
		if( hd )
			updateTime();
		loadLightVariables( light );
		shader.getProjectionViewMatrix().loadMatrix( camera.getProjectionViewMatrix() );
	}
	
	private void loadLightVariables( Light light ) {
		shader.getLightBias().loadVec2( light.getBias() );
		shader.getLightColour().loadVec3( light.getColour().getVector() );
		shader.getLightDirection().loadVec3( light.getDirection() );
	}
	
	private void doGeneralSettings() {
		OpenGLUtils.cullBackFaces( true );
		OpenGLUtils.enableAlphaBlending();
		OpenGLUtils.enableDepthTest( true );
		OpenGLUtils.antialias( true );
	}
	
	private void chooseShader( boolean highDef ) {
		this.shader = highDef ? hdShader : ldShader;
	}
	
	private void updateTime() {
		time += 0.01f * WIND_SPEED;
		time %= 1;
		hdShader.getTime().loadFloat( time );
	}
	
	private void loadBatchUniforms( IStaticBatch batch, boolean hd ) {
		shader.getAlpha().loadFloat( batch.getAlpha() );
		if( hd ) 
			hdShader.getProgression().loadFloat( batch.getProgression() );
	}
	
}
